/*
	Code: Calculator client		calculatorClient.java
	Date: 10th October 2000

	Simple client program that remotely calls a set of arithmetic
	methods available on the remote calculatorimpl object

*/

import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;

public class SafeEntryUserClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf {
	
	final static Scanner cc = new Scanner(System.in);
	final static String nricRegex = "^[STFG]\\d{7}[A-Z]$";
	final static String dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";

	public SafeEntryUserClient() throws RemoteException{
		
	}
	
	public void callBack(String s) throws java.rmi.RemoteException {
	
		System.out.println("callback:" + s);
	}
	
	public static Users createNewUserDialogue() {
		System.out.println("~~~~~~~~~~~~~~~~ Registration selected ~~~~~~~~~~~~~~~~ ");
		cc.nextLine();
		System.out.print("Enter Name: ");
		String nric = "";
		String name = cc.nextLine();
		while (!nric.matches(nricRegex)) {
			System.out.print("Enter NRIC: ");
			nric = cc.nextLine();
		}
		System.out.print("Enter Password: ");
		String password = cc.nextLine();
		System.out.print("Officer (Y/N): ");
		String officer = cc.nextLine();
		Users newUser = new Users();
		if (officer.equals("Y")) {
			newUser = new Users(name, nric, "Officer", password);
		} else {
			newUser = new Users(name, nric, password);
		}
		return newUser;
	}

	// Display login dialogue
	public static Users loginDialogue() {
		String nric = cc.nextLine();
		System.out.println("~~~~~~~~~~~~~~~~ Login selected ~~~~~~~~~~~~~~~~ ");
		System.out.print("Enter NRIC: ");
		nric = cc.nextLine();
		System.out.print("Enter Password: ");
		String password = cc.nextLine();
		Users loginUser = new Users(nric, password);
		return loginUser;
	}	
	
    public static void main(String[] args) {
        
       String reg_host = "localhost";
       int reg_port = 1099;
       
       if (args.length == 1) {
       	reg_port = Integer.parseInt(args[0]);
      } else if (args.length == 2) {
      	reg_host = args[0];
      	reg_port = Integer.parseInt(args[1]);
      }
        
	try {

		SafeEntryUserClient SEC = new SafeEntryUserClient();
		
	    // Create the reference to the remote object through the remiregistry			
        SafeEntryUser SEUser = (SafeEntryUser)//Naming.lookup("rmi://localhost/CalculatorService");
        Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/CalculatorService");
            
		/*
		 *  Now use the reference c to call remote methods
		 *  SEU.selfCheckIn(SEC);
		 *  SEU.selfCheckOut(SEC);
		 */
        boolean logout = false;
		int choice = 0;
		String message;

		System.out.print("~~~~~~~~~~~~~~~~ Starting TraceTogether ~~~~~~~~~~~~~~~~ ");
        while(true) {
        	System.out.println("\n\nSelect 1 for registration\nSelect 2 for login\nSelect 3 to exit\n");
			choice = cc.nextInt();
			SEUser.selfCheckIn(SEC);
			SEUser.selfCheckOut(SEC);
        }
	}

	catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }
    }
}

