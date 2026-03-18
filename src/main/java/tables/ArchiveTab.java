package tables;

import java.util.ArrayList;
import java.util.List;

public class ArchiveTab {

    private ArrayList[] list;
    private int maxLines;

    private int start = 0;
    private int size = 0;

    public ArchiveTab(int maxLines) {
        this.maxLines = maxLines;
        this.list = new ArrayList[maxLines];
    }

    public void addLine(List<Character> line) {

        int index = (start + size) % maxLines;

        if (size == maxLines) {
            list[index] = new ArrayList<>(line);
            start = (start + 1) % maxLines;
        } else {
            list[index] = new ArrayList<>(line);
            size++;
        }
    }

    public List<Character> getLine(int i) {
        if (i < 0 || i >= size) {
            return new ArrayList<>();
        }

        int index = (start + i) % maxLines;
        return list[index];
    }

    public String getLineAsString(int i) {
        List<Character> line = getLine(i);

        StringBuilder sb = new StringBuilder();
        for (Character c : line) {
            sb.append(c == null ? ' ' : c);
        }

        return sb.toString();
    }

    public List<String> getAllAsString() {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            result.add(getLineAsString(i));
        }

        return result;
    }

    public void clear() {
        start = 0;
        size = 0;
        list = new ArrayList[maxLines];
    }

    public int size() {
        return size;
    }
}