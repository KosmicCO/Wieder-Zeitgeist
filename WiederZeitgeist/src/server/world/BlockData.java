/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

/**
 * A small interface for storing extra block data.
 * @author TARS
 */
public interface BlockData {
    
    /**
     * Gets the block type that this is associated with.
     * @return The associated block type.
     */
    public int getBlockType();
}
