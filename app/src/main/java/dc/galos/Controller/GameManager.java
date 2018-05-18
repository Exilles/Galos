package dc.galos.Controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dc.galos.View.Game;

public class GameManager {
    private final static int PERIOD = 1500;
    private static MainCircle mainCircle; // круг игрока
    private static ArrayList<EnemyCircle> enemy_circles; // массив вражеских кругов
    private static ArrayList<ImmortalCircle> immortal_circles; // массив неуязвимых кругов
    private static ArrayList<VanishingCircle> vanishing_circles; // массив исчезающих кругов
    private static CanvasView canvasView;
    private static Timer timer;
    private static int width;
    private static int height;
    private boolean vanish = true;
    private int reward; // выигрыш за победу в уровнях
    private static int mode;
    private static int score; // количество пройденных подряд уровней в этот раз
    private static int all_rewards; // сумма всех вознаграждений за победы
    private float rate; // коэффициент, на который умножается награда
    private static boolean life = false;
    private static boolean deceleration = false;

    private static int money;
    private static int record;
    private static String status;
    private static int all_levels;
    private static int all_money;
    private static int all_eating;
    private static int all_wins;


    GameManager(CanvasView canvasView, int w, int h) {
        GameManager.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initGameMode();
        startTimer();
    }

    private static void initGameMode(){
        GameModes gameModes = new GameModes(mainCircle);
        gameModes.generateMode(mode);
        enemy_circles = gameModes.getEnemy_circles();
        immortal_circles = gameModes.getImmortal_circles();
        vanishing_circles = gameModes.getVanishing_circles();
    }

    private static void setEnemyCirclesColor() {
        for (EnemyCircle circle : enemy_circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    private static void setVanishingCirclesColor(){
        for (VanishingCircle circle : vanishing_circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initMainCircle() {
        mainCircle = new MainCircle(width / 2, height / 2);
    }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);
        for (EnemyCircle circle : enemy_circles) {
            canvasView.drawCircle(circle);
        }
        for (ImmortalCircle circle : immortal_circles) {
            canvasView.drawCircle(circle);
        }
        for (VanishingCircle circle : vanishing_circles) {
            canvasView.drawCircle(circle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        checkEmptyCircles();
        checkEnemyCirclesCollision(); // проверка коллизии вражеских кругов
        checkImmortalCirclesCollision(); // проверка коллизии неуязвимых кругов
        checkVanishingCirclesCollision(); // проверка коллизии исчезающих кругов
        moveCircles();
    }

    private void startTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if (vanish) {
                    vanish = false;
                    setVanishingCirclesColor();
                }
                else {
                    vanish = true;
                    for (VanishingCircle circle : vanishing_circles) {
                        circle.goVanish();
                    }
                }
            }
        }, 0, PERIOD);
    }

    private void setCirclesColor(){
        setEnemyCirclesColor();
        if (!vanish) setVanishingCirclesColor();
    }

    // проверка коллизии с вражеским кругом
    private void checkEnemyCirclesCollision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle : enemy_circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating + 1, all_wins);
                    //DatabaseHelper.updateAllEating();
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    setCirclesColor();
                    break;
                }
                else lifeLoser();// вызывает диалог в соответствии с life
            }
        }
        if (circleForDel != null) enemy_circles.remove(circleForDel);
    }

    // проверка коллизии с неуязвимым кругом
    private void checkImmortalCirclesCollision() {
        for (ImmortalCircle circle : immortal_circles) {
            if (mainCircle.isIntersect(circle)) {
                lifeLoser(); // вызывает диалог в соответствии с life
            }
        }
    }

    // проверка коллизии с исчезающим кругом
    private void checkVanishingCirclesCollision() {
        SimpleCircle circleForDel = null;
        for (VanishingCircle circle : vanishing_circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    getData();
                    DatabaseHelper.updateData(money, record, all_levels, all_money, all_eating + 1, all_wins);
                    //DatabaseHelper.updateAllEating();
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    setCirclesColor();
                    break;
                }
                else lifeLoser();// вызывает диалог в соответствии с life
            }
        }
        if (circleForDel != null) vanishing_circles.remove(circleForDel);
    }

    private void checkEmptyCircles(){
        if (enemy_circles.isEmpty() && vanishing_circles.isEmpty()) {
            life = false;
            deceleration = false;
            setReward(); // прибавляем к деньгам вознаграждение и обновляем рекорд (если нужно)
            Game.showDialog(score, reward, all_rewards, 1); // вызывает диалог
        }
    }

    // вызывает диалог в соответствии с тем, использован бонус life или нет
    private void lifeLoser(){
        setVanishingCirclesColor();
        if (life) {
            Game.showDialog(score, reward, all_rewards, 3); // вызывает диалог
            life = false;
            deceleration = false;
        }
        else {
            life = false;
            deceleration = false;
            int _score = score;
            int _all_rewards = all_rewards;
            zeroScoreAndRewards(); // обнуляем счет и вознаграждение
            Game.showDialog(_score, reward, _all_rewards, 2); // вызывает диалог
        }
    }

    public static void gameEnd() {
        mainCircle.initRadius();
        initGameMode();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle : enemy_circles) {
            circle.moveOneStep();
        }
        for (ImmortalCircle circle : immortal_circles) {
            circle.moveOneStep();
        }
        for (VanishingCircle circle : vanishing_circles) {
            circle.moveOneStep();
        }
    }

    private void setReward() {
        score += 1;
        switchRate(score);
        reward = Math.round((score * rate) + rate); // вознаграждение за победу
        all_rewards += reward; // подсчёт суммы всех вознаграждений за победу
        getData();
        DatabaseHelper.updateData(money + reward, score, all_levels, all_money + reward, all_eating, all_wins + 1);
        //DatabaseHelper.updateMoneyAndRecord(reward, score);
        //DatabaseHelper.updateAllMoneyAndWins(reward);
    }

    private void zeroScoreAndRewards() {
        score = 0;
        reward = 0;
        all_rewards = 0;
    }

    private void switchRate(int _score) {
        if (_score >= 1 && _score <= 10) rate = 1.0f;
        if (_score >= 11 && _score <= 20) rate = 1.5f;
        if (_score >= 21 && _score <= 35) rate = 2.0f;
        if (_score >= 36 && _score <= 50) rate = 2.5f;
        if (_score >= 51 && _score <= 75) rate = 3.0f;
        if (_score >= 76 && _score <= 100) rate = 3.5f;
        if (_score >= 101) rate = 4.0f;
    }

    public static void useLifeBonus() {
        life = true;
    }

    public static void useDecelerationBonus() {
        deceleration = true;
        for (EnemyCircle circle : enemy_circles) {
            circle.decelerationSpeed();
        }
        for (ImmortalCircle circle : immortal_circles) {
            circle.decelerationSpeed();
        }
        for (VanishingCircle circle : vanishing_circles) {
            circle.decelerationSpeed();
        }
    }

    public static void useGrowthBonus() {
        mainCircle.growRadius();
        setEnemyCirclesColor();
        setVanishingCirclesColor();
        canvasView.redraw();
    }

    private static void getData(){
        money = DatabaseHelper.getMoney();
        record = DatabaseHelper.getRecord();
        status = DatabaseHelper.getStatus();
        all_levels = DatabaseHelper.getAll_levels();
        all_money = DatabaseHelper.getAll_money();
        all_eating = DatabaseHelper.getAll_eating();
        all_wins = DatabaseHelper.getAll_wins();
    }

    public static int getMode() {
        return mode;
    }

    public static void setMode(int _mode) {
        mode = _mode;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GameManager.score = score;
    }

    public static void setAll_rewards(int all_rewards) {
        GameManager.all_rewards = all_rewards;
    }

    public static int getAll_rewards() {
        return all_rewards;
    }

    public static boolean isLife() {
        return life;
    }

    public static boolean isDeceleration() {
        return deceleration;
    }

    public static void setLife(boolean life) {
        GameManager.life = life;
    }

    public static void setDeceleration(boolean deceleration) {
        GameManager.deceleration = deceleration;
    }

    public static void setTimer(Timer timer) {
        GameManager.timer = timer;
    }

    public static Timer getTimer() {
        return timer;
    }
}
