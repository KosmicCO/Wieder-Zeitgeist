/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view.render_util;

import util.vec.Vector;
import static util.vec.Vector.EPSILON;

/**
 * A basic immutable color class.
 *
 * @author Kosmic
 */
public class VColor {

    public static final VColor AMBER = new VColor(1, .75, 0);
    public static final VColor BLACK = new VColor(0, 0, 0);
    public static final VColor BLUE = new VColor(0, 0, 1);

    public static final VColor CLEAR = new VColor(0, 0, 0, 0);
    public static final VColor CYAN = new VColor(0, 1, 1);
    public static final VColor GREEN = new VColor(0, 1, 0);
    public static final VColor GREY = new VColor(.5, .5, .5);
    public static final VColor LIME = new VColor(.75, 1, 0);
    public static final VColor MAGENTA = new VColor(1, 0, 1);
    public static final VColor ORANGE = new VColor(1, .5, 0);
    public static final VColor PURPLE = new VColor(.75, 0, 1);
    public static final VColor RED = new VColor(1, 0, 0);

    public static final VColor VERMILION = new VColor(1, .25, 0);
    public static final VColor VIOLET = new VColor(.375, 0, 1);
    public static final VColor WHITE = new VColor(1, 1, 1);
    public static final VColor YELLOW = new VColor(1, 1, 0);

    /**
     * Creates a gray based off of the value given.
     *
     * @param value The based value.
     * @return The gray color based on the value.
     */
    public static VColor gray(double value) {
        return new VColor(value, value, value);
    }

    /**
     * The 4D vector of rgba.
     */
    public final Vector vec;

    /**
     * Constructs the color from rgba.
     *
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     * @param a Alpha.
     */
    public VColor(double r, double g, double b, double a) {
        vec = new Vector(r, g, b, a);
    }

    /**
     * Constructs a color with alpha.
     *
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public VColor(double r, double g, double b) {
        vec = new Vector(r, g, b, 1);
    }

    /**
     * Constructs a color from a vector of values, resizing the vector if it is
     * not the appropriate size.
     *
     * @param v The input vector to turn into a color.
     */
    public VColor(Vector v) {
        if (!(v.dim == 3 || v.dim == 4)) {
            throw new IllegalArgumentException("Color vector must be length 3 or 4: " + v.dim + " != 3, 4");
        }
        if (v.dim == 3) {
            vec = v.resize(4).replace(3, 1);
        } else {
            vec = v;
        }
    }

    /**
     * Returns the alpha value.
     *
     * @return The alpha value.
     */
    public double a() {
        return vec.get(3);
    }

    /**
     * Creates a new color with the alpha value changed.
     *
     * @param a The new alpha value.
     * @return The new color.
     */
    public VColor a(double a) {
        return new VColor(vec.replace(3, a));
    }

    /**
     * Returns the blue value.
     *
     * @return The blue value.
     */
    public double b() {
        return vec.get(2);
    }

    /**
     * Creates a new color with the blue value changed.
     *
     * @param b The new blue value.
     * @return The new color.
     */
    public VColor b(double b) {
        return new VColor(vec.replace(2, b));
    }

    /**
     * Returns the green value.
     *
     * @return The green value.
     */
    public double g() {
        return vec.get(1);
    }

    /**
     * Creates a new color with the green value changed.
     *
     * @param g The new green value.
     * @return The new color.
     */
    public VColor g(double g) {
        return new VColor(vec.replace(1, g));
    }

    /**
     * Returns the gray scale version of the color.
     *
     * @return The grey version of the color.
     */
    public VColor gray() {
        double average = (r() + g() + b()) / 3;
        return new VColor(average, average, average);
    }

    /**
     * Mixes the two colors based of of alpha.
     *
     * @param top The other color to be on top.
     * @return The new color.
     */
    public VColor mix(VColor top) {
        if (top.a() + EPSILON > 1) {
            return top;
        }
        if (top.a() < EPSILON) {
            return this;
        }
        double newAlpha = (1.0 - top.a()) * a() + top.a();
        Vector newCol = vec.bimap(top.vec, (b, t) -> ((1 - top.a()) * a() * b + top.a() * t) / newAlpha);
        return new VColor(newCol.replace(3, newAlpha));
    }

    /**
     * Multiplies the color by the scalar, capping off at 0 and 1.
     *
     * @param scalar The scalar to multiply.
     * @return The new color.
     */
    public VColor mult(double scalar) {
        return new VColor(vec.mult(scalar).map(d -> Math.min(1, Math.max(0, d))).replace(3, a()));
    }

    /**
     * Returns the red value.
     *
     * @return The red value.
     */
    public double r() {
        return vec.get(0);
    }

    /**
     * Creates a new color with the red value changed.
     *
     * @param r The new red value.
     * @return The new color.
     */
    public VColor r(double r) {
        return new VColor(vec.replace(0, r));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("TC: ");
        s.append(vec);
        return s.toString();
    }
}
