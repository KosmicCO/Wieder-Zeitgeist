/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world;

import java.util.Map;
import server.world.generator.GenStep;
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

    private final boolean[] finishedStep;
    private boolean loaded;
    
    /**
     * The position of the chunk in the world.
     */
    public final IntVector position;

    
    // BLOCKS
    
    private Map<IntVector, BlockData> wallData = null;
    private Map<IntVector, BlockData> floorData = null;
    private BlockColumn[] wallColumns = null;
    private BlockColumn[] floorColumns = null;
    
    private int heightGenerated = Integer.MAX_VALUE;

    
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
     * Returns whether the chunk is not in the process of being unloaded.
     * @return True if the chunk is not being unloaded.
     */
    public boolean isLoaded(){
        return loaded;
    }

    /**
     * For the world generator to set the data for the BLOCKS step.
     *
     * @param wallData The extra block data map for walls.
     * @param floorData The extra block data map for floors.
     * @param wallColumns The wall block columns.
     * @param floorColumns The floor block columns.
     * @param genHeight The height which the chunk generated blocks to.
     */
    public void setBlocksStep(Map<IntVector, BlockData> wallData, Map<IntVector, BlockData> floorData, BlockColumn[] wallColumns, BlockColumn[] floorColumns, int genHeight) {
        if (finishedStep(BlocksStep.STEP)) {
            throw new RuntimeException("Cannot initialize the BLOCKS step more than once.");
        }
        this.wallData = wallData;
        this.floorData = floorData;
        this.wallColumns = wallColumns;
        this.floorColumns = floorColumns;
        heightGenerated = genHeight;
        finishedStep[BlocksStep.STEP.id()] = true;
    }
    
    /**
     * For the world generator to set the data for the RENDER step.
     */
    public void setRenderStep(){
        if (finishedStep(RenderStep.STEP)) {
            throw new RuntimeException("Cannot initialize the BLOCKS step more than once.");
        }
        
        finishedStep[RenderStep.STEP.id()] = true;
    }
}
