package model;

import model.interfaces.IShapeDesignStrategy;

import java.awt.*;
import java.awt.geom.Path2D;

public class TriangleStrategy implements IShapeDesignStrategy {
    private final Point xyBegin;
    private final Point xyEnd;

    public TriangleStrategy(Point _xyBegin, Point _xyEnd){
        this.xyBegin = _xyBegin;
        this.xyEnd = _xyEnd;
    }
    @Override
    public Shape createShape() {
        Path2D tri = new Path2D.Float();
        tri.moveTo(xyBegin.x, xyBegin.y);
        tri.lineTo(xyBegin.x, xyEnd.y);
        tri.lineTo(xyEnd.x, xyEnd.y);
        tri.closePath();
        return tri;

    }
}
