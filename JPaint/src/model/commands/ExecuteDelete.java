package model.commands;

import model.Delete;
import model.interfaces.IExecuteCommand;
import model.interfaces.IUndoRedo;

public class ExecuteDelete implements IUndoRedo, IExecuteCommand {

    Delete delShape;

    public ExecuteDelete(Delete _delShape){
        this.delShape = _delShape;
    }
    @Override
    public void execute() {
        delShape.remove();
        CommandHistory.add(this);
    }

    @Override
    public void undo() {
        delShape.undo();

    }

    @Override
    public void redo() {
        delShape.redo();

    }
}
