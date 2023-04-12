package thud.mymusicapp.interfaces.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import thud.mymusicapp.R;
import thud.mymusicapp.data.User;
import thud.mymusicapp.processing.UserAdapter;

public class Register extends AppCompatActivity {

    TextInputLayout layoutUsername, layoutPassword, layoutConfirmPassword, layoutFullName, layoutEmail;
    TextInputEditText txtUsername, txtPassword, txtConfirmPassword, txtFullName, txtEmail;
    Button btnRegister;
    String strUsername, strPassword = "", strConfirmPassword, strFullName, strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        layoutUsername = findViewById(R.id.layout_username);
        layoutPassword = findViewById(R.id.layout_password);
        layoutConfirmPassword = findViewById(R.id.layout_confirm_password);
        layoutFullName = findViewById(R.id.layout_fullname);
        layoutEmail = findViewById(R.id.layout_email);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        txtConfirmPassword = findViewById(R.id.confirm_password);
        txtFullName = findViewById(R.id.fullname);
        txtEmail = findViewById(R.id.email);
        btnRegister = findViewById(R.id.button_register);

        // khi click nut dang ky
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kiem tra cac truong khong rong
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

                // confirm password phai match voi password o tren
                String confirmPassword = txtConfirmPassword.getText().toString().trim();
                if (confirmPassword.length() == 0)
                {
                    layoutConfirmPassword.setError("This field is required!");
                    txtConfirmPassword.requestFocus();
                    return;
                } else if (!confirmPassword.equalsIgnoreCase(strPassword)) {
                    layoutConfirmPassword.setError("Confirmation password does not match!");
                    txtConfirmPassword.requestFocus();
                    return;
                } else {
                    layoutConfirmPassword.setError(null);
                }
                strConfirmPassword = confirmPassword;

                String fullname = txtFullName.getText().toString().trim();
                if (fullname.length() == 0)
                {
                    layoutFullName.setError("This field is required!");
                    txtFullName.requestFocus();
                    return;
                } else {
                    layoutFullName.setError(null);
                }
                strFullName = fullname;

                // ngoai ra con kiem tra email dung ding dang xx@xx.xx
                String email = txtEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.length() == 0)
                {
                    layoutEmail.setError("This field is required!");
                    txtEmail.requestFocus();
                    return;
                } else if (!email.matches(emailPattern)) {
                    layoutEmail.setError("This email is not valid!");
                    txtEmail.requestFocus();
                    return;
                } else {
                    layoutEmail.setError(null);
                }
                strEmail = email;

                UserAdapter userAdapter = new UserAdapter(getApplicationContext());
                // kiem tra neu user da ton tai
                if (userAdapter.isExistUser(strUsername))
                {
                    Toast.makeText(getApplicationContext(), "This username already exists.", Toast.LENGTH_SHORT).show();
                    layoutUsername.setError("This username already exists.");
                    userAdapter.close();
                    return;
                } else {
                    layoutUsername.setError(null);
                    User newUser = new User(strUsername, strPassword, strFullName, strEmail);
                    userAdapter.insertUser(newUser);
                    userAdapter.close();
                    Toast.makeText(getApplicationContext(), "Register successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    // khi click vao textView Login
    public void startLoginForm(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}