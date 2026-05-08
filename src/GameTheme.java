import java.awt.Color;

public class GameTheme {
    public final Color backgroundTop;
    public final Color backgroundTopBottom;
    public final Color areaTop;
    public final Color areaBottom;

    public GameTheme(Color backgroundTop, Color backgroundTopBottom, Color areaTop, Color areaBottom) {
        this.backgroundTop = backgroundTop;
        this.backgroundTopBottom = backgroundTopBottom;
        this.areaTop = areaTop;
        this.areaBottom = areaBottom;
    }

    public static GameTheme createCurrentTheme() {
        return new GameTheme(
            new Color(20, 5, 45),
            new Color(10, 30, 60),
            new Color(40, 10, 80, 200),
            new Color(10, 60, 80, 200)
        );
    }
}