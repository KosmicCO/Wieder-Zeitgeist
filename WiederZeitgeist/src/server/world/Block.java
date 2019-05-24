/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a block.
 *
 * @author TARS
 */
public class Block {

    private static final Map<Short, Block> ID_BLOCK_MAP = new HashMap();
    private static final Map<String, Block> NAME_BLOCK_MAP = new HashMap();

    /**
     * Gets the block definition associated with the given block id.
     *
     * @param id The block id.
     * @return The associated block definition.
     */
    public static Block getBlockFromID(short id) {
        return ID_BLOCK_MAP.get(id);
    }

    /**
     * Gets the block definition associated with the given name.
     *
     * @param name The block name.
     * @return The associated block.
     */
    public static Block getBlockFromName(String name) {
        return NAME_BLOCK_MAP.get(name);
    }

    private static void initializeBlock(Block b) {
        if (ID_BLOCK_MAP.containsKey(b.id)) {
            throw new RuntimeException("Cannot initialize two blocks with the same id: " + b.id);
        }
        if (NAME_BLOCK_MAP.containsKey(b.name)) {
            throw new RuntimeException("Cannot initialize two blocks with the same name: " + b.name);
        }
        ID_BLOCK_MAP.put(b.id, b);
        NAME_BLOCK_MAP.put(b.name, b);
    }

    /**
     * Loads in all of the block information from the YAML file.
     *
     * @param filePath The path to the blocks.yml file.
     * @throws java.io.FileNotFoundException Throws if the file is not found.
     * @throws com.esotericsoftware.yamlbeans.YamlException Throws if there is
     * an error parsing the YAML file.
     */
    public static void loadIntBlocks(String filePath) throws FileNotFoundException, YamlException {
        YamlReader blocksReader = new YamlReader(new FileReader(filePath));
        while (true) {
            LoadedBlockData lbd = blocksReader.read(LoadedBlockData.class);
            if (lbd == null) {
                break;
            }
            switch (lbd.type) {
                case "Generic":
                    initializeBlock(new Block(lbd));
                    break;
            }
        }
    }
    /**
     * The block id.
     */
    public final short id;

    /**
     * The name of the block
     */
    public final String name;

    /**
     * Creates a new block definition;
     *
     * @param name The name of the block.
     * @param id The block id.
     */
    public Block(String name, short id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Creates a new block definition from data loaded in from blocks.yml.
     *
     * @param lbd The loaded data.
     */
    public Block(LoadedBlockData lbd) {
        this(lbd.name, lbd.id);
    }

    /**
     * Data which a block could utilize. Only the fields necessary to create the
     * given block definition should be filled.
     */
    protected static class LoadedBlockData {

        /**
         * Loaded block id.
         */
        public short id;

        /**
         * Loaded block name.
         */
        public String name;

        /**
         * Block template to use.
         */
        public String type;
    }
}
