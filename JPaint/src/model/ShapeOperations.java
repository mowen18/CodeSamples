package model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import model.interfaces.IShadeDesignStrategy;
import model.interfaces.IOperations;
import model.interfaces.IUndoRedo;
import view.interfaces.PaintCanvasBase;

public class ShapeOperations implements IUndoRedo, IOperations{
    Shape shape;
    private PaintCanvasBase canvas;
    private ShapeBuildInfo shapeBuildInfo;
    private Graphics2D g2d;


    public ShapeOperations(PaintCanvasBase canvas, ShapeBuildInfo shapeBuildInfo) {
        this.canvas = canvas;
        this.g2d = canvas.getGraphics2D();
        this.shapeBuildInfo = shapeBuildInfo;
        this.shape = ShapeBuilder.buildShape(shapeBuildInfo);

    }
    public ShapeOperations(ShapeOperations draw, PaintCanvasBase paintCanvasBase){
        ShapeBuildInfo attributes = draw.shapeBuildInfo;
        this.shapeBuildInfo = new ShapeBuildInfo(attributes.xyStart, attributes.xyEnd);
        this.shapeBuildInfo.setPrimary(attributes.primC);
        this.shapeBuildInfo.setSecondary(attributes.secC);
        this.shapeBuildInfo.setShape(attributes.shape);
        this.shapeBuildInfo.setShade(attributes.shade);
        this.canvas = paintCanvasBase;
        this.g2d = paintCanvasBase.getGraphics2D();
        this.shape = ShapeBuilder.buildShape(shapeBuildInfo);

    }

    public void drawGraphic() {
        IShadeDesignStrategy sPaintDesign;
        sPaintDesign = switch (shapeBuildInfo.shade) {
            case OUTLINE_AND_FILLED_IN -> StaticFactoryPaintDesign.outlineAndFilled(shapeBuildInfo.primC,shapeBuildInfo.secC,shape,g2d);
            case FILLED_IN -> StaticFactoryPaintDesign.filled(shapeBuildInfo.primC, shape, g2d);
            case OUTLINE -> StaticFactoryPaintDesign.outline(shapeBuildInfo.primC, shape, g2d);
        };
        if(sPaintDesign == null)
            sPaintDesign = NullObjectSingleton.getInstance();

        sPaintDesign.createShade();
    }

    @Override
    public void make() {
        CanvasList.selectedList.emptyContents();
        drawGraphic();
        CanvasList.shapeList.add(this);
        canvas.repaint();
    }

    @Override
    public void moveSelection(int xNew, int yNew) {
        AffineTransform trans = new AffineTransform();
        trans.translate(xNew,yNew);
        this.setShape(trans.createTransformedShape(shape));
        
    }

    public boolean collided(Shape s){
        Rectangle r1 = shape.getBounds();
        Rectangle r2 = s.getBounds();

        return r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;
    }

    public void outlineSelected(){
        float[] tick = {6.0f,6.0f};
        OutlineStrategy ol = new OutlineStrategy(Color.red, shape, g2d);
        ol.setOutline(new BasicStroke(5.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,10f,tick,0.0f));
        ol.createShade();
    }

    @Override
    public IOperations copyCanvas() {
        return new ShapeOperations(this, canvas);
    }

    @Override
    public IOperations pasteCanvas() {
        ShapeOperations sp = new ShapeOperations(this,canvas);
        AffineTransform tf = new AffineTransform();
        tf.translate(150,150);
        sp.setShape(tf.createTransformedShape(shape));
        return sp;
    }

    @Override
    public List<IOperations> getShapeList() {
        List<IOperations> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    public void setG2d(Graphics2D graphics2d) {
        this.g2d = graphics2d;
    }


    public void setShape(Shape shape) {
        this.shape = shape;
        Rectangle r = shape.getBounds();
        this.shapeBuildInfo.setStartXY(new Point(r.x, r.y));
        this.shapeBuildInfo.setEndXY(new Point((r.x + r.width), (r.y + r.height)));

    }

    @Override
    public void undo() {
        removeShape();
    }

    @Override
    public void redo() {
        make();
    }

	@Override
	public void removeShape() {
        CanvasList.shapeList.remove(this);
		
	}

}
