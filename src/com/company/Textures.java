package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Textures {
    enum Image {
        VIEWFINDER,
        BULLET, L_BULLET, R_BULLET, SHOT
    }

    private static Hashtable<Integer, BufferedImage> blocks = new Hashtable<>();
    private static Hashtable<Image, BufferedImage> images = new Hashtable<>();

    static void init() {
        try {
            initBlocks();
            initImages();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initImages() throws IOException {
        images.put(Image.VIEWFINDER, ImageIO.read(new File("res/viewfinder.png")));
        images.put(Image.BULLET, ImageIO.read(new File("res/assets/bullet.png")));
        images.put(Image.L_BULLET, ImageIO.read(new File("res/assets/light_bullet.png")));
        images.put(Image.R_BULLET, ImageIO.read(new File("res/assets/red_bullet.png")));
        images.put(Image.SHOT, ImageIO.read(new File("res/assets/shot.png")));
    }

    private static void initBlocks() throws IOException{
        blocks.put(1, ImageIO.read(new File("res/Stk textures/egypt_stone.png")));
        blocks.put(2, ImageIO.read(new File("res/Stk textures/jungle_stone.png")));
        blocks.put(3, ImageIO.read(new File("res/Stk textures/metal_plates.png")));
        blocks.put(4, ImageIO.read(new File("res/Stk textures/palmtree.png")));
        blocks.put(5, ImageIO.read(new File("res/Stk textures/sand_stone.png")));
        blocks.put(6, ImageIO.read(new File("res/Stk textures/wood.png")));
    }

    public static Hashtable<Image, BufferedImage> getImages() {
        return images;
    }

    public static Hashtable<Integer, BufferedImage> getBlocks() {
        return blocks;
    }
}
