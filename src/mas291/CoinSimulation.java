
import java.util.*;

public class CoinSimulation {

    // Hàm chạy menu cho bài toán đồng xu
    public static void run(Scanner scanner) {
        while (true) {
            System.out.print("Enter subproblem (1.1, 1.2, 1.3, 1.4, 1.5, or 'exit' to return): ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1.1": {
                    // Nhập xác suất p và số lần tung n
                    double p = Validation.getDouble(scanner, "Enter p (0 to 1): ", 0, 1);
                    int n = Validation.getPositiveInt(scanner, "Enter number of tosses (n): ");
                    List<String> result = toss(p, n); // Mô phỏng tung n lần

                    // In kết quả dãy tung
                    System.out.println("Toss results: " + result);

                    // Đếm số H và T
                    long countH = result.stream().filter(s -> s.equals("H")).count();
                    long countT = result.stream().filter(s -> s.equals("T")).count();
                    System.out.printf("Heads (H): %d%n", countH);
                    System.out.printf("Tails (T): %d%n", countT);
                    break;
                }

                case "1.2": {
                    // Mô phỏng N lần tung 1 đồng xu, tính tần suất xuất hiện mặt H
                    double p = Validation.getDouble(scanner, "Enter p (0 to 1): ", 0, 1);
                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    int countHeads = 0;
                    for (int i = 0; i < N; i++) {
                        if (toss(p, 1).get(0).equals("H")) {
                            countHeads++;
                        }
                    }
                    System.out.printf("Relative frequency of heads: %.4f%n", (double) countHeads / N);
                    break;
                }
                case "1.3": {
                    // Mô phỏng N lần tung n đồng xu, đếm số lần đổi chuỗi (run)
                    double p = Validation.getDouble(scanner, "Enter p (0 to 1): ", 0, 1);
                    double q = 1 - p;
                    int n = Validation.getPositiveInt(scanner, "Enter number of tosses (n): ");
                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    List<Integer> runs = new ArrayList<>();
                    for (int i = 0; i < N; i++) {
                        runs.add(countRuns(toss(p, n)));
                    }
                    double mean = runs.stream().mapToInt(i -> i).average().orElse(0);
                    double var = runs.stream().mapToDouble(i -> Math.pow(i - mean, 2)).sum() / N;
                    double theoMean = 1 + 2 * (n - 1) * p * q; // Kỳ vọng lý thuyết
                    double theoVar = 2 * p * q * (2 * n - 3 - 2 * p * q * (3 * n - 5)); // Phương sai lý thuyết
                    System.out.printf("Mean runs: %.2f, Variance: %.2f%n", mean, var);
                    System.out.printf("Theoretical mean: %.2f, Theoretical variance: %.2f%n", theoMean, theoVar);
                    break;
                }
                case "1.4": {
                    // Tính kỳ vọng số lần tung cho đến khi số H > số T
                    double p = Validation.getDouble(scanner, "Enter p (0 to 1): ", 0, 1);
                    if (p <= 0.5) {
                        System.out.println("Theoretical expectation is undefined for p <= 0.5 (will diverge).");
                    } else {
                        double theoretical = 1.0 / (2 * p - 1);
                        System.out.printf("Theoretical expected number of tosses (E[X]) = 1 / (2p - 1) = %.4f%n", theoretical);
                    }

                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    int total = 0;
                    for (int i = 0; i < N; i++) {
                        total += tossUntilHeadsExceedsTails(p);
                    }
                    double empirical = (double) total / N;
                    System.out.printf("Simulated expected number of tosses: %.4f%n", empirical);
                    break;
                }

                case "1.5": {
                    // Sinh xâu S(a, b) gồm a ký tự A và b ký tự B, đếm số RunA
                    int a = Validation.getPositiveInt(scanner, "Enter a (number of A's): ");
                    int b = Validation.getPositiveInt(scanner, "Enter b (number of B's): ");
                    long size = binomial(a + b, a); // Số lượng xâu S(a, b)
                    System.out.println("|S(a,b)| = " + size);
                    int N = Validation.getPositiveInt(scanner, "Enter number of simulations (N): ");
                    int totalRunA = 0;
                    for (int i = 0; i < N; i++) {
                        String s = generateRandomS(a, b);
                        totalRunA += countRunA(s);
                    }
                    double mean = (double) totalRunA / N;
                    double exact = (double) a * (b + 1) / (a + b); // Kỳ vọng lý thuyết
                    System.out.printf("Approximate mean RunA: %.2f, Exact mean: %.2f%n", mean, exact);
                    break;
                }
                case "exit":
                    return; // Thoát về menu chính
                default:
                    System.out.println("Invalid choice."); // Nhập sai lựa chọn
            }
        }
    }

    // Mô phỏng tung n đồng xu với xác suất p ra mặt H
    private static List<String> toss(double p, int n) {
        Random rand = new Random();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(rand.nextDouble() < p ? "H" : "T"); // Nếu nhỏ hơn p thì ra H, ngược lại ra T
        }
        return result;
    }

    // Đếm số lần đổi chuỗi (run) trong dãy tung
    private static int countRuns(List<String> tosses) {
        if (tosses.isEmpty()) {
            return 0;
        }
        int runs = 1;
        for (int i = 1; i < tosses.size(); i++) {
            if (!tosses.get(i).equals(tosses.get(i - 1))) {
                runs++;
            }
        }
        return runs;
    }

    // Mô phỏng tung cho đến khi số H > số T, trả về số lần tung
    private static int tossUntilHeadsExceedsTails(double p) {
        Random rand = new Random();
        int heads = 0, tails = 0, total = 0;
        if (++total > 1_000_000) {
            throw new RuntimeException("Simulation did not terminate (possibly p <= 0.5)");
        }
        while (heads <= tails) {
            if (rand.nextDouble() < p) {
                heads++;
            } else {
                tails++;
            }
            total++;
        }
        return total;
    }

    // Sinh ngẫu nhiên xâu S(a, b) gồm a ký tự A và b ký tự B
    private static String generateRandomS(int a, int b) {
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            list.add('A');
        }
        for (int i = 0; i < b; i++) {
            list.add('B');
        }
        Collections.shuffle(list); // Trộn ngẫu nhiên
        StringBuilder sb = new StringBuilder();
        for (char c : list) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Đếm số RunA trong xâu S (mỗi đoạn liên tiếp các ký tự A tính là 1 RunA)
    private static int countRunA(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A' && (i == 0 || s.charAt(i - 1) != 'A')) {
                count++;
            }
        }
        return count;
    }

    // Tính tổ hợp C(n, k)
    private static long binomial(int n, int k) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    // Tính giai thừa n!
    private static long factorial(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }
}
// Kết thúc lớp mô phỏng đồng xu
