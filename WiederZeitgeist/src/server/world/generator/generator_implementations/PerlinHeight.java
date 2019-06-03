/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator.generator_implementations;

import java.util.HashMap;
import java.util.Random;
import server.world.Chunk;
import static server.world.Chunk.SIZE;
import static server.world.Chunk.SIZE_POW;
import server.world.generator.GenStep;
import server.world.generator.WorldGenerator;
import server.world.generator.base_gen_steps.BlocksStep;
import server.world.generator.base_gen_steps.RenderStep;
import util.Noise;
import util.block_columns.BlockColumn;
import util.block_columns.RunLengthColumn;
import util.vec.IntVector;

/**
 * A generator which uses perlin noise to vary the height border between two
 * block types.
 *
 * @author TARS
 */
public class PerlinHeight implements WorldGenerator {

    /**
     * The block of the bottom half.
     */
    public final int bottomBlock;

    private final Noise noise;

    /**
     * The block of the top half.
     */
    public final int topBlock;

    /**
     * Creates a new world generator from the top and bottom blocks and a seed.
     *
     * @param botID The block on the bottom half.
     * @param topID The block on the top half.
     * @param seed The seed of the world.
     */
    public PerlinHeight(int botID, int topID, Random seed) {
        bottomBlock = botID;
        topBlock = topID;
        noise = new Noise(seed);
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
                    chunk.floorColumns[i] = new RunLengthColumn(topBlock, WorldGenerator.MIN_WORLD_HEIGHT, WorldGenerator.MAX_WORLD_HEIGHT);
                }
                for (int i = 0; i < chunk.wallColumns.length; i++) {
                    chunk.wallColumns[i] = new RunLengthColumn(topBlock, WorldGenerator.MIN_WORLD_HEIGHT, WorldGenerator.MAX_WORLD_HEIGHT);
                }
                chunk.heightGenerated = WorldGenerator.MIN_WORLD_HEIGHT;
                for (int i = 0; i < Chunk.SIZE; i++) {
                    for (int j = 0; j < Chunk.SIZE; j++) {
                        int height = (int) (noise.fbm2d(i + SIZE * chunk.position.x(), j + SIZE * chunk.position.y(), 4, 0.01) * 50);
                        chunk.wallColumns[posIndex(i, j)].setRange(chunk.wallColumns[posIndex(i, j)].minHeight(), height, bottomBlock);
                        chunk.floorColumns[posIndex(i, j)].setRange(chunk.floorColumns[posIndex(i, j)].minHeight(), height + 1, bottomBlock);
                    }
                }

                chunk.setStepCompleted(BlocksStep.STEP);
                break;
        }
    }

    @Override
    public void generateToLevel(Chunk chunk, int level) {
        // Already generated to the min world height
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
        if (x > SIZE || y > SIZE || x < 0 || y < 0) {
            throw new IllegalArgumentException("Position out of bounds: (" + x + ", " + y + ").");
        }
        return (x << SIZE_POW) | y;
    }
}
