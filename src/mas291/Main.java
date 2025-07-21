
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) { // Vòng lặp chính, lặp lại cho đến khi người dùng chọn thoát
            System.out.println("\n=== MAS291 - SU25 Computer Project ===");
            System.out.println("1. Problem 1: Coin Simulation"); // Bài toán 1: Mô phỏng tung đồng xu
            System.out.println("2. Problem 2: Permutation Analysis"); // Bài toán 2: Phân tích hoán vị
            System.out.println("3. Problem 3: Billiard Simulation"); // Bài toán 3: Mô phỏng bi-a
            System.out.println("0. Exit"); // Thoát chương trình
            System.out.print("Choose a problem (0-3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) { // Xử lý lựa chọn của người dùng
                case "1":
                    CoinSimulation.run(scanner); // Gọi hàm chạy bài toán 1
                    break;
                case "2":
                    PermutationAnalysis.run(scanner); // Gọi hàm chạy bài toán 2
                    break;
                case "3":
                    BilliardSimulation.run(scanner); // Gọi hàm chạy bài toán 3
                    break;
                case "0":
                    System.out.println("Goodbye!"); // Thông báo tạm biệt
                    return; // Thoát khỏi hàm main, kết thúc chương trình
                default:
                    System.out.println("Invalid choice. Try again."); // Thông báo nhập sai
            }
        }
    }
}
// Kết thúc chương trình chính
