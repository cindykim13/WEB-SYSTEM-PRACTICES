package lab8;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankSimulation {
    private static final int NUM_ACCOUNTS = 10;
    private static final double INITIAL_BALANCE = 1000;
    private static final int NUM_THREADS = 200; // Số lượng luồng đồng thời
    private static final int NUM_ITERATIONS = 1000; // Mỗi luồng thực hiện bao nhiêu giao dịch

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank(NUM_ACCOUNTS, INITIAL_BALANCE);
        double initialTotal = bank.getTotalBalance();

        System.out.println("Initial Total Balance: " + initialTotal);
        System.out.println("Starting simulation with " + NUM_THREADS + " threads...");

        // Tạo Thread Pool
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        Random random = new Random();

        long startTime = System.currentTimeMillis();

        // Submit các task chuyển tiền
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            executor.submit(() -> {
                int from = random.nextInt(NUM_ACCOUNTS);
                int to = random.nextInt(NUM_ACCOUNTS);
                double amount = 10; // Chuyển số tiền cố định để dễ theo dõi

                if (from != to) {
                    bank.transfer(from, to, amount);
                }
            });
        }

        // Đóng executor và chờ tất cả task hoàn thành
        executor.shutdown();
        // Chờ tối đa 1 phút. Đây là bước quan trọng để đảm bảo tính toán xong mới in kết quả.
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();

        System.out.println("Simulation finished in " + (endTime - startTime) + "ms");
        System.out.println("Did all tasks finish? " + finished);

        double finalTotal = bank.getTotalBalance();
        System.out.println("Final Total Balance: " + finalTotal);

        System.out.println("--------------------------------------------------");
        if (initialTotal == finalTotal) {
            System.out.println("KẾT QUẢ: ĐÚNG (Nhưng đáng ngờ nếu không có lock)");
        } else {
            System.out.println("KẾT QUẢ: SAI LỆCH -> " + (finalTotal - initialTotal));
            System.out.println("CHỨNG MINH THÀNH CÔNG: Hệ thống không thread-safe!");
        }
    }
}