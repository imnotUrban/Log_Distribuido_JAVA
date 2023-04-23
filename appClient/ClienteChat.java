import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;


class ClienteChat {

    private static final int PORT = 4002;

    static public void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Uso: ClienteChat nombreCliente");
            return;
        }

        try {
            //Sacamos la fecha y hora de la operación
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yy';'HH:mm:ss");
            String fechaHoraActualStr = fechaHoraActual.format(formato);
            String inicio_Op = "Inicio de operacion;"+args[0]+";"+fechaHoraActualStr;
            System.out.println(inicio_Op);
            String ruta = "log"+args[0]+".txt";

            //Abre el archivo log (TMB lo crea si es que no hay otro nuevo)
            File archivo = new File(ruta); //Crea archivo con los logs  

            PrintWriter writer = new PrintWriter(archivo);
            writer.println(inicio_Op);
            writer.close();


            
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            ServicioChat srv = (ServicioChat) registry.lookup("Chat");

            ClienteImpl c = new ClienteImpl();

            srv.alta(c);

            Scanner ent = new Scanner(System.in);

            String apodo = args[0];
            srv.envio(c, apodo, inicio_Op);
            System.out.println(apodo + " dice > ");
            String msg;
            String msgExit = "EXIT";


            int nCorrelativo = 1;
            while (ent.hasNextLine()) {

                msg = ent.nextLine();
                if (msg.equals(msgExit)) {
                    LocalDateTime fechaHoraActual_close = LocalDateTime.now();
                    DateTimeFormatter formato_close = DateTimeFormatter.ofPattern("dd-MM-yy';'HH:mm:ss");
                    String fechaHoraActualStr_close = fechaHoraActual_close.format(formato_close);
                    srv.envio(c, apodo, " Se ha cerrado el nodo: "+apodo+" a las: "+fechaHoraActualStr_close);
                    break;
                } else {

                    
                    //Sacamos la fecha y hora de la operación
                    LocalDateTime fechaHoraActual_1 = LocalDateTime.now();
                    DateTimeFormatter formato_1 = DateTimeFormatter.ofPattern("dd-MM-yy';'HH:mm:ss");
                    String fechaHoraActualStr_1 = fechaHoraActual_1.format(formato_1);
                    String string_envio = nCorrelativo+";"+fechaHoraActualStr_1+";"+apodo+";"+msg;
                    System.out.println(string_envio);
                    /**
                     * Re Escribe en el archivo
                     */
                    // Lee el archivo de texto
                    BufferedReader reader = new BufferedReader(new FileReader(ruta));
                    String contenido = "";
                    String linea = reader.readLine();
                    while (linea != null) {
                        contenido += linea + "\n";
                        linea = reader.readLine();
                    }
                    reader.close();

                    // Escribe en el archivo de texto
                    PrintWriter writer2 = new PrintWriter(new FileWriter(ruta));
                    writer2.println(string_envio);
                    writer2.print(contenido);
                    writer2.close();

                    System.out.println("Se escribio en el archivo.");


                    srv.envio(c, apodo, string_envio);
                }

                System.out.println(apodo + " dice > ");

            }
            srv.baja(c);
            System.exit(0);
        } catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        } catch (Exception e) {
            System.err.println("Excepcion en ClienteChat:");
            e.printStackTrace();
        }
    }
}
