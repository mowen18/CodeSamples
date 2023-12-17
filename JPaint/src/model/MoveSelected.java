package model;

import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;
import view.interfaces.PaintCanvasBase;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class MoveSelected implements IUndoRedo {
    private final Point xyStart;
    private Point xyEnd;
    public Point newLoc;
    PaintCanvasBase canvas;

    public MoveSelected(Point _xyStart, Point _newLoc, PaintCanvasBase canvas){
        this.xyStart = _xyStart;
        this.newLoc = _newLoc;
        this.canvas = canvas;

    }

    public void xyNew(int x, int y){
        AffineTransform trans = new AffineTransform();
        trans.translate(x,y);
        for(IOperations shape : CanvasList.selectedList.canvasContents()){
            shape.moveSelection(x,y);
        }
        canvas.repaint();

    }
    public void xyEnd(Point xyEnd){
        this.xyEnd = xyEnd;
    }

    @Override
    public void redo(){
        xyNew(xyEnd.x - xyStart.x, xyEnd.y - xyStart.y);
    }
    @Override
    public void undo(){
        xyNew(xyStart.x - xyEnd.x,xyStart.y - xyEnd.y);
    }

}
