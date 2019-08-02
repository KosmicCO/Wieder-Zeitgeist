/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.fontString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author TARS
 */
public class IDString implements Iterable<Integer> {

    private final int[] idString;
    public final int length;
    private int hash;
    private boolean hashed;

    public IDString(int... tiles) {
        idString = new int[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] < 0) {
                throw new IllegalArgumentException("An id cannot be less than 0");
            }
            idString[i] = tiles[i];
        }
        length = idString.length;
        hash = 0;
        hashed = false;
    }
    
    public IDString(List<Integer> tiles){
        idString = new int[tiles.size()];
        for (int i = 0; i < tiles.size(); i++) {
            if(tiles.get(i) < 0){
                throw new IllegalArgumentException("An id cannot be less than 0");
            }
            idString[i] = tiles.get(i);
        }
        length = idString.length;
        hash = 0;
        hashed = false;
    }

    public boolean isEmpty() {
        return idString.length == 0;
    }

    public int getTile(int index) {
        if (index >= idString.length) {
            throw new ArrayIndexOutOfBoundsException("Index was not in bounds: 0 - " + idString.length);
        }
        return idString[index];
    }
    
    public List<Integer> toList(){
        List<Integer> idList = new ArrayList();
        for(int id : idString){
            idList.add(id);
        }
        return idList;
    }

    @Override
    public int hashCode() {
        if (hashed) {
            return hash;
        }
        hash = Arrays.hashCode(idString);
        hashed = true;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        IDString other = (IDString) obj;
        if (this.hashed && other.hashed) {
            if (this.hash != other.hash) {
                return false;
            }
        }

        return Arrays.equals(this.idString, other.idString);
    }
    
    @Override
    public String toString(){
        if(length == 0){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[" + idString[0]);
        for (int i = 1; i < length; i++) {
            sb.append(", ").append(idString[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IDStringIterator(this);
    }

    private class IDStringIterator implements Iterator<Integer> {

        private int index;
        private final IDString string;

        public IDStringIterator(IDString string) {
            this.string = string;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < string.length;
        }

        @Override
        public Integer next() {
            int next = string.idString[index];
            index++;
            return next;
        }

    }
}
