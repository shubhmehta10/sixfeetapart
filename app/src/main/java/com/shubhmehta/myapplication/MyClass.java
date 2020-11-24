package com.shubhmehta.myapplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyClass {
    public void printvariableToFile (String fileName, String myVariable) {
        File myFile;
        myFile=new File("inputfile.txt");
        if(!myFile.exists()){
            try {
                myFile.createNewFile();
                FileWriter fw = new FileWriter("inputfile.txt");
                PrintWriter pw = new PrintWriter(fw);
                // Write variable to file
                // pw.print("Writing variable to file");
                //pw.println("The variable is below: ");
                pw.println(myVariable);
                // Close
                pw.close();
               // } catch (Exception ex){
                //Logger.getLogger(NewJFrame.class.getName()).log(Level.FINE, null, ex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}