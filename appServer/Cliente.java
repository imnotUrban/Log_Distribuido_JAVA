
import java.rmi.*;

interface Cliente extends Remote {
    void notificacion(String apodo, String m) throws RemoteException;
}
