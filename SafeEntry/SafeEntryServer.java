
import java.rmi.Naming;	//Import naming classes to bind to rmiregistry

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;

public class SafeEntryServer {
	static int port = 1099;
   //SafeEntryServer constructor
   public SafeEntryServer() {
     
     //Construct a new CalculatorImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
    	 SafeEntryUser SEU = new SafeEntryUserimpl();
    	 Naming.rebind("rmi://localhost:" + port + "/CalculatorService", SEU);
    	 
    	 // Disable logging for MongoDB, except for SEVERE warnings
    	 Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
    	 mongoLogger.setLevel(Level.SEVERE);
    	 
    	 // Create connection string and configurations for MongoDB
    	 ConnectionString connectionString = new ConnectionString("mongodb+srv://root:gideon97@mycluster2.j0hmg.mongodb.net/test?retryWrites=true&w=majority");
    	 CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
    	 CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),pojoCodecRegistry);
    	 MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry).build();
    	 
    	 // Connect to the database
    	 com.mongodb.client.MongoClient mongoClient = MongoClients.create(clientSettings);
    	 MongoDatabase db = mongoClient.getDatabase("SafeEntry");
    	 MongoCollection<Users> usersCollection = db.getCollection("Users", Users.class);
    	 MongoCollection<Transactions> TransactionsCollection = db.getCollection("Transactions", Transactions.class);
    	 MongoCollection<InfectedLocations> infectedLocationsCollection = db.getCollection("InfectedLocations",InfectedLocations.class);
    	 MongoCollection<FamilyMembers> FamilyMembersCollection = db.getCollection("FamilyMembers",FamilyMembers.class);
     

    	 // Creation of objects
    	 Transactions newTransaction = new Transactions();
    	 InfectedLocations infectedLocation = new InfectedLocations();
    	 FamilyMembers familyMember = new FamilyMembers();
    	 // Creation of ArrayList to store objects
    	 ArrayList<Transactions> transactionList = new ArrayList<Transactions>();
    	 ArrayList<FamilyMembers> familyMembersList = new ArrayList<FamilyMembers>();
    	 ArrayList<Transactions> familyTransList = new ArrayList<Transactions>();
    	 ArrayList<InfectedLocations> infectedLocationList = new ArrayList<InfectedLocations>();
       	
     } 
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }

   public static void main(String args[]) {
     	//Create the new Calculator server
	if (args.length == 1)
		port = Integer.parseInt(args[0]);
	
	new SafeEntryServer();
   }
}
