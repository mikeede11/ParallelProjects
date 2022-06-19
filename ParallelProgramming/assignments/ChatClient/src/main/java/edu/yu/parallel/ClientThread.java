package edu.yu.parallel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClientThread extends Thread {

    BufferedReader fromClient;
    PrintWriter clientSignature;
    ArrayList<PrintWriter> clientsList;
    String clientName;

    public ClientThread(BufferedReader inputStream, PrintWriter outputObject, ArrayList<PrintWriter> clientsList, String clientName){
        fromClient = inputStream;
        clientSignature = outputObject;
        this.clientsList = clientsList;
        this.clientName = clientName;
    }
    public void run() {
        String message = "";
        try {
            while ((message = fromClient.readLine()) != null) {
                message = clientName + message;
                System.out.println(message);//assumes there is always at least one other client to send message for simplicity and no duplicate statements
                for(PrintWriter pw: clientsList){
                    if(pw != clientSignature){
                        pw.println(message);
                    }
                }
            }
            fromClient.close();
            clientSignature.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Hello from a thread!");
    }

}