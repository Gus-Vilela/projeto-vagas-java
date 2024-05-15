package server;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import server.hibernate.dao.CandidatoDao;
import server.hibernate.models.Candidato;
import server.hibernate.utils.HibernateUtil;
import server.socket.EchoServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSession()) {
            if (session != null) {
                System.out.println("Conexão do db estabelecida com sucesso!");
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        try {
            EchoServer echoServer = new EchoServer(22222);
            echoServer.start();
        } catch (IOException e) {
            System.err.println("Could not start server.");
            e.printStackTrace();
        }

    }
}
