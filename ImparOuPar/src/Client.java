import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
  private static final String HOST = "localhost";
  private static final int PORT = 8080;

  public static void main(String[] args) {
    try {
      Socket socket = new Socket(HOST, PORT);
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

      System.out.println("Conectado ao servidor.");
      System.out.print("Digite seu nome: ");
      String playerName = consoleInput.readLine();
      output.println(playerName);

      System.out.println(input.readLine());

      System.out.println("Modos de jogo:");
      System.out.println("1 - Jogador vs Máquina");
      System.out.println("2 - Jogador vs Jogador");
      System.out.print("Digite o modo de jogo: ");
      String gameMode = consoleInput.readLine();
      output.println(gameMode);

      if (gameMode.equals("1")) {
        playAgainstMachine(input, output, consoleInput);
      } else if (gameMode.equals("2")) {
        playAgainstPlayer(input, output, consoleInput);
      }

      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void playAgainstMachine(BufferedReader input, PrintWriter output, BufferedReader consoleInput)
      throws IOException {
    System.out.println(input.readLine());
    int playerChoice = Integer.parseInt(consoleInput.readLine());
    output.println(playerChoice);

    System.out.println(input.readLine());
    int playerNumber = Integer.parseInt(consoleInput.readLine());
    output.println(playerNumber);

    System.out.println(input.readLine());
  }

  private static void playAgainstPlayer(BufferedReader input, PrintWriter output, BufferedReader consoleInput)
      throws IOException {
    System.out.println(input.readLine());

    while (true) {
      String response = input.readLine();
      System.out.println(response);

      if (response.startsWith("Resultado:")) {
        break;
      }

      int choice = Integer.parseInt(consoleInput.readLine());
      output.println(choice);
    }
  }
}
