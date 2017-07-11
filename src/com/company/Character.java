package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Character {
    private double speed = 0.03, sprintSpeed = 0.06, minDistToBlock = 0.1;

    private Point2D pos, dir = new Point2D(0, -1);

    private int[][] map;

    public Character(double x, double y, int[][] map) {
        pos = new Point2D(x, y);
        this.map = map;
        normalizeDir();
    }

    private void go(double speed, Point2D dir) {
        pos = pos.add(dir.multiply(speed));
        Point2D safePos = pos.add(new Point2D(dir.getX() < 0 ? -1 : 1, dir.getY() < 0 ? -1 : 1).multiply(minDistToBlock));

        if (block(dir, safePos) != 0) {
            Pair<Point2D, Boolean> collisionInfo = collisionInfo(dir);
            pos = collisionInfo.getValue() ? new Point2D(Math.floor(pos.getX()) + (dir.getX() < 0 ? 1 + minDistToBlock : -minDistToBlock), pos.getY()) :
                    new Point2D(pos.getX(), Math.floor(pos.getY()) + (dir.getY() < 0 ? 1 + minDistToBlock : -minDistToBlock));
        }
    }

    Pair<Point2D, Boolean> collisionInfo(Point2D vec) {     // TODO
        double x = vec.getX() < 0 ? Math.floor(pos.getX()) : Math.ceil(pos.getX()), y = vec.getY() < 0 ? Math.floor(pos.getY()) : Math.ceil(pos.getY()),
                tg = vec.getY() / vec.getX(), diffX = 1 / tg;     // diffY = tg
        Point2D px = new Point2D(x, tg * (x - pos.getX()) + pos.getY()), py = new Point2D((y - pos.getY()) / tg + pos.getX(), y), closer = null;

        while (closer == null || block(vec, closer) == 0) {
            if (closer == px)
                px = px.add(new Point2D(1, tg));
            else if (closer == py)
                py = py.add(new Point2D(diffX, 1));

            closer = distSquared(pos, px) < distSquared(pos, py) ? px : py;
        }

        return new Pair<>(closer, closer == px);
    }

    private static double distSquared(Point2D p0, Point2D p1) {
        double diffX = p0.getX() - p1.getX(), diffY = p0.getY() - p1.getY();
        return diffX * diffX + diffY * diffY;
    }

    int block(Point2D vec, Point2D collisionPoint) {
        int y = (int) collisionPoint.getY() - (vec.getY() < 0 ? 1 : 0), x = (int) collisionPoint.getX() - (vec.getX() < 0 ? 1 : 0);
        return y >= 0 && y < map.length && x >= 0 && x < map[0].length ? map[y][x] : 1;
    }

    void turn(double angle) {
        double sin = Math.sin(angle), cos = Math.cos(angle);
        dir = new Mtx2x2(cos, -sin, sin, cos).apply(dir);
    }

    void shoot() {

    }

    void aim() {

    }

    void forward() {
        go(speed, dir);
    }

    void backward() {
        go(speed, new Mtx2x2(-1, 0, 0, -1).apply(dir));
    }

    void left() {

    }

    void right() {

    }

    void sprint() {
        go(sprintSpeed, dir);
    }

    private void normalizeDir() {
        dir = dir.multiply(1 / dir.magnitude());
    }

    public Point2D getPos() {
        return pos;
    }

    public Point2D getDir() {
        return dir;
    }
}
