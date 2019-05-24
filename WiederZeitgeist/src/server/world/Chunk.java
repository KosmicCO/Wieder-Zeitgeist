/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import java.util.Map;
import server.world.generator.GenStep;
import server.world.generator.WorldGenerator;
import server.world.generator.base_gen_steps.BlocksStep;
import server.world.generator.base_gen_steps.RenderStep;
import util.block_columns.BlockColumn;
import util.vec.IntVector;

/**
 * An object which keeps data for a specific region of space, which is usually a
 * square area.
 *
 * @author TARS
 */
public class Chunk {

    /**
     * The power of two which is the chunk width.
     */
    public static final int WIDTH_POW = 5;

    /**
     * The chunk width.
     */
    public static final int WIDTH = (int) Math.pow(2, WIDTH_POW);

    static {
        if (WIDTH > 0x0000_4000) {
            throw new RuntimeException("Width cannot be larger than MAX_SHORT / 2.");
        }
    }

    private final boolean[] finishedStep;
    private boolean loaded;

    /**
     * The position of the chunk in the world.
     */
    public final IntVector position;

    // BLOCKS

    public Map<IntVector, BlockData> wallData = null;
    public Map<IntVector, BlockData> floorData = null;
    public BlockColumn[] wallColumns = null;
    public BlockColumn[] floorColumns = null;

    public int heightGenerated = Integer.MAX_VALUE;

    // RENDER
    /**
     * Generates a black chunk.
     *
     * @param pos The position of the chunk in the world.
     * @param chunkSteps The number of chunk steps to keep track of.
     */
    public Chunk(IntVector pos, int chunkSteps) {
        position = pos;
        finishedStep = new boolean[chunkSteps];
        loaded = true;
    }

    /**
     * Returns whether the chunk have generated the given steps.
     *
     * @param gs The steps to check.
     * @return Whether every step was generated.
     */
    public boolean finishedStep(GenStep... gs) {
        for (GenStep step : gs) {
            if (!finishedStep[step.id()]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the block at a given location, generating down to the location if it
     * has not been generated yet.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height of the column to access.
     * @param wall Whether to access a wall block or a floor block.
     * @return The block id and meta id at the given location.
     */
    public int getBlock(int x, int y, int height, boolean wall) {
        if (height < WorldGenerator.MIN_WORLD_HEIGHT || height > WorldGenerator.MAX_WORLD_HEIGHT) {
            throw new IllegalArgumentException("Cannot access height outside of the world boundaries.");
        }
        if (height < heightGenerated) {
            World.generateChunkLevel(this, height);
        }
        int ind = posIndex(x, y);
        return wall ? wallColumns[ind].getBlock(height) : floorColumns[ind].getBlock(height);
    }
    
    /**
     * Gets the block metadata at a given location, generating down to the location if it has not been generated yet.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height of the column to access.
     * @param wall Whether to access the metadata of a wall block or floor block.
     * @return The metadata at the given location.
     */
    public BlockData getBlockData(int x, int y, int height, boolean wall) {
        if (height < WorldGenerator.MIN_WORLD_HEIGHT || height > WorldGenerator.MAX_WORLD_HEIGHT) {
            throw new IllegalArgumentException("Cannot access height outside of the world boundaries.");
        }
        if (height < heightGenerated) {
            World.generateChunkLevel(this, height);
        }
        return wall ? wallData.get(new IntVector(x, y, height)) : floorData.get(new IntVector(x, y, height));
    }

    /**
     * Returns whether the chunk is not in the process of being unloaded.
     *
     * @return True if the chunk is not being unloaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets the block at a given location, generating down to the location if it
     * has not been generated yet.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param height The height of the column to access.
     * @param wall Whether to access a wall block or a floor block.
     * @param block The block id and meta id to set.
     * @param metaData The metadata associated with the block. Null if none.
     */
    public void setBlock(int x, int y, int height, boolean wall, int block, BlockData metaData) {
        if (height < WorldGenerator.MIN_WORLD_HEIGHT || height > WorldGenerator.MAX_WORLD_HEIGHT) {
            throw new IllegalArgumentException("Cannot access height outside of the world boundaries.");
        }
        if (height < heightGenerated) {
            World.generateChunkLevel(this, height);
        }
        int ind = posIndex(x, y);
        if (wall) {
            wallColumns[ind].setBlock(height, block);
            wallData.remove(new IntVector(x, y, height));
            if(metaData != null){
                wallData.put(new IntVector(x, y, height), metaData);
            }
        } else {
            floorColumns[ind].setBlock(height, block);
            floorData.remove(new IntVector(x, y, height));
            if(metaData != null){
                floorData.put(new IntVector(x, y, height), metaData);
            }
        }
    }

    /**
     * Set the chunk to be unloaded.
     */
    public void setToUnloaded() {
        loaded = false;
    }

    /**
     * Called by the generator to mark that a given generation step was
     * completed.
     *
     * @param gs The step in question.
     */
    public void setStepCompleted(GenStep gs) {
        if (finishedStep[gs.id()]) {
            throw new RuntimeException("Attept to mark a step completed multiple times.");
        }

        finishedStep[gs.id()] = true;
    }

    /**
     * Turns the given in-chunk coordinates into those used to access the array
     * of columns.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The integer index to the array.
     */
    public int posIndex(int x, int y) {
        if (x > WIDTH || y > WIDTH || x < 0 || y < 0) {
            throw new IllegalArgumentException("Position out of bounds: (" + x + ", " + y + ").");
        }
        return (x << WIDTH_POW) | y;
    }
}
