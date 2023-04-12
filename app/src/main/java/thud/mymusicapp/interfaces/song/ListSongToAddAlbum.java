package thud.mymusicapp.interfaces.song;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import thud.mymusicapp.processing.MusicAdapter;
import thud.mymusicapp.R;
import thud.mymusicapp.data.MusicFiles;

public class ListSongToAddAlbum extends AppCompatActivity {
    MusicAdapter musicAdapter;
    ArrayList<MusicFiles> mfiles;
    ListView listSongs;
    ImageView addBtn;
    int albumid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_to_add_album);

        albumid = getIntent().getIntExtra("albumid", 0);
        Log.e("asdf", "" + albumid);
        musicAdapter = new MusicAdapter(getApplicationContext());
        mfiles = musicAdapter.ListAllMusicFile(1);

        listSongs = findViewById(R.id.listViewSong);
        customAdapter ca = new customAdapter();
        listSongs.setAdapter(ca);
    }

    class customAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mfiles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // setup view
            View myView = getLayoutInflater().inflate(R.layout.item_music_to_add, null);
            TextView textSong = myView.findViewById(R.id.music_file_name);
            ImageView btnAdd = myView.findViewById(R.id.add_btn);
            // khi click nut + o cuoi list bai hat
            // tien hanh add bai hat do vao album
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = mfiles.get(position).getPath();
                    String title = mfiles.get(position).getTitle();
                    String artist = mfiles.get(position).getArtist();
                    String duration = mfiles.get(position).getDuration();
                    MusicFiles musicFiles = new MusicFiles(path, title, artist, albumid, duration, albumid);
                    musicAdapter.insertMusicFile(musicFiles);
                    Toast.makeText(getApplicationContext(), "Add songs successfully!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", "OK");
                    setResult(Activity.RESULT_OK, resultIntent);
                    musicAdapter.close();
                    finish();
                }
            });
            textSong.setSelected(true);
            textSong.setText(mfiles.get(position).getTitle());

            return myView;
        }
    }
}