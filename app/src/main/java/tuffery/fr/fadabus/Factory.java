package tuffery.fr.fadabus;

import java.text.SimpleDateFormat;
import java.util.Locale;

import tuffery.fr.fadabus.contract.ICommunicationManager;
import tuffery.fr.fadabus.contract.IDatabaseManager;
import tuffery.fr.fadabus.manager.Communication;
import tuffery.fr.fadabus.manager.Database;

/**
 * Created by Aurelien on 15/05/2017.
 */

public enum  Factory {
    instance;
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
    static Communication communication;
    static Database database;

    public ICommunicationManager getICommunicationManager(){
        if (communication == null){
            communication = Communication.instance;
        }
        return communication;
    }

    public IDatabaseManager getIDatabaseManager(){
        if (database == null){
            database = Database.instance;
        }
        return database;
    }
}
