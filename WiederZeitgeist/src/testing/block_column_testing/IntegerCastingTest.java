/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.block_column_testing;

import static util.Utils.combineInts;
import static util.Utils.firstInt;
import static util.Utils.lastInt;

/**
 * Tests how certain integer/long casting methods work to be able to use them appropriately.
 * @author TARS
 */
public class IntegerCastingTest {

    /**
     * Tests how Java specifically handles certain integer casts.
     * @param args Takes no arguments.
     */
    public static void main(String[] args) {
        long s = 0x4F13_F42F_F8A5_F620L;
        System.out.println(Integer.toHexString(firstInt(s)));
        System.out.println(Integer.toHexString(lastInt(s)));
        
        System.out.println(Long.toHexString(combineInts(firstInt(s), lastInt(s))));
    }
}
