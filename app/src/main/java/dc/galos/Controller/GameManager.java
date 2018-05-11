package dc.galos.Controller;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dc.galos.View.Game;

public class GameManager {
    private static MainCircle mainCircle; // круг игрока
    private static ArrayList<EnemyCircle> enemy_circles; // массив вражеских кругов
    private static ArrayList<ImmortalCircle> immortal_circles; // массив неуязвимых кругов
    private static ArrayList<VanishingCircle> vanishing_circles; // массив исчезающих кругов
    private static CanvasView canvasView;
    private static int mode = 1;
    private static int width;
    private static int height;
    private Timer timer;
    private boolean vanish = true;
    private int winning = 0; // выигрыш за победу в уровнях
    private int score = 0; // количество пройденных подряд уровней в этот раз
    private int sum = 0; // сумма всех вознаграждений за победы
    private float rate; // коэффициент, на который умножается награда
    public static boolean life = false;
    public static boolean deceleration = false;

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
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
        if (mode != 4) mode ++;
        else mode = 1;
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
        checkEmptyCircles(); // проверка условия победы
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
        }, 0, 2000);
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
            Game.showDialog(score, winning, sum, 1); // вызывает диалог
        }
    }

    // вызывает диалог в соответствии с тем, использован бонус life или нет
    private void lifeLoser(){
        setVanishingCirclesColor();
        if (life) {
            Game.showDialog(score, winning, sum, 3); // вызывает диалог
            life = false;
        }
        else {
            life = false;
            deceleration = false;
            Game.showDialog(score, winning, sum, 2); // вызывает диалог
            zeroReward(); // обнуляем счет и вознаграждение
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
        winning = Math.round((score * rate) + rate); // вознаграждение за победу
        sum += winning; // подсчёт суммы всех вознаграждений за победу
        DatabaseHelper.updateReward(winning, score);
    }

    private void zeroReward() {
        score = 0;
        winning = 0;
        sum = 0;
    }

    private void switchRate(int _score) {
        switch (_score) {
            case 1:
                rate = 1.0f;
                break;
            case 11:
                rate = 1.5f;
                break;
            case 21:
                rate = 2.0f;
                break;
            case 36:
                rate = 2.5f;
                break;
            case 51:
                rate = 3.0f;
                break;
            case 76:
                rate = 3.5f;
                break;
            case 101:
                rate = 4.0f;
                break;
        }
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
}
