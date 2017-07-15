package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Created by Lenovo on 10.07.2017.
 */
public class Camera extends JPanel {
    private final int resX, resY, wallHeight, weaponX, weaponY;

    private BufferedImage rendered;

    private Hero hero;

    public Camera(int resX, int resY, Hero hero) {
        this.resX = resX;
        this.resY = resY;
        this.wallHeight = resY;
        weaponX = resX - 1000;
        weaponY = resY - 500;
        this.hero = hero;

        rendered = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g) {
        double tempH = wallHeight / hero.getFov() * hero.getDefaultFov();       // blocks
        Point2D dir = hero.getDir(), plane = new Point2D(-dir.getY(), dir.getX()).multiply(Math.tan(hero.getFov() / 2) * dir.magnitude()), vec = dir.add(plane),
                deltaPlane = plane.multiply((double) 2 / resX), pos = hero.getPos();

        for (int i = 0; i < resX; i++, vec = vec.subtract(deltaPlane)) {
            Pair<Point2D, Boolean> collisionInfo = hero.collisionInfo(vec);
            Point2D collisionPoint = collisionInfo.getKey();
            boolean pxSide = collisionInfo.getValue();

            int j = 0, h = (int) (tempH * dir.magnitude() / pos.distance(collisionPoint)), emptyH = (resY - h) / 2;

            BufferedImage img = Textures.getBlocks().get(hero.block(vec, collisionPoint));
            double tempX = (pxSide ? collisionPoint.getY() : collisionPoint.getX()) % 1;
            tempX += tempX < 0 ? 1 : 0;
            int x = (int) (tempX * img.getWidth());

            for (; j < emptyH; j++)
                rendered.setRGB(i, j, Color.blue.getRGB());
            for (; j < resY && j < resY - emptyH - 1; j++) {
//                rendered.setRGB(i, j, Color.yellow.getRGB());
                int y = (j - emptyH) * img.getHeight() / h;
                rendered.setRGB(i, j, img.getRGB(x, y));
            }
            for (; j < resY; j++)
                rendered.setRGB(i, j, Color.gray.getRGB());
        }

        g.drawImage(rendered, 0, 0, null);

        BufferedImage img = Textures.getWeapons().getOrDefault(hero.getWeapon(), null);     // weapon
        if (img != null) {
            AffineTransform at = new AffineTransform();
            at.translate(img.getWidth() / 2, img.getHeight() / 2);
            at.rotate(hero.getWeaponAngle(), img.getWidth() / 5, img.getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            g.drawImage(op.filter(img, null), weaponX, weaponY, null);
        }

        BufferedImage viewfinder = Textures.getImages().get(Textures.Image.VIEWFINDER);     // viewfinder
        g.drawImage(viewfinder, (resX - viewfinder.getWidth()) / 2, (resY - viewfinder.getHeight()) / 2, null);
    }
}
