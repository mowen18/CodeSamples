package model.commands;

import model.UndoRedo;
import model.interfaces.IExecuteCommand;

public class ExecuteUndo implements IExecuteCommand {
	
	  UndoRedo undo;
	  public ExecuteUndo(UndoRedo undo) {
	      this.undo = undo;
	    }

	  @Override
	  public void execute() {
	        undo.operateUndo();
	    }

}
