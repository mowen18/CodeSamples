package model;
import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;
import view.interfaces.PaintCanvasBase;

import java.util.ArrayList;
import java.util.List;

public class CopyPaste implements IUndoRedo {
    List<IOperations> selectedCanvas = CanvasList.selectedList.canvasContents();
    List<IOperations> copyList = CanvasList.copyList.canvasContents();
    List<IOperations> shapes = CanvasList.shapeList.canvasContents();
    List<IOperations> pList = new ArrayList<>();

    private PaintCanvasBase canvas;

    public CopyPaste(){}
    public CopyPaste(PaintCanvasBase _canvas){
        this.canvas = _canvas;

    }

    public void paste(){
        pList.clear();
        for(IOperations shape : copyList){
            IOperations sPasted = shape.pasteCanvas();
            shapes.add(sPasted);
            pList.add(sPasted);

        }
        canvas.repaint();
    }
    public void copy(){
        copyList.clear();

        for(IOperations shape : selectedCanvas){
            IOperations sCopy = shape.copyCanvas();
            copyList.add(sCopy);
        }
    }

    @Override
    public void undo() {
        for(IOperations pShape : pList){
            shapes.removeAll(pShape.getShapeList());

        }
        canvas.repaint();
    }

    @Override
    public void redo() {
        shapes.addAll(pList);
        canvas.repaint();

    }

}
