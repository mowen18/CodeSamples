package model;

import java.awt.*;

import model.interfaces.IShadeDesignStrategy;
import model.interfaces.IShapeDesignStrategy;

public class StaticFactoryPaintDesign {
    public static IShapeDesignStrategy triangle(Point p1, Point p2){
        return new TriangleStrategy(p1,p2);
    }

    public static IShapeDesignStrategy ellipse(Integer X, Integer Y, Integer width, Integer height){
        return new EllipseStrategy(X,Y,width,height);
    }
    public static IShapeDesignStrategy rectangle(Integer X, Integer Y, Integer width, Integer height){
        return new RectangleStrategy(X,Y,width,height);
    }
	public static IShadeDesignStrategy outlineAndFilled(Color primaryColor, Color secondaryColor, Shape shape, Graphics2D g2) {
        return new FillAndOutlineStrategy(primaryColor, secondaryColor, shape, g2);
    }
    public static IShadeDesignStrategy filled(Color primaryColor, Shape shape, Graphics2D g2) {
        return new FilledStrategy(primaryColor, shape, g2);
    }
    public static IShadeDesignStrategy outline(Color primaryColor, Shape shape, Graphics2D g2) {
        return new OutlineStrategy(primaryColor, shape, g2);
    }

}
