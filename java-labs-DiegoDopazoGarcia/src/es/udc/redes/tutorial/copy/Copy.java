package es.udc.redes.tutorial.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) {

        if(args.length < 2){
            System.out.println("Not enough parameters\n");
            System.exit(-1);
        }

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(args[0]);
            out = new FileOutputStream(args[1]);
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } catch(IOException e){
            System.out.println("IO exception: " + e.getMessage());
        }finally {
            //closes the streams
            try{
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }catch(IOException e){//catches any possible exception while closing the streams
                System.out.println("IO exception: " + e.getMessage());
            }
        }

    }
    
}
