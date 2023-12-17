package model;

import java.awt.*;

public class ShapeBuildInfo {
    public Point xyStart;
    public Point xyEnd;
    public ShapeType shape;
    public ShapeShadingType shade;
    public Color primC;
    public Color secC;
    public Integer width;
    public Integer height;
    public Integer X;
    public Integer Y;


    public ShapeBuildInfo(Point xyStart, Point xyEnd) {
        this.xyStart = xyStart;
        this.xyEnd = xyEnd;
        this.Y = Math.min(xyStart.y, xyEnd.y);
        this.X = Math.min(xyStart.x, xyEnd.x);
        this.height = (xyEnd.y >= xyStart.y) ? xyEnd.y - xyStart.y : xyStart.y - xyEnd.y;
        this.width = (xyEnd.x >= xyStart.x) ? xyEnd.x - xyStart.x : xyStart.x - xyEnd.x;
    }

    public void setShape(ShapeType _shape) {
        this.shape = _shape;
    }

    public void setStartXY(Point _startP) {
        this.xyStart = _startP;
    }

    public void setShade(ShapeShadingType _shade) {
        this.shade = _shade;
    }

    public void setEndXY(Point _endP) {
        this.xyEnd = _endP;
    }

    public void setPrimary(Color p) {
        this.primC = p;
    }

    public void setSecondary(Color secC) {
        this.secC = secC;
    }



}
