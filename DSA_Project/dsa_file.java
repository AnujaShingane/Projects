"""
  SafeRouteDSA.java (Problem: Time Pressure & Safety)
  Uses Dijkstra's algorithm to prioritize safety over pure distance.
"""

import java.util.*;

public class SafeRouteDSA {
    static class Edge {
        int targetNode;
        double distance;
        double hazardLevel; // 1.0 is normal, >1.0 is dangerous

        public Edge(int targetNode, double distance, double hazardLevel) {
            this.targetNode = targetNode;
            this.distance = distance;
            this.hazardLevel = hazardLevel;
        }
    }

    static class NodeCost {
        int nodeId;
        double cost;
        public NodeCost(int nodeId, double cost) {
            this.nodeId = nodeId;
            this.cost = cost;
        }
    }

    // Returns the safest route cost. Graph is an Adjacency List.
    public static double findSafestPath(List<List<Edge>> graph, int start, int destination) {
        PriorityQueue<NodeCost> pq = new PriorityQueue<>(Comparator.comparingDouble(nc -> nc.cost));
        double[] minCost = new double[graph.size()];
        Arrays.fill(minCost, Double.MAX_VALUE);

        pq.add(new NodeCost(start, 0.0));
        minCost[start] = 0.0;

        while (!pq.isEmpty()) {
            NodeCost current = pq.poll();

            if (current.nodeId == destination) return current.cost;
            if (current.cost > minCost[current.nodeId]) continue;

            for (Edge edge : graph.get(current.nodeId)) {
                // Algorithm logic: Cost is heavily penalized by hazard level
                double calculatedCost = current.cost + (edge.distance * edge.hazardLevel);

                if (calculatedCost < minCost[edge.targetNode]) {
                    minCost[edge.targetNode] = calculatedCost;
                    pq.add(new NodeCost(edge.targetNode, calculatedCost));
                }
            }
        }
        return -1; // No path found
    }
}


"""
  ProfitOptimizerDSA.java (Problem: Low Earnings)
  Uses Dynamic Programming to maximize profit for a driver's remaining shift.
"""

  public class ProfitOptimizerDSA {
    
    // 0/1 Knapsack to maximize profit within remaining shift minutes
    public static int maximizeEarnings(int[] payouts, int[] expectedMins, int remainingShiftMins) {
        int n = payouts.length;
        int[][] dp = new int[n + 1][remainingShiftMins + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= remainingShiftMins; w++) {
                if (expectedMins[i - 1] <= w) {
                    // Max of (Taking the order) OR (Skipping the order)
                    dp[i][w] = Math.max(
                        payouts[i - 1] + dp[i - 1][w - expectedMins[i - 1]], 
                        dp[i - 1][w]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        return dp[n][remainingShiftMins];
    }
}


"""
  OrderMatchingDSA.java (Problem: Inefficient Assignment)
Uses a Min-Heap to quickly match the closest available driver to a new high-priority order.
"""

  import java.util.*;

public class OrderMatchingDSA {
    
    static class DriverDistance {
        int driverId;
        double distanceToRestaurant;

        public DriverDistance(int driverId, double distanceToRestaurant) {
            this.driverId = driverId;
            this.distanceToRestaurant = distanceToRestaurant;
        }
    }

    // Min-Heap to always pop the closest driver in O(log N) time
    public static int assignClosestDriver(int[] availableDriverIds, double[] distances) {
        PriorityQueue<DriverDistance> minHeap = new PriorityQueue<>(
            Comparator.comparingDouble(d -> d.distanceToRestaurant)
        );

        for (int i = 0; i < availableDriverIds.length; i++) {
            minHeap.add(new DriverDistance(availableDriverIds[i], distances[i]));
        }

        // Return the best driver ID (or -1 if no drivers)
        return minHeap.isEmpty() ? -1 : minHeap.poll().driverId;
    }
}


"""
  FatigueWindowDSA.java (Problem: Overworking)
Uses a Deque to maintain a sliding window of hours worked in the last 24 hours.
"""


  import java.util.*;

public class FatigueWindowDSA {
    
    static class Shift {
        long timestamp; // epoch time in seconds
        int hoursWorked;

        public Shift(long timestamp, int hoursWorked) {
            this.timestamp = timestamp;
            this.hoursWorked = hoursWorked;
        }
    }

    private Deque<Shift> shiftWindow = new LinkedList<>();
    private int currentTotalHours = 0;
    private final int MAX_SAFE_HOURS = 12;

    // Sliding Window Algorithm
    public boolean isDriverFatigued(long currentTimestamp, int newlyCompletedHours) {
        // Add new shift
        shiftWindow.addLast(new Shift(currentTimestamp, newlyCompletedHours));
        currentTotalHours += newlyCompletedHours;

        // Remove shifts older than 24 hours (86400 seconds)
        while (!shiftWindow.isEmpty() && (currentTimestamp - shiftWindow.peekFirst().timestamp > 86400)) {
            Shift oldShift = shiftWindow.pollFirst();
            currentTotalHours -= oldShift.hoursWorked;
        }

        // Check if limit exceeded
        return currentTotalHours > MAX_SAFE_HOURS;
    }
}


"""
  BatchRoutingDSA.java (Problem: Bad Order Stacking)
Uses the Nearest Neighbor Heuristic to solve the multi-dropoff Routing (TSP variation).
"""

  public class BatchRoutingDSA {

    // Returns the ordered sequence of drop-off points to minimize travel distance
    public static List<Integer> getOptimalDropoffRoute(double[][] distanceMatrix, int startLocationIndex) {
        int n = distanceMatrix.length;
        boolean[] visited = new boolean[n];
        List<Integer> route = new ArrayList<>();
        
        int currentLoc = startLocationIndex;
        visited[currentLoc] = true;
        route.add(currentLoc);

        // Nearest Neighbor algorithm (O(N^2))
        for (int step = 1; step < n; step++) {
            int nearestNeighbor = -1;
            double minDistance = Double.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (!visited[j] && distanceMatrix[currentLoc][j] < minDistance) {
                    minDistance = distanceMatrix[currentLoc][j];
                    nearestNeighbor = j;
                }
            }

            if (nearestNeighbor != -1) {
                visited[nearestNeighbor] = true;
                route.add(nearestNeighbor);
                currentLoc = nearestNeighbor;
            }
        }
        return route;
    }
}
