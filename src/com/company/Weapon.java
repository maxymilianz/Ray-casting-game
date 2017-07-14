package com.company;

import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class Weapon {
    int power, accuracy;

    BufferedImage img;

    public Weapon(int power, int accuracy, BufferedImage img) {
        this.power = power;
        this.accuracy = accuracy;
        this.img = img;
    }
}
