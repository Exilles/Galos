package dc.galos.Controller;

import android.os.AsyncTask;
import com.github.kevinsawicki.http.HttpRequest;

public class JSONParser {

    private static String url = "";
    private static String param_1, value_1, param_2, value_2;

    public static void updateUserPassword(String _password){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "password";
        value_2 = _password;
        new ParseTask().execute();
    }

    public static void updateUserEmail(String _email){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "email";
        value_2 = _email;
        new ParseTask().execute();
    }

    public static void updateUserMoney(String _money){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "money";
        value_2 = _money;
        new ParseTask().execute();
    }

    public static void updateUserRecord(String _record){
        url = "https://galos.000webhostapp.com/update_user.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "record";
        value_2 = _record;
        new ParseTask().execute();
    }

    public static void updateAchievementsStatus(String _status){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "status";
        value_2 = _status;
        new ParseTask().execute();
    }

    public static void updateAchievementsAllLevels(String _all_levels){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "all_levels";
        value_2 = _all_levels;
        new ParseTask().execute();
    }

    public static void updateAchievementsAllMoney(String _all_money){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "all_money";
        value_2 = _all_money;
        new ParseTask().execute();
    }

    public static void updateAchievementsAllEating(String _all_eating){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "all_eating";
        value_2 = _all_eating;
        new ParseTask().execute();
    }

    public static void updateAchievementsAllWins(String _all_wins){
        url = "https://galos.000webhostapp.com/update_achievements.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "all_wins";
        value_2 = _all_wins;
        new ParseTask().execute();
    }

    public static void updateResumeMode(String _mode){
        url = "https://galos.000webhostapp.com/update_resume.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "mode";
        value_2 = _mode;
        new ParseTask().execute();
    }

    public static void updateResumeScore(String _score){
        url = "https://galos.000webhostapp.com/update_resume.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "score";
        value_2 = _score;
        new ParseTask().execute();
    }

    public static void updateResumeAllRewards(String _all_rewards){
        url = "https://galos.000webhostapp.com/update_resume.php";
        param_1 = "_id";
        value_1 = Integer.toString(DatabaseHelper.getId());
        param_2 = "all_rewards";
        value_2 = _all_rewards;
        new ParseTask().execute();
    }

    private static class ParseTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return HttpRequest.get(url, true, param_1, value_1, param_2, value_2).body();
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
        }
    }
}
