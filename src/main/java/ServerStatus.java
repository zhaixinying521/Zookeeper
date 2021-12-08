import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerStatus {
    @Test
    public void serverStatus() throws IOException {
        String host = "192.168.88.120";
        int port = 2181;
        String cmd = "stat";

        Socket sock = new Socket(host, port);
        BufferedReader reader = null;

        try {
            OutputStream outstream = sock.getOutputStream();
            // 通过Zookeeper的四字命令获取服务器的状态
            outstream.write(cmd.getBytes());
            outstream.flush();
            sock.shutdownOutput();

            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("Mode: ") != -1) {
                    System.out.println(line.replaceAll("Mode: ", "").trim());
                }
            }
        } finally {
            sock.close();
            if (reader != null) {
                reader.close();
            }
        }
    }
}