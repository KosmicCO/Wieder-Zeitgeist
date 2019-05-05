/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * A collection of functions which do not fall under a specific usage.
 *
 * @author Kosmic
 */
public final class Utils {

    /**
     * Returns the time as a double in seconds.
     *
     * @return The time in seconds.
     */
    public static double getTime() {
        return System.nanoTime() * 1E-9;
    }

    /**
     * Returns the first 8 bytes of the long as an integer.
     *
     * @param l The long to half.
     * @return The first half as an integer.
     */
    public static int firstInt(long l) {
        return (int) (l >> 32);
    }

    /**
     * Returns the last 8 bytes of the long as an integer.
     *
     * @param l The long to half.
     * @return The last half as an integer.
     */
    public static int lastInt(long l) {
        return (int) (l & 0xFFFF_FFFF);
    }

    /**
     * Appends the bytes of last to the bytes of first and returns as a long.
     *
     * @param first The first bytes as an int.
     * @param last The last bytes as an int.
     * @return The appended as a long.
     */
    public static long combineInts(int first, int last) {
        return (((long) first) << 32) | (((long) last) & 0x0000_0000_FFFF_FFFFL);
    }
}
