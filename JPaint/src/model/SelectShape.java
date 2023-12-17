package model;

import java.awt.*;

import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;
import view.interfaces.PaintCanvasBase;

public class SelectShape implements IUndoRedo {
	
    private final Point xyBegin;
    private final Point xyEnd;
    private final Graphics2D g2d;
    private final PaintCanvasBase paintCanvas;

    public SelectShape(Point xyBegin, Point xyEnd, PaintCanvasBase paintCanvas) {
        this.g2d = paintCanvas.getGraphics2D();
        this.paintCanvas = paintCanvas;
        this.xyBegin = xyBegin;
        this.xyEnd = xyEnd;
    }

    public void doSelect() {
        CanvasList.selectedList.emptyContents();
        BBox bbox = new BBox();
        bbox.setBBox(xyBegin, xyEnd);
        bbox.drawBBox(g2d);
        for(IOperations s : CanvasList.shapeList.canvasContents()){
            if(s.collided(bbox.bbox))
                CanvasList.selectedList.add(s);
        }
        paintCanvas.repaint();
    }

    public void undo() {
        CanvasList.selectedList.emptyContents();
        paintCanvas.repaint();
    }

    public void redo() {
        doSelect();
        paintCanvas.repaint();
    }

}
