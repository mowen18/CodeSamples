package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import model.interfaces.IShadeDesignStrategy;

public class FilledStrategy implements IShadeDesignStrategy {
    Color color;
    Shape shape;
    private final Graphics2D g2d;


    public FilledStrategy(Color color, Shape shape, Graphics2D g2d) {
        this.color = color;
        this.shape = shape;
        this.g2d = g2d;
    }

    @Override
    public void createShade() {
        g2d.setPaint(color);
        g2d.fill(shape);
    }

}
