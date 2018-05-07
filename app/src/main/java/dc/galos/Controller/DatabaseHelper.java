package dc.galos.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "galos.db";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MONEY = "money";
    public static final String COLUMN_RECORD = "record";
    private static Toast toast;
    private Context myContext;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static void showInformation(Context context, String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // вставка новой записи при регистрации
    public static void insertRow (Context context, String login, String password, String email) {
        databaseHelper = new DatabaseHelper(context);

        // создаем базу данных
        databaseHelper.create_db();

        // открываем подключение
        db = databaseHelper.open();

        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о пользователе является значениями ключей
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_LOGIN, login);
        contentValues.put(DatabaseHelper.COLUMN_PASSWORD, password);
        contentValues.put(DatabaseHelper.COLUMN_EMAIL, email);
        contentValues.put(DatabaseHelper.COLUMN_MONEY, 0);
        contentValues.put(DatabaseHelper.COLUMN_RECORD, 0);

        db.insert(DatabaseHelper.TABLE, null, contentValues);
    }

    // поиск введенных данных на наличие в БД
    public static int searchRow(Context context, String login, String password, String email, int index) {
        databaseHelper = new DatabaseHelper(context);
        String query;

        // создаем базу данных
        databaseHelper.create_db();

        // открываем подключение
        db = databaseHelper.open();

        switch (index) {
            case 1: // поиск по логину и паролю
                query = String.format("SELECT \"%s\" from \"%s\" WHERE \"%s\" = \"%s\" AND \"%s\" = \"%s\"", DatabaseHelper.COLUMN_ID, DatabaseHelper.TABLE, DatabaseHelper.COLUMN_LOGIN, login, DatabaseHelper.COLUMN_PASSWORD, password);
                cursor = db.rawQuery(query, null);
                return cursor.getCount();
            case 2: //  поиск по логину
                query = String.format("SELECT \"%s\" from \"%s\" WHERE \"%s\" = \"%s\"", DatabaseHelper.COLUMN_ID, DatabaseHelper.TABLE, DatabaseHelper.COLUMN_LOGIN, login);
                cursor = db.rawQuery(query, null);
                return cursor.getCount();
            case 3: // поиск по майлу
                query = String.format("SELECT \"%s\" from \"%s\" WHERE \"%s\" = \"%s\"", DatabaseHelper.COLUMN_ID, DatabaseHelper.TABLE, DatabaseHelper.COLUMN_EMAIL, email);
                cursor = db.rawQuery(query, null);
                return cursor.getCount();
            default:
                return 0;
        }

    }
}
