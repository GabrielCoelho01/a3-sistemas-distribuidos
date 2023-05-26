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
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress());

            Thread thread = new Thread(new PlayerThread(socket));
            thread.start();
        }
    }

    private static class PlayerThread implements Runnable {
        private final Socket socket;

        public PlayerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                int playerScore = 0;
                int opponentScore = 0;

                for (int i = 0; i < 3; i++) {
                    int playerChoice = inputStream.readInt();
                    int playerNumber = inputStream.readInt();

                    int opponentChoice = 3 - playerChoice; // Determine opponent's choice (odd or even)
                    int opponentNumber = (int) (Math.random() * 6) + 1;

                    outputStream.writeInt(opponentChoice);
                    outputStream.writeInt(opponentNumber);
                    outputStream.flush();

                    int sum = playerChoice + playerNumber + opponentChoice + opponentNumber;
                    String result;

                    if (sum % 2 == 0) {
                        result = "Par. ";
                        if (playerChoice == 2) {
                            result += "Você ganhou!";
                            playerScore++;
                        } else {
                            result += "Oponente ganhou!";
                            opponentScore++;
                        }
                    } else {
                        result = "ímpar. ";
                        if (playerChoice == 1) {
                            result += "Você ganhou!";
                            playerScore++;
                        } else {
                            result += "Oponente ganhou!";
                            opponentScore++;
                        }
                    }

                    outputStream.writeUTF(result);
                    outputStream.flush();
                }

                outputStream.writeInt(playerScore);
                outputStream.writeInt(opponentScore);
                outputStream.flush();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
