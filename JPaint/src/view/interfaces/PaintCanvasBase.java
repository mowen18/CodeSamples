package view.interfaces;

import javax.swing.*;

import model.interfaces.IApplicationState;

import java.awt.*;

public abstract class PaintCanvasBase extends JComponent {
    public abstract Graphics2D getGraphics2D();
}
