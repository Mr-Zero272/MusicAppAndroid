package thud.mymusicapp.interfaces.album;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import thud.mymusicapp.interfaces.song.ListSongToAddAlbum;
import thud.mymusicapp.processing.AlbumDetailAdapter;
import thud.mymusicapp.processing.MusicAdapter;
import thud.mymusicapp.R;
import thud.mymusicapp.data.MusicFiles;

public class AlbumDetails extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView albumPhoto;
    FloatingActionButton addSongs;
    public static FloatingActionButton reloadDetailAlbum;
    int albumID;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();
    MusicAdapter musicAdapter;
    AlbumDetailAdapter albumDetailAdapter;
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.recyclerView);
        albumPhoto = findViewById(R.id.albumPhoto);
        addSongs = findViewById(R.id.add_item_album);
        reloadDetailAlbum = findViewById(R.id.reload_detail_album);
        albumID = getIntent().getIntExtra("albumid", 0);
        // khi click nut + tren goc khi mo mot album de them bai hat vao album
        // goi activity de list danh sach bai hat tren thiet bi
        addSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListSongToAddAlbum.class);
                intent.putExtra("albumid", albumID);
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
            }
        });
        musicAdapter = new MusicAdapter(getApplicationContext());
        albumSongs = musicAdapter.ListAllMusicFile(albumID);
        musicAdapter.close();
        // nut reload lai view khi them, sua, xoa bai hat
        reloadDetailAlbum.setVisibility(View.GONE);
        reloadDetailAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "reload click", Toast.LENGTH_SHORT).show();
                musicAdapter = new MusicAdapter(getApplicationContext());
                albumSongs = musicAdapter.ListAllMusicFile(albumID);
                musicAdapter.close();
                onResume();
            }
        });
        Glide.with(this)
                .load(R.drawable.tempmusicicon)
                .into(albumPhoto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // cho nay dung try catch vi khi tu activity listallmusicfile an nut back thi se khong co data tra ve dan den ngoai le
        try {
            String result = data.getStringExtra("result");
            if (result.equalsIgnoreCase("OK"))
            {
                musicAdapter = new MusicAdapter(getApplicationContext());
                albumSongs = musicAdapter.ListAllMusicFile(albumID);
                musicAdapter.close();
                onResume();
            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e)
        {
            //Toast.makeText(this, "There was nothing change", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)) {
            albumDetailAdapter = new AlbumDetailAdapter(this, albumSongs);
            recyclerView.setAdapter(albumDetailAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    RecyclerView.VERTICAL, false));
        }
    }
}