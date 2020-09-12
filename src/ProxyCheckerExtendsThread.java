import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ProxyCheckerExtendsThread {
    public static void main(String[] args) throws Exception {
        // Введите путь к файлу со списком адресов
        FileReader reader = new FileReader("C://Test/test.txt");
        int c;
        String ipList = "";
        while ((c = reader.read()) != -1) {
            ipList += (char) c;
        }
        // Разделяем строки
        String[] ipListArray = ipList.split("\r\n");
        for (int i = 0; i < ipListArray.length; i++) {
            // Задаем разделитель
            String[] splitString = ipListArray[i].split(":");
            String ip = splitString[0].trim();
            int port = Integer.parseInt(splitString[1]);
            MyThread t1 = new MyThread(ip, port);
            t1.start();
        }
    }
}

class MyThread extends Thread {
    String ip;
    int port;

    public MyThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
        try {
            URLConnection connection = new URL("https://www.google.ru").openConnection(proxy);
            InputStream is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[256];
            int rc;
            StringBuilder sb = new StringBuilder();
            while ((rc = reader.read(buffer)) != -1)
                sb.append(buffer, 0, rc);
            reader.close();
            System.out.println(getName() + "- ip: " + ip + ":" + port + " - ДОСТУПЕН");
        } catch (Exception e) {
            System.out.println(getName() + "- ip: " + ip + ":" + port + " - недоступен");
        }
    }
}