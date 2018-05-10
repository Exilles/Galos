package dc.galos.Controller;

import android.graphics.Color;

import java.util.Random;

public class EnemyCircle extends SimpleCircle {

    private static final int FROM_RADIUS = 10;
    private static final int TO_RADIUS = 110;
    private static final int ENEMY_COLOR = Color.BLACK;
    private static final int FOOD_COLOR = Color.LTGRAY;
    private static int random_speed = 10;
    private int dx;
    private int dy;

    private EnemyCircle(int x, int y, int radius, int dx, int dy) {
        super(x, y, radius);
        this.dx = dx;
        this.dy = dy;
    }

    public static EnemyCircle getRandomCircle() {
        Random random = new Random();
        int x = random.nextInt(GameManager.getWidth());
        int y = random.nextInt(GameManager.getHeight());
        int dx = 1 + random.nextInt(random_speed);
        int dy = 1 + random.nextInt(random_speed);
        int radius = FROM_RADIUS + random.nextInt(TO_RADIUS - FROM_RADIUS);
        return new EnemyCircle(x, y, radius, dx, dy);
    }

    public void setEnemyOrFoodColorDependsOn(MainCircle mainCircle) {
        if (isSmallerThan(mainCircle)) {
            setColor(FOOD_COLOR);
        } else {
            setColor(ENEMY_COLOR);
        }
    }

    public boolean isSmallerThan(SimpleCircle circle) {
        return radius < circle.radius;
    }

    public void moveOneStep() {
        x += dx;
        y += dy;
        checkBounds();
    }

    private void checkBounds() {
        if (x > GameManager.getWidth() || x < 0) {
            dx = -dx;
        }
        if (y > GameManager.getHeight() || y < 0) {
            dy = -dy;
        }
    }

    public static int getRandom_speed() {
        return random_speed;
    }

    public static void setRandom_speed(int random_speed) {
        EnemyCircle.random_speed = (int)(random_speed / 2);
    }
}
