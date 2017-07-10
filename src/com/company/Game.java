package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Game extends JFrame {
    private int resX = 1280, resY = 600, fps = 60, msPerFrame = 1000 / fps;
    private int level = 0;

    private Camera camera;
    private Character character;

    private LinkedList<int[][]> maps = new LinkedList<>();

    public Game() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ray-casting game");
        setFocusable(true);

        getContentPane().setPreferredSize(new Dimension(resX, resY));

        newGame();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        run();
    }

    private void newGame() {
        int[][] map = maps.get(level);
        character = new Character(2, 2, map);
        camera = new Camera(resX, resY, character, map);
        getContentPane().add(camera);
    }

    private void run() {
        while (true) {
            long time = System.currentTimeMillis();

            repaint();

            try {
                Thread.sleep(msPerFrame - (System.currentTimeMillis() - time));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initMaps() {
        maps.add(new int[][]{{1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 1, 1}});
    }
}
