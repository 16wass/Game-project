//package de.tum.cit.ase.maze.entities;
//
//import com.badlogic.gdx.math.Vector2;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.PriorityQueue;
//
//public class Pathfinding {
//
//    private static final int MOVE_COST = 10;
//
//    private static class Node implements Comparable<Node> {
//        Vector2 position;
//        int cost;
//        int heuristic;
//
//        Node(Vector2 position, int cost, int heuristic) {
//            this.position = position;
//            this.cost = cost;
//            this.heuristic = heuristic;
//        }
//
//        @Override
//        public int compareTo(Node other) {
//            return Integer.compare(cost + heuristic, other.cost + other.heuristic);
//        }
//    }
//
//    public List<Vector2> findPath(Vector2 start, Vector2 goal, World world) {
//        int[][] costMap = world.getCostMap(); // Assuming your World class provides a cost map
//
//        PriorityQueue<Node> openSet = new PriorityQueue<>();
//        List<Vector2> path = new ArrayList<>();
//        boolean[][] closedSet = new boolean[world.getMapWidth()][world.getMapHeight()];
//
//        int startX = (int) start.x;
//        int startY = (int) start.y;
//        int goalX = (int) goal.x;
//        int goalY = (int) goal.y;
//
//        openSet.add(new Node(new Vector2(startX, startY), 0, heuristic(startX, startY, goalX, goalY)));
//
//        while (!openSet.isEmpty()) {
//            Node current = openSet.poll();
//
//            if (current.position.x == goalX && current.position.y == goalY) {
//                // Reconstruct path
//                path.add(new Vector2(goalX, goalY));
//                while (current.position.x != startX || current.position.y != startY) {
//                    path.add(current.position);
//                    current = closedSet[(int) current.position.x][(int) current.position.y];
//                }
//                Collections.reverse(path);
//                return path;
//            }
//
//            closedSet[(int) current.position.x][(int) current.position.y] = true;
//
//            for (int x = -1; x <= 1; x++) {
//                for (int y = -1; y <= 1; y++) {
//                    if (x == 0 && y == 0) {
//                        continue;
//                    }
//
//                    int neighborX = (int) current.position.x + x;
//                    int neighborY = (int) current.position.y + y;
//
//                    if (neighborX >= 0 && neighborX < world.getMapWidth() &&
//                            neighborY >= 0 && neighborY < world.getMapHeight() &&
//                            !world.isObstacle(neighborX, neighborY)) {
//
//                        int newCost = current.cost + MOVE_COST;
//                        int heuristic = heuristic(neighborX, neighborY, goalX, goalY);
//
//                        Node neighbor = new Node(new Vector2(neighborX, neighborY), newCost, heuristic);
//
//                        if (!closedSet[neighborX][neighborY] && !openSet.contains(neighbor)) {
//                            openSet.add(neighbor);
//                            closedSet[neighborX][neighborY] = current;
//                        }
//                    }
//                }
//            }
//        }
//
//        return null; // No path found
//    }
//
//    private int heuristic(int x1, int y1, int x2, int y2) {
//        // Simple heuristic: Manhattan distance
//        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
//    }
//}
//
