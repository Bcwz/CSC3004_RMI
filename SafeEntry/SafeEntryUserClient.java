/*
	Code: Safe Entry User Client	
	
	Simple user client program that remotely calls a set of trace together
	methods available on the remote SafeEntryUserImpl class
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException; //Import the MalformedURLException class so you can catch it
import java.rmi.Naming; //Import the rmi naming - so you can lookup remote object
import java.rmi.NotBoundException; //Import the NotBoundException class so you can catch it
import java.rmi.RemoteException; //Import the RemoteException class so you can catch it
import java.util.ArrayList;
import java.util.Scanner;

import classes.FamilyMembers;
import classes.Transactions;
import classes.Users;

public class SafeEntryUserClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf {

	final static Scanner cc = new Scanner(System.in);
	final static String nricRegex = "^[STFG]\\d{7}[A-Z]$";

	public SafeEntryUserClient() throws RemoteException {
	}

	public void callBack(String s) throws java.rmi.RemoteException {
		System.out.println("\nServer callback message: " + s + "\n");
	}

	public static void officerCallBackClient(String message) {
		System.out.println(message);

	}

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		String reg_host = "localhost";
		int reg_port = 1099;

		if (args.length == 1) {
			reg_port = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			reg_host = args[0];
			reg_port = Integer.parseInt(args[1]);
		}

		try {

			// Creation of client object
			SafeEntryUserClient SEC = new SafeEntryUserClient();

			// Create the reference to the remote object through the RMIregistry. Remote methods are called through the SEUser obj
			SafeEntryUser SEUser = (SafeEntryUser) Naming
					.lookup("rmi://" + reg_host + ":" + reg_port + "/SafeEntryService");

			// User input variables and the objects
			int choice = 0;
			String clientName = "", clientNRIC = "", clientLocation = "", familyMemberName = "", familyMemberNRIC = "";
			Transactions transactionObject = new Transactions();
			FamilyMembers FamilyMemberObject = new FamilyMembers();
			ArrayList<FamilyMembers> familyMembersList = new ArrayList<FamilyMembers>();
			ArrayList<Transactions> familyTransactionList = new ArrayList<Transactions>();
			

			// Starting the application, reads user inputs
			System.out.println("~~~~~~~~~~~~~~~~ Starting TraceTogether ~~~~~~~~~~~~~~~~ ");
			System.out.print("\nEnter name: ");
			clientName = cc.nextLine();
			System.out.print("Enter NRIC: ");
			clientNRIC = cc.nextLine();
			while (!clientNRIC.matches(nricRegex)) {
				System.out.print("Wrong NRIC format. Please try again. Enter NRIC: ");
				clientNRIC = cc.nextLine();
			}
			System.out.print("Enter location: ");
			clientLocation = cc.nextLine();
		
			while (true) {

				System.out.println(
						"\n\nEnter 1 for Self Check-in\nEnter 2 for Self Check-out\nEnter 3 for Group Check-in\nEnter 4 for Group Check-out\nEnter 5 to view history\nEnter 6 to add new family member\nEnter 7 to delete existing family member\nEnter 8 to exit");
				choice = cc.nextInt();

				switch (choice) {
				case 1: // Performs a self check-in. selfCheckIn() invocation method called

					System.out.println("\nSelf Check-in selected\n");
					transactionObject = new Transactions(clientName, clientNRIC, clientLocation);
					SEUser.selfCheckIn(SEC, transactionObject);
					break;
					
				case 2: // Performs a self check-out. selfCheckOut() invocation method called

					System.out.println("\nSelf Check-out selected\n");
					transactionObject = new Transactions(clientName, clientNRIC, clientLocation);
					SEUser.selfCheckOut(SEC, transactionObject);
					break;

				case 3: // Performs a group check-in. groupCheckIn() invocation method called
					
					System.out.println("\nGroup Check-in selected\n");
					
					// Check if the familyMembersList is empty
					if (familyMembersList.isEmpty()) {
						System.out.println("No family member found. Please add one family member.");
					} else {
						
						// Create Transactions objects based on family member list
						familyTransactionList.removeAll(familyTransactionList);
						for (int counter = 0; counter < familyMembersList.size(); counter++) {
							familyTransactionList.add(new Transactions(familyMembersList.get(counter).getName(),
									familyMembersList.get(counter).getNric(), clientLocation));
						}

						SEUser.groupCheckIn(SEC, familyTransactionList);
					}
					break;

				case 4: // Performs a group check-out. groupCheckOut() invocation method called
					
					System.out.println("\nGroup Check-out selected\n");
					
					// Check if the familyMembersList is empty
					if (familyMembersList.isEmpty()) {
						System.out.println("No family member found. Please add one family member.");
					} else {
						SEUser.groupCheckOut(SEC, familyTransactionList);
					}

					break;

				case 5: // View user's history. viewHistory() invocation method called

					System.out.println("\nView history selected\n");
					SEUser.viewHistory(SEC, new Users(clientName, clientNRIC));
					break;

				case 6: // Add family members to the familyMembersList. Does not perform any invocation methods
					
					cc.nextLine();
					familyMemberNRIC = "";
					System.out.print("\nAdd new family member\nEnter name: ");
					familyMemberName = cc.nextLine();

					while (!familyMemberNRIC.matches(nricRegex)) {
						System.out.print("Enter NRIC: ");
						familyMemberNRIC = cc.nextLine();
					}
					FamilyMemberObject = new FamilyMembers(familyMemberName, familyMemberNRIC, clientName);

					if (familyMembersList.contains(FamilyMemberObject)) {
						System.out.println("Record exist, please try again.");
					} else {
						familyMembersList.add(FamilyMemberObject);
						System.out.println("Family member added sucessfully");
					}
					break;
					
				case 7:  // Remove family members from the familyMembersList. Does not perform any invocation methods
					
					System.out.println("\nDelete family member selected");
					if (familyMembersList.isEmpty()) {
						System.out.println("No family member found!");
					} else {
						for (int counter = 0; counter < familyMembersList.size(); counter++) {
							System.out.println("\n----------------Record " + (counter + 1) + "------------------");
							System.out.println("Name: " + familyMembersList.get(counter).getName());
							System.out.println("NRIC: " + familyMembersList.get(counter).getNric());
						}
						System.out.print("Select record to delete: ");
						int record = cc.nextInt();

						familyMembersList.remove(record - 1);
						System.out.println("Family member deleted sucessfully");
					}
					break;

				case 8:  // Exit the client program
					System.out.println("Exiting");
					System.exit(0);

				default:
					System.out.println("Invalid choice");
				}

			}
		}

		catch (MalformedURLException murle) {
			System.out.println();
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (java.lang.ArithmeticException ae) {
			System.out.println();
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		}
	}
}
