import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxProfit {

    static final String[] NAMES = {"T", "P", "C"};
    static final int[] COST = {5, 4, 10};
    static final int[] RATE = {1500, 1000, 2000};

    static int[] best;

    public static void main(String[] args) {
        int[] inputs = (args.length > 0)
                ? new int[]{Integer.parseInt(args[0])}
                : new int[]{7, 8, 13};

        for (int n : inputs) {
            int maxEarnings = solve(n);
            List<int[]> solutions = reconstruct(n);

            System.out.println("Time Unit: " + n + "  ->  Earnings: $" + maxEarnings);
            int i = 1;
            for (int[] c : solutions) {
                System.out.println("  " + (i++) + ". T: " + c[0] + " P: " + c[1] + " C: " + c[2]);
            }
            System.out.println();
        }
    }

    // best[t] = best earnings using t time units, building one property at a time
    static int solve(int n) {
        best = new int[n + 1];
        for (int t = 1; t <= n; t++) {
            for (int b = 0; b < NAMES.length; b++) {
                if (COST[b] <= t) {
                    int earned = RATE[b] * (t - COST[b]) + best[t - COST[b]];
                    if (earned > best[t]) best[t] = earned;
                }
            }
        }
        return best[n];
    }

    // Walk the table back to find every mix that hits the best value (ties happen, e.g. n=7)
    static List<int[]> reconstruct(int n) {
        List<int[]> solutions = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        backtrack(n, new int[]{0, 0, 0}, solutions, seen);
        return solutions;
    }

    static void backtrack(int t, int[] counts, List<int[]> solutions, Set<String> seen) {
        if (best[t] == 0) {
            String key = counts[0] + "-" + counts[1] + "-" + counts[2];
            if (seen.add(key)) {
                solutions.add(new int[]{counts[0], counts[1], counts[2]});
            }
            return;
        }
        for (int b = 0; b < NAMES.length; b++) {
            if (COST[b] <= t) {
                int earned = RATE[b] * (t - COST[b]) + best[t - COST[b]];
                if (earned == best[t]) {
                    counts[b]++;
                    backtrack(t - COST[b], counts, solutions, seen);
                    counts[b]--;
                }
            }
        }
    }
}
