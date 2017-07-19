package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Camera extends JPanel {
    private final int resX, resY, wallHeight, weaponX, weaponY, floorSize = 4, ceilingSize = 4, halfResY;      // floorSize and ceilingSize are in tiles

    private BufferedImage rendered;

    private Hero hero;

    private int[][] map;

    private LinkedList<NPC> NPCs, NPCsToDraw;

    public Camera(int resX, int resY, Hero hero, int[][] map, LinkedList<NPC> NPCs) {
        this.resX = resX;
        this.resY = resY;
        wallHeight = resY;
        halfResY = resY / 2;
        weaponX = resX - 1000;
        weaponY = resY - 500;
        this.hero = hero;
        this.map = map;
        this.NPCs = NPCs;

        rendered = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g) {
        checkNPCs();
        render(g);
        drawWeapon(g);
        drawViewfinder(g);
    }

    private void drawViewfinder(Graphics g) {
        BufferedImage viewfinder = Textures.getSprites().get(Sprite.Sprites.VIEWFINDER).getImage();
        g.drawImage(viewfinder, (resX - viewfinder.getWidth()) / 2, (resY - viewfinder.getHeight()) / 2, null);
    }

    private void drawWeapon(Graphics g) {
        BufferedImage img = Textures.getSprites().get(Textures.getWeapons().get(hero.getWeapon())).getImage();
        if (img != null) {
            AffineTransform at = new AffineTransform();
            at.translate(img.getWidth() / 2, img.getHeight() / 2);
            at.rotate(hero.getWeaponAngle(), img.getWidth() / 5, img.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            g.drawImage(op.filter(img, null), weaponX, weaponY, null);
        }
    }

    private void checkNPCs() {
        NPCsToDraw = new LinkedList<>();

        for (NPC i : NPCs) {
            Point2D vec = i.getPos().subtract(hero.getPos()), dir = hero.getDir(), zero = new Point2D(1, 0), perp = new Point2D(-vec.getY(), vec.getX());
            double vecAngle = vec.angle(zero), dirAngle = dir.angle(zero);
            vecAngle = vec.getY() < 0 ? 360 - vecAngle : vecAngle;
            dirAngle = dir.getY() < 0 ? 360 - dirAngle : dirAngle;
            perp = perp.multiply(1 / vec.magnitude() * Textures.getSprites().get(Textures.getNPCs().get(i.getType()).get(i.getPosition())).getImage().getWidth() /
                    Textures.getSprites().get(Textures.getBlocks().get(1)).getImage().getWidth());
            vec = vec.add(perp.multiply(vecAngle < dirAngle ? 1 : -1));

            if (vec.angle(dir) < hero.getFov() * 90 / Math.PI)
                NPCsToDraw.add(i);
        }
    }

    private void render(Graphics g) {       // TODO DRAW NPCS HERE
        int wallCenterZ = (int) (halfResY * hero.getzDir());
        double fovRatio = hero.getDefaultFov() / hero.getFov();
        Point2D dir = hero.getDir(), plane = new Point2D(-dir.getY(), dir.getX()).multiply(Math.tan(hero.getFov() / 2) * dir.magnitude()), vec = dir.add(plane),
                deltaPlane = plane.multiply((double) 2 / resX), pos = hero.getPos();

        for (int i = 0; i < resX; i++, vec = vec.subtract(deltaPlane)) {
            Pair<Point2D, Boolean> collisionInfo = hero.collisionInfo(vec);
            Point2D collisionPoint = collisionInfo.getKey();

            int j = 0, h = (int) (wallHeight * fovRatio * vec.magnitude() / pos.distance(collisionPoint)), emptyH = wallCenterZ - h / 2;

            BufferedImage img = Textures.getSprites().get(Textures.getBlocks().get(hero.block(vec, collisionPoint))).getImage();
            int x = (int) (((collisionInfo.getValue() ? collisionPoint.getY() : collisionPoint.getX()) % 1) * img.getWidth());

            for (; j < emptyH; j++) {
                double d = halfResY * vec.magnitude() / (wallCenterZ - j) * fovRatio;
                Point2D p = hero.getPos().add(vec.multiply(d / vec.magnitude()));
                int tile = map[(int) p.getY()][(int) p.getX()];

                BufferedImage ceiling = Textures.getSprites().get(Textures.getCeilings().getOrDefault(tile, Sprite.Sprites.CEILING0)).getImage();
                rendered.setRGB(i, j, ceiling.getRGB((int) ((p.getX() % ceilingSize) / ceilingSize * ceiling.getWidth()),
                        (int) ((p.getY() % ceilingSize) / ceilingSize * ceiling.getHeight())));
            }
            for (; j < resY && j < emptyH + h; j++)
                rendered.setRGB(i, j, img.getRGB(x, (j - emptyH) * img.getHeight() / h));
            for (; j < resY; j++) {
                double d = halfResY * vec.magnitude() / (j - wallCenterZ) * fovRatio;
                Point2D p = hero.getPos().add(vec.multiply(d / vec.magnitude()));
                int tile = map[(int) p.getY()][(int) p.getX()];

                BufferedImage floor = Textures.getSprites().get(Textures.getFloors().getOrDefault(tile, Sprite.Sprites.FLOOR0)).getImage();
                rendered.setRGB(i, j, floor.getRGB((int) ((p.getX() % floorSize) / floorSize * floor.getWidth()), (int) ((p.getY() % floorSize) / floorSize * floor.getHeight())));
            }
        }

        g.drawImage(rendered, 0, 0, null);
    }
}
