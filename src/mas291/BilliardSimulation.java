
import java.util.*;

public class BilliardSimulation {

    // Hàm chạy menu cho bài toán bi-a
    public static void run(Scanner scanner) {
        Random rand = new Random();
        while (true) {
            System.out.print("Enter subproblem (3.1, 3.2, 3.2b, or 'exit' to return): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "3.1": {
                    // Nhập các tham số vị trí và góc xuất phát
                    double a = Validation.getDouble(scanner, "Enter a (0 to 1): ", 0, 1);
                    double b = Validation.getDouble(scanner, "Enter b (0 to 1): ", 0, 1);
                    double x = Validation.getDouble(scanner, "Enter x (0 to 2 c0): ", 0, 2 * Math.PI);
                    // Tính điểm tiếp xúc đầu tiên và góc mới sau va chạm
                    double[] res = firstContact(a, b, x, 1.0);
                    System.out.printf("First contact point: (%.4f, %.4f), New direction angle: %.4f radians%n", res[0], res[1], res[2]);
                    break;
                }
                case "3.2": {
                    // Mô phỏng nhiều lần để tính kỳ vọng vị trí trả về trên bàn vuông
                    int N = Validation.getPositiveInt(scanner, "Enter number of trials (N): ");
                    double total = 0;
                    for (int i = 0; i < N; i++) {
                        double a = rand.nextDouble() * 0.5;
                        double x = rand.nextDouble() * Math.PI;
                        total += simulateUntilOA(a, x, 1.0);
                    }
                    System.out.printf("Expected return position E(R) [square table]: %.4f%n", total / N);
                    break;
                }
                case "3.2b": {
                    // Mô phỏng với bàn hình chữ nhật chiều cao l
                    double l = Validation.getDouble(scanner, "Enter rectangle height l (>0): ", 0.0001, 100);
                    int N = Validation.getPositiveInt(scanner, "Enter number of trials (N): ");
                    double total = 0;
                    for (int i = 0; i < N; i++) {
                        double a = rand.nextDouble() * 0.5;
                        double x = rand.nextDouble() * Math.PI;
                        total += simulateUntilOA(a, x, l);
                    }
                    System.out.printf("Expected return position E(R) [height l=%.2f]: %.4f%n", l, total / N);
                    break;
                }
                case "exit":
                    return; // Thoát về menu chính
                default:
                    System.out.println("Invalid choice."); // Nhập sai lựa chọn
            }
        }
    }

    // Tính điểm tiếp xúc đầu tiên của bi với thành bàn và góc phản xạ mới
    // a, b: vị trí xuất phát; x: góc xuất phát; l: chiều cao bàn
    private static double[] firstContact(double a, double b, double x, double l) {
        double dx = Math.cos(x), dy = Math.sin(x); // Tính thành phần vận tốc theo trục x, y
        double t = Double.POSITIVE_INFINITY, c = a, d = b, angle = x;

        // Xét va chạm với các thành bàn
        if (dx > 0) {
            double t1 = (1 - a) / dx, y1 = b + t1 * dy;
            if (y1 >= 0 && y1 <= l && t1 < t) {
                t = t1;
                c = 1;
                d = y1;
                angle = Math.PI - x; // Phản xạ qua trục đứng
            }
        }
        if (dx < 0) {
            double t2 = -a / dx, y2 = b + t2 * dy;
            if (y2 >= 0 && y2 <= l && t2 < t) {
                t = t2;
                c = 0;
                d = y2;
                angle = Math.PI - x;
            }
        }
        if (dy > 0) {
            double t3 = (l - b) / dy, x3 = a + t3 * dx;
            if (x3 >= 0 && x3 <= 1 && t3 < t) {
                t = t3;
                c = x3;
                d = l;
                angle = -x; // Phản xạ qua trục ngang
            }
        }
        if (dy < 0) {
            double t4 = -b / dy, x4 = a + t4 * dx;
            if (x4 >= 0 && x4 <= 1 && t4 < t) {
                t = t4;
                c = x4;
                d = 0;
                angle = -x;
            }
        }

        // Đảm bảo góc nằm trong [0, 2pi)
        angle = (angle % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI);
        return new double[]{c, d, angle}; // Trả về vị trí tiếp xúc và góc mới
    }

    // Mô phỏng cho đến khi bi quay lại cạnh OA (d = 0), trả về vị trí c trên cạnh đó
    private static double simulateUntilOA(double a, double x, double l) {
        double c = a, d = 0, angle = x;
        for (int i = 0; i < 1000; i++) { // Giới hạn số lần lặp để tránh vòng lặp vô hạn
            double[] res = firstContact(c, d, angle, l);
            c = res[0];
            d = res[1];
            angle = res[2];
            if (Math.abs(d) < 1e-10) { // Nếu bi chạm cạnh OA
                return c;
            }
        }
        return -1; // Trường hợp không quay lại cạnh OA sau 1000 lần
    }
}
// Kết thúc lớp mô phỏng bi-a
