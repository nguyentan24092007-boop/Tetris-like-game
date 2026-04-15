import java.awt.Color;

public class Block {
    private Color color;
    private boolean haveSquare;

    public Block() {
        this.color = Color.BLACK;
        this.haveSquare = false;
    }
    public boolean hasPiece() {
        return this.haveSquare;
    }
    public Color getColor() { return this.color; }

    public void have(Color newColor) {
        this.haveSquare = true;
        this.color = newColor;
    }
    public void dontHave() {
        this.haveSquare = false;
        this.color = Color.BLACK;
    }
}
