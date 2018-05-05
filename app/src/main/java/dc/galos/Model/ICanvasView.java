package dc.galos.Model;

import dc.galos.Controller.SimpleCircle;

public interface ICanvasView {
    void drawCircle(SimpleCircle circle);

    void redraw();

    void showMessage(String text);
}
