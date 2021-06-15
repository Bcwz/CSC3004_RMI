import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface SafeEntryServerimpl extends Remote {

	void addListener(String listener) throws RemoteException;

	void removeListener(String listener) throws RemoteException;

}
