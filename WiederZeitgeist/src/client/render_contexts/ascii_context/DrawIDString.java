/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.render_contexts.ascii_context;

import static client.render_contexts.ascii_context.ASCIIRender.drawForeground;
import graphics.Color;
import graphics.Window;
import java.util.List;
import util.fontString.IDString;
import util.math.VectorN;

/**
 *
 * @author TARS
 */
public class DrawIDString {

    public static final DrawIDString DEFAULT_DRAW;

    static {
        Color colorCodes[] = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE,
            null, null, null, null, null, null, null, null, null, null, null, null,
            Color.VERMILION, Color.AMBER, Color.LIME, Color.CYAN, Color.VIOLET, Color.MAGENTA,
            Color.WHITE, new Color(1. / 3., 1. / 3, 1. / 3), new Color(2. / 3., 2. / 3, 2. / 3), Color.BLACK};
        for (int i = 0; i < 6; i++) {
            colorCodes[i + 6] = darken(colorCodes[i]);
            colorCodes[i + 12] = lighten(colorCodes[i]);
        }
        DEFAULT_DRAW = new DrawIDString(colorCodes);
    }

    private static Color lighten(Color col) {
        return new Color((col.r + 1) / 2, (col.g + 1) / 2, (col.b + 1) / 2);
    }

    private static Color darken(Color col) {
        return new Color(col.r / 2, col.g / 2, col.b / 2);
    }

    private final Color[] codeColorList;

    public DrawIDString(Color[] codeColorList) {
        if (codeColorList.length != 28) {
            throw new IllegalArgumentException("Code color list must be 28 in length");
        }
        this.codeColorList = new Color[32];
        System.arraycopy(codeColorList, 0, this.codeColorList, 0, codeColorList.length);
    }

    public void drawTileString(IDString idString, VectorN position) {
        drawTileString(idString, position, Color.BLACK, true, 0, -1);
    }

    public void drawTileString(IDString idString, VectorN position, Color defaultColor) {
        drawTileString(idString, position, defaultColor, true, 0, -1);
    }

    public void drawTileString(IDString idString, VectorN position, Color defaultColor, boolean allowColorCodes, int start, int maxLength) {
        if (position.dim != 2) {
            throw new IllegalArgumentException("Position vector must be 2 dimensional");
        }
        double x = position.x();
        Color currentColor = defaultColor;
        int tilesDrawn = 0;
        int stringIndex = 0;

        while (stringIndex < idString.length && (maxLength == -1 || tilesDrawn < maxLength)) {
            int currentID = idString.getTile(stringIndex);
            if (currentID == 29) {
                currentColor = defaultColor;
            } else if (currentID < 28) {
                Color newColor = codeColorList[currentID];
                if (allowColorCodes && newColor != null) {
                    currentColor = newColor;
                }
            } else if (stringIndex >= start) {

                VectorN pos = position.set(0, x + tilesDrawn);
                drawForeground((int) pos.x(), (int) pos.y(), currentID, currentColor);
                tilesDrawn++;
            }
            stringIndex++;
        }
    }

    public void drawTilieString(List<Integer> idString, VectorN position, Color defaultColor, boolean allowColorCodes, int start, int maxLength) {
        if (position.dim != 2) {
            throw new IllegalArgumentException("Position vector must be 2 dimensional");
        }
        double x = position.x();
        Color currentColor = defaultColor;
        int tilesDrawn = 0;
        int stringIndex = 0;

        while (stringIndex < idString.size() && (maxLength == -1 || tilesDrawn < maxLength)) {
            int currentID = idString.get(stringIndex);
            if (currentID == 29) {
                currentColor = defaultColor;
            } else if (currentID < 28) {
                Color newColor = codeColorList[currentID];
                if (allowColorCodes && newColor != null) {
                    currentColor = newColor;
                }
            } else if (stringIndex >= start) {
                VectorN pos = position.set(0, x + tilesDrawn);
                drawForeground((int) pos.x(), (int) pos.y(), currentID, currentColor);
                tilesDrawn++;
            }
            stringIndex++;
        }
    }
}
