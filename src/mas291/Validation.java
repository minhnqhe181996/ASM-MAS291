
// Lớp hỗ trợ kiểm tra và nhập dữ liệu từ người dùng một cách an toàn
import java.util.Scanner;

public class Validation {

    // Hàm nhập số thực trong khoảng [min, max] từ người dùng
    public static double getDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value; // Giá trị hợp lệ
                }
                System.out.printf("Value must be between %.2f and %.2f.%n", min, max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Hàm nhập số nguyên dương từ người dùng
    public static int getPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                int value = Integer.parseInt(line);
                if (value > 0) {
                    return value; // Giá trị hợp lệ
                }
                System.out.println("Number must be positive.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    // Hàm nhập một hoán vị n số từ 1 đến n, kiểm tra hợp lệ
    public static int[] getPermutation(Scanner scanner, int n) {
        while (true) {
            try {
                System.out.print("Enter permutation of " + n + " unique integers (1 to " + n + "): ");
                String[] tokens = scanner.nextLine().trim().split("\\s+");
                if (tokens.length != n) {
                    System.out.println("Exactly " + n + " values are required.");
                    continue;
                }
                int[] perm = new int[n];
                boolean[] seen = new boolean[n + 1]; // Đánh dấu các số đã xuất hiện
                for (int i = 0; i < n; i++) {
                    int val = Integer.parseInt(tokens[i]);
                    if (val < 1 || val > n || seen[val]) {
                        throw new IllegalArgumentException(); // Giá trị không hợp lệ hoặc trùng lặp
                    }
                    perm[i] = val;
                    seen[val] = true;
                }
                return perm; // Trả về hoán vị hợp lệ
            } catch (Exception e) {
                System.out.println("Invalid permutation. Please enter unique integers between 1 and " + n + ".");
            }
        }
    }

}
// Kết thúc lớp kiểm tra dữ liệu
