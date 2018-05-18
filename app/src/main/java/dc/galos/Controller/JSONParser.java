package dc.galos.Controller;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

public class JSONParser {

    private static String url = "";
    private static String param_1 = "_id";
    private static String value_1 = Integer.toString(DatabaseHelper.getId());
    private static String param_2;
    private static String value_2;
    private static String param_3;
    private static String value_3;
    private static String param_4;
    private static String value_4;

    public static void updateUserPassword(String _password){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_2 = "password";
        value_2 = _password;
        new UpdateUserPasswordThread().execute();
    }

    public static void updateUserEmail(String _email){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_2 = "email";
        value_2 = _email;
        new UpdateUserEmailThread().execute();
    }

    public static void updateUserMoney(String _money){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_2 = "money";
        value_2 = _money;
        Log.d("my log", "Пытаюсь обновить деньги");
        new UpdateUserMoneyThread().execute();
    }

    public static void updateUserRecord(String _record){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_2 = "record";
        value_2 = _record;
        Log.d("my log", "Пытаюсь обновить рекорд");
        new UpdateUserRecordThread().execute();
    }

    public static void updateAchievementsStatus(String _status){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_2 = "status";
        value_2 = _status;
        new UpdateAchievementsStatusThread().execute();
    }

    public static void updateAchievementsAllLevels(String _all_levels){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_2 = "all_levels";
        value_2 = _all_levels;
        new UpdateAchievementsAllLevelsThread().execute();
    }

    public static void updateAchievementsAllMoney(String _all_money){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_2 = "all_money";
        value_2 = _all_money;
        new UpdateAchievementsAllMoneyThread().execute();
    }

    public static void updateAchievementsAllEating(String _all_eating){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_2 = "all_eating";
        value_2 = _all_eating;
        new UpdateAchievementsAllEatingThread().execute();
    }

    public static void updateAchievementsAllWins(String _all_wins){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_2 = "all_wins";
        value_2 = _all_wins;
        new UpdateAchievementsAllWinsThread().execute();
    }

    public static void updateResume(String _mode, String _score, String _all_rewards){
        url = "https://galos.000webhostapp.com/update_resume.php";
        param_2 = "mode";
        value_2 = _mode;
        param_3 = "score";
        value_3 = _score;
        param_4 = "all_rewards";
        value_4 = _all_rewards;
        new UpdateResumeThread().execute();
    }

    private static class UpdateUserPasswordThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateUserEmailThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateUserMoneyThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateUserRecordThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateAchievementsStatusThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateAchievementsAllLevelsThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateAchievementsAllMoneyThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateAchievementsAllEatingThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateAchievementsAllWinsThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }

    private static class UpdateResumeThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2, param_3, value_3, param_4, value_4).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }
}
