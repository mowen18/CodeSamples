package model;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import model.interfaces.IShapeDesignStrategy;

public class EllipseStrategy implements IShapeDesignStrategy {
    Integer width;
    Integer height;
    Integer X;
    Integer Y;

    public EllipseStrategy(Integer X, Integer Y, Integer width, Integer height) {
        this.X = X;
        this.Y = Y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Shape createShape() {
        return new Ellipse2D.Float(X, Y, width, height);
    }

}