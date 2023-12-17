package model;

import java.awt.*;

public class BBox {
    public Shape bbox;
    BBox(){}

    public Shape newBBox(Point xyStart, Point xyEnd){
        ShapeBuildInfo shapeAttributes = new ShapeBuildInfo(xyStart,xyEnd);
        shapeAttributes.setShape(ShapeType.RECTANGLE);

        return ShapeBuilder.buildShape(shapeAttributes);
    }
    public void setBBox(Point xyStart, Point xyEnd){
        bbox = newBBox(xyStart,xyEnd);
    }

    public void drawBBox(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(Color.green);
        g2d.draw(bbox);


    }
}
