package dc.galos.Controller;

import java.util.ArrayList;

import dc.galos.View.Game;

public class GameManager {
    private static final int MAX_CIRCLES = 10;
    private static MainCircle mainCircle;
    private static ArrayList<EnemyCircle> circles;
    private static CanvasView canvasView;
    private static int width;
    private static int height;
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
        initEnemyCircles();
    }

    private static void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        circles = new ArrayList<EnemyCircle>();
        for (int i = 0; i < MAX_CIRCLES; i++) {
            EnemyCircle circle;
            do {
                circle = EnemyCircle.getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
        }
        calculateAndSetCirclesColor();
    }

    private static void calculateAndSetCirclesColor() {
        for (EnemyCircle circle : circles) {
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
        for (EnemyCircle circle : circles) {
            canvasView.drawCircle(circle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        checkCollision();
        moveCircles();
    }

    private void checkCollision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle : circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    calculateAndSetCirclesColor();
                    break;
                }
                else {
                    if (life) {
                        Game.showDialog(score, winning, sum, 3); // вызывает диалог
                        life = false;
                        return;
                    }
                    else {
                        life = false;
                        deceleration = false;
                        Game.showDialog(score, winning, sum, 2); // вызывает диалог
                        zeroReward(); // обнуляем счет и вознаграждение
                        return;
                    }
                }
            }
        }
        if (circleForDel != null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()) {
            life = false;
            deceleration = false;
            setReward(); // прибавляем к деньгам вознаграждение и обновляем рекорд (если нужно)
            Game.showDialog(score, winning, sum, 1); // вызывает диалог
        }
    }

    public static void gameEnd() {
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle : circles) {
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
        for (EnemyCircle circle : circles) {
            circle.decelerationSpeed();
        }
    }

    public static void useGrowthBonus() {
        mainCircle.growRadius();
        calculateAndSetCirclesColor();
        canvasView.redraw();
    }
}
