/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asciiserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 *
 * @author mega-user
 */
public class AsciiServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length < 1) {
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected (" + socket.getRemoteSocketAddress().toString().replace("/", "") + ")");

                DataInputStream in = new DataInputStream(socket.getInputStream());

                byte[] buffer = new byte[4096];
                byte[] ISObits;
                byte[] msgToSend;

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String text = "";
                String responseText = "";
                String msg = "";

                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

                //do {
                    in.read(buffer, 0, buffer.length);
                    
                    String clientMessage = new String(buffer);
                    System.out.println(clientMessage);

                    //ISObits = ISOUtil.asciiToEbcdic(clientMessage.replace("200", "210"));
                    responseText = clientMessage.replace("200", "210");
                    ISObits = responseText.getBytes();
                    //for(byte b : ISObits){
                    //    msg = msg + b;
                    //}

                    AsciiServer is = new AsciiServer();
                    msgToSend = is.buildHeader(ISObits);
                    dOut.write(ISObits); 
                    //writer.println("Response From Server: " + clientMessage);

                //} while (!text.equals("bye"));

                socket.close();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public byte[] buildHeader(byte[] msg) {
        //header
        //String value = eventContext.getMessage().getProperty("header",PropertyScope.SESSION);
        byte[] header = "00".getBytes();
        ByteBuffer byteBuff = ByteBuffer.allocate(msg.length + 2);
        header[0] = (byte) (msg.length / 256);
        header[1] = (byte) (msg.length % 256);
        byteBuff.put(header);
        byteBuff.put(msg);
        byteBuff.compact();
        return byteBuff.array();
    }

}
