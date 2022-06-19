package edu.yu.parallel;

/** Skeleton client to set up and display a GUI that allows users to enter AND
 * a GUI that displays received text.  When a user enters text into the GUI,
 * currently the GUI is implemented to print that text to the console.
 * Students will need to add code to send this text to the server and to
 * display text that it receives from the server
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class SimpleChatClient {
    JTextArea incoming;
    JTextField outgoing;
    PrintWriter out;
    BufferedReader in;

    public void go(PrintWriter out, BufferedReader in) {
        JFrame frame = new JFrame("Ludicrously Simple Chat Client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 500);
        frame.setVisible(true);
        this.out = out;
        this.in = in;
        //thread
        ReceiveMessageThread receiver = new ReceiveMessageThread();
        receiver.start();
        //
    }



    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            SendMessageThread sender = new SendMessageThread();
            sender.start();
        }
    }


    public static void main(String[] args) {
        try {
            Socket kkSocket = new Socket(InetAddress.getLocalHost(), 444);//bind socket
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);//use this to send to server - this is the road out of the client to the server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));//get stuff from server - this is the road frrom the server to the client

            System.out.println("networking established");
            new SimpleChatClientA().go(out, in);
        }catch(IOException e){
            e.printStackTrace();
        }

        //bind to socket, get road to and from socket(server comm)- C
        //upon button press set textarea and send to server
        //listen to server and set text area


    }

    private class SendMessageThread extends Thread{

        public void run() {
            try {
                String message = "sent > " + outgoing.getText();
                incoming.setText(incoming.getText() + "\n You " + message);
                out.println(message);
                System.out.println("client read You " + message);//send this to server
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");//reset to blank to put in new input
            outgoing.requestFocus();
        }
    }
    private class ReceiveMessageThread extends Thread{
        public void run() {
            String fromServer = "";
            try {
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("client read " + fromServer);
                    incoming.setText(incoming.getText() + "\n" + fromServer);
                }
                in.close();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
