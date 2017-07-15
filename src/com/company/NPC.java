package com.company;

import javafx.geometry.Point2D;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class NPC extends Character {
    BufferedImage img;

    public NPC(double speed, double sprintSpeed, int health, int mana, int stamina, int maxStamina, Point2D pos, Point2D dir, LinkedList<Weapon.Weapons> weapons, BufferedImage img) {
        super(speed, sprintSpeed, health, mana, stamina, maxStamina, pos, dir, weapons);
        this.img = img;
    }
}
