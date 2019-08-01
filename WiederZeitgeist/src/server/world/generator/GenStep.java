/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.world.generator;

/**
 * Blank interface defining the steps of chunk loading.
 *
 * @author TARS
 */
public interface GenStep {

    /**
     * The id of the step. It must be the same among every object of the same
     * class. It also must function as an index of the array.
     *
     * @return The id of the GenStep.
     */
    public int id();
}
