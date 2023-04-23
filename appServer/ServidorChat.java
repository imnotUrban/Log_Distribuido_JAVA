
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


class ServidorChat  {

    private static final int PORT = 4002;
    static public void main (String args[]) {

        System.setProperty("java.rmi.server.hostname","127.0.0.1");

        try {

            ServicioChatImpl srv = new ServicioChatImpl();
            ServicioChat stub =(ServicioChat) UnicastRemoteObject.exportObject(srv,0);

            Registry registry = LocateRegistry.getRegistry("127.0.0.1",PORT);

            System.out.println("Servidor escuchando en el puerto " + String.valueOf(PORT));

            registry.bind("Chat", stub);
        }

        catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
