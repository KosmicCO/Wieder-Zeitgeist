/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import server.world.Block;

/**
 * Rendered chunk interface.
 *
 * @author TARS
 */
public interface RenderedChunk {

    /**
     * Returns the maximum height contiguously connected by the same block.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height.
     * @param wall Whether the block is a wall or floor block.
     * @return The maximum contiguous height.
     */
    public int getMaxBlockHeight(int x, int y, int height, boolean wall);

    /**
     * Returns the maximum height of the block column.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param wall Whether the column is a wall or floor column.
     * @return The maximum height of the column.
     */
    public int getMaxColumnHeight(int x, int y, boolean wall);
    /**
     * Returns the minimum height contiguously connected by the same block.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height.
     * @param wall Whether the block is a wall or floor block.
     * @return The minimum contiguous height.
     */
    public int getMinBlockHeight(int x, int y, int height, boolean wall);

    /**
     * Returns the minimum height of the block column.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param wall Whether the column is a wall or floor column.
     * @return The minimum height of the column.
     */
    public int getMinColumnHeight(int x, int y, boolean wall);
    
    /**
     * Gets the block at the given location in the chunk.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height.
     * @param wall Whether the block is a wall or floor block.
     * @return A block wrapper for the block at the specified location.
     */
    public Block getRenBlock(int x, int y, int height, boolean wall);
}
