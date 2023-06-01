import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
        System.out.println("Servidor iniciado, aguardando conexões...");

        while (true) {
            Socket player1Socket = serverSocket.accept();
            System.out.println("Jogador 1 conectado: " + player1Socket.getInetAddress());

            Socket player2Socket = serverSocket.accept();
            System.out.println("Jogador 2 conectado: " + player2Socket.getInetAddress());

            Thread thread = new Thread(new GameThread(player1Socket, player2Socket));
            thread.start();
        }
    }

    private static class GameThread implements Runnable {
        private final Socket player1Socket;
        private final Socket player2Socket;

        public GameThread(Socket player1Socket, Socket player2Socket) {
            this.player1Socket = player1Socket;
            this.player2Socket = player2Socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream player1OutputStream = new ObjectOutputStream(player1Socket.getOutputStream());
                ObjectInputStream player1InputStream = new ObjectInputStream(player1Socket.getInputStream());

                ObjectOutputStream player2OutputStream = new ObjectOutputStream(player2Socket.getOutputStream());
                ObjectInputStream player2InputStream = new ObjectInputStream(player2Socket.getInputStream());

                playGame(player1OutputStream, player1InputStream, player2OutputStream, player2InputStream);

                player1Socket.close();
                player2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void playGame(ObjectOutputStream player1OutputStream, ObjectInputStream player1InputStream,
                              ObjectOutputStream player2OutputStream, ObjectInputStream player2InputStream) throws IOException {
            int pontuacaoJogador1 = 0;
            int pontuacaoJogador2 = 0;

            for (int i = 0; i < 3; i++) {
                int escolhaJogador1 = player1InputStream.readInt();
                int numeroJogador1 = player1InputStream.readInt();

                int escolhaJogador2 = player2InputStream.readInt();
                int numeroJogador2 = player2InputStream.readInt();

                player1OutputStream.writeInt(escolhaJogador2);
                player1OutputStream.writeInt(numeroJogador2);
                player1OutputStream.flush();

                player2OutputStream.writeInt(escolhaJogador1);
                player2OutputStream.writeInt(numeroJogador1);
                player2OutputStream.flush();

                int sum = escolhaJogador1 + numeroJogador1 + escolhaJogador2 + numeroJogador2;
                String resultadoJogador1;
                String resultadoJogador2;

                if (sum % 2 == 0) {
                    resultadoJogador1 = "Par. ";
                    resultadoJogador2 = "Par. ";
                    if (escolhaJogador1 == 2) {
                        resultadoJogador1 += "Você ganhou!";
                        pontuacaoJogador1++;
                    } else {
                        resultadoJogador1 += "Jogador 2 ganhou!";
                        pontuacaoJogador2++;
                    }
                    if (escolhaJogador2 == 2) {
                        resultadoJogador2 += "Você ganhou!";
                        pontuacaoJogador2++;
                    } else {
                        resultadoJogador2 += "Jogador 1 ganhou!";
                        pontuacaoJogador1++;
                    }
                } else {
                    resultadoJogador1 = "ímpar. ";
                    resultadoJogador2 = "ímpar. ";
                    if (escolhaJogador1 == 1) {
                        resultadoJogador1 += "Você ganhou!";
                        pontuacaoJogador1++;
                    } else {
                        resultadoJogador1 += "Jogador 2 ganhou!";
                        pontuacaoJogador2++;
                    }
                    if (escolhaJogador2 == 1) {
                        resultadoJogador2 += "Você ganhou!";
                        pontuacaoJogador2++;
                    } else {
                        resultadoJogador2 += "Jogador 1 ganhou!";
                        pontuacaoJogador1++;
                    }
                }

                player1OutputStream.writeUTF(resultadoJogador1);
                player1OutputStream.flush();
                player2OutputStream.writeUTF(resultadoJogador2);
                player2OutputStream.flush();
            }

            player1OutputStream.writeInt(pontuacaoJogador1);
            player1OutputStream.writeInt(pontuacaoJogador2);
            player1OutputStream.flush();

            player2OutputStream.writeInt(pontuacaoJogador2);
            player2OutputStream.writeInt(pontuacaoJogador1);
            player2OutputStream.flush();
        }
    }
}