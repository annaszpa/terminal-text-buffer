package terminalbuffer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import element.Color;
import element.Style;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import element.Element;

public class TerminalBufferTest {

    private TerminalBuffer tb;

    @BeforeEach
    public void setup() {
        tb = new TerminalBuffer(10, 5, 5);
    }

    private List<Element> toElements(String s) {
        return s.chars()
                .mapToObj(c -> new Element((char) c))
                .collect(Collectors.toList());
    }

    @Test
    public void testCursorMovementWithinBounds() {
        tb.setCurrentCursorPosition(0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            tb.moveRight(3);
        });
    }

    @Test
    public void testCursorAndScreenOutput() {
        tb.setCurrentCursorPosition(0, 0);

        tb.write(toElements("HELLO MOVE RIGHT, TESTING CURSOR MOVEMENT"));

        int[] expectedCursor = {1, 4};
        int[] actualCursor = tb.getCurrentCursorPosition();
        assertArrayEquals(expectedCursor, actualCursor);

        List<String> expectedScreen = Arrays.asList(
                "HELLO MOVE",
                " RIGHT, TE",
                "STING CURS",
                "OR MOVEMEN",
                "T"
        );

        List<String> actualScreen = tb.getEntireScreen();

        assertEquals(expectedScreen.size(), actualScreen.size());

        for (int i = 0; i < expectedScreen.size(); i++) {
            assertEquals(expectedScreen.get(i), actualScreen.get(i));
        }
    }

    @Test
    public void testOverwriteText() {
        tb.setCurrentCursorPosition(0, 0);
        tb.overwrite(toElements("HELLO"));

        String line = tb.getString(0);
        assertEquals("HELLO", line.trim());
    }

    @Test
    public void testOverwriteWrapsToNextLine() {
        tb.write(toElements("ABC"));
        tb.setCurrentCursorPosition(2, 0);
        tb.overwrite(toElements("XYZ"));

        String str = tb.getString(0);

        assertTrue(str.endsWith("Z"));
        assertTrue(str.startsWith("ABXY"));
    }

    @Test
    public void testWriteInsertsCharacter() {
        tb.setCurrentCursorPosition(0, 0);
        tb.write(toElements("HELLO"));

        tb.setCurrentCursorPosition(2, 0);
        tb.write(toElements("X"));

        String line = tb.getString(0);

        assertEquals('H', line.charAt(0));
        assertEquals('E', line.charAt(1));
        assertEquals('X', line.charAt(2));
        assertEquals('L', line.charAt(3));
        assertEquals('L', line.charAt(4));
        assertEquals('O', line.charAt(5));
    }

    @Test
    public void testWriteOverflowsLine() {
        tb.setCurrentCursorPosition(0, 0);
        tb.write(toElements("ABCDEFGHIJK"));

        String line0 = tb.getString(0);
        String line1 = tb.getString(1);

        assertEquals(10, line0.length());
        assertEquals("K", line1);
    }

    @Test
    public void testFillLineEmpty() {
        tb.currentTab.rollingTab.fillLine(0, 0, new Element(' '));

        String line = tb.getString(2);
        assertEquals("", line.trim());
    }

    @Test
    public void testFillLineWithCharacter() {
        tb.fillLine(0, 0, '*');

        String line = tb.getString(0);
        assertEquals("**********", line);
    }

    @Test
    public void testGetEntireScreenOrder() {
        tb.setCurrentCursorPosition(0, 0);
        tb.write(toElements("LINE1"));

        tb.setCurrentCursorPosition(0, 1);
        tb.write(toElements("LINE2"));

        List<String> screen = tb.getEntireScreen();

        assertEquals("LINE1", screen.get(0).trim());
        assertEquals("LINE2", screen.get(1).trim());
    }

    @Test
    public void testSingleLineGoesToArchive() {
        tb.write(toElements("LINE1"));

        for (int i = 0; i < 5; i++) {
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.get(0).contains("LINE1"));
    }

    @Test
    public void testMultipleLinesGoToArchive() {
        for (int i = 0; i < 5; i++) {
            tb.write(toElements("L" + i));
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.get(0).contains("L0"));
        assertTrue(all.get(0).contains("L1"));
    }

    @Test
    public void testArchiveOrder() {
        for (int i = 0; i < 7; i++) {
            tb.write(toElements("LINE" + i));
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.get(0).contains("LINE0"));
        assertTrue(all.get(0).contains("LINE1"));
    }

    @Test
    public void testArchiveLimit() {
        tb = new TerminalBuffer(10, 3, 3);

        for (int i = 0; i < 10; i++) {
            tb.write(toElements("L" + i));
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertFalse(all.get(0).contains("L0"));
    }

    @Test
    public void testScreenAndArchiveTogether() {
        for (int i = 0; i < 10; i++) {
            tb.write(toElements("LINE" + i));
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.size() > 5);
    }

    @Test
    public void testWriteTriggersScroll() {
        tb.write(toElements(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

        List<String> all = tb.getEntireScreen();

        assertTrue(all.size() > 5);
    }
    @Test
    public void testElementConstructorWithChar() {
        Element e = new Element('A');

        assertEquals('A', e.character);
        assertEquals(Color.DEFAULT, e.foreground);
        assertEquals(Color.DEFAULT, e.background);
        assertTrue(e.styles.isEmpty());
    }
    @Test
    public void testElementFullConstructor() {
        Element e = new Element(
                'X',
                Color.RED,
                Color.BLUE,
                EnumSet.of(Style.BOLD)
        );

        assertEquals('X', e.character);
        assertEquals(Color.RED, e.foreground);
        assertEquals(Color.BLUE, e.background);
        assertTrue(e.styles.contains(Style.BOLD));
    }

    @Test
    public void testWritePreservesElementProperties() {
        TerminalBuffer tb = new TerminalBuffer(10, 2, 2);

        Element e = new Element(
                'A',
                Color.GREEN,
                Color.BLACK,
                EnumSet.of(Style.UNDERLINE)
        );

        tb.write(Arrays.asList(e));

        Element stored = tb.currentTab.getElement(0, 0);

        assertEquals('A', stored.character);
        assertEquals(Color.GREEN, stored.foreground);
        assertEquals(Color.BLACK, stored.background);
        assertTrue(stored.styles.contains(Style.UNDERLINE));
    }

    @Test
    public void testOverwriteDoesNotShiftElements() {
        TerminalBuffer tb = new TerminalBuffer(10, 2, 2);

        tb.write(tb.currentTab.wrapString("ABCDE"));

        tb.setCurrentCursorPosition(2, 0);
        tb.overwrite(Arrays.asList(new Element('X')));

        String line = tb.getString(0);

        assertEquals("ABXDE", line);
    }
    @Test
    public void testWriteShiftsElements() {
        TerminalBuffer tb = new TerminalBuffer(10, 2, 2);

        tb.write(tb.currentTab.wrapString("ABCDE"));

        tb.setCurrentCursorPosition(2, 0);
        tb.write(Arrays.asList(new Element('X')));

        String line = tb.getString(0);

        assertEquals("ABXCDE", line);
    }
}