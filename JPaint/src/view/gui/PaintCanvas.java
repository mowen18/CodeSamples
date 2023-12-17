package view.gui;

import model.CanvasList;
import view.interfaces.PaintCanvasBase;
import java.util.List;

import model.interfaces.IOperations;

import java.awt.*;

public class PaintCanvas extends PaintCanvasBase{

    public Graphics2D getGraphics2D() {
        return (Graphics2D)getGraphics();
    }
    
    protected void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);

        Graphics2D g2d = (Graphics2D) graphic;
        List<IOperations> shapeList = CanvasList.shapeList.canvasContents();
        List<IOperations> selectedList = CanvasList.selectedList.canvasContents();


        for (IOperations shape : shapeList) {
            shape.setG2d(g2d);
            shape.drawGraphic();

            if(selectedList.contains(shape)){
                shape.outlineSelected();
            }
        }
    }

  
}
