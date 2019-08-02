/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.fontString;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author TARS
 */
public class IDStringEncoder {

    private static final String FONT_MAP_PATH = "resources/font/font.map";
    private static Map<Integer, Integer> encoderMap;

    public static void initialize() {
        File fontMapFile = new File(FONT_MAP_PATH);
        encoderMap = new HashMap();
        try {
            Scanner fontMapScanner = new Scanner(fontMapFile);
            int currentID = 0;
            boolean numerical = false;
            while (fontMapScanner.hasNextLine()) {
                String toEncode = fontMapScanner.nextLine();
                if (toEncode.length() == 0) {
                    continue;
                }
                if (toEncode.equals("Toggle Numerical")) {
                    numerical = !numerical;
                    continue;
                }
                if (numerical) {
                    String[] numerals = toEncode.split(" ");
                    switch (numerals.length) {
                        case 1:
                            encoderMap.put(Integer.parseInt(numerals[0]), currentID);
                            break;
                        case 2:
                            currentID = Integer.parseInt(numerals[1]);
                            encoderMap.put(Integer.parseInt(numerals[0]), currentID);
                            break;
                        default:
                            throw new RuntimeException("The font.map file is improperly formatted: " + toEncode);
                    }
                } else {
                    char uni = toEncode.charAt(0);
                    if (toEncode.length() > 1) {
                        currentID = Integer.parseInt(toEncode.substring(1));
                    }
                    encoderMap.put((int) uni, currentID);
                }
                currentID++;
            }
            fontMapScanner.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isMapped(char c) {
        return encoderMap.containsKey(c);
    }
    
    public static IDString encode(int[] s){
        int[] idArray = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            Integer c = encoderMap.get(s[i]);
            if (c == null) {
                throw new IllegalArgumentException("String contains unmapped characters: " + s[i]);
            }
            idArray[i] = c;
        }
        return new IDString(idArray);
    }

    public static IDString encode(String s) {
        int[] idArray = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            Integer c = encoderMap.get((int) s.charAt(i));
            if (c == null) {
                throw new IllegalArgumentException("String contains unmapped characters: " + s.charAt(i));
            }
            idArray[i] = c;
        }
        return new IDString(idArray);
    }
    
    public static Integer encode(int c){
        return encoderMap.get(c);
    }
}
