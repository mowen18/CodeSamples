package model;
import java.awt.Point;

import model.commands.ExecuteDraw;
import model.interfaces.IApplicationState;
import model.interfaces.IStartCommand;
import view.interfaces.PaintCanvasBase;

public class Draw implements IStartCommand {
    ShapeBuildInfo shapeBuildInfo;
    private final Point xyStart;
    private final Point xyEnd;
    private final PaintCanvasBase canvas;

    public Draw(Point xyStart, Point xyEnd, PaintCanvasBase _canvas, IApplicationState state) {
        this.canvas = _canvas;
        this.xyStart = xyStart;
        this.xyEnd = xyEnd;
        this.shapeBuildInfo = new ShapeBuildInfo(xyStart, xyEnd);
        this.shapeBuildInfo.setShape(state.getActiveShapeType());
        this.shapeBuildInfo.setShade(state.getActiveShapeShadingType());
        this.shapeBuildInfo.setPrimary(MapColor.getColor(state.getActivePrimaryColor()));
        this.shapeBuildInfo.setSecondary(MapColor.getColor(state.getActiveSecondaryColor()));
    }

    public void doCommand() {
        if (!xyStart.equals(xyEnd)) {
            ExecuteDraw executeDraw = new ExecuteDraw(new ShapeOperations(canvas, shapeBuildInfo));
            executeDraw.execute();
        }
    }

}
