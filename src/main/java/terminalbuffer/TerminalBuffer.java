package terminalbuffer;

import element.Element;
import tables.ArchiveTab;
import tables.CurrentTab;

import java.util.ArrayList;
import java.util.List;

public class TerminalBuffer {

    private ArchiveTab archiveTab;
    public CurrentTab currentTab;

    public TerminalBuffer(int width, int height, int maxArchiveLines) {
        this.archiveTab = new ArchiveTab(maxArchiveLines);
        this.currentTab = new CurrentTab(width, height);
    }

    private void captureScroll() {
        List<List<Element>> removedLines = currentTab.consumeRemovedLines();

        for (List<Element> line : removedLines) {
            archiveTab.addLine(line);
        }
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

    public void overwrite(List<Element> elements) {
        this.currentTab.overwrite(elements);
        captureScroll();
    }

    public void write(List<Element> elements) {
        this.currentTab.write(elements);
        captureScroll();
    }

    public void fillCharacter(char c) {
        this.currentTab.fillWithChar(c);
        captureScroll();
    }

    public void fillEmptyLine() {
        this.currentTab.fillWithChar(' ');
        captureScroll();
    }

    public void insertEmptyLine() {
        this.currentTab.insertEmptyLine();
        captureScroll();
    }

    public void clearTheScreen() {
        this.currentTab.clearTheScreen();
    }

    public void clearAll() {
        this.currentTab.clearTheScreen();
        this.archiveTab.clear();
    }

    public List<String> getEntireScreen() {
        List<String> result = new ArrayList<>();

        result.addAll(archiveTab.getAllAsString());
        result.addAll(this.currentTab.getEntireScreen());

        return result;
    }

    public String getString(int x) {
        return this.currentTab.getString(x);
    }

    public void fillLine(int x, int y, char c) {
        this.currentTab.fillWithChar(c);
    }

    public List<Element> getArchiveLine(int i) {
        return archiveTab.getLine(i);
    }
}