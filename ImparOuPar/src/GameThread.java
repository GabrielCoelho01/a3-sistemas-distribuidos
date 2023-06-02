import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class GameThread extends Thread {
    private Socket player1Socket;
    private Socket player2Socket;

    public GameThread(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream player1Input = new DataInputStream(player1Socket.getInputStream());
            DataOutputStream player1Output = new DataOutputStream(player1Socket.getOutputStream());

            DataInputStream player2Input = new DataInputStream(player2Socket.getInputStream());
            DataOutputStream player2Output = new DataOutputStream(player2Socket.getOutputStream());

            int round = 1;
            boolean gameRunning = true;

            while (gameRunning) {
                // Solicita o palpite do jogador 1
                player1Output.writeUTF("Digite seu palpite (1 para ímpar, 2 para par):");
                int player1Guess = player1Input.readInt();

                // Define o palpite do jogador 2
                int player2Guess;
                if (round == 1) {
                    player2Guess = (player1Guess == 1) ? 2 : 1;
                } else {
                    player2Guess = getRandomGuess(player1Guess);
                }

                // Solicita um número aleatório do jogador 1
                player1Output.writeUTF("Digite um número aleatório:");
                int player1Number = player1Input.readInt();

                // Solicita um número aleatório do jogador 2
                player2Output.writeUTF("Digite um número aleatório:");
                int player2Number = player2Input.readInt();

                // Calcula a soma dos números
                int sum = player1Number + player2Number;

                // Verifica se a soma é par ou ímpar
                String result = (sum % 2 == 0) ? "par" : "ímpar";

                // Exibe o resultado para ambos os jogadores
                player1Output.writeUTF("Resultado: " + result);
                player2Output.writeUTF("Resultado: " + result);

                // Determina o vencedor
                int winner;
                if ((sum % 2 == 0 && player1Guess == 2) || (sum % 2 != 0 && player1Guess == 1)) {
                    winner = 1;
                } else {
                    winner = 2;
                }

                // Exibe o vencedor e perdedor da rodada
                player1Output.writeUTF("Vencedor da rodada: Jogador " + winner);
                player2Output.writeUTF("Vencedor da rodada: Jogador " + winner);

                // Verifica se o jogo deve ser reiniciado
                if (round >= 3) {
                    player1Output.writeUTF("Deseja reiniciar o jogo? (1 para reiniciar, 2 para finalizar)");
                    player2Output.writeUTF("Deseja reiniciar o jogo? (1 para reiniciar, 2 para finalizar)");

                    int restartChoice1 = player1Input.readInt();
                    int restartChoice2 = player2Input.readInt();

                    if (restartChoice1 == 1 && restartChoice2 == 1) {
                        round = 1;
                    } else {
                        gameRunning = false;
                    }
                } else {
                    round++;
                }
            }

            player1Socket.close();
            player2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRandomGuess(int guess) {
        Random random = new Random();
        int randomGuess;

        do {
            randomGuess = random.nextInt(2) + 1;
        } while (randomGuess == guess);

        return randomGuess;
    }
}
