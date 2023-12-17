package model;

import java.awt.Shape;

import model.interfaces.IShapeDesignStrategy;

public class ShapeBuilder {

	public static Shape buildShape(ShapeBuildInfo shapeInfo) {
            ShapeType shape = shapeInfo.shape;
            IShapeDesignStrategy shapeDesign = switch (shape) {
                    case RECTANGLE -> StaticFactoryPaintDesign.rectangle(shapeInfo.X,shapeInfo.Y,shapeInfo.width,shapeInfo.height);
                    case ELLIPSE -> StaticFactoryPaintDesign.ellipse(shapeInfo.X,shapeInfo.Y,shapeInfo.width,shapeInfo.height);
                    case TRIANGLE -> StaticFactoryPaintDesign.triangle(shapeInfo.xyStart, shapeInfo.xyEnd);
            };
            return shapeDesign.createShape();
    }

}
