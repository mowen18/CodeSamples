package model;

import model.commands.ExecuteSelect;
import model.interfaces.IStartCommand;
import view.interfaces.PaintCanvasBase;

import java.awt.*;

public class Select implements IStartCommand {
    private final Point xyStart;
    private final Point xyEnd;
    private final PaintCanvasBase canvas;

    public Select(Point _xyStart, Point _xyEnd, PaintCanvasBase _canvas){
        this.xyStart = _xyStart;
        this.xyEnd = _xyEnd;
        this.canvas = _canvas;
    }
    public void doCommand(){
        ExecuteSelect executeSelect = new ExecuteSelect(new SelectShape(xyStart,xyEnd,canvas));
        executeSelect.execute();
    }

}
