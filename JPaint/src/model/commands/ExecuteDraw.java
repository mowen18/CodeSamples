package model.commands;

import model.interfaces.IExecuteCommand;
import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;

public class ExecuteDraw implements IUndoRedo, IExecuteCommand {

    IOperations shape;

    public ExecuteDraw(IOperations newshape) {
        this.shape = newshape;
    }

    @Override
    public void execute() {
        shape.make();
        CommandHistory.add(this);
    }

    @Override
    public void undo() {
        ((IUndoRedo) shape).undo();
    }

    @Override
    public void redo() {
        ((IUndoRedo) shape).redo();
    }

}
