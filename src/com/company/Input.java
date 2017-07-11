package com.company;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

/**
 * Created by Lenovo on 11.07.2017.
 */
public class Input implements MouseListener, KeyListener {
    private double sensitivity = 0.003;

    Component component;
    Point mousePos;
    Robot robot;

    Character character;

    int[][] map;

    Hashtable<Integer, Boolean> mouseKeys = new Hashtable<>();
    Hashtable<Integer, Boolean> keys = new Hashtable<>();

    public Input(Component component, Character character, int[][] map) {
        this.component = component;
        this.character = character;
        this.map = map;

        initMouseKeys();
        initKeys();

        mousePos = MouseInfo.getPointerInfo().getLocation();

        try {
            robot = new Robot();
        }
        catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void initMouseKeys() {
        mouseKeys.put(MouseEvent.BUTTON1, false);
        mouseKeys.put(MouseEvent.BUTTON3, false);
    }

    private void initKeys() {
        keys.put(KeyEvent.VK_ESCAPE, false);

        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_D, false);
        keys.put(KeyEvent.VK_SHIFT, false);
    }

    void update() {
        updateMouse();
        updateKeys();
    }

    private void updateMouse() {
        Point oldMousePos = mousePos;
        mousePos = MouseInfo.getPointerInfo().getLocation();
        character.turn((oldMousePos.x - mousePos.x) * sensitivity);
        correctMouse();
    }

    private void correctMouse() {
        int w = component.getWidth(), h = component.getHeight();
        Point p = component.getLocationOnScreen(), m = new Point(mousePos);

        m.x += m.x < p.x ? w : m.x >= p.x + w ? -w : 0;
        m.y += m.y < p.y ? h : m.y >= p.y + h ? -h : 0;

        if (!mousePos.equals(m))
            robot.mouseMove(m.x, m.y);
    }

    private void updateKeys() {
        if (mouseKeys.get(MouseEvent.BUTTON1))
            character.shoot();
        if (mouseKeys.get(MouseEvent.BUTTON3))
            character.aim();

        if (keys.get(KeyEvent.VK_W))
            character.forward();
        if (keys.get(KeyEvent.VK_S))
            character.backward();
        if (keys.get(KeyEvent.VK_A))
            character.left();
        if (keys.get(KeyEvent.VK_D))
            character.right();
        if (keys.get(KeyEvent.VK_SHIFT))
            character.sprint();
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        mouseKeys.put(e.getButton(), false);
    }

    public void mousePressed(MouseEvent e) {
        mouseKeys.put(e.getButton(), true);
    }

    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }
}
