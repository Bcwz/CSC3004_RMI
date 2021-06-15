
import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SafeEntryServer {
	private ArrayList<String> clientListener = new ArrayList();
	private RMIClientIntf c;
	static int port = 1099;
   //SafeEntryServer constructor
   public SafeEntryServer() {
     
     //Construct a new CalculatorImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
    	 //For SafeEntry Normal users
    	 SafeEntryUser SEU = new SafeEntryUserimpl();
    	 Naming.rebind("rmi://localhost:" + port + "/CalculatorService", SEU);
    	 
    	 //For SafeEntryOfficer
    	 SafeEntryOfficer SEO = new SafeEntryOfficerimpl();
    	 Naming.rebind("rmi://localhost:" + port + "/OfficerService", SEO);
			
     } 
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }
   
   
   
   //Use to notify listener
   private void addListener (String listener) throws RemoteException{
	   clientListener.add(listener);
   }
   
   private void removeListener (String listener) throws RemoteException{
	   clientListener.remove(listener);
   }
   
   
   

   public static void main(String args[]) {
     	//Create the new Calculator server
	if (args.length == 1)
		port = Integer.parseInt(args[0]);
	
	new SafeEntryServer();
   }
}
