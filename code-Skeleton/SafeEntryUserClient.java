
import java.rmi.Naming;			//Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;	//Import the RemoteException class so you can catch it
import java.net.MalformedURLException;	//Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;	//Import the NotBoundException class so you can catch it
import java.util.Scanner;  // Import the Scanner class

public class SafeEntryUserClient {

    public static void main(String[] args)throws InterruptedException {
    	final Scanner scan = new Scanner(System.in);
    	final String nricRegex = "^[STFG]\\d{7}[A-Z]$";
    	final String dateTimeRegex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4} (2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$";
		/*
		 * //use localhost if running the server locally or use IP address of the server
		 * String reg_host = "localhost"; int reg_port = 1099;
		 * 
		 * if (args.length == 1) { reg_port = Integer.parseInt(args[0]); } else if
		 * (args.length == 2) { reg_host = args[0]; reg_port =
		 * Integer.parseInt(args[1]); }
		 */

	try {

	    // Create the reference to the remote object through the remiregistry
		SafeEntryUser SE = (SafeEntryUser)
				Naming.lookup("rmi://localhost/SafeEntryService");

		
				/* for testing
				 * System.out.println("Check in="+ SE.checkin("checkin"));
				 * System.out.println("Check out="+ SE.checkout("checkout"));
				 * System.out.println("Group Check in="+ SE.groupcheckin("groupcheckin"));
				 * System.out.println("Group Check out="+ SE.groupcheckout("groupcheckout"));
				 */
		
		while (true) {
			System.out.println("~~~~~~~~~~~~~~~~ SafeEntry Check-in selected ~~~~~~~~~~~~~~~~ ");
			System.out.println("Enter Name: ");
			String name = scan.nextLine();
			
			//Copy paste this Thread for all checkin/checkout
			new Thread(new Runnable() {
				public void run() {
					try {
						//call SafeEntry functions here
						System.out.println("Check in="+ SE.checkin(name));
						}
					catch (RemoteException e) {
						e.printStackTrace();
						}
					}}).start();
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
