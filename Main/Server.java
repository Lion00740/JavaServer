import java.io.*;
import java.net.*;
public class Server {
    public static int number;
    public static DatagramSocket datagramSocket;
    public static void main(String[] args) {
        try {
            datagramSocket = new DatagramSocket(8080);
            byte[] bytes = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(bytes, bytes.length);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                String message;
                datagramSocket.receive(receivePacket);
                message = new String(receivePacket.getData(),0, receivePacket.getLength());
                number =  Integer.parseInt(message);

                System.out.println("Client connected: " + datagramSocket);

                // Обработка подключенного клиента в отдельном потоке
                String str = String.valueOf(receivePacket.getAddress());
                ServerController serverController = new ServerController(str, receivePacket.getPort());
                serverController.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}