import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selecione o modo de jogo:");
        System.out.println("1. PvP");
        System.out.println("2. PVE ");

        int mode = scanner.nextInt();
        if (mode == 1) {
            playPvP(scanner);
        } else if (mode == 2) {
            playAgainstComputer(scanner);
        } else {
            System.out.println("Modo inválido.");
        }

        scanner.close();
    }

    private static void playPvP(Scanner scanner) throws IOException {
        Socket player1Socket = new Socket("localhost", 3000);
        System.out.println("Conectado como Jogador 1!");
        ObjectOutputStream player1OutputStream = new ObjectOutputStream(player1Socket.getOutputStream());
        ObjectInputStream player1InputStream = new ObjectInputStream(player1Socket.getInputStream());

        System.out.print("Digite o endereço IP do Jogador 2: ");
        String player2IP = scanner.next();
        System.out.print("Digite a porta do Jogador 2: ");
        int player2Port = scanner.nextInt();

        Socket player2Socket = new Socket(player2IP, player2Port);
        System.out.println("Conectado como Jogador 2!");
        ObjectOutputStream player2OutputStream = new ObjectOutputStream(player2Socket.getOutputStream());
        ObjectInputStream player2InputStream = new ObjectInputStream(player2Socket.getInputStream());

        playGame(scanner, player1OutputStream, player1InputStream, player2OutputStream, player2InputStream);

        player1Socket.close();
        player2Socket.close();
    }

    private static void playAgainstComputer(Scanner scanner) throws IOException {
        Socket socket = new Socket("localhost", 3000);
        System.out.println("Conectado ao servidor!");
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        playGame(scanner, outputStream, inputStream, outputStream, inputStream);

        socket.close();
    }

    private static void playGame(Scanner scanner, ObjectOutputStream outputStream1, ObjectInputStream inputStream1,
                                 ObjectOutputStream outputStream2, ObjectInputStream inputStream2) throws IOException {
        int pontuacaoJogador1 = 0;
        int pontuacaoJogador2 = 0;

        for (int i = 0; i < 3; i++) {
            System.out.println("Rodada " + (i + 1));

            int escolhaJogador1 = getPlayerChoice(scanner, 1);
            int numeroJogador1 = getPlayerNumber(scanner);

            outputStream1.writeInt(escolhaJogador1);
            outputStream1.writeInt(numeroJogador1);
            outputStream1.flush();

            int escolhaJogador2 = getPlayerChoice(scanner, 2);
            int numeroJogador2 = getPlayerNumber(scanner);

            outputStream2.writeInt(escolhaJogador2);
            outputStream2.writeInt(numeroJogador2);
            outputStream2.flush();

            try {
                String resultado1 = inputStream1.readUTF();
                System.out.println("Jogador 1: " + resultado1);

                String resultado2 = inputStream2.readUTF();
                System.out.println("Jogador 2: " + resultado2);

                if (resultado1.equals("Você ganhou!")) {
                    pontuacaoJogador1++;
                } else if (resultado2.equals("Você ganhou!")) {
                    pontuacaoJogador2++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Fim do jogo!");
        System.out.println("Pontuação do Jogador 1: " + pontuacaoJogador1);
        System.out.println("Pontuação do Jogador 2: " + pontuacaoJogador2);

        if (pontuacaoJogador1 > pontuacaoJogador2) {
            System.out.println("Jogador 1 venceu o jogo!");
        } else if (pontuacaoJogador2 > pontuacaoJogador1) {
            System.out.println("Jogador 2 venceu o jogo!");
        } else {
            System.out.println("Empate!");
        }
    }

    private static int getPlayerChoice(Scanner scanner, int playerNumber) {
        int escolhaJogador = 0;
        while (escolhaJogador != 1 && escolhaJogador != 2) {
            System.out.println("Jogador " + playerNumber + ", escolha ímpar ou par (1 para ímpar, 2 para par):");
            escolhaJogador = scanner.nextInt();
            if (escolhaJogador != 1 && escolhaJogador != 2) {
                System.out.println("Escolha inválida. Por favor, escolha 1 para ímpar ou 2 para par.");
            }
        }
        return escolhaJogador;
    }

    private static int getPlayerNumber(Scanner scanner) {
        System.out.println("Escolha um número:");
        return scanner.nextInt();
    }
}