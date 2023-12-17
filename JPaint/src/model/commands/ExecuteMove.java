package model.commands;

import model.CanvasList;
import model.MoveSelected;
import model.interfaces.IExecuteCommand;
import model.interfaces.IUndoRedo;

public class ExecuteMove implements IUndoRedo, IExecuteCommand {
    MoveSelected shapeMove;

    public ExecuteMove(MoveSelected shapeMove){
        this.shapeMove = shapeMove;
    }
    @Override
    public void redo(){
        shapeMove.redo();
    }
    @Override
    public void undo(){
        shapeMove.undo();
    }
    @Override
    public void execute(){
        if(!CanvasList.selectedList.canvasContents().isEmpty()){
            shapeMove.xyNew(shapeMove.newLoc.x,shapeMove.newLoc.y);
            CommandHistory.add(this);
        }
    }
}
