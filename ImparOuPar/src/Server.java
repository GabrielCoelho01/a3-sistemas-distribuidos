import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private static final int PORT = 8080;
  static int playersCount = 0;
  static PlayerThread waitingPlayer = null;

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Servidor iniciado. Aguardando conexões...");

      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("Novo jogador conectado: " + socket);

        PlayerThread playerThread = new PlayerThread(socket);
        playerThread.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
