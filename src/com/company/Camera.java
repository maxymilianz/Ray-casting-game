package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Camera extends JPanel {
    private final int resX, resY, wallHeight;

    private BufferedImage rendered;

    private Character character;

    private int[][] map;

    public Camera(int resX, int resY, Character character, int[][] map) {
        this.resX = resX;
        this.resY = resY;
        this.wallHeight = resY;
        this.character = character;
        this.map = map;

        rendered = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g) {
        double tempH = wallHeight / character.getFov() * character.getDefaultFov();
        Point2D dir = character.getDir(), plane = new Point2D(-dir.getY(), dir.getX()).multiply(Math.tan(character.getFov() / 2) * dir.magnitude()), vec = dir.add(plane),
                deltaPlane = plane.multiply((double) 2 / resX), pos = character.getPos();

        for (int i = 0; i < resX; i++, vec = vec.subtract(deltaPlane)) {
            Pair<Point2D, Boolean> collisionInfo = character.collisionInfo(vec);
            Point2D collisionPoint = collisionInfo.getKey();
            boolean pxSide = collisionInfo.getValue();

            int j = 0, h = (int) (tempH * dir.magnitude() / pos.distance(collisionPoint)), emptyH = (resY - h) / 2;

            BufferedImage img = Textures.getBlocks().get(character.block(vec, collisionPoint));
            double tempX = (pxSide ? collisionPoint.getY() : collisionPoint.getX()) % 1;
            tempX += tempX < 0 ? 1 : 0;
            int x = (int) (tempX * img.getWidth());

            for (; j < emptyH; j++)
                rendered.setRGB(i, j, Color.blue.getRGB());
            for (; j < resY && j < resY - emptyH - 1; j++) {
//                rendered.setRGB(i, j, Color.yellow.getRGB());
                int y = (j - emptyH) * img.getHeight() / h;
                rendered.setRGB(i, j, img.getRGB(x, y));
            }
            for (; j < resY; j++)
                rendered.setRGB(i, j, Color.gray.getRGB());
        }

        g.drawImage(rendered, 0, 0, null);

        BufferedImage viewfinder = Textures.getImages().get(Textures.Image.VIEWFINDER);
        g.drawImage(viewfinder, (resX - viewfinder.getWidth()) / 2, (resY - viewfinder.getHeight()) / 2, null);
    }
}
