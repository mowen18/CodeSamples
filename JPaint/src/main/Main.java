package main;


import controller.IJPaintController;
import controller.JPaintController;
import controller.MouseHandler;
import model.persistence.ApplicationState;
import view.gui.Gui;
import view.gui.GuiWindow;
import view.gui.PaintCanvas;
import view.interfaces.IGuiWindow;
import view.interfaces.PaintCanvasBase;
import view.interfaces.IUiModule;

public class Main {
    public static void main(String[] args){
        PaintCanvasBase paintCanvas = new PaintCanvas();
        IGuiWindow guiWindow = new GuiWindow(paintCanvas);
        IUiModule uiModule = new Gui(guiWindow);
        ApplicationState appState = new ApplicationState(uiModule);
       // IJPaintController controller = new JPaintController(uiModule, appState);

        
        IJPaintController controller = new JPaintController(uiModule, appState, paintCanvas);
        
        controller.setup();

        MouseHandler mhandler = new MouseHandler(paintCanvas, appState);
        paintCanvas.addMouseListener(mhandler);
        paintCanvas.addMouseMotionListener(mhandler);
    }
}
