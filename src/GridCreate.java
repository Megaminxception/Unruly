import java.util.Random;

public class GridCreate {
    public int[][] board; // Represents the generated board
    public int[][] solvedBoard; // Represents the solved version of the board
    private static int randI; // holds a random integer
    private static int randJ; // holds a random integer
    private static int whiteOrBlack; // holds a random int that decides if the tile will be white or black
    private int boardSize; // defines the length and width of the game board

    public GridCreate(int boardSize) {
        board = new int[boardSize][boardSize];
        solvedBoard = new int[boardSize][boardSize];
        this.boardSize = boardSize;
    }
    
    /**
     * Uses random number generators to create a randomly generated
    * game board, then checks to see if the created board is solvable
    * or not. If the board is solvable, it is returned in the form of a
    * two-dimensional String array. If the board is not solvable, the
    * method continues to randomly generate boards until it creates one
    * that can be solved.
     */
    public void boardGenerate() {
        //TODO Loading Screen?
        Random random = new Random();

        randI = random.nextInt(boardSize);
        randJ = random.nextInt(boardSize);
        whiteOrBlack = random.nextInt();
        //  If whiteOrBlack is even, the tile is white – if it's odd, the tile will be black.
        if (whiteOrBlack % 2 == 0) {whiteOrBlack = 1;} else {whiteOrBlack = 2;}
        int basePieces = (int)(boardSize*boardSize*0.22); // How many pieces should be placed on the board by default

        while (true) {
            for (int i = 1; i <= basePieces;   i++) {
                if (board[randI][randJ] != 0) { // Then this piece already has a tile, so reroll and try again!
                    randI = random.nextInt(boardSize);
                randJ = random.nextInt(boardSize);
                    i--;
                    continue;
                }
                // Place the tiles
                board[randI][randJ] = whiteOrBlack;
                solvedBoard[randI][randJ] = whiteOrBlack;
                // Reroll all the random variables defining where tiles are placed, and what color the tile is
                randI = random.nextInt(boardSize);
                randJ = random.nextInt(boardSize);
                whiteOrBlack = random.nextInt();
                whiteOrBlack = random.nextInt();if (whiteOrBlack % 2 == 0) {whiteOrBlack = 1;} else {whiteOrBlack = 2;}
            }
            if (GridSolve.solve(solvedBoard)) {
                return; // A solveable game board and its solved version have been generated, so we're done here!
            } else {
                // Clear the boards
                board = new int[boardSize][boardSize];
                solvedBoard = new int[boardSize][boardSize];
                continue; // This isn't needed, but illustrates that we reset the boards and try again until a solveable board is generated
            }
        }

    }

}
