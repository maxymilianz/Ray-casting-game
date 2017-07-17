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
    private final int resX, resY, wallHeight, weaponX, weaponY;

    private BufferedImage rendered;

    private Hero hero;

    LinkedList<NPC> NPCs, NPCsToDraw;

    public Camera(int resX, int resY, Hero hero, LinkedList<NPC> NPCs) {
        this.resX = resX;
        this.resY = resY;
        this.wallHeight = resY;
        weaponX = resX - 1000;
        weaponY = resY - 500;
        this.hero = hero;
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
            BufferedImage img = Textures.getSprites().get(Textures.getNPCs().get(i.getType()).get(i.getPosition())).getImage();
            perp = perp.multiply(1 / vec.magnitude() * img.getWidth() / Textures.getSprites().get(Textures.getBlocks().get(1)).getImage().getWidth());
            vec = vec.add(perp.multiply(vecAngle < dirAngle ? 1 : -1));

            if (vec.angle(dir) < hero.getFov() * 90 / Math.PI)
                NPCsToDraw.add(i);
        }
    }

    private void render(Graphics g) {
        double tempH = wallHeight / hero.getFov() * hero.getDefaultFov();
        Point2D dir = hero.getDir(), plane = new Point2D(-dir.getY(), dir.getX()).multiply(Math.tan(hero.getFov() / 2) * dir.magnitude()), vec = dir.add(plane),
                deltaPlane = plane.multiply((double) 2 / resX), pos = hero.getPos();

        for (int i = 0; i < resX; i++, vec = vec.subtract(deltaPlane)) {
            Pair<Point2D, Boolean> collisionInfo = hero.collisionInfo(vec);
            Point2D collisionPoint = collisionInfo.getKey();
            boolean pxSide = collisionInfo.getValue();

            for (NPC j : NPCsToDraw) {
                
            }

            int j = 0, h = (int) (tempH * dir.magnitude() / pos.distance(collisionPoint)), emptyH = (resY - h) / 2;

            BufferedImage img = Textures.getSprites().get(Textures.getBlocks().get(hero.block(vec, collisionPoint))).getImage();
            double tempX = (pxSide ? collisionPoint.getY() : collisionPoint.getX()) % 1;
            tempX += tempX < 0 ? 1 : 0;
            int x = (int) (tempX * img.getWidth());

            for (; j < emptyH; j++)
                rendered.setRGB(i, j, Color.blue.getRGB());
            for (; j < resY && j < resY - emptyH - 1; j++) {
                int y = (j - emptyH) * img.getHeight() / h;
                rendered.setRGB(i, j, img.getRGB(x, y));
            }
            for (; j < resY; j++)
                rendered.setRGB(i, j, Color.gray.getRGB());
        }

        g.drawImage(rendered, 0, 0, null);
    }
}
