package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket = null;
        int port = Integer.parseInt(argv[0]);
        try {
            // Create a server socket
            serverSocket = new ServerSocket(port);
            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket channel =  serverSocket.accept();
                // Set the input channel
                BufferedReader sInput = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                // Set the output channel
                PrintWriter sOutput = new PrintWriter(channel.getOutputStream(), true);
                // Receive the client message
                String received = sInput.readLine();
                System.out.println("SERVER: Received " + received + " from " + channel.getInetAddress().toString() + ":" + channel.getPort());
                // Send response to the client
                sOutput.println(received);
                System.out.println("SERVER: Sending " + received + " to " + channel.getInetAddress().toString() + ":" + channel.getPort());
                // Close the streams
                sInput.close();
                sOutput.close();
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
