import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SafeEntryOfficerClient extends java.rmi.server.UnicastRemoteObject implements RMIClientIntf{

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
