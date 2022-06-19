package edu.yu.parallel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class VerySimpleChatServer {
    public static void main(String args[]) {

        System.out.println("Starting Server ...");

        ArrayList<PrintWriter> clients = new ArrayList<>();
        int portNumber = 444;
        String clientName = "";
        int clientPort = -1;
        while(true) {

            try  {
                ServerSocket serverSocket1 = new ServerSocket(portNumber);
                while(true) {
                    Thread.sleep(2000);
                    Socket clientSocket1 = serverSocket1.accept();
                    clientPort = clientSocket1.getPort();
                    System.out.println("client socket =Socket [addr=" + clientSocket1.getInetAddress() + ",port=" + clientPort + ",localport=" + clientSocket1.getLocalPort() + "]");
                    System.out.println("getPort() =" + clientPort + " getLocalPort=" + clientSocket1.getLocalPort());
                    clientName = "{socket port=" + clientPort + "} ";
                    System.out.println("Connected to " + clientName);
                    PrintWriter out1 =
                            new PrintWriter(clientSocket1.getOutputStream(), true);
                    BufferedReader in1 = new BufferedReader(
                            new InputStreamReader(clientSocket1.getInputStream()));
                    String outputLine = "";
                    clients.add(out1);
                    ClientThread a = new ClientThread(in1, out1, clients, clientName);
                    a.start();
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("ERROR COULD NOT BIND/LISTEN TO SOCKET OR GET OUTPUT/INPUT STREAM");
            }
        }
    }



}
