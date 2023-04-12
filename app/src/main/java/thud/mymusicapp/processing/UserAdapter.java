package thud.mymusicapp.processing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import thud.mymusicapp.data.DbHelper;
import thud.mymusicapp.data.User;

public class UserAdapter {
    private DbHelper myDbHelper;
    private SQLiteDatabase db;
    private String[] allColumns = {"id", "username", "password", "fullname", "email"};

    public UserAdapter(Context context) {
        myDbHelper = new DbHelper(context);
        db = myDbHelper.getWritableDatabase();
    }

    public long insertUser(User user){
        db = myDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUserName());
        values.put("password", user.getPassword());
        values.put("fullname", user.getFullName());
        values.put("email", user.getEmail());
        return db.insert("user", null, values);
    }

    public int updateUser(int id, String fullName, String email)
    {
        ContentValues values = new ContentValues();
        values.put("fullname", fullName);
        values.put("email", email);
        return db.update("user", values, "id = " + id, null);
    }

    public int deleteUser(int id){
        return db.delete("user", "id = " + id, null);
    }

    public User cursorToUser(Cursor cursor){
        User values = new User();
        values.setId(cursor.getInt(0));
        values.setUserName(cursor.getString(1));
        values.setPassword(cursor.getString(2));
        values.setFullName(cursor.getString(3));
        values.setEmail(cursor.getString(4));
        return values;
    }

    public List<User> ListAllUser(){
        List<User> lstUser = new ArrayList<User>();
        Cursor cursor = db.query("user", allColumns, null,
                null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                User values = cursorToUser(cursor);
                lstUser.add(values);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lstUser;
    }

    public Boolean isExistUser(String username){
        Boolean exist = false;
        List<User> lstUser = ListAllUser();
        int i = 0;
        while (!exist && i < lstUser.size())
            if (lstUser.get(i).getUserName().equalsIgnoreCase(username))
                exist =  true;
            else
                i++;
        return exist;
    }

    public void close(){
        db.close();
        myDbHelper.close();
    }
}
