import java.util.*;

public class ShamirSecretSharing {

    // Method to decode the y value from the given base
    private static int decodeYValue(String base, String value) {
        int baseInt = Integer.parseInt(base);
        return Integer.parseInt(value, baseInt);
    }

    // Manually parse the JSON-like structure and extract points (x, y)
    public static List<int[]> parsePoints(Map<String, Map<String, String>> jsonObject) {
        List<int[]> points = new ArrayList<>();
        Map<String, String> keys = jsonObject.get("keys");
        int n = Integer.parseInt(keys.get("n"));
        int k = Integer.parseInt(keys.get("k"));

        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                int x = Integer.parseInt(key);
                Map<String, String> root = jsonObject.get(key);
                String base = root.get("base");
                String value = root.get("value");

                int y = decodeYValue(base, value);
                points.add(new int[]{x, y});
            }
        }
        return points;
    }

    // Method to implement Lagrange Interpolation
    public static double lagrangeInterpolation(List<int[]> points) {
        double constantTerm = 0.0;
        int k = points.size();  // We will use k points

        // Implementing Lagrange interpolation
        for (int i = 0; i < k; i++) {
            int[] pointI = points.get(i);
            double yi = pointI[1];
            double term = yi;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int[] pointJ = points.get(j);
                    term *= ((0 - pointJ[0]) * 1.0) / (pointI[0] - pointJ[0]);
                }
            }
            constantTerm += term;
        }
        return constantTerm;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Map<String, String>> jsonObject = new HashMap<>();

        // Reading the "keys"
        System.out.println("Enter the number of points (n): ");
        String n = scanner.nextLine();
        System.out.println("Enter the minimum number of points (k): ");
        String k = scanner.nextLine();

        Map<String, String> keys = new HashMap<>();
        keys.put("n", n);
        keys.put("k", k);
        jsonObject.put("keys", keys);

        // Reading the points
        System.out.println("Enter the number of points to input:");
        int pointCount = Integer.parseInt(n);
        for (int i = 0; i < pointCount; i++) {
            System.out.println("Enter x for point " + (i + 1) + ": ");
            String x = scanner.nextLine();
            System.out.println("Enter the base for point " + (i + 1) + ": ");
            String base = scanner.nextLine();
            System.out.println("Enter the value for point " + (i + 1) + ": ");
            String value = scanner.nextLine();

            Map<String, String> point = new HashMap<>();
            point.put("base", base);
            point.put("value", value);
            jsonObject.put(x, point);
        }

        // Parse the points
        List<int[]> points = parsePoints(jsonObject);

        // Apply Lagrange interpolation to find the constant term
        double constantTerm = lagrangeInterpolation(points);

        // Print the constant term (the secret c), rounded to the nearest integer
        System.out.println("The constant term (c) is: " + Math.round(constantTerm));
    }
}
