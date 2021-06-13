import java.rmi.*;
	
public interface RMIClientIntf extends Remote {

	public void callBack(String string) throws java.rmi.RemoteException;

}