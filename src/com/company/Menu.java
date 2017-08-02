package com.company;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;

public class Menu extends JPanel {
    private class Input implements MouseListener {
        private Point click = new Point(), press = new Point();

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
        MAIN, LEVEL, DIFFICULTY, HIGHSCORES, OPTIONS, CREDITS, QUIT
    }

    private enum Text {
        TITLE, NEW_GAME, CONTINUE, HIGHSCORES, OPTIONS, CREDITS, QUIT
    }

    private int resX, resY;

    private Game game;
    private Input input = new Input();
    private Mode mode = Mode.MAIN;

    private Hashtable<Text, String> strings = new Hashtable<>();
    private Hashtable<Mode, LinkedList<Pair<BufferedImage, Point>>> texts = new Hashtable<>();

    public Menu(int resX, int resY, Game game) {
        this.resX = resX;
        this.resY = resY;
        this.game = game;

        initStrings();

        try {
            initTexts();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update() {

    }

    public void paint(Graphics g) {
        g.drawImage(Textures.getSprites().get(Sprite.Sprites.MENU_BG).getImage(), 0, 0, resX, resY, null);

        for (Pair<BufferedImage, Point> p : texts.get(mode)) {
            Point point = p.getValue();
            g.drawImage(p.getKey(), point.x, point.y, null);
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

    private void initTexts() throws Exception {
        Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/UnrealT.ttf"));
        LinkedList<Pair<Mode, Pair<Text, Text[]>>> list = new LinkedList<>();
        int endingX = resX * 890 / 1920;        // 890 and 1920 are arbitrary

        list.add(new Pair<>(Mode.MAIN, new Pair<>(Text.TITLE, new Text[]{Text.NEW_GAME, Text.CONTINUE, Text.HIGHSCORES, Text.OPTIONS, Text.CREDITS, Text.QUIT})));

        for (Pair<Mode, Pair<Text, Text[]>> p : list) {
            g2d.setFont(font.deriveFont(100f));
            FontMetrics fm = g2d.getFontMetrics();
            Pair<Text, Text[]> pair = p.getValue();
            Text[] textArray = pair.getValue();

            LinkedList<Pair<BufferedImage, Point>> temp = new LinkedList<>();
            BufferedImage img = stringToImage(strings.get(pair.getKey()), fm, Color.WHITE);
            temp.add(new Pair<>(img, new Point((resX - img.getWidth()) / 2, 40)));      // 40 is arbitrary

            texts.put(p.getKey(), temp);

            g2d.setFont(font.deriveFont(40f));
            fm = g2d.getFontMetrics();

            for (int i = 0; i < textArray.length; i++) {
                img = stringToImage(strings.get(textArray[i]), fm, Color.WHITE);
                temp.add(new Pair<>(img, new Point(endingX - img.getWidth(), 200 + 60 * i)));       // 200 and 60 are arbitrary
            }
        }
    }

    private void initStrings() {
        strings.put(Text.TITLE, "DUNGEON RAIDER");
        strings.put(Text.NEW_GAME, "NEW GAME");
        strings.put(Text.CONTINUE, "CONTINUE");
        strings.put(Text.HIGHSCORES, "HIGHSCORES");
        strings.put(Text.OPTIONS, "OPTIONS");
        strings.put(Text.CREDITS, "CREDITS");
        strings.put(Text.QUIT, "QUIT");
    }
}
