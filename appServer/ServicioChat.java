
import java.rmi.*;

public interface ServicioChat extends Remote {
    void alta(Cliente c) throws RemoteException;
    void baja(Cliente c) throws RemoteException;
    void envio(Cliente e, String apodo, String m) throws RemoteException;
}
