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
    private long time = System.currentTimeMillis();
    private double sensitivity = 0.003;

    private Point mousePos;
    private Robot robot;

    private Game game;
    private Hero hero;
    private Game.State state = Game.State.GAME;

    private Hashtable<Integer, Boolean> mouseKeys = new Hashtable<>();
    private Hashtable<Integer, Boolean> keys = new Hashtable<>();

    public Input(Game game, Hero hero) {
        this.game = game;
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

    void resume(long time) {
        this.time = time;
        state = Game.State.GAME;
        robot.mouseMove(mousePos.x, mousePos.y);
    }

    void update() {
        updateMouse();
        updateKeys();
    }

    private void updateMouse() {
        if (state == Game.State.PAUSE)
            return;

        Point oldMousePos = mousePos;
        mousePos = MouseInfo.getPointerInfo().getLocation();
        hero.turn((oldMousePos.x - mousePos.x) * sensitivity);
        hero.lookVertical((oldMousePos.y - mousePos.y) * sensitivity);
        correctMouse();
    }

    private void correctMouse() {
        int margin = 40, w = game.getWidth() - 2 * margin, h = game.getHeight() - 2 * margin;
        Point p = game.getLocationOnScreen(), m = new Point(mousePos);
        p.x += margin;
        p.y += margin;

        m.x += m.x < p.x ? w : m.x >= p.x + w ? -w : 0;
        m.y += m.y < p.y ? h : m.y >= p.y + h ? -h : 0;

        if (!mousePos.equals(m)) {
            robot.mouseMove(m.x, m.y);
            mousePos = m;
        }
    }

    private void updateKeys() {
        if (keys.get(KeyEvent.VK_ESCAPE)) {
            long newTime = System.currentTimeMillis();

            if (newTime - time >= 500) {
                game.pause();
                state = Game.State.PAUSE;
                time = newTime;
            }
        }

        if (state == Game.State.PAUSE)
            return;

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
