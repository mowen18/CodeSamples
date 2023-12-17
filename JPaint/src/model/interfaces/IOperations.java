package model.interfaces;

import java.util.List;
import java.awt.*;

public interface IOperations {

    void setG2d(Graphics2D graphics2d);
    
    void drawGraphic();

    void removeShape();

    void make();

    void moveSelection(int xNew, int yNew);

    List<IOperations> getShapeList();

    void outlineSelected();

    IOperations copyCanvas();

    IOperations pasteCanvas();

    boolean collided(Shape box);
}