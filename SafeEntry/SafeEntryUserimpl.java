/*
	Code: CalculatorImpl remote object	calculatorimpl.java
	Date: 10th October 2000

	Contains the arithmetic methods that can be remotley invoked
*/

// The implementation Class must implement the rmi interface (calculator)
// and be set as a Remote object on a server
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;

import com.mongodb.client.MongoCollection;

public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser {

	private RMIClientIntf c;

    // Implementations must have an explicit constructor
    // in order to declare the RemoteException exception

    public SafeEntryUserimpl() throws java.rmi.RemoteException {
        super();
    }

    // Implementation of the add method
    // The two long parameters are added added and the result is retured
    public void selfCheckIn(RMIClientIntf client, String NRIC) throws java.rmi.RemoteException {
    	
		    c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					//Thread.sleep(timer);
					c.callBack("Check-in SUCCESS. NRIC : " + NRIC);
					
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
			});
		thread.start();
		return;
    }

    // Subtract the second parameter from the first and return the result
    public void selfCheckOut(RMIClientIntf client, String NRIC)
        throws java.rmi.RemoteException {
     
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					//Thread.sleep(timer);
					
					c.callBack("Check-out SUCCESS. NRIC = " + NRIC);
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
			});
		thread.start();
	return;
    }
}
