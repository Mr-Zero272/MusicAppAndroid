package thud.mymusicapp.interfaces.album;

import static thud.mymusicapp.interfaces.MainActivity.reload;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import thud.mymusicapp.R;
import thud.mymusicapp.processing.AlbumAdapter;

public class Edit_Album extends AppCompatActivity {
    TextInputLayout layoutEditAlbum;
    TextInputEditText txtEditNameAlbum;
    Button btnAccept;
    String strEditNameAlbum;
    AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        layoutEditAlbum = findViewById(R.id.layout_albumName_edit);
        txtEditNameAlbum = findViewById(R.id.newAlbumName_edit);
        btnAccept = findViewById(R.id.btn_accept_edit);
        int albumid = getIntent().getIntExtra("albumId", 0);
        String albumName = getIntent().getStringExtra("albumName");
        txtEditNameAlbum.setText(albumName);
        txtEditNameAlbum.requestFocus();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiem tra album name khon rong sau do update
                String nameAlbum = txtEditNameAlbum.getText().toString().trim();
                if (nameAlbum.length() == 0)
                {
                    layoutEditAlbum.setError("Please enter new album name!");
                    txtEditNameAlbum.requestFocus();
                    return;
                } else {
                    layoutEditAlbum.setError(null);
                }
                strEditNameAlbum = nameAlbum;
                albumAdapter = new AlbumAdapter(getApplicationContext());
                albumAdapter.updateAlbum(albumid, strEditNameAlbum);
                albumAdapter.close();
                Toast.makeText(getApplicationContext(), "Edit album successfully!", Toast.LENGTH_SHORT).show();
                reload.performClick();
                finish();
            }
        });
    }

    public void closeAC(View view){
        finish();
    }
}