package model;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import model.interfaces.IShapeDesignStrategy;

public class RectangleStrategy implements IShapeDesignStrategy {
    Integer width;
    Integer height;
    Integer X;
    Integer Y;

    public RectangleStrategy(Integer X, Integer Y, Integer _w, Integer _h) {
        this.X = X;
        this.Y = Y;
        this.width = _w;
        this.height = _h;
    }

    @Override
    public Shape createShape() {
        return new Rectangle2D.Float(X, Y, width, height);
    }

}
