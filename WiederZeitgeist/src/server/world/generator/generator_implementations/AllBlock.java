/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.generator_implementations;

import server.world.Chunk;
import server.world.generator.GenStep;
import server.world.generator.WorldGenerator;
import util.vec.IntVector;

/**
 *
 * @author TARS
 */
public class AllBlock implements WorldGenerator {

    private final int blockToFill;

    /**
     * Generates the world to be full of the block given.
     * @param block The block to fill the world with.
     */
    public AllBlock(int block) {
        blockToFill = block;
    }

    @Override
    public Chunk createChunk(IntVector chunkPos) {
        return new Chunk(chunkPos);
    }

    @Override
    public void generateStep(Chunk chunk, GenStep gs) {
        switch (gs) {
            case RENDER:
                chunk.setRenderStep();
                break;
            case BLOCKS:
                // generate the columns, but need to implement a block columns first.
                chunk.setBlocksStep(null, null, null, null, 0);
                break;
        }
    }

    @Override
    public GenStep[] getDependencies(GenStep gs) {
        return gs.getDependencies();
    }
}
