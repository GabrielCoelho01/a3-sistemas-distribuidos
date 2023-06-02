import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Conectado ao servidor!");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);

            System.out.println("Escolha o modo de jogo:");
            System.out.println("1 - Jogador vs Jogador");
            System.out.println("2 - Jogador vs Máquina");
            int mode = scanner.nextInt();

            if (mode == 1) {
                System.out.println("Aguardando oponente...");
            } else {
                System.out.println("Você está jogando contra a máquina.");
            }

            while (true) {
                String message = input.readUTF();
                System.out.println(message);

                if (message.startsWith("Digite")) {
                    int guess = scanner.nextInt();
                    output.writeInt(guess);
                } else if (message.startsWith("Resultado")) {
                    String result = input.readUTF();
                    System.out.println(result);
                } else if (message.startsWith("Vencedor")) {
                    String winner = input.readUTF();
                    System.out.println(winner);
                } else if (message.startsWith("Deseja")) {
                    System.out.println("Digite 1 para reiniciar ou 2 para finalizar:");
                    int choice = scanner.nextInt();
                    output.writeInt(choice);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
