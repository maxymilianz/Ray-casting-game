package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Game extends JFrame {
    enum State {
        MENU, GAME, PAUSE
    }

    private int resX = 1280, resY = 600, fps = 60, msPerFrame = 1000 / fps;
    private int nrAccuracy = 1;
    private int level = 0, difficulty = 5;

    private Camera camera;
    private Hero hero;
    private Input input;
    private Menu menu;
    private State state = State.MENU;

    private LinkedList<int[][]> maps = new LinkedList<>();
    private LinkedList<NPC> NPCs = new LinkedList<>();
    private static Hashtable<Integer, Pair<Double, Double>> wallHeight = new Hashtable<>();        // height and starting y (from top)

    public Game() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ray-casting game");
        setFocusable(true);
        setResizable(false);

        getContentPane().setPreferredSize(new Dimension(resX, resY));

        Textures.init();
        initMaps();
        initWallHeight();

//        newGame();
        menu = new Menu(resX, resY, this);
        getContentPane().add(menu);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        run();
    }

    void pause() {

    }

    void resume() {

    }

    void newGame() {
        int[][] map = maps.get(level);
        Character.setMap(map);
        Weapon.initWeapons();

        hero = new Hero(0.03, 0.06, 100, 100, 100, 100, 100, 100, new Point2D(4.5, 4.5), new Point2D(0, 1),
                new LinkedList<>());

        input = new Input(this, hero);
        addMouseListener(input);
        addKeyListener(input);

        camera = new Camera(resX, resY, hero, map, NPCs);
        getContentPane().add(camera);

        initNPCs();

        getContentPane().setCursor(Toolkit.getDefaultToolkit().
                createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0, 0), "blank"));
    }

    private void run() {
        while (true) {
            long time = System.currentTimeMillis();

            if (state == State.MENU)
                menu.update();
            else {
                input.update();
                hero.update();
            }

            Sprite.update();
            repaint();

            try {
                Thread.sleep(msPerFrame - (System.currentTimeMillis() - time));
            }
            catch (Exception e) { }
        }
    }

    private void initNPCs() {
        Random r = new Random();
        int[][] map = maps.get(level);
        int nrOfEnemies = difficulty + r.nextInt(nrAccuracy) * (r.nextBoolean() ? 1 : -1);
        nrOfEnemies = nrOfEnemies < 0 ? 0 : nrOfEnemies;

        for (int i = 0; i < nrOfEnemies; i++) {
            int x = r.nextInt(map[0].length), y = r.nextInt(map.length);
            boolean collides = true;

            while (collides) {
                collides = map[y][x] != 0;

                for (NPC j : NPCs)
                    if (!collides)
                        collides = x == (int) j.getPos().getX() && y == (int) j.getPos().getY();

                if (collides) {
                    x = r.nextInt(map[0].length);
                    y = r.nextInt(map.length);
                }
            }

            NPCs.add(new NPC(0.03, 0.06, 10, 10, 10, 10, 10, 10, new Point2D(x + 0.5, y + 0.5), new Point2D(0, 1),
                    new LinkedList<>(), NPC.Attitude.EVIL, NPC.NPCs.BALDRIC));
        }
    }

    private void initWallHeight() {
        wallHeight.put(0, new Pair<>(0d, 0d));

        for (int i = 1; i < 5; i++)
            wallHeight.put(i, new Pair<>(1d, 0d));

        wallHeight.put(5, new Pair<>(.3, .7));
        wallHeight.put(6, new Pair<>(.6, .4));
    }

    private void initMaps() {
        maps.add(new int[][]{{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
                            {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                            {1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
                            {1,0,3,5,0,6,3,0,2,0,0,0,0,0,2},
                            {1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
                            {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                            {1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
                            {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                            {1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
                            {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                            {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                            {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                            {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                            {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}});
    }

    String[] getHighscores() {
        return new String[0];
    }

    State getGameState() {
        return state;
    }

    public static Hashtable<Integer, Pair<Double, Double>> getWallHeight() {
        return wallHeight;
    }
}
