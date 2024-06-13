package server.socket;

import com.google.gson.Gson;
import server.controllers.*;
import server.controllers.Candidato.*;
import com.google.gson.reflect.TypeToken;
import server.controllers.Empresa.*;
import server.controllers.Experiencia.ApagarExperiencias;
import server.controllers.Experiencia.AtualizarExperiencia;
import server.controllers.Experiencia.CadastrarExperiencia;
import server.controllers.Experiencia.VisualizarExperiencias;
import server.controllers.Vaga.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

class ClientHandler extends Thread {
    protected Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        System.out.println ("Nova conexão: " + clientSocket.getInetAddress().getHostAddress());

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader( clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                System.out.println ("Server received: " + inputLine);

                if (inputLine.equals("Bye."))
                    break;

                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>(){}.getType();
                Map<String, Object> receivedData;

                try {
                    receivedData = gson.fromJson(inputLine, type);
                    System.out.println("Server received: " + receivedData);
                } catch (Exception e) {
                    System.out.println("Erro no formato da requisição");
                    out.println("Erro no formato da requisição");
                    break;
                }

                String response = null;

                if (receivedData.get("operacao") == null) {
                    System.out.println("Operação não fornecida");
                    out.println("Operação não fornecida");
                    break;
                }

                switch ((String) receivedData.get("operacao")) {
                    case "loginCandidato":
                        response = LoginCandidato.loginCandidato(receivedData, gson);
                        break;
                    case "cadastrarCandidato":
                        response = CadastrarCandidato.cadastrarCandidato(receivedData, gson);
                        break;
                    case "visualizarCandidato":
                        response = VisualizarCandidato.visualizarCandidato(receivedData, gson);
                        break;
                    case "atualizarCandidato":
                        response = AtualizarCandidato.atualizarCandidato(receivedData, gson);
                        break;
                    case "apagarCandidato":
                        response = ApagarCandidato.apagarCandidato(receivedData, gson);
                        break;
                    case "logout":
                        response = Logout.logout(receivedData, gson);
                        break;
                    case "cadastrarEmpresa":
                        response = CadastrarEmpresa.cadastrarEmpresa(receivedData, gson);
                        break;
                    case "loginEmpresa":
                        response = LoginEmpresa.loginEmpresa(receivedData, gson);
                        break;
                    case "visualizarEmpresa":
                        response = VisualizarEmpresa.visualizarEmpresa(receivedData, gson);
                        break;
                    case "atualizarEmpresa":
                        response = AtualizarEmpresa.atualizarEmpresa(receivedData, gson);
                        break;
                    case "apagarEmpresa":
                        response = ApagarEmpresa.apagarEmpresa(receivedData, gson);
                        break;
                    case "cadastrarCompetenciaExperiencia":
                        response = CadastrarExperiencia.cadastrarExperiencia(receivedData, gson);
                        break;
                    case "visualizarCompetenciaExperiencia":
                        response = VisualizarExperiencias.visualizarExperiencias(receivedData, gson);
                        break;
                    case "apagarCompetenciaExperiencia":
                        response = ApagarExperiencias.apagarExperiencias(receivedData, gson);
                        break;
                    case "atualizarCompetenciaExperiencia":
                        response = AtualizarExperiencia.atualizarExperiencia(receivedData, gson);
                        break;
                     case "cadastrarVaga":
                        response = CadastrarVaga.cadastrarVaga(receivedData, gson);
                        break;
                     case "listarVagas":
                        response = ListarVagas.listarVagas(receivedData, gson);
                        break;
                     case "apagarVaga":
                        response = ApagarVaga.apagarVaga(receivedData, gson);
                        break;
                     case "visualizarVaga":
                        response = VisualizarVaga.visualizarVaga(receivedData, gson);
                        break;
                     case "atualizarVaga":
                        response = AtualizarVaga.atualizarVaga(receivedData, gson);
                        break;
                    case "filtrarVagas":
                        response = FiltrarVagas.filtrarVagas(receivedData, gson);
                        break;
                    default:
                        System.out.println("Operação não reconhecida");
                        out.println("Operação não reconhecida");
                        break;
                }
                if(response != null) {
                    out.println(response);
                    out.flush();
                    System.out.println("Server sent: " + response);
                }

            }
            System.out.println("Fechando a conexão com o cliente");
            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.out.println("Conexão com o cliente fechada");
        }
    }
}