package dc.galos.Controller;

import java.util.ArrayList;

public class GameManager {
    private static final int MAX_CIRCLES = 10;
    private MainCircle mainCircle;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private static int width;
    private static int height;
    private int winning = 0; // выигрыш за победу в уровнях
    private int score = 0; // количество пройденных подряд уровней в этот раз
    private float rate; // коэффициент, на который умножается награда
    private int sum = 0;

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    private void initEnemyCircles() {
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

    private void calculateAndSetCirclesColor() {
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
                } else {
                    gameEnd("Score: " + Integer.toString(score) + ". Sum: " + Integer.toString(sum) + "$");
                    zeroReward(); // обнуляем счет и вознаграждение
                    return;
                }
            }
        }
        if (circleForDel != null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()) {
            setReward(); // прибавляем к деньгам вознаграждение и обновляем рекорд (если нужно)
            gameEnd("Reward: " + Integer.toString(winning) + "$");
        }
    }

    private void gameEnd(String text) {
        canvasView.showMessage(text);
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
        sum += winning;
        DatabaseHelper.updateReward(winning, score);
    }

    private void zeroReward() {
        score = 0;
        winning = 0;
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
}
