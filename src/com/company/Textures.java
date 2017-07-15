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
        BULLET, L_BULLET, R_BULLET, SHOT,
    }

    private static Hashtable<Sprite.Sprites, Sprite> sprites = new Hashtable<>();
    private static Hashtable<Integer, Sprite> blocks = new Hashtable<>();
    private static Hashtable<Image, Sprite> images = new Hashtable<>();
    private static Hashtable<Weapon.Weapons, Sprite> weapons = new Hashtable<>();

    static void init() {
        try {
            initBlocks();
            initImages();
            initWeapons();

            initSprites();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initWeapons() throws IOException {
        weapons.put(Weapon.Weapons.S_SWORD, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/scithersword.png"))}));
    }

    private static void initImages() throws IOException {
        images.put(Image.VIEWFINDER, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/viewfinder.png"))}));

        images.put(Image.BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/bullet.png"))}));
        images.put(Image.L_BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/light_bullet.png"))}));
        images.put(Image.R_BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/red_bullet.png"))}));
        images.put(Image.SHOT, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/shot.png"))}));
    }

    private static void initBlocks() throws IOException{
        blocks.put(1, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/egypt_stone.png"))}));
        blocks.put(2, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/jungle_stone.png"))}));
        blocks.put(3, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/metal_plates.png"))}));
        blocks.put(4, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/palmtree.png"))}));
        blocks.put(5, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/sand_stone.png"))}));
        blocks.put(6, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/wood.png"))}));
    }

    private static void initSprites() throws IOException {
        sprites.put(Sprite.Sprites.S_SWORD, weapons.get(Weapon.Weapons.S_SWORD));

        sprites.put(Sprite.Sprites.VIEWFINDER, images.get(Image.VIEWFINDER));

        sprites.put(Sprite.Sprites.BULLET, images.get(Image.BULLET));
        sprites.put(Sprite.Sprites.L_BULLET, images.get(Image.L_BULLET));
        sprites.put(Sprite.Sprites.R_BULLET, images.get(Image.R_BULLET));
        sprites.put(Sprite.Sprites.SHOT, images.get(Image.SHOT));

        sprites.put(Sprite.Sprites.BG1, blocks.get(1));
        sprites.put(Sprite.Sprites.BG2, blocks.get(2));
        sprites.put(Sprite.Sprites.BG3, blocks.get(3));
        sprites.put(Sprite.Sprites.BG4, blocks.get(4));
        sprites.put(Sprite.Sprites.BG5, blocks.get(5));
        sprites.put(Sprite.Sprites.BG6, blocks.get(6));
    }

    public static Hashtable<Weapon.Weapons, Sprite> getWeapons() {
        return weapons;
    }

    public static Hashtable<Image, Sprite> getImages() {
        return images;
    }

    public static Hashtable<Integer, Sprite> getBlocks() {
        return blocks;
    }

    public static Hashtable<Sprite.Sprites, Sprite> getSprites() {
        return sprites;
    }
}
