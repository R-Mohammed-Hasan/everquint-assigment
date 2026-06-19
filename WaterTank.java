import java.util.Arrays;

public class WaterTank {

    public static void main(String[] args) {
        int[] heights = (args.length > 0)
                ? Arrays.stream(args).mapToInt(Integer::parseInt).toArray()
                : new int[]{0, 4, 0, 0, 0, 6, 0, 6, 4, 0};

        for (int h : heights) {
            if (h < 0) {
                System.out.println("Heights must be >= 0.");
                return;
            }
        }

        System.out.println("Input:  " + Arrays.toString(heights));
        System.out.println("Output: " + trappedWater(heights) + " Units");
        System.out.println();
        printDiagram(heights);
    }

    static int trappedWater(int[] h) {
        int left = 0, right = h.length - 1;
        int leftMax = 0, rightMax = 0, total = 0;

        // Move the shorter side inward; that side decides the water level
        while (left < right) {
            if (h[left] < h[right]) {
                if (h[left] >= leftMax) leftMax = h[left];
                else total += leftMax - h[left];
                left++;
            } else {
                if (h[right] >= rightMax) rightMax = h[right];
                else total += rightMax - h[right];
                right--;
            }
        }
        return total;
    }

    static int[] waterPerColumn(int[] h) {
        int n = h.length;
        int[] leftMax = new int[n], rightMax = new int[n], water = new int[n];
        int run = 0;
        for (int i = 0; i < n; i++) { run = Math.max(run, h[i]); leftMax[i] = run; }
        run = 0;
        for (int i = n - 1; i >= 0; i--) { run = Math.max(run, h[i]); rightMax[i] = run; }
        for (int i = 0; i < n; i++) {
            water[i] = Math.max(0, Math.min(leftMax[i], rightMax[i]) - h[i]);
        }
        return water;
    }

    // '#' = block, '~' = trapped water
    static void printDiagram(int[] h) {
        int[] water = waterPerColumn(h);
        int maxLevel = 0;
        for (int i = 0; i < h.length; i++) maxLevel = Math.max(maxLevel, h[i] + water[i]);

        for (int level = maxLevel; level >= 1; level--) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < h.length; i++) {
                if (h[i] >= level) row.append(" # ");
                else if (h[i] + water[i] >= level) row.append(" ~ ");
                else row.append("   ");
            }
            System.out.println(row);
        }
    }
}
