package com.company;

import javafx.geometry.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Game extends JFrame {
    private int resX = 1280, resY = 600, fps = 60, msPerFrame = 1000 / fps;
    private int level = 0;

    private Camera camera;
    private Hero hero;
    private Input input;

    private LinkedList<int[][]> maps = new LinkedList<>();

    public Game() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ray-casting game");
        setFocusable(true);

        getContentPane().setPreferredSize(new Dimension(resX, resY));

        Textures.init();
        initMaps();

        newGame();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        run();
    }

    private void newGame() {
        int[][] map = maps.get(level);
        Character.setMap(map);
        Weapon.initWeapons();

        hero = new Hero(0.03, 0.06, 100, 100, 100, 100, new Point2D(2.5, 2.5), new Point2D(1, 0), new LinkedList<>());

        input = new Input(this, hero);
        addMouseListener(input);
        addKeyListener(input);

        camera = new Camera(resX, resY, hero);
        getContentPane().add(camera);

        getContentPane().setCursor(Toolkit.getDefaultToolkit().
                createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0, 0), "blank"));
    }

    private void run() {
        while (true) {
            long time = System.currentTimeMillis();

            input.update();
            hero.update();
            Sprite.updateAll();
            repaint();

            try {
                Thread.sleep(msPerFrame - (System.currentTimeMillis() - time));
            }
            catch (Exception e) { }
        }
    }

    private void initMaps() {
        maps.add(new int[][]{{1, 1, 1, 1, 1, 1, 1, 1},
                             {1, 0, 0, 1, 0, 0, 0, 1},
                             {1, 0, 0, 0, 0, 1, 0, 1},
                             {1, 1, 1, 1, 1, 1, 1, 1}});
    }
}
