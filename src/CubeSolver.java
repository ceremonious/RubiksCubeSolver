import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class CubeSolver {
    public static void main(String[] args) {
        RubiksCube2x2 test = new RubiksCube2x2();
        test.scramble("F' U R R U F' U B'");
        System.out.println(test);
        List<String> solved = solve(test);
        System.out.println(solved);
    }

    //Solves the given Rubiks cube using the A* algorithm
    public static List<String> solve(RubiksCube2x2 cube) {
        if (cube == null) {
            throw new IllegalArgumentException("Cube cannot be null.");
        }
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        HashMap<RubiksCube2x2, Integer> visited = new HashMap<>();
        queue.add(new SearchNode(cube, null, null));
        SearchNode current = queue.peek();
        while (!current.isSolved()) {
            current = queue.poll();
            List<RubiksCube2x2.Neighbor> neighbors = current.getCube().getNeighbors();
            for (RubiksCube2x2.Neighbor neighbor : neighbors) {
                RubiksCube2x2 neighborCube = neighbor.getCube();
                //Only add the neighbor to the PQ if it not has been visited before with fewer moves
                int movesToVisit = visited.getOrDefault(neighborCube, Integer.MAX_VALUE);
                if (current.getNumMoves() + 1 < movesToVisit) {
                    SearchNode next = new SearchNode(neighborCube, neighbor.getMove(), current);
                    queue.add(next);
                    visited.put(neighborCube, next.getNumMoves());
                }
            }
        }
        LinkedList<String> output = new LinkedList<>();
        SearchNode node = current;
        while (node != null) {
            if (node.getMove() != null) {
                output.addFirst(node.getMove());
            }
            node = node.previous;
        }
        return output;
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private RubiksCube2x2 cube;
        private SearchNode previous;
        private String move;
        private int moves;

        public SearchNode(RubiksCube2x2 cube, String move, SearchNode previous) {
            this.cube = cube;
            this.previous = previous;
            this.move = move;
            if (previous == null)
                moves = 0;
            else
                moves = previous.moves + 1;
        }
        public int compareTo(SearchNode that) {
            return moves + cube.getHammingDistance() - that.moves -
                    that.cube.getHammingDistance();
        }
        public SearchNode getPrevious() {
            return previous;
        }
        public RubiksCube2x2 getCube() {
            return cube;
        }
        public String getMove() {
            return move;
        }
        public int getNumMoves() { return moves; }
        public boolean isSolved() {
            return cube.isSolved();
        }
        @Override
        public int hashCode() {
            return cube.hashCode();
        }
    }
}
