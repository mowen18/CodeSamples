package model;

import java.util.ArrayList;
import java.util.List;

import model.interfaces.IOperations;

public class CanvasList {
    public static final CanvasList shapeList = new CanvasList();
    public static final CanvasList selectedList = new CanvasList();
    public static final CanvasList copyList = new CanvasList();
    private final List<IOperations> canvasList = new ArrayList<>();


    public void add(IOperations sh) {

        if (!canvasList.contains(sh)) {
            canvasList.add(sh);
        }
    }
    public void remove(IOperations sh) {
        canvasList.remove(sh);

    }
    
    public void emptyContents() {
        canvasList.clear();
    }
    public List<IOperations> canvasContents() {
        return canvasList;
    }


}
