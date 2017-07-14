package com.company;

import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class RangedWeapon extends Weapon {
    double bulletSpeed;

    public RangedWeapon(int power, int accuracy, BufferedImage img, double bulletSpeed) {
        super(power, accuracy, img);
        this.bulletSpeed = bulletSpeed;
    }
}
