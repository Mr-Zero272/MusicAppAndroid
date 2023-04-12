package thud.mymusicapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MyMusicApp.db";
    private static final int DB_VERSION = 1;

    // Table User
    // Table 1
    public static final String TABLE1_NAME = "user";
    public static final String TABLE1_COL_ID = "id";
    public static final String TABLE1_COL_USERNAME = "username";
    public static final String TABLE1_COL_PASSWORD = "password";
    public static final String TABLE1_COL_FULLNAME = "fullname";
    public static final String TABLE1_COL_EMAIL = "email";

    // Table Album
    // Table 2
    public static final String TABLE2_NAME = "album";
    public static final String TABLE2_COL_ID = "id";
    public static final String TABLE2_COL_ALBUMNAME = "albumname";
    public static final String TABLE2_COL_USERID = "userid";
    public static final String TABLE2_COL_FK_USERID = "userid";
    // Table Songs
    // Table 3
    public static final String TABLE3_NAME = "songs";
    public static final String TABLE3_COL_ID = "id";
    public static final String TABLE3_COL_TITLE = "title";
    public static final String TABLE3_COL_PATH = "path";
    public static final String TABLE3_COL_ARTIST = "artist";
    public static final String TABLE3_COL_DURATION = "duration";
    public static final String TABLE3_COL_AlBUMID = "albumid";
    public static final String TABLE3_COL_FK_ALBUMID = "albumid";

    // SQL statements
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE1_NAME + "("
                    + TABLE1_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TABLE1_COL_USERNAME + " TEXT, "
                    + TABLE1_COL_PASSWORD + " TEXT, "
                    + TABLE1_COL_FULLNAME + " TEXT, "
                    + TABLE1_COL_EMAIL + " TEXT);";

    private static final String CREATE_TABLE_ALBUM =
            "CREATE TABLE " + TABLE2_NAME + "("
                    + TABLE2_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TABLE2_COL_ALBUMNAME + " TEXT, "
                    + TABLE2_COL_USERID + " INTEGER, "
                    +  "FOREIGN KEY(" + TABLE2_COL_FK_USERID + ") REFERENCES " + TABLE1_NAME + "(" + TABLE1_COL_ID + "))";

    private static final String CREATE_TABLE_SONGS =
            "CREATE TABLE " + TABLE3_NAME + "("
                    + TABLE3_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TABLE3_COL_TITLE + " TEXT, "
                    + TABLE3_COL_PATH + " TEXT, "
                    + TABLE3_COL_ARTIST + " TEXT, "
                    + TABLE3_COL_DURATION + " TEXT, "
                    + TABLE3_COL_AlBUMID + " INTEGER, "
                    +  "FOREIGN KEY(" + TABLE3_COL_FK_ALBUMID + ") REFERENCES " + TABLE2_NAME + "(" + TABLE2_COL_ID + "))";

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ALBUM);
        db.execSQL(CREATE_TABLE_SONGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        onCreate(db);
    }
}
