package dc.galos.Controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
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
    private static final String COLUMN_REMEMBER = "remember";

    // названия столбцов таблицы achievements
    private static final String TABLE_ACHIEVEMENTS = "achievements"; // название таблицы в бд
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_ALL_LEVELS = "all_levels";
    private static final String COLUMN_ALL_MONEY = "all_money";
    private static final String COLUMN_ALL_EATING = "all_eating";
    private static final String COLUMN_ALL_WINS = "all_wins";
    private static final String COLUMN_ID_USER = "id_user";

    // названия столбцов таблицы resume
    private static final String TABLE_RESUME = "resume"; // название таблицы в бд
    private static final String COLUMN_MODE = "mode";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_ALL_REWARDS = "all_rewards";

    // данные из strings.xml о названии, описании и награде за достижения
    private static String[] titleAchievements;
    private static String[] descriptionAchievements;
    private static String[] rewardAchievements;

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

    // данные о последней игре авторизированного пользователя
    private static int mode;
    private static int score;
    private static int all_rewards;

    // заголовки для списка достижений
    private static String TITLE = "achievement"; // Название достижения
    private static String DESCRIPTION = "description"; // Описание достижения
    private static String STATUS = "status"; // Статус достижения
    private static String REWARD = "reward";  // Вознаграждение за достижение

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
        titleAchievements = myContext.getResources().getStringArray(R.array.title_achievements);
        descriptionAchievements = myContext.getResources().getStringArray(R.array.description_achievements);
        rewardAchievements = myContext.getResources().getStringArray(R.array.reward_achievements);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    private void create_db(){
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

    public static void showInformation(String _text, int _y) {
        if (toast != null) {
            toast.cancel();
        }
        View view;
        TextView text;
        toast = Toast.makeText(myContext, _text, Toast.LENGTH_SHORT);
        view = toast.getView();
        text = view.findViewById(android.R.id.message);
        text.setTextColor(myContext.getResources().getColor(R.color.colorAccent));
        view.setBackground(ContextCompat.getDrawable(myContext, R.drawable.toast_border));
        text.setTypeface(Typeface.createFromAsset(myContext.getResources().getAssets(), "a_futurica_extrabold.ttf"));
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 70, _y);
        toast.show();
    }

    public static void showInformation(String _text) {
        if (toast != null) {
            toast.cancel();
        }
        View view;
        TextView text;
        toast = Toast.makeText(myContext, _text, Toast.LENGTH_SHORT);
        view = toast.getView();
        text = view.findViewById(android.R.id.message);
        text.setTextColor(myContext.getResources().getColor(R.color.colorAccent));
        view.setBackground(ContextCompat.getDrawable(myContext, R.drawable.toast_border));
        text.setTypeface(Typeface.createFromAsset(myContext.getResources().getAssets(), "a_futurica_extrabold.ttf"));
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private static void showAchievement(String _text) {
        if (toast != null) {
            toast.cancel();
        }
        View view;
        TextView text;
        toast = Toast.makeText(myContext, _text, Toast.LENGTH_SHORT);
        view = toast.getView();
        text = view.findViewById(android.R.id.message);
        text.setTextColor(myContext.getResources().getColor(R.color.black));
        view.setBackground(ContextCompat.getDrawable(myContext, R.drawable.toast_border));
        text.setWidth(700);
        text.setTypeface(Typeface.createFromAsset(myContext.getResources().getAssets(), "a_futurica_extrabold.ttf"));
        text.setTextSize(20);
        text.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // получаем данные об аккаунте
    public static void getGuestData(){
        String query = String.format("SELECT \"%s\", \"%s\" FROM \"%s\"", COLUMN_MONEY, COLUMN_RECORD, TABLE_USERS);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        id = 1;
        login = "Гость";
        money = cursor.getInt(cursor.getColumnIndex(COLUMN_MONEY));
        record = cursor.getInt(cursor.getColumnIndex(COLUMN_RECORD));

        cursor.close();
    }

    // получаем данные о достижениях аккаунта
    public static void getAchievementsGuest(){
        String query = String.format("SELECT * FROM \"%s\" WHERE \"%s\" = \"%s\"", TABLE_ACHIEVEMENTS, COLUMN_ID_USER, id);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
        all_levels = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_LEVELS));
        all_money = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_MONEY));
        all_eating = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_EATING));
        all_wins = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_WINS));

        cursor.close();
    }

    // получаем данные о достижениях аккаунта
    public static void getResumeGuest(){
        String query = String.format("SELECT * FROM \"%s\" WHERE \"%s\" = \"%s\"", TABLE_RESUME, COLUMN_ID_USER, id);

        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        mode = cursor.getInt(cursor.getColumnIndex(COLUMN_MODE));
        score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
        all_rewards = cursor.getInt(cursor.getColumnIndex(COLUMN_ALL_REWARDS));

        cursor.close();
    }

    // получаем данные об аккаунте
    public static void getUserData(int _id, String _login, String _password, String _email, int _money, int _record){
        id = _id;
        login = _login;
        password = _password;
        email = _email;
        money = _money;
        record = _record;
    }

    // получаем данные о достижениях аккаунта
    public static void getAchievementsUser(String _status, int _all_levels, int _all_money, int _all_eating, int _all_wins){
        status = _status;
        all_levels = _all_levels;
        all_money = _all_money;
        all_eating = _all_eating;
        all_wins = _all_wins;
    }

    // получаем данные о достижениях аккаунта
    public static void getResumeUser( int _mode, int _score, int _all_rewards){
        mode = _mode;
        score = _score;
        all_rewards = _all_rewards;
    }

    public static void rememberOrForgetUser(String _remember){
        ContentValues contentValues = new ContentValues();
        if (_remember.equals("true")) contentValues.put(COLUMN_REMEMBER, "true");
        else contentValues.put(COLUMN_REMEMBER, "false");
        db.update(TABLE_USERS, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
    }

    public static boolean searchRememberUser(){
        String query = String.format("SELECT * FROM \"%s\" WHERE " +
                        "\"%s\" = 'true'", TABLE_USERS, COLUMN_REMEMBER);
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

            getAchievementsGuest(); // получение данных о его достижениях
            getResumeGuest();

            return true;
        }
        else return false;
    }

    public static ArrayList<HashMap<String, Object>> getAchievements() {
        ArrayList<HashMap<String, Object>> achievementsList = new ArrayList<>();
        HashMap<String, Object> hashMap;

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

    // обновляет status в таблице achievements
    private static void updateStatus(int position){
        if (status.charAt(position) != '1') {

            StringBuilder newStatus = new StringBuilder(status);
            newStatus.setCharAt(position, '1');
            status = String.valueOf(newStatus);

            money += Integer.parseInt(rewardAchievements[position]);
            showAchievement("Получено достижение: " + titleAchievements[position]); // отображение сообщения о получении достижения
        }
    }

    // проверка получения достижений по рекорду
    private static void checkRecord(){
        switch (record){
            case 5:
                updateStatus(0);
                break;
            case 15:
                updateStatus(1);
                break;
            case 30:
                updateStatus(2);
                break;
            case 60:
                updateStatus(3);
                break;
            case 120:
                updateStatus(4);
                break;
            case 250:
                updateStatus(5);
                break;
        }
    }

    // проверка получения достижений по общему количеству сыгранных уровней
    private static void checkAllLevels(){
        switch (all_levels){
            case 1:
                updateStatus(6);
                break;
            case 50:
                updateStatus(7);
                break;
            case 200:
                updateStatus(8);
                break;
            case 500:
                updateStatus(9);
                break;
        }
    }

    // проверка получения достижений по общему количеству поглощенных кругов
    private static void checkAllEating(){
        switch (all_eating){
            case 1:
                updateStatus(10);
                break;
            case 50:
                updateStatus(11);
                break;
            case 300:
                updateStatus(12);
                break;
            case 700:
                updateStatus(13);
                break;
        }
    }

    // проверка получения достижений по общему количеству денег за всю игру
    private static void checkAllMoney(){
        if (money >= 20) updateStatus(14);
        if (money >= 200) updateStatus(15);
        if (money >= 600) updateStatus(16);
        if (money >= 3000) updateStatus(17);
    }

    // проверка получения достижений по накопленным деньгам
    private static void checkMoney(){
        if (money >= 200) updateStatus(18);
        if (money >= 1000) updateStatus(19);
        if (money >= 3000) updateStatus(20);
    }

    // проверка получения достижений по общему количеству пройденных уровней
    private static void checkAllWins(){
        switch (all_wins){
            case 1:
                updateStatus(21);
                break;
            case 50:
                updateStatus(22);
                break;
            case 200:
                updateStatus(23);
                break;
            case 700:
                updateStatus(24);
                break;
        }
    }

    // проверка на достижение БОГ
    private static void checkGod(){
        if (status.equals("11111111111111111111111111")) updateStatus(25);
    }

    private static void checkAchievements(){
        checkRecord();
        checkMoney();
        checkAllLevels();
        checkAllEating();
        checkAllMoney();
        checkAllWins();
        checkGod();
    }

    // обновление информации
    public static void updateData(int _money, int _record, int _all_levels, int _all_money, int _all_eating, int _all_wins) {
        money = _money;
        if (record < _record) record = _record;

        getAchievementsUser(status, _all_levels, _all_money, _all_eating, _all_wins); // обновление user
        checkAchievements(); // обновление status

        if (login.equals("Гость")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MONEY, money);
            contentValues.put(COLUMN_RECORD, record);
            db.update(TABLE_USERS, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
            contentValues.clear();
            contentValues.put(COLUMN_STATUS, status);
            contentValues.put(COLUMN_ALL_LEVELS, all_levels);
            contentValues.put(COLUMN_ALL_MONEY, all_money);
            contentValues.put(COLUMN_ALL_EATING, all_eating);
            contentValues.put(COLUMN_ALL_WINS, all_wins);
            db.update(TABLE_ACHIEVEMENTS, contentValues,COLUMN_ID_USER + "= ?", new String[]{Integer.toString(id)});
        }
        else JSONParser.updateData(Integer.toString(money), Integer.toString(record), status, Integer.toString(all_levels),
                Integer.toString(all_money), Integer.toString(all_eating), Integer.toString(all_wins));

    }

    public static void updateResume(int _mode, int _score, int _all_rewards){

        mode =_mode;
        score = _score;
        all_rewards =_all_rewards;

        if (login.equals("Гость")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MODE, _mode);
            contentValues.put(COLUMN_SCORE, _score);
            contentValues.put(COLUMN_ALL_REWARDS, _all_rewards);
            db.update(TABLE_RESUME, contentValues,COLUMN_ID + "= ?", new String[]{Integer.toString(id)});
        }
        else JSONParser.updateResume(Integer.toString(_mode), Integer.toString(_score), Integer.toString(_all_rewards));
    }

    public static int getId() {
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

    public static int getMode() {
        return mode;
    }

    public static int getScore() {
        return score;
    }

    public static int getAll_rewards() {
        return all_rewards;
    }

    public static String getStatus() {
        return status;
    }

    public static void setId(int id) {
        DatabaseHelper.id = id;
    }

    public static void setLogin(String login) {
        DatabaseHelper.login = login;
    }

    public static void setPassword(String password) {
        DatabaseHelper.password = password;
    }

    public static void setEmail(String email) {
        DatabaseHelper.email = email;
    }

    public static int getAll_levels() {
        return all_levels;
    }

    public static int getAll_money() {
        return all_money;
    }

    public static int getAll_eating() {
        return all_eating;
    }

    public static int getAll_wins() {
        return all_wins;
    }
}
