/*
	Code: Calculator client		calculatorClient.java
	Date: 10th October 2000

	Simple client program that remotely calls a set of arithmetic
	methods available on the remote calculatorimpl object

*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException; //Import the MalformedURLException class so you can catch it
import java.rmi.Naming; //Import the rmi naming - so you can lookup remote object
import java.rmi.NotBoundException; //Import the NotBoundException class so you can catch it
import java.rmi.RemoteException; //Import the RemoteException class so you can catch it
import java.util.Scanner;

import classes.Transactions;
import classes.Users;

public class SafeEntryUserClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf {

	final static Scanner cc = new Scanner(System.in);
	final static String nricRegex = "^[STFG]\\d{7}[A-Z]$";
	final static String dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";

	public SafeEntryUserClient() throws RemoteException {

	}

	public void callBack(String s) throws java.rmi.RemoteException {

		System.out.println("callback:" + s);
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

			SafeEntryUserClient SEC = new SafeEntryUserClient();

			// Create the reference to the remote object through the remiregistry
			SafeEntryUser SEUser = (SafeEntryUser) // Naming.lookup("rmi://localhost/CalculatorService");
			Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/CalculatorService");

			boolean logout = false;
			int choice = 0;
			String message, name, nric, location;
			Transactions transactionObject = new Transactions();

			while (true) {
				System.out.println("~~~~~~~~~~~~~~~~ Starting TraceTogether ~~~~~~~~~~~~~~~~ ");
				System.out.println(
						"\n\nEnter 1 for Self Check-in\nEnter 2 for Self Check-out\nEnter 3 for Group Check-in\nEnter 4 for Group Check-out\nEnter 5 to view history\nEnter 6 to view possible exposure\nEnter 7 to add new family member\nEnter 8 to delete existing family member\nEnter 9 to exit");
				choice = cc.nextInt();
				switch (choice) {
				case 1:
					System.out.println("\nSelf Check-in selected!\n");
					cc.nextLine();
					System.out.print("\nEnter name: ");
					name = cc.nextLine();
					nric = "";
					while (!nric.matches(nricRegex)) {
						System.out.print("Enter NRIC: ");
						nric = cc.nextLine();
					}
					System.out.print("\nEnter location: ");
					location = cc.nextLine();

					transactionObject = new Transactions(name, nric, location);

					SEUser.selfCheckIn(SEC, transactionObject);

					break;
				case 2:

					System.out.println("\nSelf Check-out selected!\n");
					cc.nextLine();
					System.out.print("\nEnter name: ");
					name = cc.nextLine();
					nric = "";
					while (!nric.matches(nricRegex)) {
						System.out.print("Enter NRIC: ");
						nric = cc.nextLine();
					}
					System.out.print("\nEnter location: ");
					location = cc.nextLine();

					transactionObject = new Transactions(name, nric, location);

					SEUser.selfCheckOut(SEC, transactionObject);

					break;

				case 3:
					SEUser.groupCheckIn(SEC, "S555555G");

				case 4:
					SEUser.groupCheckOut(SEC, "S9999999G");

				default:
					break;
				}

				/*
				 * SEUser.selfCheckIn(SEC); SEUser.selfCheckOut(SEC);
				 */
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
