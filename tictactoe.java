import java.util.*;
public class tictactoe {
    private static final char EMPTY = ' ';
    private static final char PLAYER = 'X';
    private static final char COMPUTER = 'O';

    private char[] board = new char[9];
    private Scanner scanner = new Scanner(System.in);

    public tictactoe() {
        Arrays.fill(board, EMPTY);
    }

    public void play() {
        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Choose mode:\n1) 2Players\n2) Computer");
        int mode = readIntInRange(1, 2);

        int currentPlayer = 0; // 0 -> X (human1), 1 -> O (human2 or computer)
        printBoard();

        while (true) {
            if (mode == 1) {
                // Human vs Human
                System.out.printf("%s turn. Enter position (1-9): ", currentPlayer == 0 ? "Player1(X)" : "Player2(O)");
                int pos = readMove();
                board[pos] = currentPlayer == 0 ? 'X' : 'O';
            } else {
                // Human vs Computer
                if (currentPlayer == 0) {
                    System.out.print("Player (X) turn. Enter position (1-9): ");
                    int pos = readMove();
                    board[pos] = PLAYER;
                } else {
                    System.out.println("Computer (O) is thinking...");
                    int pos = bestMove();
                    board[pos] = COMPUTER;
                    System.out.printf("Computer plays at %d\n", pos + 1);
                }
            }

            printBoard();

            if (hasWon('X')) {
                System.out.println("Player1 X wins!");
                break;
            }
            if (hasWon('O')) {
                if (mode == 2) System.out.println("Computer (O) wins!");
                else System.out.println("Player2 O wins!");
                break;
            }
            if (isFull()) {
                System.out.println("It's a draw!");
                break;
            }

            currentPlayer = 1 - currentPlayer; // switch
        }

        System.out.println("Game over. Thanks for playing!");
    }

    private int readMove() {
        while (true) {
            int n = readIntInRange(1, 9) - 1; // convert to 0-index
            if (board[n] == EMPTY) return n;
            System.out.print("That cell is occupied. Try again: ");
        }
    }

    private int readIntInRange(int lo, int hi) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= lo && v <= hi) return v;
            } catch (NumberFormatException e) {
                // fall through
            }
            System.out.printf("Please enter a number between %d and %d: ", lo, hi);
        }
    }

    private boolean hasWon(char p) {
        int[][] wins = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
        };
        for (int[] w : wins) {
            if (board[w[0]] == p && board[w[1]] == p && board[w[2]] == p) return true;
        }
        return false;
    }

    private boolean isFull() {
        for (char c : board) if (c == EMPTY) return false;
        return true;
    }

    private void printBoard() {
        System.out.println();
        for (int r = 0; r < 3; r++) {
            int i = r * 3;
            System.out.printf(" %c | %c | %c\n", displayChar(board[i]), displayChar(board[i+1]), displayChar(board[i+2]));
            if (r < 2) System.out.println("---+---+---");
        }
        System.out.println();
    }

    private char displayChar(char c) {
        return c == EMPTY ? ' ' : c;
    }

    // Minimax AI
    private int bestMove() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                board[i] = COMPUTER;
                int score = minimax(false);
                board[i] = EMPTY;
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        return move;
    }

    private int minimax(boolean isMaximizing) {
        if (hasWon(COMPUTER)) return 10;
        if (hasWon(PLAYER)) return -10;
        if (isFull()) return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    board[i] = COMPUTER;
                    best = Math.max(best, minimax(false));
                    board[i] = EMPTY;
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (board[i] == EMPTY) {
                    board[i] = PLAYER;
                    best = Math.min(best, minimax(true));
                    board[i] = EMPTY;
                }
            }
            return best;
        }
    }

    public static void main(String[] args) {
        tictactoe game = new tictactoe();
        game.play();
    }
}
