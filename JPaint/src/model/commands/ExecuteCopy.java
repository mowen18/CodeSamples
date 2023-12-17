package model.commands;

import model.CopyPaste;
import model.interfaces.IExecuteCommand;

public class ExecuteCopy implements IExecuteCommand {

    CopyPaste cShape;
    public ExecuteCopy(CopyPaste sCopy){
        this.cShape = sCopy;
    }
    @Override
    public void execute() {
        cShape.copy();
    }
}
