import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
public class ServerController extends Thread {
        private ArrayList<File> medias;
        private File directory;
        private File[] mediaFiles;
        private String address;
        private int port;
        ServerController(String address, int port) {
            this.address = address;
            this.port = port;
        }

        @Override
        public void run() {
            medias = new ArrayList<File>();
            directory = new File("media");
            mediaFiles = directory.listFiles();

            if (mediaFiles != null)
            {
                medias.addAll(Arrays.asList(mediaFiles));
            }

            byte[] title = medias.get(Server.number).getName().getBytes();

            try {
                InetAddress clientAddress = InetAddress.getByName("localhost");
                int clientPort = port;

                //отправка клиенту названия файла

                DatagramPacket sendPacket = new DatagramPacket(title, title.length, clientAddress, clientPort);
                Server.datagramSocket.send(sendPacket);

                //поэтапная отправка клиенту файла

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(medias.get(Server.number)));
                byte[] buffer = new byte[65507];
                int bytesRead;

                while ((bytesRead = bis.read(buffer)) != -1) {
                    sendPacket = new DatagramPacket(buffer, bytesRead, clientAddress, clientPort);
                    Thread.sleep(1);
                    Server.datagramSocket.send(sendPacket);
                }

                bis.close();

                System.out.println("Файл отправлен клиенту.");

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
}
