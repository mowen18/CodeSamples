package model;

import java.awt.*;
import java.awt.Graphics2D;

import model.interfaces.IShadeDesignStrategy;

public class FillAndOutlineStrategy implements IShadeDesignStrategy {
    Shape shape;
    Color primary;
    Color secondary;
    private final Graphics2D g2d;


    public FillAndOutlineStrategy(Color primary, Color secondary, Shape shape, Graphics2D _g2d) {
        this.primary = primary;
        this.secondary = secondary;
        this.shape = shape;
        this.g2d = _g2d;
    }


    @Override
    public void createShade() {
        g2d.setStroke(new BasicStroke(5));
        g2d.setPaint(secondary);
        g2d.draw(shape);
        g2d.setPaint(primary);
        g2d.fill(shape);
    }

}
