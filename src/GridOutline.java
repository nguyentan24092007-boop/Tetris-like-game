import java.awt.*;

public class GridOutline {
    final int WIDTH = 300;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    public GridOutline() {
        left_x = 110;
        right_x = left_x + WIDTH;
        top_y = 75;
        bottom_y = top_y + HEIGHT;
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);
    }
}
