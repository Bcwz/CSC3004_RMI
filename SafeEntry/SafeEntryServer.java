
import java.rmi.Naming; //Import naming classes to bind to rmiregistry
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SafeEntryServer {
	private ArrayList<String> clientListener = new ArrayList<String>();
	private RMIClientIntf c;
	static int port = 1099;

	// SafeEntryServer constructor
	public SafeEntryServer() {

		// Construct a new CalculatorImpl object and bind it to the local rmiregistry
		// N.b. it is possible to host multiple objects on a server by repeating the
		// following method.

		try {
			// For SafeEntry Normal users

			SafeEntryUser SEU = new SafeEntryUserImpl();
			Naming.rebind("rmi://localhost:" + port + "/SafeEntryService", SEU);

			// For SafeEntryOfficer
			SafeEntryOfficer SEO = new SafeEntryOfficerImpl();
			Naming.rebind("rmi://localhost:" + port + "/OfficerService", SEO);

			System.out.println("Server started!");

		} catch (Exception e) {
			System.out.println("Server Error: " + e);
		}
	}

	// Use to notify listener
	private void addListener(String listenerNRIC) throws RemoteException {
		clientListener.add(listenerNRIC);
	}

	private void removeListener(String listenerNRIC) throws RemoteException {
		clientListener.remove(listenerNRIC);
	}

	private void notifyListener(RMIClientIntf client, String NRIC) throws RemoteException {

	}

	public static void main(String args[]) {
		// Create the new Calculator server
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		new SafeEntryServer();
	}
}
