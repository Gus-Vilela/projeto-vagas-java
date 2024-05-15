package cliente.socket;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class SingletonEchoClient {
    private static SingletonEchoClient instance = null;
    private static String serverHostname;
    private static Socket echoSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    private SingletonEchoClient(String serverHostname) {
        SingletonEchoClient.serverHostname = serverHostname;
    }

    public static void getInstance(String serverHostname) throws IOException {
        if (instance == null) {
            instance = new SingletonEchoClient(serverHostname);
            connect();
        }
    }

    public static SingletonEchoClient getInstance() throws IOException {
        if (instance == null) {
            throw new IllegalStateException("Não foi possível manter a conexão com o servidor.");
        }
        return instance;
    }

    public static void connect() throws IOException {
        try {
            echoSocket = new Socket(serverHostname, 22222);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host desconhecido: " + serverHostname);
            throw new IOException("Host desconhecido: " + serverHostname);
        } catch (IOException e) {
            System.err.println("Erro de I/O para a conexão com: " + serverHostname);
            throw new IOException("Erro de I/O para a conexão com: " + serverHostname);
        }
    }

    public void sendJson(
            Map<String, Object> data
    ) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        out.println(json);
        out.flush();
        System.out.println("Client sent: " + json);
    }

    public String listenToServer() throws IOException {
        String inputLine;
        do {
            inputLine = in.readLine();
            if (inputLine != null) {
                System.out.println("Client received: " + inputLine);
                return inputLine;
            }
        } while (inputLine == null);
//        inputLine = in.readLine();
//        if (inputLine != null) {
//            System.out.println("Server sent: " + inputLine);
//            return inputLine;
//        }
       throw new IOException("Error na comunicação com o servidor");
    }

    public void close() throws IOException {
        out.close();
        in.close();
        echoSocket.close();
        instance = null;
    }
}
