package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {

    public static void main(String[] args){
        if(args.length < 1){
            System.err.println("Format: java <path to the java file> port");
            System.exit(-1);
        }

        ServerSocket server = null;
        int port = Integer.parseInt(args[0]);

        try{

            server = new ServerSocket(port);

            while(true){
                Socket client = server.accept();

                ServerThread thread = new ServerThread(client);

                thread.start();
            }


        }catch (SocketTimeoutException e){
            System.err.println("Timeout exception");
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                assert server != null;
                server.close();
            }catch (IOException e){
                System.err.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
}
