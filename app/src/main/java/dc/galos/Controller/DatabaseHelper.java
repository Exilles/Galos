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
import java.util.ArrayList;
import java.util.HashMap;

import dc.galos.R;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static Toast toast;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static Context myContext;

    // данные о аккаунте авторизированного пользователя
    private static int id;
    private static String login;
    private static String password;
    private static String email;
    private static int money;
    private static int record;

    // данные о достижениях авторизированного пользователя
    private static String status;
    private static int all_levels;
    private static int all_money;
    private static int all_eating;
    private static int all_wins;

    // данные о БД
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "galos.db";
    private static final int SCHEMA = 1; // версия базы данных

    // _id записи в БД
    private static final String COLUMN_ID = "_id";

    // названия столбцов таблицы users
    private static final String TABLE_USERS = "users"; // название таблицы в бд
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_MONEY = "money";
    private static final String COLUMN_RECORD = "record";

    // названия столбцов таблицы achievements
    private static final String TABLE_ACHIEVEMENTS = "achievements"; // название таблицы в бд
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_ALL_LEVELS = "all_levels";
    private static final String COLUMN_ALL_MONEY = "all_money";
    private static final String COLUMN_ALL_EATING = "all_eating";
    private static final String COLUMN_ALL_WINS = "all_wins";
    private static final String COLUMN_ID_USER = "id_user";

    // заголовки для списка достижений
    private static String TITLE = "achievement"; // Название достижения
    private static String DESCRIPTION = "description"; // Описание достижения
    private static String STATUS = "status"; // Статус достижения
    private static String REWARD = "reward";  // Вознаграждение за достижение

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
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
    private SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static void setMyContext(Context context) {
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.create_db();
        db = databaseHelper.open();
    }

    public static void showInformation(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(myContext, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // вставка новой записи при регистрации
    public static void insertRowUsers (String _login, String _password, String _email) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LOGIN, _login);
        contentValues.put(COLUMN_PASSWORD, _password);
        contentValues.put(COLUMN_EMAIL, _email);
        contentValues.put(COLUMN_MONEY, 0);
        contentValues.put(COLUMN_RECORD, 0);

        db.insert(TABLE_USERS, null, contentValues); // создаем аккаунт

        searchRowUsers(_login, _password, null, 1); // получаем информацию об аккаунте

        //insertRowAchievements(); // создаем новый список достижений для аккаунта
    }

    // поиск введенных данных на наличие в БД
    public static int searchRowUsers(String _login, String _password, String _email, int index) {
        String query;
        int count;

        switch (index) {
            case 1: // поиск по логину и паролю
                query = String.format("SELECT \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\" FROM \"%s\" WHERE " +
                        "\"%s\" = \"%s\" AND \"%s\" = \"%s\"", COLUMN_ID, COLUMN_LOGIN, COLUMN_PASSWORD, COLUMN_EMAIL,
                        COLUMN_MONEY, COLUMN_RECORD, TABLE_USERS, COLUMN_LOGIN, _login, COLUMN_PASSWORD, _password);
                cursor = db.rawQuery(query, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    login = cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN));
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                    money = cursor.getInt(cursor.getColumnIndex(COLUMN_MONEY));
                    record = cursor.getInt(cursor.getColumnIndex(COLUMN_RECORD));
                    cursor.close();
                    return 1;
                }
                else return 0;
            case 2: //  поиск по логину
                query = String.format("SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = \"%s\"", COLUMN_ID, TABLE_USERS, COLUMN_LOGIN, _login);
                cursor = db.rawQuery(query, null);
                count = cursor.getCount();
                return count;
            case 3: // поиск по майлу
                query = String.format("SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = \"%s\"", COLUMN_ID, TABLE_USERS, COLUMN_EMAIL, _email);
                cursor = db.rawQuery(query, null);
                count = cursor.getCount();
                return count;
            default:
                return 0;
        }
    }

    // изменение информации об аккаунте
    public static void updateRowUsers(String _password, String _email) {
        ContentValues contentValues = new ContentValues();

        if (!_password.equals("")) {
            password = _password;
            contentValues.put(COLUMN_PASSWORD, _password);
        }
        if (!_email.equals("")) {
            email = _email;
            contentValues.put(COLUMN_EMAIL, _email);
        }

        db.update(TABLE_USERS, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
    }

    public static ArrayList<HashMap<String, Object>> getAchievements() {
        ArrayList<HashMap<String, Object>> achievementsList = new ArrayList<>();
        HashMap<String, Object> hashMap;

        getAchievementsData();

        String[] titleAchievements = myContext.getResources().getStringArray(R.array.title_achievements);
        String[] descriptionAchievements = myContext.getResources().getStringArray(R.array.description_achievements);
        String[] rewardAchievements = myContext.getResources().getStringArray(R.array.reward_achievements);

        for (int i = 0; i < 26; i++) {
            hashMap = new HashMap<>();
            hashMap.put(TITLE, titleAchievements[i]); // Название
            hashMap.put(DESCRIPTION, descriptionAchievements[i]); // Описание
            if (status.charAt(i) == '1') hashMap.put(STATUS, myContext.getResources().getString(R.string.received)); // Статус
            else hashMap.put(STATUS, myContext.getResources().getString(R.string.not_received)); // Статус
            hashMap.put(REWARD, myContext.getResources().getString(R.string.reward) + " " + rewardAchievements[i] + "$"); // Вознаграждение
            achievementsList.add(hashMap);
        }

        return achievementsList;
    }

    private static void getAchievementsData(){
        String query = String.format("SELECT \"%s\", \"%s\", \"%s\", \"%s\", \"%s\" FROM \"%s\" WHERE \"%s\" = \"%s\"",
                COLUMN_STATUS, COLUMN_ALL_LEVELS, COLUMN_ALL_MONEY, COLUMN_ALL_EATING, COLUMN_ALL_WINS, TABLE_ACHIEVEMENTS,
                COLUMN_ID_USER, id);
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
        all_levels = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_LEVELS));
        all_money = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_MONEY));
        all_eating = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_EATING));
        all_wins = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_WINS));

        cursor.close();
    }

    // увеличиваем общее количество уровней в таблице achievements
    public static void updateAllLevels(){
        ContentValues contentValues = new ContentValues();
        all_levels ++;
        contentValues.put(COLUMN_ALL_LEVELS, all_levels);
        db.update(TABLE_ACHIEVEMENTS, contentValues,COLUMN_ID_USER + "= ?", new String[]{Integer.toString(id)});
    }

    // увеличиваем общее количество денег и побед в таблице achievements
    public static void updateAllMoneyAndWins(int _reward){
        ContentValues contentValues = new ContentValues();
        all_money += _reward;
        all_wins ++;
        contentValues.put(COLUMN_ALL_MONEY, all_money);
        contentValues.put(COLUMN_ALL_WINS, all_wins);
        db.update(TABLE_ACHIEVEMENTS, contentValues,COLUMN_ID_USER + "= ?", new String[]{Integer.toString(id)});
    }

    // увеличиваем общее количество съеденных в таблице achievements
    public static void updateAllEating(){
        ContentValues contentValues = new ContentValues();
        all_eating ++;
        contentValues.put(COLUMN_ALL_EATING, all_eating);
        db.update(TABLE_ACHIEVEMENTS, contentValues,COLUMN_ID_USER + "= ?", new String[]{Integer.toString(id)});
    }

    // изменение количества денег и рекорда пользователя
    public static void updateMoneyAndRecord(int _money, int _record) {
        ContentValues contentValues = new ContentValues();

        money += _money;
        contentValues.put(COLUMN_MONEY, money);

        if (record < _record) {
            record = _record;
            contentValues.put(COLUMN_RECORD, _record);
        }

        db.update(TABLE_USERS, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
    }

    // изменение количества денег из-за покупки бонуса
    public static void buyBonus(int _money) {
        ContentValues contentValues = new ContentValues();

        money -= _money;
        contentValues.put(COLUMN_MONEY, money);

        db.update(TABLE_USERS, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
    }

    public static int getSession() {
        return id;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static String getEmail() {
        return email;
    }

    public static int getMoney() {
        return money;
    }

    public static int getRecord() {
        return record;
    }
}
