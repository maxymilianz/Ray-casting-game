package com.company;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class RangedWeapon extends Weapon {
    double bulletSpeed;

    public RangedWeapon(int power, int accuracy, double bulletSpeed) {
        super(power, accuracy);
        this.bulletSpeed = bulletSpeed;
    }
}
