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
    private static Hashtable<Sprite.Sprites, Sprite> sprites = new Hashtable<>();
    private static Hashtable<Integer, Sprite.Sprites> blocks = new Hashtable<>();
    private static Hashtable<Integer, Sprite.Sprites> floors = new Hashtable<>();
    private static Hashtable<Integer, Sprite.Sprites> ceilings = new Hashtable<>();
    private static Hashtable<NPC.NPCs, Hashtable<NPC.Position, Sprite.Sprites>> NPCs = new Hashtable<>();
    private static Hashtable<Weapon.Weapons, Sprite.Sprites> weapons = new Hashtable<>();
    private static Hashtable<RangedWeapon.Bullets, Sprite.Sprites> bullets = new Hashtable<>();

    static void init() {
        try {
            initSprites();

            initBlocks();
            initFloors();
            initCeilings();
            initNPCs();
            initWeapons();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initWeapons() {
        weapons.put(Weapon.Weapons.S_SWORD, Sprite.Sprites.S_SWORD);
    }

    private static void initNPCs() {
        Hashtable<NPC.Position, Sprite.Sprites> tempBaldric = new Hashtable<>();
        tempBaldric.put(NPC.Position.STANDING, Sprite.Sprites.B_STANDING);
        tempBaldric.put(NPC.Position.FALLING, Sprite.Sprites.B_FALLING);
        tempBaldric.put(NPC.Position.FALLED, Sprite.Sprites.B_FALLED);
        tempBaldric.put(NPC.Position.CASTING, Sprite.Sprites.B_CASTING);
        tempBaldric.put(NPC.Position.WALKING, Sprite.Sprites.B_WALKING);

        NPCs.put(NPC.NPCs.BALDRIC, tempBaldric);

        // TODO REST
    }

    private static void initCeilings() {
        for (int i = 0; i < 7; i++)
            ceilings.put(i, Sprite.Sprites.CEILING0);
    }

    private static void initFloors() {
        for (int i = 0; i < 7; i++)
            floors.put(i, Sprite.Sprites.FLOOR0);
    }

    private static void initBlocks() {
        blocks.put(1, Sprite.Sprites.BG1);
        blocks.put(2, Sprite.Sprites.BG2);
        blocks.put(3, Sprite.Sprites.BG3);
        blocks.put(4, Sprite.Sprites.BG4);
        blocks.put(5, Sprite.Sprites.BG5);
        blocks.put(6, Sprite.Sprites.BG6);
    }

    private static void initSprites() throws IOException {
        sprites.put(Sprite.Sprites.S_SWORD, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/scithersword.png"))}));

        sprites.put(Sprite.Sprites.VIEWFINDER, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/viewfinder.png"))}));

        sprites.put(Sprite.Sprites.BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/bullet.png"))}));
        sprites.put(Sprite.Sprites.L_BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/light_bullet.png"))}));
        sprites.put(Sprite.Sprites.R_BULLET, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/red_bullet.png"))}));
        sprites.put(Sprite.Sprites.SHOT, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/assets/shot.png"))}));

        sprites.put(Sprite.Sprites.BG1, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/egypt_stone.png"))}));
        sprites.put(Sprite.Sprites.BG2, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/jungle_stone.png"))}));
        sprites.put(Sprite.Sprites.BG3, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/metal_plates.png"))}));
        sprites.put(Sprite.Sprites.BG4, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/palmtree.png"))}));
        sprites.put(Sprite.Sprites.BG5, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/sand_stone.png"))}));
        sprites.put(Sprite.Sprites.BG6, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/Stk textures/wood.png"))}));

        sprites.put(Sprite.Sprites.FLOOR0, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/pie/rustytiles/rustytiles01_diff.jpg"))}));

        sprites.put(Sprite.Sprites.CEILING0, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/ceilings/0m.jpg"))}));

        sprites.put(Sprite.Sprites.B_STANDING, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/NPCs/baldric/standing.png"))}));
        sprites.put(Sprite.Sprites.B_FALLING, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/NPCs/baldric/falling0.png")),
                ImageIO.read(new File("res/NPCs/baldric/falling1.png")), ImageIO.read(new File("res/NPCs/baldric/falling2.png")),
                ImageIO.read(new File("res/NPCs/baldric/falling3.png"))}));
        sprites.put(Sprite.Sprites.B_FALLED, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/NPCs/baldric/falled.png"))}));
        sprites.put(Sprite.Sprites.B_CASTING, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/NPCs/baldric/casting0.png")),
                ImageIO.read(new File("res/NPCs/baldric/casting1.png")), ImageIO.read(new File("res/NPCs/baldric/casting2.png")),
                ImageIO.read(new File("res/NPCs/baldric/casting3.png")), ImageIO.read(new File("res/NPCs/baldric/casting4.png")),
                ImageIO.read(new File("res/NPCs/baldric/casting5.png"))}));
        sprites.put(Sprite.Sprites.B_WALKING, new Sprite(new BufferedImage[]{ImageIO.read(new File("res/NPCs/baldric/walking0.png")),
                ImageIO.read(new File("res/NPCs/baldric/walking1.png")), ImageIO.read(new File("res/NPCs/baldric/walking2.png")),
                ImageIO.read(new File("res/NPCs/baldric/walking3.png")), ImageIO.read(new File("res/NPCs/baldric/walking4.png")),
                ImageIO.read(new File("res/NPCs/baldric/walking5.png")), ImageIO.read(new File("res/NPCs/baldric/walking6.png")),
                ImageIO.read(new File("res/NPCs/baldric/walking7.png"))}));
    }

    public static Hashtable<Weapon.Weapons, Sprite.Sprites> getWeapons() {
        return weapons;
    }

    public static Hashtable<NPC.NPCs, Hashtable<NPC.Position, Sprite.Sprites>> getNPCs() {
        return NPCs;
    }

    public static Hashtable<RangedWeapon.Bullets, Sprite.Sprites> getBullets() {
        return bullets;
    }

    public static Hashtable<Integer, Sprite.Sprites> getCeilings() {
        return ceilings;
    }

    public static Hashtable<Integer, Sprite.Sprites> getFloors() {
        return floors;
    }

    public static Hashtable<Integer, Sprite.Sprites> getBlocks() {
        return blocks;
    }

    public static Hashtable<Sprite.Sprites, Sprite> getSprites() {
        return sprites;
    }
}
