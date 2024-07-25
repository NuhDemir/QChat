import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {

        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done!!");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
        }
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader Started...");

            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("Exit")) {
                        System.out.println("Server terminated the chatting");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("----Connection is Closed----");
            }
        };
        new Thread(r1).start();
    }


    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("EXIT")) {
                        socket.close();
                        break;
                    }

                    System.out.print("----Connection is Closed----");
                }
            } catch (Exception e) {
            }
            ;
        };

        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("This is client...");
    }
}