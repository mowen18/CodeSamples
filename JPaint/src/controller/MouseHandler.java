package controller;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.*;
import model.interfaces.IApplicationState;
import model.interfaces.IStartCommand;
import view.interfaces.PaintCanvasBase;

public class MouseHandler extends MouseAdapter{
    private final PaintCanvasBase canvas;
    private Point xyStart;
    private Point xyEnd;
    private final IApplicationState state;
    private int mouseX1;
    private int mouseY1;
    private Move mv;
    private boolean moved = false;

    public MouseHandler(PaintCanvasBase canvas, IApplicationState state) {
        this.canvas = canvas;
        this.state = state;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xyStart = e.getPoint();
        xyEnd = e.getPoint();
        mouseX1 = e.getX();
        mouseY1 = e.getY();
        moved = false;
    }

    public void mouseDragged(MouseEvent e){
        moved = true;

        int x = e.getX();
        int y = e.getY();
        if(state.getActiveMouseMode().equals(MouseMode.MOVE)){
            mv = new Move(xyStart,new Point(x - mouseX1,y - mouseY1),canvas);
            mouseX1 = x;
            mouseY1 = y;
            mv.doCommand();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        xyEnd = e.getPoint();
        IStartCommand s = null;

        switch (state.getActiveMouseMode()) {
            case DRAW:
                s = new Draw(xyStart, xyEnd, canvas,state);
                break;
            case SELECT:
                s = new Select(xyStart,xyEnd,canvas);
            	break;
            case MOVE:
                if(mv != null && moved == true)
                    mv.stopMove(xyEnd);
            	break;
        }

        if (s == null) {
            s = NullObjectSingleton.getInstance();
        }
        s.doCommand();
    }

}
