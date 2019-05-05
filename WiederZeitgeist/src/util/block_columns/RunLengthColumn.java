/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.block_columns;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import static util.Utils.combineInts;
import static util.Utils.firstInt;
import static util.Utils.lastInt;

/**
 * An implementation of BlockColumn using a run-length encoding method.
 *
 * @author TARS
 */
public class RunLengthColumn implements BlockColumn {
    
    private static int block(long enc) {
        return lastInt(enc);
    }
    private static long encode(int pos, int block) {
        return combineInts(pos, block);
    }
    private static int position(long enc) {
        return firstInt(enc);
    }
    
    private boolean activated;
    private long[] encodedBlocks;
    private TreeMap<Integer, Integer> intermediate;
    private final int maxHeight;
    private final int minHeight;

    /**
     * Creates a new BlockColumn with all values initialized to the block
     * specified.
     *
     * @param block The block to set all the values to.
     * @param minHeight The minimum allowed access height.
     * @param maxHeight The maximum allowed access height.
     */
    public RunLengthColumn(int block, int minHeight, int maxHeight) {
        encodedBlocks = new long[1];
        encodedBlocks[0] = combineInts(Integer.MAX_VALUE, block);
        intermediate = new TreeMap();
        activated = false;
        if (minHeight > maxHeight) {
            throw new IllegalArgumentException("Improper maxima range: min: " + minHeight + " > max: " + maxHeight);
        }
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public void activate() {
        intermediate = new TreeMap();
        for (long e : encodedBlocks) {
            intermediate.put(position(e), block(e));
        }

        encodedBlocks = null;
        activated = true;
    }
    
    private void checkAccessRange(int height) {
        if (!inRange(height)) {
            throw new IllegalArgumentException("Height must be in the acces range of the column: min = " + minHeight + " max = " + maxHeight + " input: " + height);
        }
    }

    @Override
    public void deactivate() {
        Iterator<Map.Entry<Integer, Integer>> iterator = intermediate.descendingMap().entrySet().iterator();
        int prev = 0;
        boolean initPrev = false;

        while (iterator.hasNext()) {
            int block = iterator.next().getValue();
            if (initPrev && block == prev) {
                iterator.remove();
            }
            initPrev = true;
            prev = block;
        }

        encodedBlocks = new long[intermediate.size()];
        int i = 0;
        for (Map.Entry<Integer, Integer> e : intermediate.entrySet()) {
            encodedBlocks[i] = encode(e.getKey(), e.getValue());
            i++;
        }

        intermediate = null;
        activated = false;
    }
    
    private int findEncodedIndex(int height) {
        int upInd = leastUpperBinarySearchRecursion(height, 0, encodedBlocks.length);
        assert (upInd != -1); // one of the assumptions was not kept
        return upInd;
    }

    @Override
    public int getBlock(int height) {
        checkAccessRange(height);
        if (activated) {
            return intermediate.get(intermediate.ceilingKey(height));
        }
        return block(encodedBlocks[findEncodedIndex(height)]);
    }

    @Override
    public int getBottomHeight(int height) {
        checkAccessRange(height);
        if (activated) {
            int block = getBlock(height);
            Map.Entry<Integer, Integer> belowLayer = intermediate.floorEntry(height);
            while (belowLayer != null) {
                if (belowLayer.getValue() != block) {
                    return belowLayer.getKey() + 1;
                }
                if (belowLayer.getKey() == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                belowLayer = intermediate.floorEntry(belowLayer.getKey() - 1);
            }
            return Integer.MIN_VALUE;
        }
        int upperIndex = findEncodedIndex(height);
        if (upperIndex == 0) {
            return Integer.MIN_VALUE;
        }
        return position(encodedBlocks[upperIndex]) + 1;
    }

    @Override
    public int getTopHeight(int height) {
        checkAccessRange(height);
        if (activated) {
            int block = getBlock(height);
            int prevHeight = intermediate.ceilingEntry(height).getKey();
            if (prevHeight == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            Map.Entry<Integer, Integer> aboveLayer = intermediate.ceilingEntry(prevHeight + 1);
            while (block == aboveLayer.getValue()) {
                prevHeight = aboveLayer.getKey();
                if (prevHeight == Integer.MAX_VALUE) {
                    return prevHeight;
                }
                aboveLayer = intermediate.ceilingEntry(prevHeight + 1);
            }
            return prevHeight;
        }
        return position(encodedBlocks[findEncodedIndex(height)]);
    }
    
    @Override
    public boolean inRange(int height) {
        return (minHeight <= height) && (height <= maxHeight);
    }
    
    private int leastUpperBinarySearchRecursion(int pos, int low, int high) {
        
        assert (low != high); // since encodedBlocks should never be empty and because this is unreachable by splitting
        
        if (high - low == 1) {
            return position(encodedBlocks[low]) < pos ? -1 : low;
        }
        
        int checkInd = low + (high - low) / 2;
        int checkPos = position(encodedBlocks[checkInd]);
        
        if (checkPos == pos) {
            return checkInd;
        }
        
        if (checkPos > pos) {
            int downInd = leastUpperBinarySearchRecursion(pos, low, checkInd);
            return downInd == -1 ? checkInd : downInd;
        }
        
        return leastUpperBinarySearchRecursion(pos, checkInd + 1, high); // there is always an upper element since Integer.MAX_VALUE always caps the array
    }

    @Override
    public int maxHeight() {
        return maxHeight;
    }

    @Override
    public int minHeight() {
        return minHeight;
    }

    @Override
    public void setBlock(int height, int block) {
        checkAccessRange(height);
        boolean editing = activated;
        if (!editing) {
            activate();
        }

        if (height > minHeight && height != Integer.MIN_VALUE) {
            intermediate.put(height - 1, getBlock(height));
        }
        intermediate.put(height, block);

        if (!editing) {
            deactivate();
        }
    }

    @Override
    public void setRange(int bottom, int top, int block) {
        checkAccessRange(bottom);
        checkAccessRange(top);
        if (bottom > top) {
            throw new IllegalArgumentException("Improper range: lower: " + bottom + " > upper: " + top);
        }
        boolean editing = activated;
        if (!editing) {
            activate();
        }

        if (bottom > minHeight && bottom != Integer.MIN_VALUE) {
            intermediate.put(bottom - 1, getBlock(bottom - 1));
        }
        intermediate.subMap(bottom, top).clear();
        intermediate.put(top, block);

        if (!editing) {
            deactivate();
        }
    }
}
