package es.udc.redes.tutorial.info;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class Info {
    public static void main(String[] args){

        if(args.length < 1){
            System.out.println("Enter a path\n");
            System.exit(-1);
        }

        try{
               Path path = Paths.get(args[0]);
               path = path.toRealPath();
               BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
               System.out.println("Size: " + attr.size());
               System.out.println("Last modification date: " + attr.lastModifiedTime() );
               System.out.println("Name: " + path.getFileName());
               System.out.println("File type/Extension: " + Files.probeContentType(path));
               System.out.println("Absolute path: " + path);

        }catch (IOException e){

            System.out.println("Exception " + e.getMessage() + "\n");
        }
    }
    
}
