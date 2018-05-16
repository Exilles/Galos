package dc.galos.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONParser {

    private static String url = "";
    private static int flag;
    private static String param_1, value_1, param_2, value_2, param_3, value_3;

    public static void getUser(String _login, String _password) {
        url = "https://galos.000webhostapp.com/get_user.php";
        flag = 1;
        param_1 = "login";
        value_1 = _login;
        param_2 = "password";
        value_2 = _password;
        new ParseTask().execute();
    }

    public static void createUser(String _login, String _password, String _email) {
        url = "https://galos.000webhostapp.com/create_user.php";
        flag = 2;
        param_1 = "login";
        value_1 = _login;
        param_2 = "password";
        value_2 = _password;
        param_3 = "email";
        value_3 = _email;
        new ParseTask().execute();
    }

    public static void updateUserPassword(String _id, String _password){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 3;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "password";
        value_2 = _password;
        new ParseTask().execute();
    }

    public static void updateUserEmail(String _id, String _email){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 3;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "email";
        value_2 = _email;
        new ParseTask().execute();
    }

    public static void updateUserMoney(String _id, String _money){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 3;
        param_1 = "_id";
        value_1 = _id;
        param_2 = "money";
        value_2 = _money;
        new ParseTask().execute();
    }

    public static void updateUserRecord(String _id, String _record){
        url = "https://galos.000webhostapp.com/update_user.php";
        flag = 3;
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
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
                case 2:
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2, param_3, value_3).body();
                case 3:
                    return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
                default:
                    return "my_log: Нет такого значения 'flag'";
            }
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
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

        }
    }
}
