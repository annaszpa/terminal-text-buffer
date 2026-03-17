package terminalbuffer;

import tables.ArchiveTab;
import tables.CurrentTab;

import java.util.List;

public class TerminalBuffer {

    private ArchiveTab archiveTab;
    public CurrentTab currentTab;

    public TerminalBuffer(int width, int height) {
        this.archiveTab = new ArchiveTab();
        this.currentTab = new CurrentTab(width, height);
    }

    public void moveRight(int n) {
        this.currentTab.moveRight(n);
    }

    public void moveLeft(int n) {
        this.currentTab.moveLeft(n);
    }

    public void moveDown(int n) {
        this.currentTab.moveDown(n);
    }

    public void moveUp(int n) {
        this.currentTab.moveUp(n);
    }

    public int[] getCurrentCursorPosition() {
        return this.currentTab.getCurrentCursorPosition();
    }

    public void setCurrentCursorPosition(int x, int y) {
        this.currentTab.setCurrentCursorPosition(x, y);
    }

    public void overwrite(String text) {
        this.currentTab.overwrite(text);
    }

    public void write(String text) {
        this.currentTab.write(text);
    }

    public void fillCharacter(char c) {
        this.currentTab.fillWithChar(c);
    }

    public void fillEmptyLine() {
        this.currentTab.fillWithChar(' ');
    }

    public void insertEmptyLine() {
        this.currentTab.insertEmptyLine();
    }

    public void clearTheScreen() {
        this.currentTab.clearTheScreen();
    }

    public List<String> getEntireScreen() {
        this.currentTab.getEntireScreen();
        return null;
    }
    public String getString(int x) {
        return this.currentTab.getString(x);
    }

    public void fillLine(int i, int i1, char c) {
        this.currentTab.fillWithChar(c);
    }
    //Clear the screen and scrollback
}