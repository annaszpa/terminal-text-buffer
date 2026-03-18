package terminalbuffer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TerminalBufferTest {

    private TerminalBuffer tb;

    @BeforeEach
    public void setup() {
        tb = new TerminalBuffer(10, 5, 5);
    }

    @Test
    public void testCursorMovementWithinBounds() {
        tb.setCurrentCursorPosition(0, 0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tb.moveRight(3);
        });
    }

    @Test
    public void testCursorAndScreenOutput() {
        tb.setCurrentCursorPosition(0, 0);

        tb.write("HELLO MOVE RIGHT, TESTING CURSOR MOVEMENT");

        int[] expectedCursor = {1, 4};
        int[] actualCursor = tb.getCurrentCursorPosition();
        assertArrayEquals(expectedCursor, actualCursor, "Cursor position should match expected");

        List<String> expectedScreen = Arrays.asList(
                "HELLO MOVE",
                " RIGHT, TE",
                "STING CURS",
                "OR MOVEMEN",
                "T"
        );

        List<String> actualScreen = tb.getEntireScreen();
        assertEquals(expectedScreen.size(), actualScreen.size(), "Number of lines should match");

        for (int i = 0; i < expectedScreen.size(); i++) {
            assertEquals(expectedScreen.get(i), actualScreen.get(i), "Line " + i + " should match expected");
        }
    }

    @Test
    public void testOverwriteText() {
        tb.setCurrentCursorPosition(0, 0);
        tb.overwrite("HELLO");

        String line = tb.getString(0);
        assertEquals("HELLO", line.trim());
    }

    @Test
    public void testOverwriteWrapsToNextLine() {
        tb.write("ABC");
        tb.setCurrentCursorPosition(2,0);
        tb.overwrite("XYZ");

        String str = tb.getString(0);

        assertTrue(str.endsWith("Z"));
        assertTrue(str.startsWith("ABXY"));
    }

    @Test
    public void testWriteInsertsCharacter() {
        tb.setCurrentCursorPosition(0, 0);
        tb.write("HELLO");
        tb.setCurrentCursorPosition(2, 0);
        tb.write("X");

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
        tb.write("ABCDEFGHIJK");

        String line0 = tb.getString(0);
        String line1 = tb.getString(1);

        assertEquals(10, line0.length());
        assertEquals("K", line1);
    }

    @Test
    public void testFillLineEmpty() {
        tb.currentTab.rollingTab.fillLine(0, 0, ' ');
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
        tb.write("LINE1");
        tb.setCurrentCursorPosition(0, 1);
        tb.write("LINE2");

        List<String> screen = tb.getEntireScreen();
        assertEquals("LINE1", screen.get(0).trim());
        assertEquals("LINE2", screen.get(1).trim());
    }

    @Test
    public void testSingleLineGoesToArchive() {
        tb.write("LINE1");
        tb.insertEmptyLine();
        tb.insertEmptyLine();
        tb.insertEmptyLine();
        tb.insertEmptyLine();
        tb.insertEmptyLine();

        List<String> all = tb.getEntireScreen();

        assertTrue(all.get(0).contains("LINE1"));
    }

    @Test
    public void testMultipleLinesGoToArchive() {
        for (int i = 0; i < 5; i++) {
            tb.write("L" + i);
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.get(0).contains("L0"));
        assertTrue(all.get(0).contains("L1"));
    }

    @Test
    public void testArchiveOrder() {
        for (int i = 0; i < 7; i++) {
            tb.write("LINE" + i);
        }

        List<String> all = tb.getEntireScreen();
        assertTrue(all.get(0).contains("LINE0"));
        assertTrue(all.get(0).contains("LINE1"));
    }

    @Test
    public void testArchiveLimit() {
        tb = new TerminalBuffer(10, 3, 3);

        for (int i = 0; i < 10; i++) {
            tb.write("L" + i);
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertFalse(all.get(0).contains("L0"));
    }

    @Test
    public void testScreenAndArchiveTogether() {
        for (int i = 0; i < 10; i++) {
            tb.write("LINE" + i);
            tb.insertEmptyLine();
        }

        List<String> all = tb.getEntireScreen();

        assertTrue(all.size() > 5);
    }

    @Test
    public void testWriteTriggersScroll() {
        tb.write("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        List<String> all = tb.getEntireScreen();
        assertTrue(all.size() > 5);
    }
}