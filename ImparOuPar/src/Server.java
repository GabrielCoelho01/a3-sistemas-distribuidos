import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Aguardando conexões...");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Jogador 1 conectado!");

                Socket player2Socket = serverSocket.accept();
                System.out.println("Jogador 2 conectado!");

                GameThread gameThread = new GameThread(player1Socket, player2Socket);
                gameThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
