/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing.block_column_testing;

import static core.Activatable.using;
import util.block_columns.BlockColumn;
import util.block_columns.RunLengthColumn;

/**
 * Tests whether the run-length encoding implementation of BlockColumn works
 * properly.
 *
 * @author TARS
 */
public class RunLengthColumnTest1 {

    /**
     * Initial tests of RunLengthColumn.
     * @param args Takes no arguments.
     */
    public static void main(String[] args) {
        BlockColumn bc = new RunLengthColumn(0, 0, 10);
        using(() -> {
            bc.setBlock(9, 1);
            bc.setRange(0, 4, 0xFFFF_FFFF);
            
            System.out.println("While Activated:\n");
            
            System.out.println("Bottom height at 3: " + bc.getBottomHeight(3));
            System.out.println("Top height at 5:    " + bc.getTopHeight(5));
            System.out.println("Top height at 9:    " + bc.getTopHeight(9));
            System.out.println("Get block at 6:     " + bc.getBlock(6));
            System.out.println("Get block at 9:     " + bc.getBlock(9));
            
        }, bc);
        
        System.out.println("While Deactivated:\n");
        
        for (int i = 10; i >= 0; i--) {
            System.out.println(bc.getBlock(i));
        }
        
        System.out.println("Bottom height at 3: " + bc.getBottomHeight(3));
        System.out.println("Top height at 5:    " + bc.getTopHeight(5));
        System.out.println("Top height at 9:    " + bc.getTopHeight(9));
        System.out.println("Get block at 6:     " + bc.getBlock(6));
        System.out.println("Get block at 9:     " + bc.getBlock(9));
    }
}
