/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.generator_implementations;

import server.world.Chunk;
import server.world.generator.GenStep;
import server.world.generator.WorldGenerator;
import server.world.generator.base_gen_steps.BlocksStep;
import util.vec.IntVector;

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
                chunk.setRenderStep();
                break;
            case 1: // BlocksStep
                // generate the columns, but need to implement a block columns first.
                chunk.setBlocksStep(null, null, null, null, 0);
                break;
        }
    }
}
