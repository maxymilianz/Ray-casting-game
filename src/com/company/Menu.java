package com.company;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

public class Menu extends JPanel {      // now I see that Menu should be just an abstract class and all the menus (main, pause, ...) should extend it
    private class Input implements MouseListener {
        private Point click = new Point(), press = new Point();

        void reset() {
            click = new Point();
            press = new Point();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            click.setLocation(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            press.setLocation(e.getX(), e.getY());
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        public Point getClick() {
            return click;
        }

        public Point getPress() {
            return press;
        }
    }

    private enum Mode {
        MAIN, LEVEL, DIFFICULTY, HIGHSCORES, OPTIONS, GRAPHICS, AUDIO, CONTROLS, CREDITS, QUIT, PAUSE
    }

    enum Text {
        BACK,
        TITLE, NEW_GAME, CONTINUE, HIGHSCORES, OPTIONS, CREDITS, QUIT,
        LEVEL, FIRST,
        DIFFICULTY, EASY, MEDIUM, HARD, EXTREME,
        SETTINGS, GRAPHICS, AUDIO, CONTROLS, APPLY, CANCEL,
        GRAPHICS_SETTINGS, FULLSCREEN, RES, RENDER_RES,
        NATIVE, NATIVE_BY_2, _1080, _720, _600, _300, ON, OFF,
        AUTHORS, CODE, M_Z, REST, INTERNET, PAGE,
        EXIT, YES, NO,
        PAUSE, MENU, RESUME,
    }

    private int resX, resY;

    private BufferedImage cursor;

    private Game game;
    private Input input = new Input();
    private Text focused = null, last;

    private HashSet<Text> goBacks = new HashSet<>();
    private HashSet<Mode> settings = new HashSet<>();

    private Stack<Mode> modeStack = new Stack<>();

    private Hashtable<Text, Dimension> resolutions = new Hashtable<>();
    private Hashtable<Text, Integer> difficulties = new Hashtable<>();
    private Hashtable<Text, Integer> levels = new Hashtable<>();
    private Hashtable<Text, BufferedImage> images = new Hashtable<>();
    private Hashtable<Text, BufferedImage> focusedImages = new Hashtable<>();
    private Hashtable<Text, Mode> modes = new Hashtable<>();
    private Hashtable<Text, String> strings = new Hashtable<>();
    private Hashtable<Text, Point> options = new Hashtable<>();
    private Hashtable<Text, Integer> chosen = new Hashtable<>();
    private Hashtable<Text, Integer> checked = new Hashtable<>();
    private Hashtable<Text, Text[]> possibilities = new Hashtable<>();
    private Hashtable<Mode, LinkedList<Pair<Text, Point>>> texts = new Hashtable<>();

    public Menu(int resX, int resY, Game game) {
        this.resX = resX;
        this.resY = resY;
        this.game = game;

        modeStack.push(Mode.MAIN);

        initStrings();
        initModes();
        initLevels();
        initDifficulties();
        initHashSets();
        initResolutions();
        initChosen();

        try {
            initTexts();
            initCursor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        addMouseListener(input);
    }

    void update() {
        focused = null;
        Point press = input.getPress(), click = input.getClick();

        for (Pair<Text, Point> p : texts.get(modeStack.peek())) {
            Text t = p.getKey();
            BufferedImage img = images.get(t);
            Point point = p.getValue();
            Rectangle rect = new Rectangle(point.x, point.y, img.getWidth(), img.getHeight());

            if (rect.contains(press)) {
                focused = t;

                if (rect.contains(click)) {
                    input.reset();

                    if (modes.containsKey(t)) {
                        last = t;
                        modeStack.push(modes.get(t));
                    }
                    else
                        action(t);
                }
            }
        }
    }

    private void action(Text clicked) {
        if (goBacks.contains(clicked))
            modeStack.pop();
        else if (difficulties.containsKey(clicked))
            game.newGame(levels.get(last), difficulties.get(clicked));
        else if (clicked == Text.CONTINUE)
            game.newGame();
        else if (possibilities.containsKey(clicked))
            iterate(clicked);
        else if (clicked == Text.APPLY)
            apply();
        else if (clicked == Text.CANCEL)
            cancel();
        else if (clicked == Text.PAGE && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://" + strings.get(Text.PAGE)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (clicked == Text.YES)
            game.exit();
        else if (clicked == Text.RESUME)
            resume();
    }

    private void apply() {      // TODO REALLY APPLY CHANGES (THIS WAITS ALMOST UNTIL THE END OF CODING, CAUSE THIS IS CONNECTED TO SERIALIZATION AND I STILL DON'T KNOW WHAT TO SER.)
        for (Pair<Text, Point> p : texts.get(modeStack.peek())) {
            Text t = p.getKey();

            if (possibilities.containsKey(t))
                chosen.put(t, checked.get(t));
        }
    }

    private void cancel() {
        for (Pair<Text, Point> p : texts.get(modeStack.peek())) {
            Text t = p.getKey();

            if (possibilities.containsKey(t))
                checked.put(t, chosen.get(t));
        }

        modeStack.pop();
    }

    private void iterate(Text t) {
        int i = checked.get(t) + 1;
        checked.put(t, i < possibilities.get(t).length ? i : 0);
    }

    private void drawCursor(Graphics g) {
        Point mouse = MouseInfo.getPointerInfo().getLocation(), window = game.getLocation();

        if (game.isFullscreen())
            g.drawImage(cursor, mouse.x - window.x, mouse.y - window.y, null);
        else
            g.drawImage(cursor, mouse.x - window.x - 3, mouse.y - window.y - 26, null);     // 3 and 26 are size of borders
    }

    public void paint(Graphics g) {
        g.drawImage(Textures.getSprites().get(Sprite.Sprites.MENU_BG).getImage(), 0, 0, resX, resY, null);

        for (Pair<Text, Point> p : texts.get(modeStack.peek())) {
            Text t = p.getKey();
            Point point = p.getValue();
            g.drawImage(t == focused ? focusedImages.get(t) : images.get(t), point.x, point.y, null);
        }

        if (settings.contains(modeStack.peek()))
            drawSettingsMenu(g);

        drawCursor(g);
    }

    private void drawSettingsMenu(Graphics g) {
        Mode mode = modeStack.peek();

        for (Pair<Text, Point> p : texts.get(mode)) {
            Text text = p.getKey();

            if (!possibilities.containsKey(text))
                continue;

            int checkedPos = checked.get(text);
            Text t = possibilities.get(text)[checkedPos];
            Point point = options.get(text);
            g.drawImage(checkedPos == chosen.get(text) ? focusedImages.get(t) : images.get(t), point.x, point.y, null);
        }
    }

    private BufferedImage stringToImage(String text, FontMetrics fm, Color c) {
        BufferedImage img = new BufferedImage(fm.stringWidth(text), fm.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(fm.getFont());
        g2d.setColor(c);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        return img;
    }

    void pause() {
        modeStack.push(Mode.PAUSE);
    }

    private void resume() {
        modeStack.pop();
        game.resume();
    }

    private void initChosen() {
        chosen.put(Text.FULLSCREEN, 0);
        chosen.put(Text.RES, 0);
        chosen.put(Text.RENDER_RES, 1);

        checked.put(Text.FULLSCREEN, 0);
        checked.put(Text.RES, 0);
        checked.put(Text.RENDER_RES, 1);
    }

    private void initResolutions() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        resolutions.put(Text.NATIVE, d);
        resolutions.put(Text.NATIVE_BY_2, new Dimension(d.width / 2, d.height / 2));
        resolutions.put(Text._1080, new Dimension(1920, 1080));
        resolutions.put(Text._720, new Dimension(1280, 720));
        resolutions.put(Text._600, new Dimension(1280, 600));
        resolutions.put(Text._300, new Dimension(640, 300));
    }

    private void initHashSets() {
        goBacks.add(Text.BACK);
        goBacks.add(Text.NO);

        settings.add(Mode.GRAPHICS);
        settings.add(Mode.AUDIO);
        settings.add(Mode.CONTROLS);
    }

    private void initDifficulties() {
        difficulties.put(Text.EASY, 5);
        difficulties.put(Text.MEDIUM, 10);
        difficulties.put(Text.HARD, 15);
        difficulties.put(Text.EXTREME, 20);
    }

    private void initLevels() {
        levels.put(Text.FIRST, 0);
    }

    private void initModes() {
        modes.put(Text.NEW_GAME, Mode.LEVEL);
        modes.put(Text.HIGHSCORES, Mode.HIGHSCORES);
        modes.put(Text.OPTIONS, Mode.OPTIONS);
        modes.put(Text.CREDITS, Mode.CREDITS);
        modes.put(Text.QUIT, Mode.QUIT);

        modes.put(Text.FIRST, Mode.DIFFICULTY);

        modes.put(Text.MENU, Mode.MAIN);

        modes.put(Text.GRAPHICS, Mode.GRAPHICS);
        modes.put(Text.AUDIO, Mode.AUDIO);
        modes.put(Text.CONTROLS, Mode.CONTROLS);
    }

    private void initCursor() throws IOException {
        cursor = ImageIO.read(new File("res/cursors/0.png"));
    }

    private void initTexts() throws Exception {     /* in this method, I decided to repeat some code in case for it to work faster
            (otherwise I would have to check if mode != CREDITS in for loop) */
        final float ratioY = (float) resY / 600, ratioX = (float) resX / 1280;
        final int titleY = (int) (40 * ratioY), textY = (int) (200 * ratioY), deltaTextY = (int) (60 * ratioY);
        final float fontSize = 40 * Math.min(ratioY, ratioX), titleFontSize = 100 * Math.min(ratioY, ratioX);

        Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/UnrealT.ttf"));
        LinkedList<Pair<Mode, Pair<Text, Text[]>>> list = new LinkedList<>();
        int endingX = resX * 890 / 1920;        // 890 and 1920 are arbitrary

        list.add(new Pair<>(Mode.MAIN, new Pair<>(Text.TITLE, new Text[]{Text.NEW_GAME, Text.CONTINUE, Text.HIGHSCORES, Text.OPTIONS, Text.CREDITS, Text.QUIT})));
        list.add(new Pair<>(Mode.LEVEL, new Pair<>(Text.LEVEL, new Text[]{Text.FIRST, Text.BACK})));
        list.add(new Pair<>(Mode.DIFFICULTY, new Pair<>(Text.DIFFICULTY, new Text[]{Text.EASY, Text.MEDIUM, Text.HARD, Text.EXTREME, Text.BACK})));
        list.add(new Pair<>(Mode.QUIT, new Pair<>(Text.EXIT, new Text[]{Text.YES, Text.NO})));
        list.add(new Pair<>(Mode.PAUSE, new Pair<>(Text.PAUSE, new Text[]{Text.MENU, Text.RESUME})));
        list.add(new Pair<>(Mode.OPTIONS, new Pair<>(Text.SETTINGS, new Text[]{Text.GRAPHICS, Text.AUDIO, Text.CONTROLS, Text.BACK})));
        list.add(new Pair<>(Mode.GRAPHICS, new Pair<>(Text.GRAPHICS_SETTINGS, new Text[]{Text.FULLSCREEN, Text.RES, Text.RENDER_RES, Text.APPLY, Text.CANCEL})));

        for (Pair<Mode, Pair<Text, Text[]>> p : list) {
            g2d.setFont(font.deriveFont(titleFontSize));
            FontMetrics fm = g2d.getFontMetrics();
            Pair<Text, Text[]> pair = p.getValue();
            Text[] textArray = pair.getValue();

            LinkedList<Pair<Text, Point>> temp = new LinkedList<>();
            Text text = pair.getKey();
            BufferedImage img = stringToImage(strings.get(text), fm, Color.YELLOW);
            images.put(text, img);
            focusedImages.put(text, img);
            temp.add(new Pair<>(text, new Point((resX - img.getWidth()) / 2, titleY)));

            g2d.setFont(font.deriveFont(fontSize));
            fm = g2d.getFontMetrics();

            for (int i = 0; i < textArray.length; i++) {
                text = textArray[i];
                img = stringToImage(strings.get(text), fm, Color.WHITE);
                images.put(text, img);
                focusedImages.put(text, stringToImage(strings.get(text), fm, Color.YELLOW));
                temp.add(new Pair<>(text, new Point(endingX - img.getWidth(), textY + deltaTextY * i)));
            }

            texts.put(p.getKey(), temp);
        }

        g2d.setFont(font.deriveFont(titleFontSize));        // credits
        FontMetrics fm = g2d.getFontMetrics();
        LinkedList<Pair<Text, Point>> temp = new LinkedList<>();
        BufferedImage img = stringToImage(strings.get(Text.AUTHORS), fm, Color.YELLOW);
        images.put(Text.AUTHORS, img);
        focusedImages.put(Text.AUTHORS, img);
        temp.add(new Pair<>(Text.AUTHORS, new Point((resX - img.getWidth()) / 2, titleY)));

        g2d.setFont(font.deriveFont(fontSize));
        fm = g2d.getFontMetrics();
        Text[] credits = new Text[]{Text.CODE, Text.M_Z, Text.REST, Text.INTERNET};
        int y = 0;

        for (int i = 0; i < credits.length; i++) {
            Text text = credits[i];
            img = stringToImage(strings.get(text), fm, Color.WHITE);
            images.put(text, img);
            focusedImages.put(text, img);
            y = textY + deltaTextY * (i / 2);
            temp.add(new Pair<>(text, new Point(i % 2 == 0 ? endingX - img.getWidth() : resX - endingX, y)));
        }

        y += deltaTextY;
        img = stringToImage(strings.get(Text.BACK), fm, Color.WHITE);
        images.put(Text.BACK, img);
        focusedImages.put(Text.BACK, stringToImage(strings.get(Text.BACK), fm, Color.YELLOW));
        temp.add(new Pair<>(Text.BACK, new Point(endingX - img.getWidth(), y)));

        y += deltaTextY;
        img = stringToImage(strings.get(Text.PAGE), fm, Color.WHITE);
        images.put(Text.PAGE, img);
        focusedImages.put(Text.PAGE, stringToImage(strings.get(Text.PAGE), fm, Color.YELLOW));
        temp.add(new Pair<>(Text.PAGE, new Point((resX - img.getWidth()) / 2, y)));

        texts.put(Mode.CREDITS, temp);

        g2d.setFont(font.deriveFont(fontSize));        // graphics options
        fm = g2d.getFontMetrics();

        Text[] tempArray = new Text[]{Text.ON, Text.OFF};

        for (Text t : tempArray) {
            images.put(t, stringToImage(strings.get(t), fm, Color.WHITE));
            focusedImages.put(t, stringToImage(strings.get(t), fm, Color.YELLOW));
        }

        possibilities.put(Text.FULLSCREEN, tempArray);
        options.put(Text.FULLSCREEN, new Point(resX - endingX, textY));

        tempArray = new Text[]{Text.NATIVE, Text.NATIVE_BY_2, Text._1080, Text._720, Text._600, Text._300};

        for (Text t : tempArray) {
            images.put(t, stringToImage(strings.get(t), fm, Color.WHITE));
            focusedImages.put(t, stringToImage(strings.get(t), fm, Color.YELLOW));
        }

        possibilities.put(Text.RES, tempArray);
        possibilities.put(Text.RENDER_RES, tempArray);
        options.put(Text.RES, new Point(resX - endingX, textY + deltaTextY));
        options.put(Text.RENDER_RES, new Point(resX - endingX, textY + 2 * deltaTextY));
    }

    private void initStrings() {
        strings.put(Text.BACK, "BACK");

        strings.put(Text.TITLE, "DUNGEON RAIDER");
        strings.put(Text.NEW_GAME, "NEW GAME");
        strings.put(Text.CONTINUE, "CONTINUE");
        strings.put(Text.HIGHSCORES, "HIGHSCORES");
        strings.put(Text.OPTIONS, "OPTIONS");
        strings.put(Text.CREDITS, "CREDITS");
        strings.put(Text.QUIT, "QUIT");

        strings.put(Text.LEVEL, "SELECT LEVEL");
        strings.put(Text.FIRST, "1ST LEVEL");

        strings.put(Text.DIFFICULTY, "SELECT DIFFICULTY");
        strings.put(Text.EASY, "EASY");
        strings.put(Text.MEDIUM, "MEDIUM");
        strings.put(Text.HARD, "HARD");
        strings.put(Text.EXTREME, "EXTREME");

        strings.put(Text.EXIT, "EXIT GAME?");
        strings.put(Text.YES, "YES");
        strings.put(Text.NO, "NO");

        strings.put(Text.AUTHORS, "CREDITS");
        strings.put(Text.CODE, "CODE");
        strings.put(Text.M_Z, "MAKSYMILIAN ZAWARTKO");
        strings.put(Text.REST, "REST");
        strings.put(Text.INTERNET, "FROM INTERNET");
        strings.put(Text.PAGE, "github.com/maxymilianz/Ray-casting-game");

        strings.put(Text.PAUSE, "PAUSE");
        strings.put(Text.MENU, "BACK TO MENU");
        strings.put(Text.RESUME, "RESUME");

        strings.put(Text.SETTINGS, "SETTINGS");
        strings.put(Text.GRAPHICS, "GRAPHICS");
        strings.put(Text.AUDIO, "AUDIO");
        strings.put(Text.CONTROLS, "CONTROLS");
        strings.put(Text.APPLY, "APPLY");
        strings.put(Text.CANCEL, "CANCEL");

        strings.put(Text.GRAPHICS_SETTINGS, "GRAPHICS SETTINGS");
        strings.put(Text.FULLSCREEN, "FULLSCREEN");
        strings.put(Text.ON, "ON");
        strings.put(Text.OFF, "OFF");
        strings.put(Text.RES, "RESOLUTION");
        strings.put(Text.RENDER_RES, "RENDER RESOLUTION");
        strings.put(Text.NATIVE, "NATIVE");
        strings.put(Text.NATIVE_BY_2, "NATIVE / 2");
        strings.put(Text._1080, "1920 x 1080");
        strings.put(Text._720, "1280 x 720");
        strings.put(Text._600, "1280 x 600");
        strings.put(Text._300, "640 x 300");
    }

    public Hashtable<Text, Dimension> getResolutions() {
        return resolutions;
    }
}
