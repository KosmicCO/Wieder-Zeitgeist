/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui.components;

import client.gui.Component;
import core.Message;
import util.fontString.IDString;
import util.fontString.IDStringEncoder;
import util.math.VectorN;

/**
 * A template label class.
 *
 * @author TARS
 */
public abstract class Label implements Component {

    private IDString label;
    private String sourceString;

    /**
     * Creates a label from a String.
     *
     * @param label The source String.
     */
    public Label(String label) {
        sourceString = label;
        this.label = IDStringEncoder.encode(label);
    }

    /**
     * Creates a label from an IDString.
     *
     * @param label The source IDString.
     */
    public Label(IDString label) {
        sourceString = null;
        this.label = label;
    }
    @Override
    public boolean contains(VectorN v) {
        return false;
    }
    /**
     * Gets the label.
     *
     * @return The IDString label.
     */
    public IDString getLabel() {
        return label;
    }

    /**
     * Returns the source string of the label. If there is none, it returns
     * null.
     *
     * @return The Source string if there was any.
     */
    public String getSourceString() {
        return sourceString;
    }
    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    /**
     * Sets the source string of the label.
     *
     * @param label The new source String.
     */
    public void setLabel(String label) {
        sourceString = label;
        this.label = IDStringEncoder.encode(label);
    }

    /**
     * Sets the text with an IDString. Also sets the source String to null.
     *
     * @param label The IDString.
     */
    public void setLabel(IDString label) {
        sourceString = null;
        this.label = label;
    }

}
