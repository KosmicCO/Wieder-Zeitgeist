/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a block.
 *
 * @author TARS
 */
public class Block {

    private static final Map<Short, Block> ID_BLOCK_MAP = new HashMap();

    /**
     * The name of the block
     */
    public final String name;

    /**
     * The block id.
     */
    public final short id;

    protected Block(String name, short id) {
        this.name = name;
        if (ID_BLOCK_MAP.containsKey(id)) {
            throw new RuntimeException("Cannot initialize two blocks with the same id");
        }
        this.id = id;
        ID_BLOCK_MAP.put(id, this);
    }
}
