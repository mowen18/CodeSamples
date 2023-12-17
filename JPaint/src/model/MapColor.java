package model;

import java.awt.Color;

import java.util.EnumMap;

public class MapColor {
	
	private static EnumMap<ShapeColor, java.awt.Color> cMap = cMap();
	private static EnumMap cMap() {
        EnumMap<ShapeColor, java.awt.Color> cMap = new EnumMap<>(ShapeColor.class);
        cMap.put(ShapeColor.WHITE, Color.WHITE);
        cMap.put(ShapeColor.YELLOW, Color.YELLOW);
        cMap.put(ShapeColor.LIGHT_GRAY, Color.LIGHT_GRAY);
        cMap.put(ShapeColor.MAGENTA, Color.MAGENTA);
        cMap.put(ShapeColor.ORANGE, Color.ORANGE);
        cMap.put(ShapeColor.PINK, Color.PINK);
        cMap.put(ShapeColor.RED, Color.RED);
        cMap.put(ShapeColor.BLUE, Color.BLUE);
        cMap.put(ShapeColor.CYAN, Color.CYAN);
        cMap.put(ShapeColor.DARK_GRAY, Color.DARK_GRAY);
        cMap.put(ShapeColor.GRAY, Color.GRAY);
        cMap.put(ShapeColor.GREEN, Color.GREEN);
        cMap.put(ShapeColor.BLACK,Color.BLACK);
        return cMap;
    }

    public static Color getColor(ShapeColor sc) {
        return cMap.get(sc);
    }

}
