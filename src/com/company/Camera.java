package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Camera extends JPanel {
    private final int resX, resY;

    private Character character;

    private int[][] map;

    Camera(int resX, int resY, Character character, int[][] map) {
        this.resX = resX;
        this.resY = resY;
        this.character = character;
        this.map = map;
    }

    public void paint(Graphics g) {

    }
}
