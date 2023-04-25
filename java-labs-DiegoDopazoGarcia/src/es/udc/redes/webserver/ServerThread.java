package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    private static void sendOkResponse(PrintWriter writer, Date sendDate, String serverName, File file, Socket socket, String method) throws IOException {
        writer.println("HTTP/1.0 200 OK");
        writer.println("Date: " + sendDate.toString());
        writer.println("Server: " + serverName);
        writer.println("Content-Length: " + file.length());
        writer.println("Content-Type: " + Files.probeContentType(file.toPath()));
        writer.println("Last-Modified: " + file.lastModified());
        writer.println("");
        if(method.equals("GET"))
            sendFile(socket, file);
    }

    private static void sendFile(Socket socket, File file) throws IOException {
        // Open a FileInputStream to read the file
        FileInputStream Istream = new FileInputStream(file);

        // Open an OutputStream to write to the socket
        OutputStream os = socket.getOutputStream();

        // Create a buffer to hold the file contents
        byte[] buffer = new byte[4096];

        // Read the file contents and send them to the receiver
        int bytesRead;
        while ((bytesRead = Istream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        // Close the streams
        Istream.close();
        os.close();
    }

    public void run() {
        try {
          // This code processes HTTP requests and generates 
          // HTTP responses
          // Uncomment next catch clause after implementing the logic
          BufferedReader sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
          String inputLine;
          String [] headers;
          HashMap<String, String> headersContent = new HashMap<>();
          String method, fileName;
          String serverName = "DiegoDopazoServer";

          if((inputLine = sInput.readLine()) != null){
              headers = inputLine.split(" ");
              method = headers[0];
              fileName = headers[1];
          }else{
              throw new RuntimeException();
          }

          File file = new File("p1-files" + File.separator + fileName);

          while((inputLine = sInput.readLine()) != null){
            if(inputLine.isEmpty()) {
                break;
            }

            headers = inputLine.split(":");
            headersContent.put(headers[0].trim(), headers[1].trim());
          }

        if(!method.equals("GET") && !method.equals("HEAD")){
            Date sendDate = new Date();
            file = new File("p1-files/error400.html");
            writer.println("HTTP/1.0 400 Bad Request");
            writer.println("Content-Type: " + Files.probeContentType(file.toPath()));
            writer.println("Content-Length: " + file.length());
            writer.println("Date: " + sendDate);
            writer.println(serverName);
            writer.println("");
            sendFile(socket, file);
        }else if (!file.exists()){
            Date sendDate = new Date();
            file = new File("p1-files/error404.html");
            writer.println("HTTP/1.0 404 Not Found");
            writer.println("Content-Type: " + Files.probeContentType(file.toPath()));
            writer.println("Content-Length: " + file.length());
            writer.println("Date: " + sendDate);
            writer.println(serverName);
            writer.println("");
            if(method.equals("GET"))
                sendFile(socket, file);
        }else{
            Date lastModificationDate = new Date(file.lastModified());
            Date sendDate = new Date();
            if(headersContent.get("If-Modified-Since") != null){

                // Parse the date string into a Date object
                Date modifiedSince = new Date(Long.parseLong(headersContent.get("If-Modified-Since")));
                // Compare the two dates using the compareTo() method
                int result = modifiedSince.compareTo(lastModificationDate);

                if(result >= 0){
                    //the If-Modified-Date is later than the last modification date of the file
                    writer.println("HTTP/1.0 304 Not modified");
                    writer.println(serverName);
                    writer.println("");

                }else{
                    sendOkResponse(writer, sendDate, serverName, file, socket, method);
                }
            }else{
                sendOkResponse(writer, sendDate, serverName, file, socket, method);
            }

        }

          sInput.close();
          writer.close();
         } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
