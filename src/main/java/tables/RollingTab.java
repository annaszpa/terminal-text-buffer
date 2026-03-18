package tables;

import element.Element;
import java.util.ArrayList;
import java.util.List;

public class RollingTab {

    int width;
    int height;

    int startLine;
    int endLine;

    boolean isFull;

    List<Element>[] lines;
    List<List<Element>> removedLines = new ArrayList<>();

    public RollingTab(int width, int height) {
        this.width = width;
        this.height = height;

        this.startLine = 0;
        this.endLine = 0;
        this.isFull = false;

        this.lines = new ArrayList[height];

        for (int i = 0; i < height; i++) {
            lines[i] = new ArrayList<>();
        }
    }

    boolean isOutOfBounds(int x, int y) {
        if (x < 0 || x >= width) return true;
        if (y < 0 || y >= height) return true;

        int idx = (y + startLine) % height;
        if (x > lines[idx].size()) return true;

        return false;
    }

    public int[] clearTheScreen() {
        for (int i = 0; i < height; i++) {
            lines[i] = new ArrayList<>();
        }
        startLine = 0;
        endLine = 0;
        isFull = false;
        return new int[]{0, 0};
    }

    public void insertEmptyLine() {
        List<Element> removed = null;

        if (isFull) {
            removed = new ArrayList<>(lines[startLine]);
            removedLines.add(removed);

            lines[startLine] = new ArrayList<>();
            startLine = (startLine + 1) % height;
        } else {
            endLine++;
            if (endLine >= height) {
                isFull = true;
            }
        }
    }

    public List<List<Element>> consumeRemovedLines() {
        List<List<Element>> result = new ArrayList<>(removedLines);
        removedLines.clear();
        return result;
    }

    public Element getElement(int x, int y) {
        if (isOutOfBounds(x, y)) {
            return null;
        }
        return lines[(y + startLine) % height].get(x);
    }

    public int getHeight() {
        return height;
    }

    public String getString(int x) {
        if (x < 0 || x >= height) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Element e : lines[(x + startLine) % height]) {
            sb.append(e == null ? ' ' : e.character);
        }

        return sb.toString();
    }

    public List<String> getEntireScreen() {
        List<String> result = new ArrayList<>();
        for (int x = 0; x < height; x++) {
            result.add(getString(x));
        }
        return result;
    }

    public int[] overwriting(List<Element> text, int x, int y) {
        int col = x;
        int row = y;

        for (Element e : text) {
            int idx = (row + startLine) % height;
            List<Element> line = lines[idx];

            while (line.size() <= col) {
                line.add(null);
            }

            line.set(col, e);
            col++;

            if (col >= width) {
                col = 0;
                row++;

                if (row >= height) {
                    insertEmptyLine();
                    row = height - 1;
                }
            }
        }

        return new int[]{col, row};
    }

    public int[] fillLine(int x, int y, Element e) {
        List<Element> line = new ArrayList<>();

        for (int i = x; i < width; i++) {
            line.add(new Element(e.character, e.foregroundColour, e.backgroundColour, e.flags));
        }

        lines[(y + startLine) % height] = line;
        return new int[]{width - 1, y};
    }

    int[] insertText(List<Element> text, int x, int y) {
        int row = y;
        int col = x;

        for (Element e : text) {
            if (row >= height) {
                insertEmptyLine();
                row = height - 1;
            }

            int idx = (row + startLine) % height;
            List<Element> line = lines[idx];

            while (line.size() < col) {
                line.add(null);
            }

            line.add(col, e);

            if (line.size() > width) {
                Element overflow = line.remove(width);
                row++;
                col = 0;

                if (row >= height) {
                    insertEmptyLine();
                    row = height - 1;
                }

                int nextIdx = (row + startLine) % height;
                lines[nextIdx].add(0, overflow);
                continue;
            }

            col++;

            if (col >= width) {
                row++;
                col = 0;
            }
        }

        return new int[]{col, row};
    }
}