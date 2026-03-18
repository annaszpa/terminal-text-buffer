package element;

public class Element {
    public char character;
    public int foregroundColour;
    public int backgroundColour;
    public int flags;

    public Element() {
        this.character = ' ';
        this.foregroundColour = 0;
        this.backgroundColour = 0;
        this.flags = 0;
    }

    public Element(char character) {
        this.character = character;
        this.foregroundColour = 0;
        this.backgroundColour = 0;
        this.flags = 0;
    }

    public Element(char character, int foregroundColour, int backgroundColour, int flags) {
        this.character = character;
        this.foregroundColour = foregroundColour;
        this.backgroundColour = backgroundColour;
        this.flags = flags;
    }
}