package com.company;

import javafx.geometry.Point2D;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Character {
    private Point2D pos, dir = new Point2D(0, -1);

    private int[][] map;

    public Character(double x, double y, int[][] map) {
        pos = new Point2D(x, y);
        this.map = map;
        normalizeDir();
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

    }

    void backward() {

    }

    void left() {

    }

    void right() {

    }

    void sprint() {

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
