package model;

import model.commands.ExecuteMove;
import model.interfaces.IStartCommand;
import view.interfaces.PaintCanvasBase;

import java.awt.*;

public class Move implements IStartCommand {

    private final Point xyStart;
    private final Point newLoc;
    MoveSelected moveSelected;
    PaintCanvasBase canvas;

    public Move(Point xyStart, Point newLoc, PaintCanvasBase canvas){
        this.xyStart = xyStart;
        this.newLoc = newLoc;
        this.canvas = canvas;
    }
    public void stopMove(Point xy){
        moveSelected.xyEnd(xy);
        ExecuteMove executeMove = new ExecuteMove(moveSelected);
        executeMove.execute();
    }
    public void doCommand(){
        moveSelected = new MoveSelected(xyStart,newLoc,canvas);
        moveSelected.xyNew(moveSelected.newLoc.x, moveSelected.newLoc.y);
    }




}
