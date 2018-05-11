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

public class DatabaseHelper extends SQLiteOpenHelper {
    private static Toast toast;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static Context myContext;

    // данные о авторизированном пользователе
    private static int id;
    private static String login;
    private static String password;
    private static String email;
    private static int money;
    private static int record;

    // данные о БД
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "galos.db";
    private static final int SCHEMA = 1; // версия базы данных

    // названия столбцов таблицы users
    private static final String TABLE_USERS = "users"; // название таблицы в бд
    private static final String COLUMN_USERS_ID = "_id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_MONEY = "money";
    private static final String COLUMN_RECORD = "record";

    // названия столбцов таблицы achievements
    private static final String TABLE_ACHIEVEMENTS = "achievements"; // название таблицы в бд
    private static final String COLUMN_ACHIEVEMENTS_ID = "_id";
    private static final String COLUMN_TITTLE = "tittle";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_REWARD = "reward";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_COUNT_LEVELS = "count_levels";
    private static final String COLUMN_COUNT_MONEY = "count_money";
    private static final String COLUMN_COUNT_EATING = "count_eating";
    private static final String COLUMN_COUNT_WINS = "count_wins";
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

        insertRowAchievements(); // создаем новый список достижений для аккаунта
    }

    // поиск введенных данных на наличие в БД
    public static int searchRowUsers(String _login, String _password, String _email, int index) {
        String query;
        int count;

        switch (index) {
            case 1: // поиск по логину и паролю
                query = String.format("SELECT \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\" FROM \"%s\" WHERE " +
                        "\"%s\" = \"%s\" AND \"%s\" = \"%s\"", COLUMN_USERS_ID, COLUMN_LOGIN, COLUMN_PASSWORD, COLUMN_EMAIL,
                        COLUMN_MONEY, COLUMN_RECORD, TABLE_USERS, COLUMN_LOGIN, _login, COLUMN_PASSWORD, _password);
                cursor = db.rawQuery(query, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_USERS_ID));
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
                query = String.format("SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = \"%s\"", COLUMN_USERS_ID, TABLE_USERS, COLUMN_LOGIN, _login);
                cursor = db.rawQuery(query, null);
                count = cursor.getCount();
                return count;
            case 3: // поиск по майлу
                query = String.format("SELECT \"%s\" FROM \"%s\" WHERE \"%s\" = \"%s\"", COLUMN_USERS_ID, TABLE_USERS, COLUMN_EMAIL, _email);
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

        db.update(TABLE_USERS, contentValues,COLUMN_USERS_ID + "= ?", new String[]{Integer.toString(id)});
    }

    public static ArrayList<HashMap<String, Object>> getAchievements() {
        ArrayList<HashMap<String, Object>> achievementsList = new ArrayList<>();
        HashMap<String, Object> hashMap;

        String query = String.format("SELECT \"%s\", \"%s\", \"%s\" FROM \"%s\"", COLUMN_TITTLE,
                COLUMN_DESCRIPTION, COLUMN_REWARD, TABLE_ACHIEVEMENTS);
        cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            hashMap = new HashMap<>();
            hashMap.put(TITLE, cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITTLE))); // Название
            hashMap.put(DESCRIPTION, cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))); // Описание
            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION)).equals("true")) hashMap.put(STATUS, "Получено"); // Статус
            else hashMap.put(STATUS, "Не получено"); // Статус
            hashMap.put(REWARD, "Награда: " + cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_REWARD)) + "$"); // Вознаграждение
            achievementsList.add(hashMap);
        }
        cursor.close();

        return achievementsList;
    }

    // изменение количества денег и рекорда пользователя
    public static void updateReward(int _money, int _record) {
        ContentValues contentValues = new ContentValues();

        money += _money;
        contentValues.put(COLUMN_MONEY, money);

        if (record < _record) {
            record = _record;
            contentValues.put(COLUMN_RECORD, _record);
        }

        db.update(TABLE_USERS, contentValues,COLUMN_USERS_ID + "= ?", new String[]{Integer.toString(id)});
    }

    // изменение количества денег из-за покупки бонуса
    public static void buyBonus(int _money) {
        ContentValues contentValues = new ContentValues();

        money -= _money;
        contentValues.put(COLUMN_MONEY, money);

        db.update(TABLE_USERS, contentValues,COLUMN_USERS_ID + "= ?", new String[]{Integer.toString(id)});
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

    public static void insertRowAchievements() {

        /*ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITTLE, "Новичёк"); // 1
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 5 уровней подряд");
        contentValues.put(COLUMN_REWARD, 5);
        contentValues.put(COLUMN_STATUS, "false");
        contentValues.put(COLUMN_COUNT_LEVELS, 0);
        contentValues.put(COLUMN_COUNT_MONEY, 0);
        contentValues.put(COLUMN_COUNT_EATING, 0);
        contentValues.put(COLUMN_COUNT_WINS, 0);
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();*/

        String query = String.format("INSERT INTO \"%s\" VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", " +
                "\"%s\", \"%s\", \"%s\")", TABLE_ACHIEVEMENTS, null, "Новичёк", "Пройдите 5 уровней подряд", 5, "false", null, null, null, null, id);
        cursor = db.rawQuery(query, null);

        /*contentValues.put(COLUMN_TITTLE, "Знаток"); // 2
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 15 уровней подряд");
        contentValues.put(COLUMN_REWARD, 15);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Профи"); // 3
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 30 уровней подряд");
        contentValues.put(COLUMN_REWARD, 30);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Мастер"); // 4
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 60 уровней подряд");
        contentValues.put(COLUMN_REWARD, 60);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Гений"); // 5
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 120 уровней подряд");
        contentValues.put(COLUMN_REWARD, 120);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Высший разум"); // 6
        contentValues.put(COLUMN_DESCRIPTION, "Пройдите 250 уровней подряд");
        contentValues.put(COLUMN_REWARD, 250);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Начало положено!"); // 7
        contentValues.put(COLUMN_DESCRIPTION, "Сыграйте в свой первый уровень");
        contentValues.put(COLUMN_REWARD, 5);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, 1);
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Тернистый путь"); // 8
        contentValues.put(COLUMN_DESCRIPTION, "Сыграйте в 50 уровней");
        contentValues.put(COLUMN_REWARD, 25);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, 50);
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Длинная тропа"); // 9
        contentValues.put(COLUMN_DESCRIPTION, "Сыграйте в 200 уровней");
        contentValues.put(COLUMN_REWARD, 100);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, 200);
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Без преград"); // 10
        contentValues.put(COLUMN_DESCRIPTION, "Сыграйте в 500 уровней");
        contentValues.put(COLUMN_REWARD, 250);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, 500);
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Дегустатор"); // 11
        contentValues.put(COLUMN_DESCRIPTION, "Поглотите 1 круг");
        contentValues.put(COLUMN_REWARD, 5);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, 1);
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Вкуснятина"); // 12
        contentValues.put(COLUMN_DESCRIPTION, "Поглотите 50 кругов");
        contentValues.put(COLUMN_REWARD, 25);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, 50);
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Обжора"); // 13
        contentValues.put(COLUMN_DESCRIPTION, "Поглотите 300 кругов");
        contentValues.put(COLUMN_REWARD, 150);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, 300);
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Чёрная дыра"); // 14
        contentValues.put(COLUMN_DESCRIPTION, "Поглотите 700 кругов");
        contentValues.put(COLUMN_REWARD, 350);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, 700);
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Хоть что-то..."); // 15
        contentValues.put(COLUMN_DESCRIPTION, "Заработайте 20 баксов за всю игру");
        contentValues.put(COLUMN_REWARD, 5);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, 20);
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Хорошие деньги"); // 16
        contentValues.put(COLUMN_DESCRIPTION, "Заработайте 200 баксов за всю игру");
        contentValues.put(COLUMN_REWARD, 50);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, 200);
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Бабосики"); // 17
        contentValues.put(COLUMN_DESCRIPTION, "Заработайте 600 баксов за всю игру");
        contentValues.put(COLUMN_REWARD, 150);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, 150);
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Жаль, что не реал..."); // 18
        contentValues.put(COLUMN_DESCRIPTION, "Заработайте 3000 баксов за всю игру");
        contentValues.put(COLUMN_REWARD, 750);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, 3000);
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Бережливый"); // 19
        contentValues.put(COLUMN_DESCRIPTION, "Накопите 200 баксов");
        contentValues.put(COLUMN_REWARD, 50);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Экономный"); // 20
        contentValues.put(COLUMN_DESCRIPTION, "Накопите 1000 баксов");
        contentValues.put(COLUMN_REWARD, 250);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Еврейская душа"); // 21
        contentValues.put(COLUMN_DESCRIPTION, "Накопите 3000 баксов");
        contentValues.put(COLUMN_REWARD, 750);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Победитель"); // 22
        contentValues.put(COLUMN_DESCRIPTION, "Победите в первом уровне");
        contentValues.put(COLUMN_REWARD, 5);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, 1);
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Звезда"); // 23
        contentValues.put(COLUMN_DESCRIPTION, "Победите в 50 уровнях");
        contentValues.put(COLUMN_REWARD, 20);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, 50);
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Гладиатор"); // 24
        contentValues.put(COLUMN_DESCRIPTION, "Победите в 200 уровнях");
        contentValues.put(COLUMN_REWARD, 50);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, 200);
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Чемпион"); // 25
        contentValues.put(COLUMN_DESCRIPTION, "Победите в 700 уровнях");
        contentValues.put(COLUMN_REWARD, 175);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, 700);
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();

        contentValues.put(COLUMN_TITTLE, "Бог"); // 26
        contentValues.put(COLUMN_DESCRIPTION, "Получите все достижения");
        contentValues.put(COLUMN_REWARD, 5000);
        contentValues.put(COLUMN_STATUS, false);
        contentValues.put(COLUMN_COUNT_LEVELS, "null");
        contentValues.put(COLUMN_COUNT_MONEY, "null");
        contentValues.put(COLUMN_COUNT_EATING, "null");
        contentValues.put(COLUMN_COUNT_WINS, "null");
        contentValues.put(COLUMN_ID_USER, id);
        db.insert(DatabaseHelper.TABLE_ACHIEVEMENTS, null, contentValues);
        contentValues.clear();*/

        db.close();
    }
}
