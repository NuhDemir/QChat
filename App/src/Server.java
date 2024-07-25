import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket server; // Sunucu soketi
    Socket socket; // İstemci ile iletişim kurmak için soket

    BufferedReader br; // İstemciden gelen veriyi okumak için BufferedReader
    PrintWriter out; // İstemciye veri göndermek için PrintWriter

    // Sunucu nesnesi oluşturucu
    public Server() {
        try {
            server = new ServerSocket(7777); // 7777 portunda sunucu soketi oluştur
            System.out.println("Sunucu bağlantı kabul etmeye hazır...");
            System.out.println("Bekleniyor...");
            socket = server.accept(); // İstemci bağlantısını kabul et

            br = new BufferedReader(new InputStreamReader(socket.getInputStream())); // İstemciden gelen veriyi okumak için BufferedReader oluştur
            out = new PrintWriter(socket.getOutputStream()); // İstemciye veri göndermek için PrintWriter oluştur

            startReading(); // Veri okumayı başlat
            startWriting(); // Veri yazmayı başlat
        } catch (Exception e) {
            e.printStackTrace(); // Hata durumunda hatayı yazdır
        }
    }

    // Veri okuma işlemini başlatan metot
    public void startReading() {
        Runnable r1 = () -> { // Yeni bir thread oluştur
            System.out.println("Okuyucu Başladı...");

            try {
                while (true) {
                    String msg = br.readLine(); // İstemciden gelen mesajı oku
                    if (msg.equals("Exit")) { // Eğer gelen mesaj "Exit" ise
                        System.out.println("İstemci sohbeti sonlandırdı...");
                        socket.close(); // Soketi kapat
                        break;
                    }
                    System.out.println("İstemci: " + msg); // Gelen mesajı ekrana yazdır
                }
            } catch (Exception e) {
                System.out.println("----Bağlantı Kapandı----"); // Hata durumunda mesaj yazdır
            }
        };
        new Thread(r1).start(); // Yeni thread başlat
    }

    // Veri yazma işlemini başlatan metot
    public void startWriting() {
        Runnable r2 = () -> { // Yeni bir thread oluştur
            System.out.println("Yazıcı Başladı...");

            try {
                while (!socket.isClosed()) { // Soket kapalı olmadığı sürece
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in)); // Konsoldan veri okumak için BufferedReader oluştur
                    String content = br1.readLine(); // Konsoldan gelen veriyi oku

                    out.println(content); // İstemciye veriyi gönder
                    out.flush(); // Veriyi gönder

                    if (content.equals("Exit")) { // Eğer gönderilen veri "Exit" ise
                        socket.close(); // Soketi kapat
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("-----Bağlantı Kapandı----"); // Hata durumunda mesaj yazdır
            }
        };
        new Thread(r2).start(); // Yeni thread başlat
    }

    // Ana metot, sunucuyu başlatır
    public static void main(String[] s) {
        System.out.println("Bu bir Sunucu... sunucu başlatılıyor.");
        new Server(); // Yeni bir sunucu nesnesi oluştur
    }

}
