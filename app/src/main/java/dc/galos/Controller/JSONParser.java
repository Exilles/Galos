package dc.galos.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {

    private static String url = "";
    private static int flag;
    private static String param_1, value_1, param_2, value_2, param_3, value_3;

    public static void createUser(String _login, String _password, String _email) {
        url = "https://galos.000webhostapp.com/create_user.php";
        flag = 3;
        param_1 = "login";
        value_1 = _login;
        param_2 = "password";
        value_2 = _password;
        param_3 = "email";
        value_3 = _email;
        new ParseTask().execute();
    }

    public static void getUser(String _login, String _password) {
        url = "https://galos.000webhostapp.com/get_user.php";
        flag = 2;
        param_1 = "login";
        value_1 = _login;
        param_2 = "password";
        value_2 = _password;
        new ParseTask().execute();
    }

    public static void getRecords() {
        url = "https://galos.000webhostapp.com/get_records.php";
        flag = 1;
        new ParseTask().execute();
    }

    public static void updateUserPassword(String _id, String _password){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 4;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "password";
        value_2 = _password;
        new ParseTask().execute();
    }

    public static void updateUserEmail(String _id, String _email){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 4;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "email";
        value_2 = _email;
        new ParseTask().execute();
    }

    public static void updateUserMoney(String _id, String _money){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 4;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "money";
        value_2 = _money;
        new ParseTask().execute();
    }

    public static void updateUserRecord(String _id, String _record){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 4;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "record";
        value_2 = _record;
        new ParseTask().execute();
    }

    private static class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            switch (flag){
                case 1:
                    return HttpRequest.get(url).body();
                case 2:
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
                case 3:
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2, param_3, value_3).body();
                case 4:
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
                default:
                    return "my_log: Нет такого значения 'flag'";
            }
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            switch (flag) {
                case 1:
                    try {
                        JSONObject dataJsonObj = new JSONObject(strJson);
                        JSONArray records = dataJsonObj.getJSONArray("records");

                        for (int i = 0; i < records.length(); i++) {
                            JSONObject user = records.getJSONObject(i);

                            String login = user.getString("login");
                            String record = user.getString("record");

                        }

                    } catch (JSONException e) {
                        Log.d("my log", "Не вышло получить данные :(");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject dataJsonObj = new JSONObject(strJson);
                        JSONArray users = dataJsonObj.getJSONArray("user");
                        JSONObject user = users.getJSONObject(0);

                        String id = user.getString("_id");
                        String login = user.getString("login");
                        String password = user.getString("password");
                        String email = user.getString("email");
                        String money = user.getString("money");
                        String record = user.getString("record");

                    } catch (JSONException e) {
                        Log.d("my log", "Не вышло получить данные :(");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /*@Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);

            /*JSONObject dataJsonObj = null;
            String secondName = "";

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray friends = dataJsonObj.getJSONArray("friends");

                // 1. достаем инфо о втором друге - индекс 1
                JSONObject secondFriend = friends.getJSONObject(1);
                secondName = secondFriend.getString("name");
                Log.d(LOG_TAG, "Второе имя: " + secondName);

                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("contacts");

                    String phone = contacts.getString("mobile");
                    String email = contacts.getString("email");
                    String skype = contacts.getString("skype");

                    Log.d(LOG_TAG, "phone: " + phone);
                    Log.d(LOG_TAG, "email: " + email);
                    Log.d(LOG_TAG, "skype: " + skype);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
