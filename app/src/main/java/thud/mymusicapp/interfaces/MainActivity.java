package thud.mymusicapp.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.interfaces.album.AddNewAlbum;
import thud.mymusicapp.interfaces.album.AlbumFragment;
import thud.mymusicapp.interfaces.song.SongsFragment;
import thud.mymusicapp.interfaces.user.Login;
import thud.mymusicapp.processing.AlbumAdapter;
import thud.mymusicapp.processing.MusicAdapter;
import thud.mymusicapp.R;
import thud.mymusicapp.processing.UserAdapter;
import thud.mymusicapp.data.Album;
import thud.mymusicapp.data.MusicFiles;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    MusicAdapter musicAdapter;
    UserAdapter userAdapter;
    AlbumAdapter albumAdapter;
    private static final String PERMISSION_GRANTED_KEY = "permission_granted";
    SharedPreferences prefs;
    static ArrayList<MusicFiles> musicFiles;
    public static final int REQUEST_CODE = 1;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 1;
    // khai bao nut dung de reload activity nay o public de co the truy cap o cac lop khac
    public static FloatingActionButton reload;
    public static int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("firstTime", MODE_PRIVATE);
        musicAdapter = new MusicAdapter(this);
        userAdapter = new UserAdapter(this);
        albumAdapter = new AlbumAdapter(this);
        SharedPreferences inforUser = getSharedPreferences("userInfo", MODE_PRIVATE);
        userid = inforUser.getInt("userid", 0);
        setContentView(R.layout.activity_main);
        // goi ham yeu can quyen o lan dau tien
        permission();
        // lay shared preferences luu tru trang thai neu vao lan dau tien
        boolean firstAdd = prefs.getBoolean(PERMISSION_GRANTED_KEY, false);
        if (firstAdd == false)
        {
            // them album device chua toan bo nhac trong thiet bi nhung chi them mot lan
            addAlbum();
        }
        reload = findViewById(R.id.btn_reload_ac);
        // nut reload ko can hien thi chi tao ra khi them xua xoa de cap nhap view
        reload.setVisibility(View.GONE);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initViewPager();
            }
        });
    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        else
        {
            // neu quyen truy cap bo nho duoc cap o lan hoi dau tien
            // tim kiem va them toan bo file mp3 vao csdl (chi them 1 lan)
            getAllAudio(this);
            // sua lai trang thai da vao lan dau tien
            prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, true).apply();
            // ham setup view cho main activity
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // neu lan dau tien quyen bi tu choi thi tiep tuc yeu cau quuyen truy cap bo nho
                getAllAudio(this);
                prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, true).apply();
                initViewPager();
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    // ham setup view cho main activity
    public void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(), "Songs");
        viewPagerAdapter.addFragment(new AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragment(Fragment fragment, String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    // ham lay tim va them cac thong tin file mp3 vao csdl
    public void getAllAudio(Context context)
    {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor cursor = context.getContentResolver().query(uri, projection,
                selection, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int album = cursor.getInt(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                int albumid = 1;

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, albumid);
                boolean firstAdd = prefs.getBoolean(PERMISSION_GRANTED_KEY, false);
                if (firstAdd == false)
                    musicAdapter.insertMusicFile(musicFiles);
            }
            cursor.close();
        }
    }

    // them album chua toan bo nhac trong thiet bi
    public void addAlbum(){
        Album tempAlbum = new Album("Device", userid);
        albumAdapter.insertNewAlbum(tempAlbum);
        albumAdapter.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem addAlbum = menu.findItem(R.id.add_album_option);
        // btn hinh dau + tren thanh action bar de them album
        // click vao goi activity them album
        addAlbum.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent intent = new Intent(getApplicationContext(), AddNewAlbum.class);
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
                return false;
            }
        });

        // btn hinh reload tren thanh action bar
        // click vao de khi da tai them file mp3 moi, no se lam moi csdl them lai album device
        MenuItem searchFileAgain = menu.findItem(R.id.reload);
        searchFileAgain.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, false).apply();
                DbHelper myDbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = myDbHelper.getWritableDatabase();
                db.delete("songs", "albumid = " + 1, null);
                myDbHelper.close();
                db.close();
                prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, false).apply();
                getAllAudio(MainActivity.this);
                prefs.edit().putBoolean(PERMISSION_GRANTED_KEY, true).apply();
                initViewPager();
                return false;
            }
        });

        // btn hinh canh cua tren thanh action bar
        // click vao khi user muon log out
        MenuItem logoutBtn = menu.findItem(R.id.logout);
        logoutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(MainActivity.this);
                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Good bye!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                logoutDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                logoutDialog.setTitle("Log out?");
                logoutDialog.setMessage("Do you really want to log out?");
                logoutDialog.create().show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {
            // Get the result data from the intent
            String result = data.getStringExtra("result");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            reload.performClick();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}