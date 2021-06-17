/*
	Code: Safe Entry Officer Client	
	
	Simple officer client program that remotely calls a set of officer's
	methods available on the remote SafeEntryOfficerImpl class
*/

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import classes.InfectedLocations;

@SuppressWarnings("serial")
public class SafeEntryOfficerClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf {

	final static Scanner cc = new Scanner(System.in);
	final static String dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";

	public SafeEntryOfficerClient() throws RemoteException {
	}

	@Override
	public void callBack(String s) throws java.rmi.RemoteException {
		System.out.println("\nServer callback message: " + s + "\n");
	}

	public static void main(String[] args) throws IOException {

		String reg_host = "localhost";
		int reg_port = 1099;

		if (args.length == 1) {
			reg_port = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			reg_host = args[0];
			reg_port = Integer.parseInt(args[1]);
		}

		// User input variable and InfectedLocations object
		int choice = 0;
		InfectedLocations infectedLocationObj = new InfectedLocations();

		try {

			// Creation of client object
			SafeEntryOfficerClient SEOC = new SafeEntryOfficerClient();

			// Create the reference to the remote object through the RMIregistry. Remote
			// methods are called through the SEOfficer obj
			SafeEntryOfficer SEOfficer = (SafeEntryOfficer) Naming
					.lookup("rmi://" + reg_host + ":" + reg_port + "/OfficerService");

			System.out.println("~~~~~~~~~~~~~~~~ Starting Officer TraceTogether ~~~~~~~~~~~~~~~~ ");

			while (true) {
				System.out.println(
						"\nEnter 1 to view infected locations\nEnter 2 to declare infected COVID location\nEnter 3 to exit");
				choice = cc.nextInt();

				switch (choice) {
				case 1: // View infected locations. retrieveAllInfectedLocations() invocation method called
					
					System.out.println("View infected location");
					SEOfficer.retrieveAllInfectedLocations(SEOC);
					break;

				case 2: // Add infected location to the records. addLocationDoes() invocation method called. notifyClient() method is called to notify all connected clients.
					
					System.out.println("Declare infected COVID location");
					cc.nextLine();
					System.out.print("\nEnter location visited by COVID-19 patient: ");
					String location = cc.nextLine();
					String checkInTime = "";
					String checkInOut = "";

					//Validation
					while (!checkInTime.matches(dateTimeRegex)) {
						System.out.print("Enter check-in time(dd/MM/yyyy HH:mm:ss): ");
						checkInTime = cc.nextLine();
					}
					while (!checkInOut.matches(dateTimeRegex)) {
						System.out.print("Enter check-out time(dd/MM/yyyy HH:mm:ss): ");
						checkInOut = cc.nextLine();
					}
					infectedLocationObj = new InfectedLocations(location, checkInTime, checkInOut);
					SEOfficer.addLocation(SEOC, infectedLocationObj);
					SEOfficer.notifyClient(SEOC);
					break;

				case 3: // Exit the client program
					
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
