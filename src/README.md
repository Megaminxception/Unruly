### Here is a quick description of the files in this folder, and what each file does.

# Main
The Main class has the main method that runs first, and begins a new instance of the Unruly class.

# Unruly
Unruly is the primary class that handles generating the GUI, and interacts with all the other classes to bring the Unruly puzzle together. All the logic of the game is also handled here, as well as updates to the visual board upon user input.

# GridCreate
GridCreate is a the class that handles generation of the game board that the user will see once they selectr the size board that they wish to solve. By verifying the generated board with the GridSolve class, there will never be a board generated that does not have a solution.

# GridSolve
GridSolve contains the ability to tell if any given game board has at least one solution, and can also solve the game board using recursion. This class is primary used to verify generated boards, check if the board is solved after any move by the user, and solve the board if the user chooses for the program to solve it for them. Additionally, GridSolve helps when generating hints, so that no hint makes a board unsolveable.