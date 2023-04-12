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
import android.util.Log;
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

import thud.mymusicapp.R;
import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.data.MusicFiles;
import thud.mymusicapp.interfaces.song.Edit_Song;
import thud.mymusicapp.interfaces.PlayerActivity;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private DbHelper myDBbHelper;
    private SQLiteDatabase db;
    private String[] allColumns = { "id", "title", "path", "artist", "duration",
            "albumid" };
    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context context){
        myDBbHelper = new DbHelper(context);
        db = myDBbHelper.getWritableDatabase();
    }

    public long insertMusicFile(MusicFiles musicFiles)
    {
        db = myDBbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", musicFiles.getTitle());
        values.put("path", musicFiles.getPath());
        values.put("artist", musicFiles.getArtist());
        values.put("duration", musicFiles.getDuration());
        values.put("albumid", musicFiles.getAlbumid());
        return db.insert("songs", null, values);
    }

    public int updateMusicFile(int idSongs, String title, String artist)
    {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("artist", artist);
        return db.update("songs", values,
                "id = " + idSongs, null );
    }

    public int deleteMusicFile(int idSongs){
        return db.delete("songs", "id = " + idSongs, null);
    }

    private MusicFiles cursorToMusicFile(Cursor cursor){
        MusicFiles values = new MusicFiles();
        values.setId(cursor.getInt(0));
        values.setTitle(cursor.getString(1));
        values.setPath(cursor.getString(2));
        values.setArtist(cursor.getString(3));
        values.setDuration(cursor.getString(4));
        values.setAlbum(cursor.getInt(5));
        return values;
    }

    public ArrayList<MusicFiles> ListAllMusicFile() {
        ArrayList<MusicFiles> lstMusicFile = new ArrayList<MusicFiles>();
        Cursor cursor = db.query("songs", allColumns, null,
                null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String path = cursor.getString(2);
                String artist = cursor.getString(3);
                String duration = cursor.getString(4);
                int album = cursor.getInt(5);

                MusicFiles musicFiles = new MusicFiles(id, path, title, artist, album, duration, album);
                // take log.e for check
                Log.e("Path: " + path, "Album: " + album);
                lstMusicFile.add(musicFiles);
            }
            cursor.close();
        }
        return lstMusicFile;
    }

    public ArrayList<MusicFiles> ListAllMusicFile(int albumid) {
        ArrayList<MusicFiles> lstMusicFile = new ArrayList<MusicFiles>();
        Cursor cursor = db.query("songs", allColumns, "albumid = " + albumid,
                null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String path = cursor.getString(2);
                String artist = cursor.getString(3);
                String duration = cursor.getString(4);
                int album = cursor.getInt(5);

                MusicFiles musicFiles = new MusicFiles(id, path, title, artist, album, duration, album);
                // take log.e for check
                Log.e("Path: " + path, "Album: " + album);
                lstMusicFile.add(musicFiles);
            }
            cursor.close();
        }
        return lstMusicFile;
    }

    public void close(){
        db.close();
        myDBbHelper.close();
    }
    public MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles)
    {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set list bai hat o tab songs
        holder.file_name.setText(mFiles.get(position).getTitle());
        Glide.with(mContext).load(R.drawable.tempmusicicon).into(holder.album_art);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // vi day la album device nen khong can gui albumid
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        // khi click nut ... o cuoi list xong hien thi menu popup
        holder.menu_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int albumid = mFiles.get(position).getAlbumid();
                int songId = mFiles.get(position).getId();
                String songName = mFiles.get(position).getTitle();
                String artistName = mFiles.get(position).getArtist();
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        // xoa bai hat, khong the xoa bai trong album device
                        case R.id.delete:
                            if (albumid == 1)
                            {
                                Toast.makeText(mContext,"Cannot delete music file in this album!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
                                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDBbHelper = new DbHelper(mContext);
                                        db = myDBbHelper.getWritableDatabase();
                                        db.delete("songs", "id = " + songId, null);
                                        reload.performClick();
                                        Toast.makeText(mContext,"Delete sucessfully!", Toast.LENGTH_SHORT).show();
                                        myDBbHelper.close();
                                        db.close();
                                    }
                                });
                                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                deleteDialog.setTitle("Delete this song?");
                                deleteDialog.setMessage("You won't be able to recover this action?");
                                deleteDialog.create().show();
                            }
                            break;
                            // edit bai hat
                        case R.id.edit:
                            Intent intent = new Intent(mContext, Edit_Song.class);
                            intent.putExtra("songId", songId);
                            intent.putExtra("songName", songName);
                            intent.putExtra("artistName", artistName);
                            intent.putExtra("from", "device");
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
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView file_name;
        ImageView album_art, menu_music;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            menu_music = itemView.findViewById(R.id.menu_music);
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
