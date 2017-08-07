package com.company;

import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 15.07.2017.
 */
public class Sprite {
    enum Sprites {
        S_SWORD,
        VIEWFINDER,
        BULLET, L_BULLET, R_BULLET, SHOT,
        BG1, BG2, BG3, BG4, BG5, BG6,
        B_STANDING, B_FALLING, B_FALLED, B_CASTING, B_WALKING, B_ATTACKING,      // baldric
        M_STANDING, M_FALLING, M_FALLED, M_CASTING, M_WALKING,       // mage
        KNIGHT, ORC,
        FLOOR0,
        CEILING0,
        H0, H1, H2, H3, H4, H5, H6, H7, H8,     // health
        M0, M1, M2, M3, M4, M5, M6, M7, M8,      // mana
        MENU_BG
    }

    enum View {
        FRONT, BACK, LEFT, RIGHT
    }

    private static int time = 0;

    private BufferedImage[] images;

    public Sprite(BufferedImage[] images) {
        this.images = images;
    }

    static void update() {
        time++;
    }

    BufferedImage getImage() {
        return images[(time / 10) % images.length];     // 10 is arbitrary
    }
}
