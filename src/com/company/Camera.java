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
    private final int resX, resY;

    private int fov = 66 * (int) Math.PI / 180;

    private BufferedImage rayCasted;
    private Point2D dir, plane;

    private Character character;

    private int[][] map;

    public Camera(int resX, int resY, Character character, int[][] map) {
        this.resX = resX;
        this.resY = resY;
        this.character = character;
        this.map = map;

        rayCasted = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g) {
        dir = character.getDir();
        plane = new Point2D(-dir.getY(), dir.getX()).multiply(Math.tan(fov / 2) * dir.magnitude());

        for (int i = 0; i < resX; i++) {
            Pair<Point2D, Boolean> collisionInfo = collisionInfo(dir.add(plane));
            Point2D collisionPoint = collisionInfo.getKey();
            boolean pxSide = collisionInfo.getValue();


        }
    }

    private Pair<Point2D, Boolean> collisionInfo(Point2D vec) {
        Point2D pos = character.getPos();
        double x = vec.getX() < 0 ? Math.floor(pos.getX()) : Math.ceil(pos.getX()), y = vec.getY() < 0 ? Math.floor(pos.getY()) : Math.ceil(pos.getY()), tg = vec.getY() / vec.getX(),
            diffX = 1 / tg;     // diffY = tg
        Point2D px = new Point2D(x, tg * (x - pos.getX()) + pos.getY()), py = new Point2D(y, (y - pos.getY()) / tg + pos.getX()), closer = null;

        while (closer == null || map[(int) Math.round(closer.getY()) - (vec.getY() < 0 ? 1 : 0)][(int) Math.round(closer.getX()) - (vec.getX() < 0 ? 1 : 0)] == 0) {
            if (closer == px)
                px.add(1, tg);
            else if (closer == py)
                py.add(diffX, 1);

            closer = distSquared(pos, px) < distSquared(pos, py) ? px : py;
        }

        return new Pair<>(closer, closer == px);
    }

    private static double distSquared(Point2D p0, Point2D p1) {
        double diffX = p0.getX() - p1.getX(), diffY = p0.getY() - p1.getY();
        return diffX * diffX + diffY * diffY;
    }
}
