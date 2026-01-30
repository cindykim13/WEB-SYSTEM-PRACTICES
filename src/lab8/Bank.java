package lab8;

import java.util.Random;

public class Bank {
    private final Account[] accounts;

    public Bank(int numAccounts, double initialBalance) {
        accounts = new Account[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            accounts[i] = new Account(i, initialBalance);
        }
    }

    public Account getAccount(int id) {
        return accounts[id];
    }

    /**
     * Phương thức chuyển tiền KHÔNG thread-safe.
     * Logic kiểm tra và trừ tiền bị tách rời, cộng thêm sleep để dễ gây lỗi.
     */
    public void transfer(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        // CHECK-THEN-ACT: Kiểm tra số dư
        if (from.getBalance() >= amount) {

            // CRITICAL SECTION BẮT ĐẦU
            try {
                // 1. Trừ tiền tài khoản nguồn
                from.withdraw(amount);

                // 2. GIẢ LẬP ĐỘ TRỄ (Tăng khả năng tranh chấp)
                // Thread nhường CPU tại đây, tạo cơ hội cho Thread khác chen vào
                Thread.sleep(1);

                // 3. Cộng tiền tài khoản đích
                to.deposit(amount);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // CRITICAL SECTION KẾT THÚC

            // System.out.println("Transferred " + amount + " from " + fromId + " to " + toId);
        }
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        return total;
    }

    public int size() {
        return accounts.length;
    }
}