package model.commands;

import model.SelectShape;
import model.interfaces.IExecuteCommand;

public class ExecuteSelect implements IExecuteCommand {
    SelectShape selectShape;

    public ExecuteSelect(SelectShape _selectShape){
        this.selectShape = _selectShape;
    }
    @Override
    public void execute(){
        selectShape.doSelect();

    }
}
