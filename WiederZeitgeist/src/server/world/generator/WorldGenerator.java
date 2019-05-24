/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator;

import server.world.Chunk;
import util.vec.IntVector;

/**
 * Interface for a world generator.
 *
 * @author TARS
 */
public interface WorldGenerator {
    
    /**
     * The maximum height of the world.
     */
    public static final int MAX_WORLD_HEIGHT = 16384;

    /**
     * The minimum height of the world.
     */
    public static final int MIN_WORLD_HEIGHT = -16384;
    
    /**
     * Generates a blank chunk for generating steps.
     *
     * @param chunkPos The chunk position.
     * @return A new chunk.
     */
    public Chunk createChunk(IntVector chunkPos);

    /**
     * Generates the chunk step for a given chunk. Throws a RuntimeException if
     * the chunk does not have the dependent chunk steps.
     *
     * @param chunk The chunk.
     * @param gs The step to generate.
     */
    public void generateChunk(Chunk chunk, GenStep gs);

    /**
     * Generates the chunk to the level indicated if it has not been already.
     * Generators should generated the world from top to bottom.
     *
     * @param chunk The chunk to generated further.
     * @param level The level to generate to.
     */
    public void generateToLevel(Chunk chunk, int level);
}
