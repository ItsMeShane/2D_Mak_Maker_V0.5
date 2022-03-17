package input;

import main.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

    boolean P_CTRL;
    boolean P_Z;
    boolean P_SHIFT;

    boolean moveCursor = false;
    MapPanel mapPanel;
    public KeyInput(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            if (JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0) == JOptionPane.YES_OPTION)
                System.exit(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int interactedKey = e.getKeyCode();

        if (interactedKey == KeyEvent.VK_CONTROL) P_CTRL = true;
        if (interactedKey == KeyEvent.VK_Z) P_Z = true;
        if (interactedKey == KeyEvent.VK_SHIFT) P_SHIFT = true;

        boolean undo = P_CTRL && P_Z && !P_SHIFT && mapPanel.undo.size() > 1;
        boolean redo = P_CTRL && P_Z && P_SHIFT && mapPanel.redo.size() > 0;

        if (undo) {
            mapPanel.redo.push(mapPanel.undo.peek());
            mapPanel.undo.pop();
            mapPanel.tileBarSettings.options.loadMap(mapPanel.undo.peek());
        }

        if (redo) {
            mapPanel.undo.push(mapPanel.redo.peek());
            mapPanel.tileBarSettings.options.loadMap(mapPanel.redo.peek());
            mapPanel.redo.pop();
        }

        if (e.isShiftDown() && e.isControlDown()) {
            moveCursor = true;
            applyCursor();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int interactedKey = e.getKeyCode();

        if (interactedKey == KeyEvent.VK_CONTROL) P_CTRL = false;
        if (interactedKey == KeyEvent.VK_Z) P_Z = false;
        if (interactedKey == KeyEvent.VK_SHIFT) P_SHIFT = false;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            if (JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0) == JOptionPane.YES_OPTION)
                System.exit(0);

        if (!e.isShiftDown() || !e.isControlDown()) {
            moveCursor = false;
            applyCursor();
        }
    }

    void applyCursor() {
        if (moveCursor)
            mapPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        else
            mapPanel.updateCursor(mapPanel.tileBar.tileValue);
    }
}