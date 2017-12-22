import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RubixCube2x2 {
    private static char[] colors = {'Y', 'G', 'W', 'B', 'O', 'R'};
    private static String[] moves = {"R", "L", "U", "D", "F", "B"};
    private int[][] cube;
    private int hammingDistance;

    public RubixCube2x2() {
        cube = new int[6][4];
        for (int i = 0; i < 6; i++) {
            int[] face = {i, i, i, i};
            cube[i] = face;
        }
        setHammingDistance();
    }

    public RubixCube2x2(int[][] newCube) {
        cube = new int[6][4];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                cube[i][j] = newCube[i][j];
            }
        }
        setHammingDistance();
    }

    public List<Neighbor> getNeighbors() {
        List<Neighbor> neighbors = new LinkedList<>();
        for (String move : moves) {
            makeMove(move);
            neighbors.add(new Neighbor(new RubixCube2x2(cube), move));
            makeMove(move + "'");
            makeMove(move + "'");
            neighbors.add(new Neighbor(new RubixCube2x2(cube), move + "'"));
            makeMove(move);
        }
        return neighbors;
    }

    public String scramble() {
        StringBuilder scramble = new StringBuilder();
        int numMoves = 25;
        Random r = new Random();
        for (int i = 0; i < numMoves; i++) {
            int rand = r.nextInt(6);
            String direction = Math.random() < 0.5 ? "" : "'";
            String move = moves[rand] + direction;
            makeMove(move);
            scramble.append(move);
            scramble.append(" ");
        }
        setHammingDistance();
        return scramble.toString();
    }

    public void scramble(String moves) {
        String[] moveList = moves.split(" ");
        for (String move : moveList) {
            makeMove(move);
        }
        setHammingDistance();
    }

    private void makeMove(String move) {
        int[] coords = null;
        int[] faceRotateCoords = null;
        if (move.charAt(0) == 'R') {
            coords = new int[]{0, 1, 0, 3, 5, 1, 5, 3, 2, 2, 2, 0, 4, 1, 4, 3};
            faceRotateCoords = new int[]{1, 0, 1, 2, 1, 3, 1, 1};
        } else if (move.charAt(0) == 'L') {
            coords = new int[]{0, 2, 0, 0, 4, 2, 4, 0, 2, 1, 2, 3, 5, 2, 5, 0};
            faceRotateCoords = new int[]{3, 0, 3, 2, 3, 3, 3, 1};
        } else if (move.charAt(0) == 'U') {
            coords = new int[]{0, 0, 0, 1, 1, 0, 1, 1, 2, 0, 2, 1, 3, 0, 3, 1};
            faceRotateCoords = new int[]{4, 0, 4, 2, 4, 3, 4, 1};
        } else if (move.charAt(0) == 'D') {
            coords = new int[]{0, 3, 0, 2, 3, 3, 3, 2, 2, 3, 2, 2, 1, 3, 1, 2};
            faceRotateCoords = new int[]{5, 0, 5, 2, 5, 3, 5, 1};
        } else if (move.charAt(0) == 'F') {
            coords = new int[]{4, 3, 4, 2, 3, 1, 3, 3, 5, 0, 5, 1, 1, 2, 1, 0};
            faceRotateCoords = new int[]{0, 0, 0, 2, 0, 3, 0, 1};
        } else if (move.charAt(0) == 'B') {
            coords = new int[]{4, 0, 4, 1, 1, 1, 1, 3, 5, 3, 5, 2, 3, 2, 3, 0};
            faceRotateCoords = new int[]{2, 0, 2, 2, 2, 3, 2, 1};
        } else {
            throw new IllegalArgumentException("Illegal move given.");
        }
        coords = move.length() == 2 ? reverseMove(coords) : coords;
        faceRotateCoords = move.length() == 2 ? reverseFaceMove(faceRotateCoords) : faceRotateCoords;
        rotate(coords);
        faceRotate(faceRotateCoords);
    }

    private int[] reverseMove(int[] coords) {
        int[] output = new int[16];
        output[0] = coords[2];
        output[1] = coords[3];
        output[2] = coords[0];
        output[3] = coords[1];
        for (int i = 4; i < output.length; i += 2) {
            output[i] = coords[18 - i];
            output[i + 1] = coords[19 - i];
        }
        return output;
    }

    private int[] reverseFaceMove(int[] coords) {
        int[] output = new int[]{coords[0], coords[1], coords[6], coords[7],
                coords[4], coords[5], coords[2], coords[3]};
        return output;
    }

    private void rotate(int[] coords) {
        if (coords.length != 16) {
            throw new IllegalArgumentException("coords must be length 16");
        }
        int temp1 = cube[coords[0]][coords[1]];
        int temp2 = cube[coords[2]][coords[3]];
        for (int i = 0; i < 12; i += 2) {
            cube[coords[i]][coords[i + 1]] = cube[coords[i + 4]][coords[i + 5]];
        }
        cube[coords[12]][coords[13]] = temp1;
        cube[coords[14]][coords[15]] = temp2;
    }

    private void faceRotate(int[] coords) {
        if (coords.length != 8) {
            throw new IllegalArgumentException("coords must be length 8");
        }
        int temp1 = cube[coords[0]][coords[1]];
        for (int i = 0; i < 6; i += 2) {
            cube[coords[i]][coords[i + 1]] = cube[coords[i + 2]][coords[i + 3]];
        }
        cube[coords[6]][coords[7]] = temp1;
    }

    private void setHammingDistance() {
        int dist = 0;
        for (int i = 0; i < cube.length; i++) {
            for (int num : cube[i]) {
                if (num != i) {
                    dist++;
                }
            }
        }
        hammingDistance = dist;
    }

    public int getHammingDistance() {
        return hammingDistance;
    }

    public boolean isSolved() {
        return hammingDistance == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof RubixCube2x2)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        for (int i = 0; i < cube.length; i++) {
            for (int j = 0; j < cube[i].length; j++) {
                if (cube[i][j] != ((RubixCube2x2) other).cube[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (int[] face : cube) {
            s += colors[face[0]] + " " + colors[face[1]] + "\n";
            s += colors[face[2]] + " " + colors[face[3]] + "\n";
        }
        return s;
    }

    public class Neighbor {
        private RubixCube2x2 cube;
        private String move;
        public Neighbor(RubixCube2x2 cube, String move) {
            this.cube = cube;
            this.move = move;
        }
        public RubixCube2x2 getCube() {
            return cube;
        }
        public String getMove() {
            return move;
        }
    }

    public static void main(String[] args) {
        RubixCube2x2 test = new RubixCube2x2();
    }
}
