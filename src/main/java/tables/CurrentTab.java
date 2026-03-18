package tables;

import java.util.ArrayList;
import java.util.List;
import element.Cursor;
import element.Element;

public class CurrentTab {

    public RollingTab rollingTab;
    private Cursor cursor;

    public CurrentTab(int width, int height) {
        this.rollingTab = new RollingTab(width, height);
        this.cursor = new Cursor(0, 0);
    }

    public void moveRight(int n) {
        int newCol = cursor.getCol() + n;
        if(rollingTab.isOutOfBounds(newCol, cursor.getRow())) {
            throw new IllegalArgumentException("Cursor movement out of bounds");
        }
        cursor.setCol(newCol);
    }

    public void moveLeft(int n) {
        int newCol = cursor.getCol() - n;
        if(rollingTab.isOutOfBounds(newCol, cursor.getRow())) {
            throw new IllegalArgumentException("Cursor movement out of bounds");
        }
        cursor.setCol(newCol);
    }

    public void moveDown(int n) {
        int newRow = cursor.getRow() + n;
        if(rollingTab.isOutOfBounds(cursor.getCol(), newRow)) {
            throw new IllegalArgumentException("Cursor movement out of bounds");
        }
        cursor.setRow(newRow);
    }

    public void moveUp(int n) {
        int newRow = cursor.getRow() - n;
        if(rollingTab.isOutOfBounds(cursor.getCol(), newRow)) {
            throw new IllegalArgumentException("Cursor movement out of bounds");
        }
        cursor.setRow(newRow);
    }

    public int[] getCurrentCursorPosition() {
        return new int[]{cursor.getCol(), cursor.getRow()};
    }

    public void setCurrentCursorPosition(int col, int row) {
        if(rollingTab.isOutOfBounds(col, row)) {
            throw new IllegalArgumentException("Cursor position out of bounds");
        }

        cursor.setCol(col);
        cursor.setRow(row);
    }

    public void overwrite(List<Element> elements) {
        int[] newPos = rollingTab.overwriting(elements, cursor.getCol(), cursor.getRow());
        cursor.setCol(newPos[0]);
        cursor.setRow(newPos[1]);
    }

    public void write(List<Element> elements) {
        int[] newPos = rollingTab.insertText(elements, cursor.getCol(), cursor.getRow());
        cursor.setCol(newPos[0]);
        cursor.setRow(newPos[1]);
    }

    public List<Element> wrapString(String s) {
        List<Element> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(new Element(c));
        }
        return list;
    }

    public void fillWithChar(char c) {
        int[] result = rollingTab.fillLine(cursor.getCol(), cursor.getRow(), new Element(c));
        cursor.setCol(result[0]);
        cursor.setRow(result[1]);
    }

    public void insertEmptyLine() {
        rollingTab.insertEmptyLine();
    }

    public void clearTheScreen() {
        int [] res = rollingTab.clearTheScreen();
        cursor.setCol(res[0]);
        cursor.setRow(res[1]);
    }

    public List<String> getEntireScreen() {
        return rollingTab.getEntireScreen();
    }

    public String getString(int row) {
        return rollingTab.getString(row);
    }

    private List<Element> toElementList(String s) {
        List<Element> list = new ArrayList<>();
        for (char c : s.toCharArray()) {
            list.add(new Element(c));
        }
        return list;
    }

    public List<List<Element>> consumeRemovedLines() {
        return rollingTab.consumeRemovedLines();
    }

    public Element getElement(int x, int y) {
        return rollingTab.getElement(x, y);
    }
}