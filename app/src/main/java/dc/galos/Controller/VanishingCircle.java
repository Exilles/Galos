package dc.galos.Controller;

import android.graphics.Color;

import java.util.Random;

public class VanishingCircle extends SimpleCircle {

    private static final int FROM_RADIUS = 60;
    private static final int TO_RADIUS = 100;
    private static final int ENEMY_COLOR = Color.argb(255, 0, 0 ,100);
    private static final int FOOD_COLOR = Color.argb(255, 10, 90 ,235);
    private static final int VANISH_COLOR = Color.TRANSPARENT;
    private static final int RANDOM_SPEED = 5;
    private float dx;
    private float dy;

    private VanishingCircle(int x, int y, int radius, int dx, int dy) {
        super(x, y, radius);
        this.dx = dx;
        this.dy = dy;
    }

    public static VanishingCircle getRandomCircle() {
        Random random = new Random();
        int x = random.nextInt(GameManager.getWidth());
        int y = random.nextInt(GameManager.getHeight());
        int dx = 1 + random.nextInt(RANDOM_SPEED);
        int dy = 1 + random.nextInt(RANDOM_SPEED);
        int radius = FROM_RADIUS + random.nextInt(TO_RADIUS - FROM_RADIUS);
        return new VanishingCircle(x, y, radius, dx, dy);
    }

    public static VanishingCircle getRandomCircle(int radius) {
        Random random = new Random();
        int x = random.nextInt(GameManager.getWidth());
        int y = random.nextInt(GameManager.getHeight());
        int dx = 1 + random.nextInt(RANDOM_SPEED);
        int dy = 1 + random.nextInt(RANDOM_SPEED);
        return new VanishingCircle(x, y, radius, dx, dy);
    }

    public void setEnemyOrFoodColorDependsOn(MainCircle mainCircle) {
        if (isSmallerThan(mainCircle)) {
            setColor(FOOD_COLOR);
        } else {
            setColor(ENEMY_COLOR);
        }
    }

    public void goVanish(){
        setColor(VANISH_COLOR);
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

    public void decelerationSpeed() {
        dx = dx / 2;
        dy = dy / 2;
    }
}
