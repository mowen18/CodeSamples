package controller;

import model.*;
import model.commands.*;
import model.interfaces.IApplicationState;
import view.EventName;
import view.interfaces.IUiModule;
import view.interfaces.PaintCanvasBase;

public class JPaintController implements IJPaintController {
    private final IUiModule uiModule;
    private final IApplicationState applicationState;
    private PaintCanvasBase paintCanvas;
    public JPaintController(IUiModule uiModule, IApplicationState applicationState,PaintCanvasBase _paintCanvas) {
        this.uiModule = uiModule;
        this.applicationState = applicationState;
        this.paintCanvas = _paintCanvas;
    }

    @Override
    public void setup() {
        setupEvents();
    }

    public void doUndo() {
        ExecuteUndo executeUndo = new ExecuteUndo(new UndoRedo(paintCanvas));
        executeUndo.execute();
    }
    public void doRedo() {
        ExecuteRedo executeRedo = new ExecuteRedo(new UndoRedo(paintCanvas));
        executeRedo.execute();

    }
    public void doCopy(){
        ExecuteCopy copyCommand = new ExecuteCopy(new CopyPaste());
        copyCommand.execute();
    }
    public void doPaste(){
        ExecutePaste executePaste = new ExecutePaste(new CopyPaste(paintCanvas));
        executePaste.execute();
    }
    public void doDelete(){
        ExecuteDelete delCommand = new ExecuteDelete(new Delete(paintCanvas));
        delCommand.execute();
    }

    private void setupEvents() {
        uiModule.addEvent(EventName.CHOOSE_SHAPE, () -> applicationState.setActiveShape());
        uiModule.addEvent(EventName.CHOOSE_PRIMARY_COLOR, () -> applicationState.setActivePrimaryColor());
        uiModule.addEvent(EventName.CHOOSE_SECONDARY_COLOR, () -> applicationState.setActiveSecondaryColor());
        uiModule.addEvent(EventName.CHOOSE_SHADING_TYPE, () -> applicationState.setActiveShadingType());
        uiModule.addEvent(EventName.CHOOSE_MOUSE_MODE, () -> applicationState.setActiveStartAndEndPointMode());
        uiModule.addEvent(EventName.UNDO, () -> doUndo());
        uiModule.addEvent(EventName.REDO, () -> doRedo());
        uiModule.addEvent(EventName.COPY, () -> doCopy());
        uiModule.addEvent(EventName.PASTE, () -> doPaste());
        uiModule.addEvent(EventName.DELETE, () -> doDelete());
    }
}
