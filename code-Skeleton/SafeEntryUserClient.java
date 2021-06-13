
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it
import java.util.Scanner;  // Import the Scanner class
import java.util.ArrayList;
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

public class SafeEntryUserClient {
	
	final static Scanner scan = new Scanner(System.in);
	final static String nricRegex = "^[STFG]\\d{7}[A-Z]$";
	final String dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";
	
	public static Users createNewUserDialogue() {
		System.out.println("~~~~~~~~~~~~~~~~ Registration selected ~~~~~~~~~~~~~~~~ ");
		scan.nextLine();
		System.out.print("Enter Name: ");
		String nric = "";
		String name = scan.nextLine();
		while (!nric.matches(nricRegex)) {
			System.out.print("Enter NRIC: ");
			nric = scan.nextLine();
		}
		System.out.print("Enter Password: ");
		String password = scan.nextLine();
		System.out.print("Officer (Y/N): ");
		String officer = scan.nextLine();
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
		String nric = scan.nextLine();
		System.out.println("~~~~~~~~~~~~~~~~ Login selected ~~~~~~~~~~~~~~~~ ");
		System.out.print("Enter NRIC: ");
		nric = scan.nextLine();
		System.out.print("Enter Password: ");
		String password = scan.nextLine();
		Users loginUser = new Users(nric, password);
		return loginUser;
	}
		

    public static void main(String[] args)throws InterruptedException {
    	
	try {
		
		// Disable logging for MongoDB, except for SEVERE warnings
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);

		// Create connection string and configurations for MongoDB
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://root:gideon97@mycluster2.j0hmg.mongodb.net/test?retryWrites=true&w=majority");
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),pojoCodecRegistry);
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).build();

		// Connect to the database
		com.mongodb.client.MongoClient mongoClient = MongoClients.create(clientSettings);
		MongoDatabase db = mongoClient.getDatabase("SafeEntry");
		MongoCollection<Users> usersCollection = db.getCollection("Users", Users.class);
		MongoCollection<Transactions> TransactionsCollection = db.getCollection("Transactions", Transactions.class);
		MongoCollection<InfectedLocations> infectedLocationsCollection = db.getCollection("InfectedLocations",InfectedLocations.class);
		MongoCollection<FamilyMembers> FamilyMembersCollection = db.getCollection("FamilyMembers",FamilyMembers.class);

		// Creation of the reference object to the remote object through the RMIRegistry
		SafeEntryUser SEUser = (SafeEntryUser) Naming.lookup("rmi://localhost/SafeEntryService");

		// Creation of objects
		Transactions newTransaction = new Transactions();
		InfectedLocations infectedLocation = new InfectedLocations();
		FamilyMembers familyMember = new FamilyMembers();

		// Creation of ArrayList to store objects
		ArrayList<Transactions> transactionList = new ArrayList<Transactions>();
		ArrayList<FamilyMembers> familyMembersList = new ArrayList<FamilyMembers>();
		ArrayList<Transactions> familyTransList = new ArrayList<Transactions>();
		ArrayList<InfectedLocations> infectedLocationList = new ArrayList<InfectedLocations>();

		// Creation of user input
		boolean logout = false;
		int choice = 0;
		String message;
		System.out.print("~~~~~~~~~~~~~~~~ Starting TraceTogether ~~~~~~~~~~~~~~~~ ");
		
		
		
		
		while (true) {
			System.out.println("\n\nSelect 1 for registration\nSelect 2 for login\nSelect 3 to exit\n");
			choice = scan.nextInt();
			switch (choice) {
			case 1: //Use Thread to process method for concurrency & multithreading
				new Thread(new Runnable() {
					public void run() {
						try {
							// Creation of new user
							Users newUser = createNewUserDialogue();
							if (SEUser.checkExistingUser(newUser, usersCollection)) {
								System.out.println("Existing user. Please login.");

							} else {
								String message = SEUser.createAccount(newUser, usersCollection) ? "User registration success."
										: "User registration failure. Please try again";
								System.out.println(message);
								}
							}
						catch (RemoteException e) {
							e.printStackTrace();
							}
						}}).start();
				
			case 2://Use Thread to process method for concurrency & multithreading
				new Thread(new Runnable() {
					public void run() {
						System.out.println("CASE 2 - TESTING");
						}}).start();
				
			case 3://Use Thread to process method for concurrency & multithreading
				new Thread(new Runnable() {
					public void run() {
						// Restarting or exiting the program
						if (logout) {
							boolean logout = false;
						} else {
							System.out.println("Exiting");
							System.exit(0);
						}
						}}).start();
				
			default:
				System.out.println("Invalid choice");
			}
			
/*			//Copy paste this Thread for all checkin/checkout
			
			new Thread(new Runnable() {
				public void run() {
					try {
						
						}
					catch (RemoteException e) {
						e.printStackTrace();
						}
					}}).start();*/
			
		}
		
	}
  // Catch the exceptions that may occur - rubbish URL, Remote exception
	// Not bound exception or the arithmetic exception that may occur in
	// one of the methods creates an arithmetic error (e.g. divide by zero)
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
