package com.company;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Character {
    private int x, y;

    private int[][] map;

    Character(int x, int y, int[][] map) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
