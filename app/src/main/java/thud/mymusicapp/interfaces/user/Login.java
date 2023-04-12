package thud.mymusicapp.interfaces.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import thud.mymusicapp.R;
import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.interfaces.MainActivity;

public class Login extends AppCompatActivity {
    TextInputLayout layoutUsername, layoutPassword;
    TextInputEditText txtUsername, txtPassword;
    CheckBox rememberme;
    Button btnLogin;
    TextView btnRegister, errorLogin;
    String strUsername, strPassword, reUsername, rePassword;
    int reId;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        layoutUsername = findViewById(R.id.layout_username);
        layoutPassword = findViewById(R.id.layout_password);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        rememberme = findViewById(R.id.chk_rememberme);
        btnLogin = findViewById(R.id.btn_login);
        errorLogin = findViewById(R.id.error_login);

        // kiem tra neu truoc do user co check remember username and password thi set vao username va password
        pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        reId = pref.getInt("userid", 0);
        reUsername = pref.getString("username", "");
        rePassword = pref.getString("password", "");

        txtUsername.setText(reUsername);
        txtPassword.setText(rePassword);
        errorLogin.setText("");

        // khi click nut login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiem tra khong rong
                String username = txtUsername.getText().toString().trim();
                if (username.length() == 0)
                {
                    layoutUsername.setError("This field is required!");
                    txtUsername.requestFocus();
                    return;
                } else {
                    layoutUsername.setError(null);
                }
                strUsername = username;

                String password = txtPassword.getText().toString().trim();
                if (password.length() == 0)
                {
                    layoutPassword.setError("This field is required!");
                    txtPassword.requestFocus();
                    return;
                } else {
                    layoutPassword.setError(null);
                }
                strPassword = password;

                // check username and password
                if (authenticateUser(strUsername, strPassword))
                {
                    int userid = getUserId(strUsername, strPassword);
                    // neu nut remeber checked
                    // luu thong tin cho lan dang nhap sau
                    if (rememberme.isChecked())
                    {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userid", userid);
                        editor.putString("username", strUsername);
                        editor.putString("password", strPassword);
                        editor.apply();
                    } else { // neu khong check luu thong tin la "" (chuoi rong)
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userid", userid);
                        editor.putString("username", "");
                        editor.putString("password", "");
                        editor.apply();
                    }
                    errorLogin.setText("");
                    Toast.makeText(getApplicationContext(), "Log in successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    errorLogin.setText("The username or password is incorrect!!");
                }
            }
        });
    }

    // khi click vao TextView Register
    public void startRegisterForm(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }

    public int getUserId(String username, String password)
    {
        int userid;
        DbHelper myDbHelper = new DbHelper(this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        String[] columns = {"id"};
        String selection = "username=? AND password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        userid = (int) cursor.getInt(0);
        myDbHelper.close();
        cursor.close();
        db.close();
        return userid;
    }

    public boolean authenticateUser(String username, String password) {
        DbHelper myDbHelper = new DbHelper(this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        String[] columns = {"id"};
        String selection = "username=? AND password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        myDbHelper.close();
        cursor.close();
        db.close();
        return count > 0;
    }

}