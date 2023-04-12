package thud.mymusicapp.processing;

import static thud.mymusicapp.interfaces.MainActivity.reload;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import thud.mymusicapp.R;
import thud.mymusicapp.data.Album;
import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.interfaces.album.AlbumDetails;
import thud.mymusicapp.interfaces.album.Edit_Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<Album> albumFiles;
    View view;

    private DbHelper myDbHelper;
    private SQLiteDatabase db;
    private String[] allColumns = { "id", "albumname", "userid"};

    public AlbumAdapter(Context context)
    {
        myDbHelper = new DbHelper(context);
        db = myDbHelper.getWritableDatabase();
    }

    public long insertNewAlbum(Album album){
        db = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("albumname", album.getAlbumName());
        values.put("userid", album.getUserid());
        return db.insert("album", null, values);
    }

    public int updateAlbum(int id, String albumName)
    {
        ContentValues values = new ContentValues();
        values.put("albumname", albumName);
        return db.update("album", values, "id = " + id, null);
    }

    public int deleteAlbum(int id){
        return db.delete("album", "id = " + id, null);
    }

    private Album cursorToAlbum(Cursor cursor){
        Album values = new Album();
        values.setId(cursor.getInt(0));
        values.setAlbumName(cursor.getString(1));
        values.setUserid(cursor.getInt(2));
        return values;
    }

    public ArrayList<Album> ListAllAlbum(){
        ArrayList<Album> lstAlbum = new ArrayList<Album>();
        Cursor cursor = db.query("album", allColumns, null, null,
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Album values = cursorToAlbum(cursor);
                lstAlbum.add(values);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lstAlbum;
    }

    public ArrayList<Album> ListAllAlbum(int userID){
        ArrayList<Album> lstAlbum = new ArrayList<Album>();
        Cursor cursor = db.query("album", allColumns, "userid = " + userID, null,
                null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Album values = cursorToAlbum(cursor);
                lstAlbum.add(values);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lstAlbum;
    }

    public Boolean isExistAlbum(int id){
        Boolean exist = false;
        List<Album> lstAlbum = ListAllAlbum();
        int i = 0;
        while ((! exist) && (i < lstAlbum.size()))
            if (lstAlbum.get(i).getId() == id)
                exist = true;
            else
                i++;
        return exist;
    }

    public AlbumAdapter(Context mContext, ArrayList<Album> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    public void close(){
        db.close();
        myDbHelper.close();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getAlbumName());
        Glide.with(mContext)
                .load(R.drawable.tempmusicicon)
                .into(holder.album_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khi click vao 1 alubm goi activity albumdetails de hien thi danh sach bai hat trong album do
                Intent intent = new Intent(mContext, AlbumDetails.class);
                int albumid = albumFiles.get(position).getId();
                intent.putExtra("albumid", albumid);
                mContext.startActivity(intent);
            }
        });
        // khi click vao nut ... tren moi album
        holder.album_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int albumid = albumFiles.get(position).getId();
                String albumName = albumFiles.get(position).getAlbumName();
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        // xoa album dong thoi xoa toan bo bai hat co trong album do
                        case R.id.delete:
                            if (albumid == 1) // albumid == 1 album device khong the xoa
                            {
                                Toast.makeText(mContext,"Cannot delete device album", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
                                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDbHelper = new DbHelper(mContext);
                                        db = myDbHelper.getWritableDatabase();
                                        db.delete("songs", "albumid = " + albumid, null);
                                        db.delete("album", "id = " + albumid, null);
                                        reload.performClick();
                                        Toast.makeText(mContext,"Delete sucessfully", Toast.LENGTH_SHORT).show();
                                        myDbHelper.close();
                                        db.close();
                                    }
                                });
                                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                deleteDialog.setTitle("Delete this album?");
                                deleteDialog.setMessage("You won't be able to recover this action?");
                                deleteDialog.create().show();
                            }
                            break;
                            // edit name album
                        case R.id.edit:
                            Intent intent = new Intent(mContext, Edit_Album.class);
                            intent.putExtra("albumId", albumid);
                            intent.putExtra("albumName", albumName);
                            mContext.startActivity(intent);
                            break;
                    }
                    return true;
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView album_image, album_menu;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);
            album_menu = itemView.findViewById(R.id.album_menu);
        }
    }

    private byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;
    }

}
