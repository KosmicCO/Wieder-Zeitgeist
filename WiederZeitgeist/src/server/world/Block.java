/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import util.Utils;

/**
 * Wrapper for the block metadata and definition.
 *
 * @author TARS
 */
public class Block {

    public final short id;
    public final short subID;
    public final BlockData blockData;

    /**
     * Creates the wrapper from the raw data stored by chunks.
     * @param raw The stored block.
     * @param bd The block's metadata.
     */
    public Block(int raw, BlockData bd) {
        id = Utils.firstShort(raw);
        subID = Utils.lastShort(raw);
        blockData = bd;
    }
    
    /**
     * Creates the wrapper from parsed information.
     * @param id The id of the block;
     * @param sid The subID of the block;
     * @param bd The block's metadata.
     */
    public Block(short id, short sid, BlockData bd){
        this.id = id;
        this.subID = sid;
        blockData = bd;
    }
}
