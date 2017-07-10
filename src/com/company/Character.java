package com.company;

import javafx.geometry.Point2D;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Character {
    private Point2D pos, dir = new Point2D(-2, 1);

    private int[][] map;

    public Character(double x, double y, int[][] map) {
        pos = new Point2D(x, y);
        this.map = map;
    }

    public Point2D getPos() {
        return pos;
    }

    public Point2D getDir() {
        return dir;
    }
}
