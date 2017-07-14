package com.company;

import javafx.geometry.Point2D;

import java.util.LinkedList;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class Character {
    double speed = 0.03, sprintSpeed = 0.06, minDistToBlock = 0.1;
    int health, mana, stamina;

    Point2D pos, dir;

    static int[][] map;

    LinkedList<Weapon> weapons = new LinkedList<>();

    public Point2D getPos() {
        return pos;
    }

    public Point2D getDir() {
        return dir;
    }

    public static void setMap(int[][] map) {
        Character.map = map;
    }
}
