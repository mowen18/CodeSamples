package model.commands;

import model.UndoRedo;
import model.interfaces.IExecuteCommand;

public class ExecuteRedo implements IExecuteCommand {
    UndoRedo redo;

    public ExecuteRedo(UndoRedo redo) {
        this.redo = redo;
    }

    @Override
    public void execute() {
        redo.operateRedo();
    }

}
