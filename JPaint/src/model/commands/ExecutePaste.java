package model.commands;

import model.CanvasList;
import model.CopyPaste;
import model.interfaces.IExecuteCommand;
import model.interfaces.IUndoRedo;

public class ExecutePaste implements IExecuteCommand, IUndoRedo {

    CopyPaste pasteShape;

    public ExecutePaste(CopyPaste _pastShape){
        this.pasteShape = _pastShape;
    }

    @Override
    public void execute() {
        if(!CanvasList.copyList.canvasContents().isEmpty()){
            pasteShape.paste();
            CommandHistory.add(this);
        }
    }

    @Override
    public void undo() {
        pasteShape.undo();

    }

    @Override
    public void redo() {
        pasteShape.redo();

    }
}
