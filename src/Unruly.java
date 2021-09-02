import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.LinkedList;

public class Unruly extends JFrame {
    // Essential visuals
    private Container pane;
    // Game board
    private JButton[][] board;
    private int[][] gameBoard;
    private int[][] solvedBoard;
    private JMenuItem boardSelection;
    private int boardSize;
    // Menu bar
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem newGame;
    private JMenuItem solve;
    private JMenuItem hint;
    private JMenuItem howToPlay;
    private JMenuItem quit;
    private JCheckBoxMenuItem undo;
    // Misc
    private int hintsUsed;
    private LinkedList<ActionEvent> undoList = new LinkedList<>();

    /**
     * Cosntructor for Unruly that sets up the JFrame extention and 
     * initializes the Unruly game.
     */
    public Unruly(int boardSize) {
        super("Unruly");
        pane = getContentPane();
        setSize(720, 720); //TODO look at having this adjust based on screen size.
        // Get the size of the screen that the application is on (when the game first starts)
        // Choose a size based on that...
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window.
        this.boardSize = boardSize;
        pane.setLayout(new GridLayout(boardSize, boardSize));
        board = new JButton[boardSize][boardSize];
        initializeMenuBar();
        initializeBoard();
        setVisible(true);  
    }

    /**
     * Main menu for Unruly, that lets you select the size of your game board when you first start the game.
     */
    public Unruly() {
        super("Unruly");
        pane = getContentPane();
        setSize(720,720);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pane.setLayout(new FlowLayout());
        setVisible(true);
        //String[] options = {"10x10", "8x8", "6x6", "4x4"};
        // 10x10 board generation is not consistently fast enough for me to be willing to include it,
        // so the choices will just be 4x4, 6x6, and 8x8.
        String[] options = {"8x8", "6x6", "4x4"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(pane, "What size board would you like to solve?"
            , "Board Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options, options[0]);
            switch (choice) {
                /* case 0: 
                    dispose();
                    new Unruly(10);
                    break; */
                case 0:
                    dispose(); 
                    new Unruly(8);
                    break;
                case 1:
                    dispose();
                    new Unruly(6);
                    break;
                case 2:
                    dispose();
                    new Unruly(4);
                    break;
                default:
                    continue;
            }
            break;
        }
        
    }

    /**
     * Initializes the game board
     */
    private void initializeBoard() {
        GridCreate grid = new GridCreate(boardSize);
        grid.boardGenerate();
        gameBoard = grid.board;
        solvedBoard = grid.solvedBoard;
        
        // Setting up the visual board to reflect the generated int board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton button = new JButton();
                board[i][j] = button;
                // KEY:
                // 0 = GRAY
                // 1 = WHITE
                // 2 = BLACK
                if (gameBoard[i][j] == 0) {
                    button.setBackground(Color.GRAY);
                    button.setForeground(Color.GRAY);
                    button.setText("0");
                } else if (gameBoard[i][j] == 1) {
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.WHITE);
                    button.setText("default 1");
                } else {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.BLACK);
                    button.setText("default 2");
                }
                if (button.getBackground().equals(Color.GRAY)) {
					button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							undoList.add(e);
							if (((JButton) e.getSource()).getBackground().equals(Color.WHITE)) {
								button.setBackground(Color.BLACK);
								button.setForeground(Color.BLACK);
								button.setText("2");
							} else if (((JButton) e.getSource()).getBackground().equals(Color.BLACK)) {
								button.setBackground(Color.GRAY);
								button.setForeground(Color.GRAY);
								button.setText("0");
							} else if (((JButton) e.getSource()).getBackground().equals(Color.GRAY)) {
								button.setBackground(Color.WHITE);
								button.setForeground(Color.WHITE);
								button.setText("1");
							}
							if (solved()) {
								JOptionPane.showMessageDialog(pane,
										"Congratulations, you solved the puzzle!" + "\nHints used: " + hintsUsed);
							}
						}
					});
				} else {
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							return;
							// this literally does nothing, but I need to have
							// ActionListeners to remove for every element
							// of the board (or else code blow up :/ )
						}
					});
					button.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}
				button.setOpaque(true);
				button.setBorderPainted(true);
				pane.add(button);
            }
        }
    }

    /**
     * Creates a new game (regenerates everything including the board).
     */
    private void newGame() {
        dispose();
        new Unruly(boardSize);
    }

    /**
     * Initializes the menu options for the game (and the undo button).
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        menu = new JMenu("Game Menu");

        // Starts a new game when clicked
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        menu.add(newGame);

        // Solves the board when clicked
        solve = new JMenuItem("Solve");
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
            }
        });
        menu.add(solve);

        // Gives a random hint when clicked
        hint = new JMenuItem("Hint");
        hint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hintsUsed++;
                hint();
            }
        });
        menu.add(hint);

        /// Shows a text dialog on how to play the game when clicked
        howToPlay = new JMenuItem("How to Play");
        howToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(pane,
                "Welcome to Unruly!\nThe objective of this game is to color each tile in a way such that:\n"
								+ "	1) No three consecutive squares, horizontally or vertically, are the same color\n"
								+ "	2) Each row and column contains the same number of black and white squares (4 of each).\n"
								+ "Click on a box to cycle through the colors (White, Black, Grey).\n"
								+ "Click on the undo button to undo your previous move (works in succession).\n"
								+ "(Note: Black and white boxes with a blue border are unchangeable!)\n"
								+ "*Hints work based off a single solution, so you may need to change your solution\n"
								+ "to adapt to a given hint!*");
            }
        });
        menu.add(howToPlay);

        // Let's the user choose a new board size when clicked (also starts a new game)
        boardSelection = new JMenuItem("Select Board Size");
        boardSelection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String[] options = {"10x10", "8x8", "6x6", "4x4"};
                // 10x10 board generation is not consistently fast enough for me to be willing to include it,
                // so the choices will just be 4x4, 6x6, and 8x8.
                String[] options = {"8x8", "6x6", "4x4"};
                int choice = JOptionPane.showOptionDialog(pane, "What size board would you like to solve?"
                , "Board Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
                switch (choice) {
                    /* case 0: 
                        dispose();
                        new Unruly(10);
                        break; */
                    case 0:
                        dispose();
                        new Unruly(8);
                        break;
                    case 1:
                    dispose();
                        new Unruly(6);
                        break;
                    case 2:
                    dispose();
                        new Unruly(4);
                        break;
                    default:
                        break;

                }
            }
        });
        menu.add(boardSelection);

        // Allows the user to quit the game (after confirmation of intent to exit)
        quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(pane, "Are you sure you would like to quit?", "Quit", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        menu.add(quit);

        // Undos the last move, as long as there is a move to undo
        undo = new JCheckBoxMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hintSolved()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            undo.setSelected(false);
                        }
                    });
                    return;
                }
                if (undoList.isEmpty()) {
                    JOptionPane.showMessageDialog(pane, "No more moves to undo!");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            undo.setSelected(false);
                        }
                    });
                    return;
                }
                // Keeps us from undoing any moves that were changed by hints (since tiles given as hints should be immutable)
                while (((JButton)undoList.getLast().getSource()).getText().matches("hint 1")
                || (((JButton)undoList.getLast().getSource()).getText().matches("hint 2"))) {
                    undoList.removeLast();
                }
                if (((JButton)undoList.getLast().getSource()).getBackground().equals(Color.WHITE)) {
                    ((JButton)undoList.getLast().getSource()).setBackground(Color.GRAY);
                    ((JButton)undoList.getLast().getSource()).setForeground(Color.GRAY);
                    ((JButton)undoList.getLast().getSource()).setText("0"); // 0 is for gray
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            undo.setSelected(false);
                        }
                    });
                    undoList.removeLast();
                    return;
                }

                if (((JButton)undoList.getLast().getSource()).getBackground().equals(Color.BLACK)) {
                    ((JButton)undoList.getLast().getSource()).setBackground(Color.WHITE);
                    ((JButton)undoList.getLast().getSource()).setForeground(Color.WHITE);
                    ((JButton)undoList.getLast().getSource()).setText("1"); // 1 is for white
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            undo.setSelected(false);
                        }
                    });
                    undoList.removeLast();
                    return;
                } else { // must be gray, so I don't need to check it – I can just edit the tileaccordingly
                    ((JButton)undoList.getLast().getSource()).setBackground(Color.BLACK);
                    ((JButton)undoList.getLast().getSource()).setForeground(Color.BLACK);
                    ((JButton)undoList.getLast().getSource()).setText("2"); // 2 is for black
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            undo.setSelected(false);
                        }
                    });
                    undoList.removeLast();
                    return;
                }
            }
        });

        //Add the menu to the frame
        menu.setBackground(Color.WHITE);
        menuBar.add(menu);
        menuBar.add(undo);
        setJMenuBar(menuBar);
    }

    /**
     * Checks if the board is solved, but doesn't take any actions if it is, unlike solved()
     * Used as part of the solved method because to reduce lines of code.
     * @return
     */
    private boolean hintSolved() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].getText().contains("0")) {
                    return false;
                }
                if (board[i][j].getText().contains("1")) {
                    gameBoard[i][j] = 1;
                } else {
                    gameBoard[i][j] = 2;
                }
            }
        }
        return GridSolve.checkOK(gameBoard);
    }

    /**
     * Places a (mostly) randomly generated hint on the board, and makes it the tile immutable after being placed.
     */
    private void hint() {
        //TODO maybe look at optimizing this method, since I think it's one of the least-efficiently written ones in this program...
        if (hintSolved()) {
            return;
        }
        Random r = new Random();
        int tries = 0;
        for (int i = r.nextInt(boardSize); i < boardSize; i = r.nextInt(boardSize)) {
            for (int j = r.nextInt(boardSize); j < boardSize; j = r.nextInt(boardSize)) {
                if (tries >= 10) {
                    for (int k = 0; k < boardSize; k++) {
                        for (int l = 0; l < boardSize; l++) {
                            if (board[k][l].getText().contains("default")) {continue;}
                            String s = "" + solvedBoard[k][l];
                            if (!board[k][l].getText().contains(s)) {
                                if (s.matches("1")) {
                                    setHint(k, l, Color.WHITE);
                                } else {setHint(k, l, Color.BLACK);}
                                if (solved()) {
                                    JOptionPane.showMessageDialog(pane,
                                    "You solved it with a hint??? Alright...\n"
                                    + "Well Congratulations anyway!\n"
                                    + "Hints used:  " + hintsUsed);
                                }
                                return;
                            }
                        }
                    }
                }
                
                if (board[i][j].getText().contains("default")){continue;}
                String s = "" + solvedBoard[i][j];
                if (!board[i][j].getText().contains(s)) {
                    if (s.matches("1")) {
                        setHint(i, j, Color.WHITE);
                    } else {
                        setHint(i, j, Color.BLACK);
                    }
                    if (solved()) {
                        JOptionPane.showMessageDialog(pane,
                        "You solved it with a hint??? Alrignt...\n"
                        + "Well Congratulations anyway!\n"
                        + "Hints used:  " + hintsUsed);
                    }
                    return;
                }
                tries ++;
            }
        }
    }

    /**
     * Checks to see if the game board is solved at any given moment
     * @return True if the board is solved, otherwise returns false
     */
    private boolean solved() {
        if (hintSolved()) {
            for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
					board[i][j].removeActionListener((board[i][j].getActionListeners())[0]);
				}
			}
            return true;
        } else return false;
    }

    /**
     * Solves the board, updating it visually based on a solved version that
     * is generated as part of the checks performed during the board's original
     * generation, when the program makes sure that the board has at least one solution.
     */
    private void solve() {
        if (hintSolved()) {
            return;
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String s = "" + solvedBoard[i][j];
                if (!board[i][j].getText().contains("" + solvedBoard[i][j])) {
                    if (s.matches("1")) {
                        setColor(i, j, Color.WHITE);
                    } else {
                        setColor(i, j, Color.BLACK);
                    }
                }
                board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                board[i][j].removeActionListener((board[i][j].getActionListeners())[0]);
            }
        }
    }

    /**
     * Takes care of the repetitive work of setting colors for normal tiles (just reduces lines of code)
     * @param board
     * @param i
     * @param j
     * @param color
     */
    private void setColor(int i, int j, Color color) {
        board[i][j].setBackground(color);
        board[i][j].setForeground(color);
        if (color == Color.WHITE) {
            board[i][j].setText("1");
        } else if (color == Color.BLACK) {
            board[i][j].setText("2");
        }
    }

    /**
     * Takes care of the repetitive work of setting colors for hint tiles (just reduces lines of code)
     * @param i
     * @param j
     * @param color
     */
    private void setHint(int i, int j, Color color) {
        board[i][j].setBackground(color);
        board[i][j].setForeground(color);
        if (color == Color.WHITE) {
            board[i][j].setText("hint 1");
        } else if (color == Color.BLACK) {
            board[i][j].setText("hint 2");
        }
        board[i][j].setBorder(BorderFactory.createLineBorder(Color.BLUE));
        board[i][j].removeActionListener((board[i][j].getActionListeners())[0]);
        board[i][j].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                return;
            }
        });
    }
}
