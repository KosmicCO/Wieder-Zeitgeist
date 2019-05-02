/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.run_length_encoding;

import core.Activatable;

/**
 * An interface for a run length encoding method for storing blocks in columns.
 * @author TARS
 */
public interface BlockColumn extends Activatable{
    
    /**
     * Returns the minimum height of the column.
     * @return The min height.
     */
    public int minHeight();
    
    /**
     * Returns the maximum height of the column.
     * @return The max height.
     */
    public int maxHeight();
    
    /**
     * Sets the range between bottom and top to the block given.
     * @param bottom The lower bound.
     * @param top The upper bound.
     * @param block The block to set to.
     */
    public void setRange(int bottom, int top, int block);
    
    /**
     * Sets a single height to be a block.
     * @param height The height to set.
     * @param block The block to set to.
     */
    public void setBlock(int height, int block);
    
    /**
     * Gets the block at a certain height.
     * @param height The height to query.
     * @return The block at the height given.
     */
    public int getBlock(int height);
    
    /**
     * Returns the least height contiguously connected by the same block at the height given.
     * @param height The input height.
     * @return The least height as described.
     */
    public int getBottomHeight(int height);
    
    /**
     * Returns the greatest height contiguously connected by the same block at the height given.
     * @param height The input height.
     * @return The greatest height as described.
     */
    public int getTopHeight(int height);
}
