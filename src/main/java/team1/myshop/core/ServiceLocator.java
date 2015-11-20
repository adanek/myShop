package team1.myshop.core;

import data.handler.DataHandler;
import team1.myshop.contracts.IDataHandler;

public class ServiceLocator {

    private static IDataHandler _dataHandler;
    private static final String lock = "This is my lock object for syncronized blocks";

    public static IDataHandler getDataHandler(){
        if(_dataHandler == null){
            synchronized (lock){
                if(_dataHandler == null){
                    _dataHandler = new DataHandler();
                }
            }
        }

        return _dataHandler;
    }
}
