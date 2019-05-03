/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Kosmic
 */
public class Utils {

    /**
     * Returns the time as a double in seconds.
     *
     * @return The time in seconds.
     */
    public static double getTime() {
        return System.nanoTime() * 1E-9;
    }
}
