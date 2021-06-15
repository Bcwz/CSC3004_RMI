import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SafeEntryOfficerClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf{
	final static Scanner cc = new Scanner(System.in);

	public SafeEntryOfficerClient() throws RemoteException {

	}
	

	@Override
	public void callBack(String s) throws java.rmi.RemoteException {

		System.out.println("callback:" + s);
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

			SafeEntryOfficerClient SEOC = new SafeEntryOfficerClient();

			// Create the reference to the remote object through the remiregistry
			SafeEntryOfficer SEOfficer = (SafeEntryOfficer) // Naming.lookup("rmi://localhost/CalculatorService");
			Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/OfficerService");
			System.out.println("OFFICER CLIENT SUCCESS");
			
			while(true) {
				System.out.println("Press 1 to see database, Press 2 to add new infected location");
				int choice = cc.nextInt();
				switch(choice) {
				case 1:
					Path fileName = Path.of("C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt");
					try {
						String actual = Files.readString(fileName);
						System.out.println(actual);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					break;
				case 2:
					System.out.println("Adding new infected location");
					try {
						SEOfficer.addLocation("JURONG POINT");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;

				default:
					break;
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
