package thud.mymusicapp.interfaces.song;

import static thud.mymusicapp.interfaces.album.AlbumDetails.reloadDetailAlbum;
import static thud.mymusicapp.interfaces.MainActivity.reload;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import thud.mymusicapp.R;
import thud.mymusicapp.processing.MusicAdapter;

public class Edit_Song extends AppCompatActivity {
    TextInputLayout layoutEditSongName, layoutEditAristName;
    TextInputEditText txtEditSongName, txtEditArtistName;
    Button btnAccept;
    String strEditSongName, strEditArtistName;
    MusicAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_song);

        layoutEditSongName = findViewById(R.id.layout_nameSong_edit);
        layoutEditAristName = findViewById(R.id.layout_nameArtist_edit);
        txtEditSongName = findViewById(R.id.nameSong_edit);
        txtEditArtistName = findViewById(R.id.nameArtist_edit);
        btnAccept = findViewById(R.id.btn_accept_edit);

        int songId = getIntent().getIntExtra("songId", 0);
        String songName = getIntent().getStringExtra("songName");
        String aritstName = getIntent().getStringExtra("artistName");
        String from = getIntent().getStringExtra("from");
        txtEditSongName.setText(songName);
        txtEditArtistName.setText(aritstName);
        txtEditSongName.requestFocus();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiem tra ten bai hat khong rong
                String nameAlbum = txtEditSongName.getText().toString().trim();
                if (nameAlbum.length() == 0)
                {
                    layoutEditSongName.setError("Please enter new song name!");
                    txtEditSongName.requestFocus();
                    return;
                } else {
                    layoutEditSongName.setError(null);
                }
                strEditSongName = nameAlbum;

                // kiem tra ten ca si khong rong
                String artist = txtEditArtistName.getText().toString().trim();
                if (artist.length() == 0)
                {
                    layoutEditAristName.setError("Please enter new artist name!");
                    txtEditArtistName.requestFocus();
                    return;
                } else {
                    layoutEditAristName.setError(null);
                }
                strEditArtistName = artist;

                musicAdapter = new MusicAdapter(getApplicationContext());
                musicAdapter.updateMusicFile(songId, strEditSongName, strEditArtistName);
                musicAdapter.close();
                Toast.makeText(getApplicationContext(), "Edit album successfully!", Toast.LENGTH_SHORT).show();
                // neu update tu tab songs thi reload main activity
                if (from.equalsIgnoreCase("device"))
                {
                    reload.performClick();
                } else { // neu update tu detail thi chi reload o trang album detail
                    reloadDetailAlbum.performClick();
                }
                finish();
            }
        });
    }

    public void closeAcEditSong(View view){
        finish();
    }
}