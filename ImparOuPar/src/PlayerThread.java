import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerThread extends Thread {
  private Socket socket;
  private BufferedReader input;
  private PrintWriter output;

  public PlayerThread(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      output = new PrintWriter(socket.getOutputStream(), true);

      String playerName = input.readLine();
      output.println("Bem-vindo, " + playerName);

      String gameMode = input.readLine();

      if (gameMode.equals("1")) {
        playAgainstMachine();
      } else if (gameMode.equals("2")) {
        playAgainstPlayer();
      }

      socket.close();
      System.out.println("Jogador desconectado: " + socket);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void playAgainstMachine() throws IOException {
    output.println("Jogador vs Máquina selecionado. Escolha 'ímpar' (1) ou 'par' (2):");
    int playerChoice = Integer.parseInt(input.readLine());
    int machineChoice = (playerChoice == 1) ? 2 : 1;

    output.println("Escolha um número de 0 a 10:");
    int playerNumber = Integer.parseInt(input.readLine());
    int machineNumber = (int) (Math.random() * 11);

    int total = playerNumber + machineNumber;
    String result = (total % 2 == playerChoice) ? "Jogador venceu!" : "Máquina venceu!";
    output.println("Resultado: " + result);
  }

  private void playAgainstPlayer() throws IOException {
    output.println("Aguardando outro jogador...");

    PlayerThread opponent;

    synchronized (Server.class) {
      if (Server.waitingPlayer == null) {
        Server.waitingPlayer = this;
        try {
          Server.class.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        opponent = Server.waitingPlayer;
      } else {
        opponent = Server.waitingPlayer;
        Server.waitingPlayer = null;
        Server.class.notify();
      }
    }

    output.println("Jogador vs Jogador selecionado. Escolha 'ímpar' (1) ou 'par' (2):");
    int player1Choice = Integer.parseInt(input.readLine());
    int player2Choice = (player1Choice == 1) ? 2 : 1;

    output.println("Escolha um número de 0 a 10:");
    int player1Number = Integer.parseInt(input.readLine());
    opponent.output.println("Sua vez de escolher um número de 0 a 10:");
    int player2Number = Integer.parseInt(opponent.input.readLine());

    int total = player1Number + player2Number;
    String result = (total % 2 == player1Choice) ? "Jogador 1 venceu!" : "Jogador 2 venceu!";
    output.println("Resultado: " + result);
    opponent.output.println("Resultado: " + result);

    synchronized (Server.class) {
      Server.waitingPlayer = null;
      Server.class.notify();
    }
  }
}
