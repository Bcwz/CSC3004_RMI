import java.util.ArrayList;
import com.mongodb.client.MongoCollection;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;

public interface SafeEntryUser extends java.rmi.Remote {	
    


    public void selfCheckIn(RMIClientIntf client) throws java.rmi.RemoteException;


    public void selfCheckOut(RMIClientIntf client) throws java.rmi.RemoteException;



}

