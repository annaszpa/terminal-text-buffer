package element;

import java.util.EnumSet;

public class Element {

    public char character;
    public Color foreground;
    public Color background;
    public EnumSet<Style> styles;

    public Element() {
        this(' ', Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class));
    }

    public Element(char character) {
        this(character, Color.DEFAULT, Color.DEFAULT, EnumSet.noneOf(Style.class));
    }

    public Element(char character, Color foreground, Color background, EnumSet<Style> styles) {
        this.character = character;
        this.foreground = foreground;
        this.background = background;
        this.styles = styles != null ? EnumSet.copyOf(styles) : EnumSet.noneOf(Style.class);
    }

    public Element copy() {
        return new Element(character, foreground, background, styles);
    }
}