import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// Ana sunucu sınıfı
public class Mserver {

    public static void main(String[] s) {
        Socket sa = null; // İstemci ile bağlantı için soket
        ServerSocket ss2 = null; // Sunucu soketi

        System.out.println("Sunucu cevap kabul etmeye başlıyor...");
        try {
            ss2 = new ServerSocket(9999); // 9999 portunda sunucu soketi oluştur

        } catch (IOException e) {
            System.out.println("Sunucu Hatası!!!");
        }
        while (true) {
            try {
                sa = ss2.accept(); // İstemci bağlantısını kabul et
                System.out.println("Bağlantı kuruldu: " + ss2.getInetAddress());
                ServerThread st = new ServerThread(sa); // Her bağlantı için yeni bir iş parçacığı oluştur
                st.start(); // İş parçacığını başlat
            } catch (Exception e) {
                System.out.println("Bağlantı Hatası!!!");
            }
        }
    }
}

// Her istemci için ayrı bir iş parçacığı sınıfı
class ServerThread extends Thread {
    String line = null; // İstemciden gelen veri
    DataInputStream is = null; // İstemciden veri okumak için veri akışı
    PrintWriter od = null; // İstemciye veri göndermek için yazıcı
    Socket s1 = null; // İstemci soketi

    // İş parçacığı oluşturucu
    public ServerThread(Socket s) {
        s1 = s; // İstemci soketini ata
    }

    // İş parçacığının çalıştırma metodu
    public void run() {

        try {
            is = new DataInputStream(s1.getInputStream()); // İstemciden veri okumak için veri akışı oluştur
            od = new PrintWriter(s1.getOutputStream()); // İstemciye veri göndermek için yazıcı oluştur
            line = is.readLine(); // İstemciden gelen veriyi oku

            // İstemciden gelen veri "Quit" olmadığı sürece devam et
            while (!line.equals("Quit")) {
                od.println(line); // İstemciye gelen veriyi geri gönder
                od.flush(); // Yazıcıyı temizle

                System.out.println("İstemciye cevap: " + line); // Konsola istemciye gönderilen veriyi yazdır
                line = is.readLine(); // İstemciden yeni veri oku
            }
            is.close(); // Veri akışını kapat
            od.close(); // Yazıcıyı kapat
            s1.close(); // Soketi kapat
        } catch (IOException ie) {
            System.out.println("Soket Kapatma Hatası!!!");
        }
    }
}
