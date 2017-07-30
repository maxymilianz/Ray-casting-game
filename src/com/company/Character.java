package com.company;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.util.LinkedList;

/**
 * Created by Lenovo on 14.07.2017.
 */
public class Character {
    double weaponAngle = 0, deltaWeaponAngle, attackingWeaponAngle = -Math.PI / 2;
    double speed, sprintSpeed, minDistToBlock = 0.1;
    int health, mana, stamina, maxStamina;

    Point2D pos, dir;

    Weapon.Weapons weapon;

    static int[][] map;

    LinkedList<Weapon.Weapons> weapons = new LinkedList<>();

    public Character(double speed, double sprintSpeed, int health, int mana, int stamina, int maxStamina, Point2D pos, Point2D dir, LinkedList<Weapon.Weapons> weapons) {
        this.speed = speed;
        this.sprintSpeed = sprintSpeed;
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
        this.pos = pos;
        this.dir = dir;
        this.weapons = weapons;

        normalizeDir();
    }

    void update() {
        if (stamina < maxStamina)
            stamina++;

        if (deltaWeaponAngle != 0)
            updateWeaponAngle();
    }

    private void updateWeaponAngle() {
        if (deltaWeaponAngle > 0) {
            if (weaponAngle < 0)
                weaponAngle += deltaWeaponAngle;
            else {
                weaponAngle = 0;
                deltaWeaponAngle = 0;
            }
        }
        else {
            if (weaponAngle > attackingWeaponAngle)
                weaponAngle += deltaWeaponAngle;
            else {
                weaponAngle = attackingWeaponAngle;
                deltaWeaponAngle *= -1;
            }
        }
    }

    private void go(double speed, Point2D dir) {
        pos = pos.add(dir.multiply(speed));
        Point2D safePos = pos.add(new Point2D(dir.getX() < 0 ? -minDistToBlock : minDistToBlock, dir.getY() < 0 ? -minDistToBlock : minDistToBlock));

        if (block(dir, safePos) != 0) {
            Pair<Point2D, Boolean> collisionInfo = collisionInfo(dir).getFirst().getKey();
            pos = collisionInfo.getValue() ? new Point2D(Math.floor(pos.getX()) + (dir.getX() < 0 ? minDistToBlock : 1 - minDistToBlock), pos.getY()) :
                    new Point2D(pos.getX(), Math.floor(pos.getY()) + (dir.getY() < 0 ? minDistToBlock : 1 - minDistToBlock));
        }
    }

    LinkedList<Pair<Pair<Point2D, Boolean>, Point2D>> collisionInfo(Point2D vec) {
        boolean found = true;       // better to init this with fake value than check if closer == null in inner while
        LinkedList<Pair<Pair<Point2D, Boolean>, Point2D>> list = new LinkedList<>();
        double tg = vec.getY() / vec.getX(), diffY = Math.abs(tg) * Math.signum(vec.getY()), diffX = 1 / Math.abs(tg) * Math.signum(vec.getX()),
                x = vec.getX() < 0 ? Math.floor(pos.getX()) : Math.ceil(pos.getX()), y = vec.getY() < 0 ? Math.floor(pos.getY()) : Math.ceil(pos.getY()), lastHeight = 0;
        Point2D closer = null, px = new Point2D(x, (x - pos.getX()) * tg + pos.getY()), py = new Point2D((y - pos.getY()) / tg + pos.getX(), y),
                dPx = new Point2D(vec.getX() < 0 ? -1 : 1, diffY), dPy = new Point2D(diffX, vec.getY() < 0 ? -1 : 1);

        while (lastHeight != 1) {
            while (found || block(vec, closer) == 0) {
                if (closer == px)
                    px = px.add(dPx);
                else if (closer == py)
                    py = py.add(dPy);

                found = false;
                closer = distSquared(pos, px) < distSquared(pos, py) ? px : py;
            }

            boolean pxSide = closer == px;
            double newHeight = Game.getWallHeight().get(block(vec, closer)).getKey();
            Point2D px1 = pxSide ? px.add(dPx) : px, py1 = !pxSide ? py.add(dPy) : py, next = distSquared(pos, px1) < distSquared(pos, py1) ? px1 : py1;

            if (newHeight >= lastHeight) {      // TODO THIS MAY BE CHECKED MORE ACCURATELY
                list.add(new Pair<>(new Pair<>(closer, pxSide), next));
                lastHeight = newHeight;
                found = true;
            }
        }

        return list;
    }

    private static double distSquared(Point2D p0, Point2D p1) {
        double diffX = p0.getX() - p1.getX(), diffY = p0.getY() - p1.getY();
        return diffX * diffX + diffY * diffY;
    }

    int block(Point2D vec, Point2D collisionPoint) {
        double tempY = collisionPoint.getY(), tempX = collisionPoint.getX();
        int y = (int) tempY - (vec.getY() < 0 && tempY == Math.round(tempY) ? 1 : 0), x = (int) tempX - (vec.getX() < 0 && tempX == Math.round(tempX) ? 1 : 0);
        return y >= 0 && y < map.length && x >= 0 && x < map[0].length ? map[y][x] : 1;
    }

    void turn(double angle) {
        double sin = Math.sin(angle), cos = Math.cos(angle);
        dir = new Mtx2x2(cos, -sin, sin, cos).apply(dir);
    }

    void attack() {
        deltaWeaponAngle = -0.2;
    }

    void forward() {
        go(speed, dir);
    }

    void backward() {
        go(speed, new Mtx2x2(-1, 0, 0, -1).apply(dir));
    }

    void left() {
        go(speed, new Mtx2x2(0, -1, 1, 0).apply(dir));
    }

    void right() {
        go(speed, new Mtx2x2(0, 1, -1, 0).apply(dir));
    }

    void sprint() {
        if (stamina > 0) {
            go(sprintSpeed, dir);
            stamina -= 2;
        }
    }

    private void normalizeDir() {
        dir = dir.multiply(1 / dir.magnitude());
    }

    public double getWeaponAngle() {
        return weaponAngle;
    }

    public Weapon.Weapons getWeapon() {
        return weapon;
    }

    public Point2D getPos() {
        return pos;
    }

    public Point2D getDir() {
        return dir;
    }

    public static void setMap(int[][] map) {
        Character.map = map;
    }
}
