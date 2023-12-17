package model;

import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;
import view.interfaces.PaintCanvasBase;

import java.util.ArrayList;
import java.util.List;

public class Delete implements IUndoRedo {
    private final PaintCanvasBase canvas;
    private final List<IOperations> removed = new ArrayList<>();
    public Delete(PaintCanvasBase _canvas){
        this.canvas = _canvas;
        removed.addAll(CanvasList.selectedList.canvasContents());
    }
    public void remove(){
        for(IOperations shape : removed){
            shape.removeShape();
        }
        canvas.repaint();
    }
    @Override
    public void undo() {
        for(IOperations shape : removed){
            shape.make();
        }
        canvas.repaint();
    }

    @Override
    public void redo() {
        remove();

    }
}
