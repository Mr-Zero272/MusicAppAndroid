package thud.mymusicapp.processing;

import static thud.mymusicapp.interfaces.album.AlbumDetails.reloadDetailAlbum;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import thud.mymusicapp.R;
import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.data.MusicFiles;
import thud.mymusicapp.interfaces.song.Edit_Song;
import thud.mymusicapp.interfaces.PlayerActivity;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumDetailAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        Glide.with(mContext)
                .load(R.drawable.tempmusicicon)
                .into(holder.album_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int albumid = albumFiles.get(position).getAlbumid();
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", position);
                intent.putExtra("albumid", albumid);
                mContext.startActivity(intent);
            }
        });
        holder.menu_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int albumid = albumFiles.get(position).getAlbumid();
                int songId = albumFiles.get(position).getId();
                String songName = albumFiles.get(position).getTitle();
                String artistName = albumFiles.get(position).getArtist();
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            if (albumid == 1)
                            {
                                Toast.makeText(mContext,"Cannot delete music file in this album!", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(mContext);
                                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DbHelper myDBbHelper = new DbHelper(mContext);
                                        SQLiteDatabase db = myDBbHelper.getWritableDatabase();
                                        db.delete("songs", "id = " + songId, null);
                                        reloadDetailAlbum.performClick();
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
                        case R.id.edit:
                            Intent intent = new Intent(mContext, Edit_Song.class);
                            intent.putExtra("songId", songId);
                            intent.putExtra("songName", songName);
                            intent.putExtra("artistName", artistName);
                            intent.putExtra("from", "albumdetail");
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
        ImageView album_image, menu_music;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_file_name);
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
