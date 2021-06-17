
import java.io.File;
import java.rmi.Naming; //Import naming classes to bind to rmiregistry

public class SafeEntryServer {
	static int port = 1099;
	private static String infectedCSVPath = "./TraceTogetherInfectedLocations.csv";
	private static String transactionCSVPath = "./TraceTogetherTransaction.csv";

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
			
			File checkInfectedLocationCSV = new File(infectedCSVPath);
			File checkTraceCSV = new File(transactionCSVPath);
			boolean infectedExists = checkInfectedLocationCSV.exists();
			boolean TraceCSVExists = checkTraceCSV.exists();
			if(infectedExists == false || TraceCSVExists == false) {
				System.out.println("CSV files missing... \ncreating csv file....\n");
				File locationCSV = new File(infectedCSVPath);
				File TraceCSV = new File(transactionCSVPath);
				locationCSV.createNewFile();
				TraceCSV.createNewFile();
				System.out.println("CSV files created successfully.\n");
			}

		} catch (Exception e) {
			System.out.println("Server Error: " + e);
		}
	}


	public static void main(String args[]) {
		// Create the new Calculator server
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		new SafeEntryServer();
	}
}
