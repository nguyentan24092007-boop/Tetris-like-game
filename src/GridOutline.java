import java.awt.*;

public class GridOutline {
    private final int WIDTH = 300;
    private final int HEIGHT = 600;
    private int left_x;
    private int top_y;
    private int right_x;
    private int bottom_y;
    

    public GridOutline() {
        this.left_x = 110;
        this.top_y = 80;
        this.right_x = this.left_x + WIDTH;
        this.bottom_y = this.top_y + HEIGHT;
    }

    public int getWidth() { return this.WIDTH; }
    public int getHeight() { return this.HEIGHT; }
    public int getLeftX() { return this.left_x; }
    public int getTopY() { return this.top_y; }
    public int getRightX() { return this.right_x; }
    public int getBottomY() { return this.bottom_y; }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(197, 240, 238));
        g2.setStroke(new BasicStroke(5f));
        g2.drawRoundRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8, 15, 15);

        
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(550, 75, 350, 180, 20, 20);
        g2.drawRoundRect(585, 280, 280, 180, 20, 20);
        g2.drawRoundRect(550, 480, 350, 203, 20, 20);
    }
}
