package dc.galos.Controller;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

public class JSONParser {

    private static String url = "";
    private static String param_1 = "_id";
    private static String value_1 = Integer.toString(DatabaseHelper.getId());
    private static String param_2, value_2, param_3, value_3, param_4, value_4, param_5, value_5,
            param_6, value_6, param_7, value_7, param_8, value_8;

    public static void updateData(String _money, String _record, String _status, String _all_levels, String _all_money, String _all_eating, String _all_wins){
        url = "https://galos.000webhostapp.com/update_data.php";
        param_2 = "money"; value_2 = _money;
        param_3 = "record"; value_3 = _record;
        param_4 = "status"; value_4 = _status;
        param_5 = "all_levels"; value_5 = _all_levels;
        param_6 = "all_money"; value_6 = _all_money;
        param_7 = "all_eating"; value_7 = _all_eating;
        param_8 = "all_wins"; value_8 = _all_wins;
        new UpdateDataThread().execute();
    }

    public static void updateResume(String _mode, String _score, String _all_rewards){
        url = "https://galos.000webhostapp.com/update_resume.php";
        param_2 = "mode"; value_2 = _mode;
        param_3 = "score"; value_3 = _score;
        param_4 = "all_rewards"; value_4 = _all_rewards;
        new UpdateResumeThread().execute();
    }

    private static class UpdateDataThread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2, param_3, value_3,
                    param_4, value_4, param_5, value_5, param_6, value_6, param_7, value_7, param_8, value_8).body();
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
