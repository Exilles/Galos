package dc.galos.Controller;

import android.graphics.Color;

public class MainCircle extends SimpleCircle{
    private static final int INIT_RADIUS = 50;
    private static final int MAIN_SPEED = 31; // чем меньше, тем быстрее
    private static final int OUR_COLOR = Color.GRAY;

    public MainCircle(int x, int y) {
        super(x, y, INIT_RADIUS);
        setColor(OUR_COLOR);
    }

    public void moveMainCircleWhenTouchAt(int x1, int y1) {
        //int dx = (x1 - x) * MAIN_SPEED / GameManager.getWidth();
        //int dy = (y1 - y) * MAIN_SPEED / GameManager.getHeight();
        int dx = (x1 - x) / MAIN_SPEED;
        int dy = (y1 - y) / MAIN_SPEED;
        x += dx;
        y += dy;
    }

    public void initRadius() {
        radius = INIT_RADIUS;
    }

    public void growRadius(SimpleCircle circle) {
        radius = (int) Math.sqrt(Math.pow(radius, 2) + Math.pow(circle.radius, 2));
    }

    public void growRadius() {
        radius = (int) Math.sqrt(Math.pow(radius, 2) + Math.pow(55, 2));
    }
}
