import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3000);
        System.out.println("Conectado ao servidor!");
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Scanner scanner = new Scanner(System.in);
        socket.setSoTimeout(5000); // Define o tempo limite de leitura para 5 segundos

        int pontuacaoJogador = 0;
        int pontuacaoOponente = 0;

        for (int i = 0; i < 3; i++) {
            System.out.println("Rodada " + (i + 1));
            int escolhaJogador = 0;
            while (escolhaJogador != 1 && escolhaJogador != 2) {
                System.out.println("Jogador, escolha ímpar ou par (1 para ímpar, 2 para par):");
                escolhaJogador = scanner.nextInt();
                if (escolhaJogador != 1 && escolhaJogador != 2) {
                    System.out.println("Escolha inválida. Por favor, escolha 1 para ímpar ou 2 para par.");
                }
            }
            System.out.println("Escolha um número:");
            int numeroJogador = scanner.nextInt();
            outputStream.writeInt(escolhaJogador);
            outputStream.writeInt(numeroJogador);
            outputStream.flush();

            try {
                int escolhaOponente = inputStream.readInt();
                int numeroOponente = inputStream.readInt();
                System.out.println("O oponente escolheu o número " + numeroOponente);
                String resultado = inputStream.readUTF();
                System.out.println(resultado);

                if (resultado.equals("Você ganhou!")) {
                    pontuacaoJogador++;
                } else if (resultado.equals("Oponente ganhou!")) {
                    pontuacaoOponente++;
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Tempo limite de leitura atingido. Continuando para a próxima rodada.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pontuacaoJogador = inputStream.readInt();
        pontuacaoOponente = inputStream.readInt();

        System.out.println("Fim do jogo!");
        System.out.println("Pontuação do jogador: " + pontuacaoJogador);
        System.out.println("Pontuação do oponente: " + pontuacaoOponente);
        if (pontuacaoJogador > pontuacaoOponente) {
            System.out.println("Você venceu o jogo!");
        } else if (pontuacaoOponente > pontuacaoJogador) {
            System.out.println("Oponente venceu o jogo!");
        } else {
            System.out.println("Empate!");
        }

        socket.close();
    }
}
