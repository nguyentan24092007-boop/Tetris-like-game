import java.awt.Color;

public class GameTheme {
    public Color backgroundDark;
    public Color backgroundLight;
    public Color gridBorder;
    public Color gridLines;
    public Color textPrimary;
    public Color scoreBackground;

    public static GameTheme createModernTheme() {
        GameTheme theme = new GameTheme();
        theme.backgroundDark = new Color(44, 62, 80);   // Dark Slate
        theme.backgroundLight = new Color(52, 73, 94);  // Lighter Slate
        theme.gridBorder = new Color(236, 240, 241);    // Cloud White
        theme.gridLines = new Color(255, 255, 255, 30); // Frosted/Transparent White
        theme.textPrimary = new Color(236, 240, 241);   // Cloud White
        theme.scoreBackground = new Color(255, 255, 255, 40); // Frosted Glass Box
        return theme;
    }
}