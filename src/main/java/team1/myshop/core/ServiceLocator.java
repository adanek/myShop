package team1.myshop.core;

import data.handler.DataHandler;
import data.handler.IDataHandler;

/**
 * Created by adanek on 20.11.2015.
 */
public class ServiceLocator {

    private static IDataHandler _dataHandler;
    private static String lock = "This is my lock object for syncronized blocks";

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
