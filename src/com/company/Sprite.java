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
        BG1, BG2, BG3, BG4, BG5, BG6
    }

    private int i = 0;

    private BufferedImage[] images;

    public Sprite() {
        images = new BufferedImage[]{null};
    }

    public Sprite(BufferedImage[] images) {
        this.images = images;
    }

    static void updateAll() {
        for (Sprites s : Sprites.values())
            Textures.getSprites().get(s).update();
    }

    void update() {
        i++;
        if (i == images.length)
            i = 0;
    }

    BufferedImage getImage() {
        return images[i];
    }
}
