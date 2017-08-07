package com.company;

import java.util.Hashtable;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class Weapon {
    enum Weapons {
        S_SWORD
    }

    int power, accuracy;

    private static Hashtable<Weapons, Weapon> weapons = new Hashtable<>();

    public Weapon(int power, int accuracy) {
        this.power = power;
        this.accuracy = accuracy;
    }

    static void initWeapons() {
        weapons.put(Weapons.S_SWORD, new Weapon(10, 1));
    }
}
