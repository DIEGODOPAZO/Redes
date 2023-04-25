package es.udc.redes.tutorial.udp.server;

import java.net.*;
import java.util.Arrays;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket server = null;
        try {
            // Create a server socket
            server = new DatagramSocket(Integer.parseInt(argv[0]));;
            String data, addr;
            InetAddress address = null;
            int port;

            // Set maximum timeout to 300 secs
            server.setSoTimeout(300000);

            while (true) {
                // Prepare datagram for reception
                byte array[] = new byte[1024];
                DatagramPacket dgramRec = new DatagramPacket(array, array.length);

                // Receive the message
                server.receive(dgramRec);
                data = new String(dgramRec.getData());
                addr = dgramRec.getSocketAddress().toString();
                addr = addr.substring(addr.indexOf('/') + 1, addr.indexOf(':'));

                address = InetAddress.getByName(addr);
                port = dgramRec.getPort();
                System.out.println("SERVER: Received " + data + " from " + dgramRec.getAddress().toString() + ":" + port);

                // Prepare datagram to send response
                DatagramPacket dsend = new DatagramPacket(data.getBytes(), data.getBytes().length, address, port);
                // Send response
                server.send(dsend);
                System.out.println("SERVER: Sending " + data + " to " +  dgramRec.getAddress().toString() + ":" + port);


            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
           System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            server.close();
        }
    }
}
