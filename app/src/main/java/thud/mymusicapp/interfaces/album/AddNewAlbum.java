package thud.mymusicapp.interfaces.album;

import static thud.mymusicapp.interfaces.MainActivity.userid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import thud.mymusicapp.processing.AlbumAdapter;
import thud.mymusicapp.R;
import thud.mymusicapp.data.Album;

public class AddNewAlbum extends AppCompatActivity {
    ImageView btnBack;
    TextInputLayout layoutAlbumName;
    TextInputEditText newAlbumName;
    String nameAlbumCreate;
    Button btnCreateNewAlbum;
    AlbumAdapter albumAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_album);

        btnBack = findViewById(R.id.back_btn);
        layoutAlbumName = findViewById(R.id.layout_albumName);
        newAlbumName = findViewById(R.id.newAlbumName);
        btnCreateNewAlbum = findViewById(R.id.btn_addAlbum);
        btnCreateNewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiem tra ten album khong duoc bo trong
                String nameAlbum = newAlbumName.getText().toString().trim();
                if (nameAlbum.length() == 0)
                {
                    layoutAlbumName.setError("Please enter new album name!");
                    newAlbumName.requestFocus();
                    return;
                } else {
                    layoutAlbumName.setError(null);
                }
                nameAlbumCreate = nameAlbum;
                // userid tu activity main public
                Album temp = new Album(nameAlbum, userid);
                albumAdapter = new AlbumAdapter(getApplicationContext());
                albumAdapter.insertNewAlbum(temp);
                albumAdapter.close();
                //Toast.makeText(getApplicationContext(), "Add album successfully!", Toast.LENGTH_SHORT).show();
                // gui ket qua ve activity main
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "Add album successfully!");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public void closeActivity(View view){
        finish();
    }
}