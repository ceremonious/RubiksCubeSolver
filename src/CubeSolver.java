import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class CubeSolver {
    public static void main(String[] args) {
        RubixCube2x2 test = new RubixCube2x2();
        test.scramble("R L U D R' L' R D'");
        System.out.println(test);
        List<String> solved = solve(test);
        System.out.println(solved);
    }

    public static List<String> solve(RubixCube2x2 cube) {
        if (cube == null) {
            throw new IllegalArgumentException("Cube cannot be null.");
        }
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        queue.add(new SearchNode(cube, null, null));
        SearchNode current = queue.peek();
        int loops = 0;
        while (!current.isSolved()) {
            current = queue.poll();
            loops++;
            if (loops % 5000 == 0)
                System.out.println(loops);
            List<RubixCube2x2.Neighbor> neighbors = current.getCube().getNeighbors();
            for (RubixCube2x2.Neighbor neighbor : neighbors) {
                RubixCube2x2 neighborCube = neighbor.getCube();
                if (current.previous == null || !neighborCube.equals(current.getPrevious().getCube())) {
                    queue.add(new SearchNode(neighborCube, neighbor.getMove(), current));
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
        private RubixCube2x2 cube;
        private SearchNode previous;
        private String move;
        private int moves;

        public SearchNode(RubixCube2x2 cube, String move, SearchNode previous) {
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
        public RubixCube2x2 getCube() {
            return cube;
        }
        public String getMove() {
            return move;
        }
        public boolean isSolved() {
            return cube.isSolved();
        }
    }
}
