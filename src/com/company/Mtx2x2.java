package com.company;

import javafx.geometry.Point2D;

/**
 * Created by Lenovo on 11.07.2017.
 */
public class Mtx2x2 {
    /*
        a b
        c d
     */

    private double a, b, c, d;

    public Mtx2x2(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    Point2D apply(Point2D p) {
        return new Point2D(a * p.getX() + b * p.getY(), c * p.getX() + d * p.getY());
    }
}
