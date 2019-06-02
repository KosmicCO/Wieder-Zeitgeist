/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.generator_implementations;

import java.util.HashMap;
import server.world.Chunk;
import server.world.generator.GenStep;
import server.world.generator.WorldGenerator;
import server.world.generator.base_gen_steps.BlocksStep;
import server.world.generator.base_gen_steps.RenderStep;
import util.block_columns.BlockColumn;
import util.block_columns.RunLengthColumn;
import util.vec.IntVector;
import static server.world.Chunk.SIZE;

/**
 * A world generator which generates a world filled with one block.
 *
 * @author TARS
 */
public class AllBlock implements WorldGenerator {

    private final int blockToFill;

    /**
     * Generates the world to be full of the block given.
     *
     * @param block The block to fill the world with.
     */
    public AllBlock(int block) {
        blockToFill = block;
    }

    @Override
    public Chunk createChunk(IntVector chunkPos) {
        return new Chunk(chunkPos, 2);
    }

    @Override
    public void generateChunk(Chunk chunk, GenStep gs) {
        if (chunk.finishedStep(gs)) {
            return;
        }
        switch (gs.id()) {
            case 0: // RenderStep
                generateChunk(chunk, BlocksStep.STEP);
                chunk.setStepCompleted(RenderStep.STEP);
                break;
            case 1: // BlocksStep
                chunk.floorColumns = new BlockColumn[SIZE * SIZE];
                chunk.wallColumns = new BlockColumn[SIZE * SIZE];
                chunk.floorData = new HashMap();
                chunk.wallData = new HashMap();
                for (int i = 0; i < chunk.floorColumns.length; i++) {
                    chunk.floorColumns[i] = new RunLengthColumn(blockToFill, WorldGenerator.MIN_WORLD_HEIGHT, WorldGenerator.MAX_WORLD_HEIGHT);
                }
                for (int i = 0; i < chunk.wallColumns.length; i++) {
                    chunk.wallColumns[i] = new RunLengthColumn(blockToFill, WorldGenerator.MIN_WORLD_HEIGHT, WorldGenerator.MAX_WORLD_HEIGHT);
                }
                chunk.heightGenerated = WorldGenerator.MIN_WORLD_HEIGHT;
                chunk.setStepCompleted(BlocksStep.STEP);
                break;
        }
    }

    @Override
    public void generateToLevel(Chunk chunk, int level) {
        // Already generated to the min world height
    }
}
