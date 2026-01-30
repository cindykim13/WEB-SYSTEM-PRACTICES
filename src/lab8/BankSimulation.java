package lab8;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankSimulation {
    // ======================================================================
    // >>> ĐIỂM KIỂM SOÁT DUY NHẤT: THAY ĐỔI VERSION TẠI ĐÂY <<<
    // ======================================================================
    private static final ExecutionMode CURRENT_MODE = ExecutionMode.V0_UNSAFE;
    // ======================================================================

    private static final int NUM_ACCOUNTS = 10;
    private static final int NUM_THREADS = 100;
    private static final double INITIAL_BALANCE = 1000;
    private static final int NUM_ITERATIONS = 1000;

    public static void main(String[] args) throws InterruptedException {
        // Truyền mode đã chọn vào Bank
        Bank bank = new Bank(NUM_ACCOUNTS, INITIAL_BALANCE, CURRENT_MODE);
        double initialTotal = bank.getTotalBalance();

        System.out.println("=============================================================");
        System.out.println("RUNNING SIMULATION WITH: " + CURRENT_MODE);
        System.out.println("=============================================================");
        System.out.println("Initial Total Balance: " + initialTotal);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        Random random = new Random();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_ITERATIONS * NUM_THREADS; i++) {
            executor.submit(() -> {
                int from = random.nextInt(NUM_ACCOUNTS);
                int to = random.nextInt(NUM_ACCOUNTS);
                double amount = 10;

                if (from != to) {
                    bank.transfer(from, to, amount);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        double finalTotal = bank.getTotalBalance();

        System.out.println("Simulation finished in " + (endTime - startTime) + "ms");
        System.out.println("Final Total Balance: " + finalTotal);

        if (Math.abs(initialTotal - finalTotal) < 0.001) {
            System.out.println(">>> KẾT QUẢ: ĐÚNG (Thread Safe)");
        } else {
            System.out.println(">>> KẾT QUẢ: SAI LỆCH " + (finalTotal - initialTotal) + " (Not Thread Safe)");
        }
    }
}