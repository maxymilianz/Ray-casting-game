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

    Hero hero;

    Hashtable<Integer, Boolean> mouseKeys = new Hashtable<>();
    Hashtable<Integer, Boolean> keys = new Hashtable<>();

    public Input(Component component, Hero hero) {
        this.component = component;
        this.hero = hero;

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
        hero.turn((oldMousePos.x - mousePos.x) * sensitivity);
        hero.lookVertical((oldMousePos.y - mousePos.y) * sensitivity);
        correctMouse();
    }

    private void correctMouse() {
        int w = component.getWidth(), h = component.getHeight();
        Point p = component.getLocationOnScreen(), m = new Point(mousePos);

        m.x += m.x < p.x ? w : m.x >= p.x + w ? -w : 0;
        m.y += m.y < p.y ? h : m.y >= p.y + h ? -h : 0;

        if (!mousePos.equals(m)) {
            robot.mouseMove(m.x, m.y);
            mousePos = m;
        }
    }

    private void updateKeys() {
        if (mouseKeys.get(MouseEvent.BUTTON1))
            hero.attack();
        if (mouseKeys.get(MouseEvent.BUTTON3))
            hero.aim();

        if (keys.get(KeyEvent.VK_W))
            hero.forward();
        if (keys.get(KeyEvent.VK_S))
            hero.backward();
        if (keys.get(KeyEvent.VK_A))
            hero.left();
        if (keys.get(KeyEvent.VK_D))
            hero.right();
        if (keys.get(KeyEvent.VK_SHIFT))
            hero.sprint();
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
