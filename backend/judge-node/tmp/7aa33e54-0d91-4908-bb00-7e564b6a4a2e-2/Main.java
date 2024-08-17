import java.util.Scanner;

class Main {

    static int SIZE = 9;

    char[][] board;
    int[] rows;
    int[] cols;
    int[] parts;
    // char[][] ans;
    boolean solved;

    public static void main(String[] args) {
        new Main().run();
    }

    void run() {
        char[][] board = new char[SIZE][SIZE];
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = sc.next().charAt(0);
            }
        }

        solveSudoku(board);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j]);
                if (j != 8) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void solveSudoku(char[][] board) {
        // init
        this.board = board;
        this.rows = new int[SIZE];
        this.cols = new int[SIZE];
        this.parts = new int[SIZE];
        this.solved = false;
        // build rows / cols / parts
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j ++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '0';
                    rows[i] |= 1 << num - 1;
                    cols[j] |= 1 << num - 1;
                    parts[getPartId(i, j)] |= 1 << num - 1;
                }
            }
        }
        // dfs
        dfs(0, 0);
    }

    void dfs(int x, int y) {
        if (solved) {
            return;
        }
        if (x >= SIZE) {
            this.solved = true;
            return;
        }
        if (board[x][y] != '.') {
            if (y != SIZE - 1) {
                dfs(x, y + 1);
            } else {
                dfs(x + 1, 0);
            }
            return;
        }
        int partId = getPartId(x, y);
        for (int num = 1; num <= SIZE; num++) {
            if ((rows[x] >> num - 1 & 1) == 0
                    && (cols[y] >> num - 1 & 1) == 0
                    && (parts[partId] >> num - 1 & 1) == 0) {
                // tag
                board[x][y] = (char)(num + '0');
                rows[x] |= 1 << num - 1;
                cols[y] |= 1 << num - 1;
                parts[partId] |= 1 << num - 1;

                // dfs
                if (y != SIZE - 1) {
                    dfs(x, y + 1);
                } else {
                    dfs(x + 1, 0);
                }

                if (solved) {
                    return;
                }

                // backtracing
                board[x][y] = '.';
                rows[x] &= ~(1 << num - 1);
                cols[y] &= ~(1 << num - 1);
                parts[partId] &= ~(1 << num - 1);
            }
        }
    }

    int getPartId(int x, int y) {
        return x / 3 * 3 + y / 3;
    }
}