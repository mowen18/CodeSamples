package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import model.interfaces.IShadeDesignStrategy;

public class OutlineStrategy implements IShadeDesignStrategy {
    BasicStroke outline = new BasicStroke(5);
    Shape shape;
    Color color;
    private final Graphics2D g2d;

    public OutlineStrategy(Color color, Shape shape, Graphics2D g2d) {
        this.shape = shape;
        this.g2d = g2d;
        this.color = color;
    }

    @Override
    public void createShade() {
        g2d.setStroke(outline);
        g2d.setPaint(color);
        g2d.draw(shape);
    }

    public void setOutline(BasicStroke stroke) {
        this.outline = stroke;
    }

}
