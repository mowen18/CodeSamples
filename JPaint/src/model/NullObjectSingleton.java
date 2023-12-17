package model;

import model.interfaces.IShadeDesignStrategy;
import model.interfaces.IStartCommand;


public class NullObjectSingleton implements IShadeDesignStrategy, IStartCommand {
    private static final NullObjectSingleton instance = new NullObjectSingleton();
    private NullObjectSingleton(){}
    public static NullObjectSingleton getInstance(){
        return instance;
    }

    @Override
    public void createShade() {

    }

    @Override
    public void doCommand() {

    }
}
