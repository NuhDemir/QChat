import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// İstemci sınıfı
public class Mclient {

    public static void main(String[] s) throws Exception {
        Socket s1 = null; // Sunucu ile bağlantı için soket
        String line = null; // Kullanıcıdan alınan veri
        DataInputStream br = null; // Konsoldan veri okumak için DataInputStream
        DataInputStream is = null; // Sunucudan veri okumak için DataInputStream
        PrintWriter os = null; // Sunucuya veri göndermek için PrintWriter

        try {
            s1 = new Socket("localhost", 9999); // "localhost" ve 9999 portu ile sunucuya bağlan
            br = new DataInputStream(System.in); // Konsoldan veri okumak için DataInputStream oluştur
            is = new DataInputStream(s1.getInputStream()); // Sunucudan veri okumak için DataInputStream oluştur
            os = new PrintWriter(s1.getOutputStream()); // Sunucuya veri göndermek için PrintWriter oluştur

        } catch (IOException e) {
            System.err.print("IO Hatası");
        }

        System.out.println("Sunucuya veri girin (Bitiş için 'Quit' yazın)--> " + s1.getRemoteSocketAddress().toString());
        String res = null; // Sunucudan gelen yanıt

        try {
            line = br.readLine(); // Konsoldan veri oku
            while (line.compareTo("Quit") != 0) { // Kullanıcı "Quit" yazmadığı sürece devam et
                os.println(line); // Kullanıcıdan gelen veriyi sunucuya gönder
                os.flush(); // Yazıcıyı temizle
                res = is.readLine(); // Sunucudan gelen yanıtı oku
                System.out.println("Sunucu Yanıtı --> " + res); // Sunucudan gelen yanıtı ekrana yazdır
                line = br.readLine(); // Konsoldan yeni veri oku
            }
            is.close(); // Sunucudan veri okuma akışını kapat
            os.close(); // Sunucuya veri yazma akışını kapat
            br.close(); // Konsoldan veri okuma akışını kapat
            s1.close(); // Soketi kapat
            System.out.println("Bağlantı Kapandı");
        } catch (IOException e) {
            System.out.println("Soket Okuma Hatası!!!");
        }
    }
}
