package com.company;

import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 15.07.2017.
 */
public class Sprite {
    private int i = 0;

    private BufferedImage[] images;

    public Sprite() {
        images = new BufferedImage[]{null};
    }

    public Sprite(BufferedImage[] images) {
        this.images = images;
    }

    BufferedImage getImage() {
        return images[i];
    }
}
