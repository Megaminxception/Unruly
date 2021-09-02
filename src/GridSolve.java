
public class GridSolve {
	/**
	 * Solves the given board using recursion (modifies the array itself)
	 * @param grid
	 * @return true if the board is solveable, false if it is not.
	 */
    public static boolean solve(int[][] grid) {
    // Base case
        int baseCase = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    baseCase++;
                }
            }
        }
        if (baseCase == 0) {
            return true;
        }
    // End base case

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 1;
                    if (checkOK(grid)) {
                        solve(grid);
                        if (onesAndTwos(grid)) {
                            return true;
                        }
                    }
                    grid[i][j] = 2;
                    if (checkOK(grid)) {
                        solve(grid);
                        if (onesAndTwos(grid)) {
                            return true;
                        }
                    }
                    grid[i][j] = 0;
                    return false;
                }
            }
        }
        return false;
    }

	public static boolean checkOK(int[][] grid) {
		int rowViolation1 = 0, rowViolation2 = 0;
		int totalViolation1 = 0, totalViolation2 = 0;
		int columnViolation1 = 0, columnViolation2 = 0;

		for (int i = 0; i < grid.length; i++) {
			totalViolation1 = 0;
			totalViolation2 = 0;
			rowViolation1 = 0;
			rowViolation2 = 0;
			for (int j = 0; j < grid.length; j++) {
				if (grid[i][j] == 1) {
					rowViolation1++;
					totalViolation1++;
					rowViolation2 = 0;
				} else if (grid[i][j] == 2) {
					rowViolation2++;
					totalViolation2++;
					rowViolation1 = 0;
				} else {
					rowViolation1 = 0;
					rowViolation2 = 0;
				}
				if (rowViolation1 >= 3 || rowViolation2 >= 3 || totalViolation1 > grid.length / 2
						|| totalViolation2 > grid.length / 2) {
					return false;
				}
			}
		}
		totalViolation1 = 0;
		totalViolation2 = 0;
		for (int i = 0; i < grid.length; i++) {
			totalViolation1 = 0;
			totalViolation2 = 0;
			columnViolation1 = 0;
			columnViolation2 = 0;
			for (int j = 0; j < grid.length; j++) {
				if (grid[j][i] == 1) {
					columnViolation1++;
					totalViolation1++;
					columnViolation2 = 0;
				} else if (grid[j][i] == 2) {
					columnViolation2++;
					totalViolation2++;
					columnViolation1 = 0;
				} else {
					columnViolation1 = 0;
					columnViolation2 = 0;
				}
				if (columnViolation1 >= 3 || columnViolation2 >= 3 || totalViolation1 > grid.length / 2
						|| totalViolation2 > grid.length / 2) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean onesAndTwos(int[][] grid) {
		int ones = 0;
		int twos = 0;
		int totalSpaces = grid.length * grid.length;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (grid[i][j] == 0) {
					continue;
				}
				if (grid[i][j] == 1) {
					ones++;
					continue;
				}
				if (grid[i][j] == 2) {
					twos++;
					continue;
				}
			}
		}
		if (ones != totalSpaces / 2 || twos != totalSpaces / 2) {
			return false;
		}
		return true;
	}
}