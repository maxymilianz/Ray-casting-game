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

    private boolean fullscreen = true;
    private int resX = 1366, resY = 768, fps = 60, msPerFrame = 1000 / fps;
    private int level, difficulty;

    private Cursor c;

    private Camera camera;
    private Hero hero;
    private Input input;
    private Menu menu;
    private State state = State.MENU;
    private Serialization settingsSerialization = new Serialization("settings.ser", Settings.class);
    private Settings settings;

    private LinkedList<int[][]> maps = new LinkedList<>();
    private LinkedList<NPC> NPCs = new LinkedList<>();
    private static Hashtable<Integer, Pair<Double, Double>> wallHeight = new Hashtable<>();        // height and starting y (from top)

    public Game() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dungeon Raider");
        setFocusable(true);
        setResizable(false);

        try {
            readSettings();
            Audio.init();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        initCursorAndFrame();

        getContentPane().setPreferredSize(new Dimension(resX, resY));

        Textures.init();
        initMaps();
        initWallHeight();
        getContentPane().setCursor(c);

        menu = new Menu(resX, resY, this);
        getContentPane().add(menu);

        if (fullscreen) {
            setUndecorated(true);
            setExtendedState(MAXIMIZED_BOTH);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        Audio.resetAndStart(Audio.Sound.MENU);
        run();
    }

    private void readSettings() throws Exception {
        settings = (Settings) settingsSerialization.deserialize();
    }

    void pause() {
        Audio.resetAndStart(Audio.Sound.MENU);
        state = State.PAUSE;
        getContentPane().remove(camera);
        menu.pause();
        getContentPane().add(menu);
        getContentPane().validate();
    }

    void resume() {
        Audio.stop(Audio.Sound.MENU);
        state = State.GAME;
        input.resume();
        getContentPane().remove(menu);
        getContentPane().add(camera);
        getContentPane().validate();
    }

    void restart() {
        newGame();
    }

    void newGame() {
        int[][] map = maps.get(level);

        Character.setMap(map);
        initNPCs();
        Weapon.initWeapons();

        hero = new Hero(0.03, 0.06, 100, 100, 100, 100, 100, 100, new Point2D(4.5, 4.5),
                new Point2D(0, 1), new LinkedList<>());

        input = new Input(this, hero);
        addMouseListener(input);
        addKeyListener(input);

        camera = new Camera(resX, resY, resX / 2, resY / 2, hero, map, NPCs);

        resume();
    }

    void newGame(int level, int difficulty) {
        this.level = level;
        this.difficulty = difficulty;
        newGame();
    }

    private void run() {
        while (true) {
            long time = System.currentTimeMillis();

            if (state != State.GAME)
                menu.update();
            else {
                input.update();
                hero.update();
            }

            Sprite.update();
            repaint();

            if (state == State.GAME) {
                try {
                    Thread.sleep(msPerFrame - (System.currentTimeMillis() - time));
                } catch (Exception e) {
                }
            }
        }
    }

    void exit() {
        System.exit(0);
    }

    private void initNPCs() {
        Random r = new Random();
        int nrOfEnemies = difficulty + r.nextInt(1) * (r.nextBoolean() ? 1 : -1);       // 1 in r.nextInt(int) is arbitrary
        int[][] map = maps.get(level);
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

            NPCs.add(new NPC(0.03, 0.06, 10, 10, 10, 10, 10, 10, new Point2D(x + 0.5, y + 0.5),
                    new Point2D(0, 1), new LinkedList<>(), NPC.Attitude.EVIL, NPC.NPCs.BALDRIC));
        }
    }

    private void initCursorAndFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        c = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TRANSLUCENT), new Point(0, 0), "blank");

        if (!fullscreen)
            return;

        Dimension d = toolkit.getScreenSize();
        resX = d.width;
        resY = d.height;
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

    public boolean isFullscreen() {
        return fullscreen;
    }

    public static Hashtable<Integer, Pair<Double, Double>> getWallHeight() {
        return wallHeight;
    }
}
