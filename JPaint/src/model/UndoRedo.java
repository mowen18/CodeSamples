package model;

import model.commands.CommandHistory;
import view.interfaces.PaintCanvasBase;



public class UndoRedo {
    PaintCanvasBase canvas;

    public UndoRedo(PaintCanvasBase canvas) {
        this.canvas = canvas;
    }

    public void operateRedo() {
        CommandHistory.redo();
        canvas.repaint();
    }
    public void operateUndo() {
        CommandHistory.undo();
        canvas.repaint();
    }
}
