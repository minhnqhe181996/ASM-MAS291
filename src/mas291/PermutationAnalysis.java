
import java.util.*;

public class PermutationAnalysis {

    // Hàm chạy menu cho bài toán hoán vị
    public static void run(Scanner scanner) {
        while (true) {
            System.out.print("Enter subproblem (2.1, 2.2, 2.3, or 'exit' to return): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "2.1": {
                    // Nhập n và số lần mô phỏng N
                    int n = Validation.getPositiveInt(scanner, "Enter n (1–20): ");
                    // Với n > 20 sẽ dễ bị sai số do độ chính xác của double
                    if (n > 20) {
                        System.out.println("Warning: n > 20 may reduce factorial accuracy.");
                    }
                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    int count = 0;
                    for (int i = 0; i < N; i++) {
                        if (isDerangement(generatePermutation(n))) {
                            count++;
                        }
                    }
                    double approx = (double) count / N;

                    // Tính xác suất lý thuyết bằng công thức
                    double theoretical = 0;
                    for (int k = 0; k <= n; k++) {
                        theoretical += Math.pow(-1, k) / factorial(k);
                    }

                    System.out.printf("Approximate derangement probability: %.4f%n", approx);
                    System.out.printf("Theoretical derangement probability (D_n / n!): %.4f%n", theoretical);
                    break;
                }

                case "2.2": {
                    // Mô phỏng N hoán vị, tính khoảng cách "hill-distance"
                    int n = Validation.getPositiveInt(scanner, "Enter n (1–20): ");
                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    List<Integer> distances = new ArrayList<>();
                    for (int i = 0; i < N; i++) {
                        distances.add(hillDistance(generatePermutation(n)));
                    }
                    double mean = distances.stream().mapToInt(i -> i).average().orElse(0);
                    double var = distances.stream().mapToDouble(i -> Math.pow(i - mean, 2)).sum() / N;
                    System.out.printf("Expected hill-distance: %.2f, Variance: %.2f%n", mean, var);
                    break;
                }
                case "2.3": {
                    // Phần a: Tính độ dài LIS của một hoán vị nhập vào
                    // Phần b: Mô phỏng N hoán vị, tính kỳ vọng độ dài LIS
                    System.out.print("Enter subproblem part (a or b): ");
                    String part = scanner.nextLine().trim();
                    if (part.equals("a")) {
                        int n = Validation.getPositiveInt(scanner, "Enter n: ");
                        int[] perm = Validation.getPermutation(scanner, n);
                        System.out.println("LIS length: " + lisLength(perm));
                    } else if (part.equals("b")) {
                        int n = Validation.getPositiveInt(scanner, "Enter n (up to 100): ");
                        int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                        double total = 0;
                        for (int i = 0; i < N; i++) {
                            total += lisLength(generatePermutation(n));
                        }
                        double mean = total / N;
                        System.out.printf("Expected LIS length: %.2f (approx 2√n = %.2f)%n", mean, 2 * Math.sqrt(n));
                    } else {
                        System.out.println("Invalid part. Enter 'a' or 'b'.");
                    }
                    break;
                }
                case "exit":
                    return; // Thoát về menu chính
                default:
                    System.out.println("Invalid choice."); // Nhập sai lựa chọn
            }
        }
    }

    // Sinh ngẫu nhiên một hoán vị của n số từ 1 đến n
    private static int[] generatePermutation(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        Collections.shuffle(list); // Trộn ngẫu nhiên
        return list.stream().mapToInt(i -> i).toArray();
    }

    // Kiểm tra một hoán vị có phải là derangement (không phần tử nào giữ nguyên vị trí)
    private static boolean isDerangement(int[] perm) {
        for (int i = 0; i < perm.length; i++) {
            if (perm[i] == i + 1) {
                return false;
            }
        }
        return true;
    }

    // Tính khoảng cách "hill-distance" của một hoán vị
    private static int hillDistance(int[] perm) {
        int sum = 0;
        for (int i = 1; i < perm.length; i++) {
            int a = perm[i - 1], b = perm[i];
            if (a < b) {
                sum += 2 * (b - a);
            } else if (a > b) {
                sum += a - b;
            }
        }
        return sum;
    }

    // Tính độ dài dãy con tăng dài nhất (LIS) của một hoán vị
    private static int lisLength(int[] perm) {
        ArrayList<Integer> lis = new ArrayList<>();
        for (int num : perm) {
            int idx = Collections.binarySearch(lis, num);
            if (idx < 0) {
                idx = -idx - 1;
            }
            if (idx == lis.size()) {
                lis.add(num);
            } else {
                lis.set(idx, num);
            }
        }
        return lis.size();

    }

    // Tính giai thừa n! (dạng double)
    private static double factorial(int n) {
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

}
// Kết thúc lớp phân tích hoán vị
