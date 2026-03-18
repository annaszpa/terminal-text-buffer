package tables;

import element.Element;
import java.util.ArrayList;
import java.util.List;

public class ArchiveTab {

    private ArrayList<Element>[] list;
    private int maxLines;

    private int start = 0;
    private int size = 0;

    public ArchiveTab(int maxLines) {
        this.maxLines = maxLines;
        this.list = new ArrayList[maxLines];
    }

    public void addLine(List<Element> line) {
        int index = (start + size) % maxLines;

        if (size == maxLines) {
            list[index] = new ArrayList<>(line);
            start = (start + 1) % maxLines;
        } else {
            list[index] = new ArrayList<>(line);
            size++;
        }
    }

    public List<Element> getLine(int i) {
        if (i < 0 || i >= size) {
            return new ArrayList<>();
        }

        int index = (start + i) % maxLines;
        return list[index];
    }

    public String getLineAsString(int i) {
        List<Element> line = getLine(i);

        StringBuilder sb = new StringBuilder();
        for (Element e : line) {
            sb.append(e == null ? ' ' : e.character);
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