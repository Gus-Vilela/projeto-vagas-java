package server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer extends Thread {
    protected ServerSocket serverSocket;
    protected boolean running = false;

    public EchoServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void run() {
        this.running = true;
        System.out.println("Socket de servidor iniciado");
        try {
            while (running) {
                System.out.println("Aguardando conexão...");
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao aceitar conexão");
        }
    }

    public void stopServer() throws IOException {
        this.running = false;
        this.serverSocket.close();
    }
}