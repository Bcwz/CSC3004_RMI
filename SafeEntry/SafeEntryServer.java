
import java.rmi.Naming;	//Import naming classes to bind to rmiregistry

public class SafeEntryServer {
	static int port = 1099;
   //calculatorserver constructor
   public SafeEntryServer() {
     
     //Construct a new CalculatorImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
       	SafeEntryUser SEU = new SafeEntryUserimpl();
       	Naming.rebind("rmi://localhost:" + port + "/CalculatorService", SEU);
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
