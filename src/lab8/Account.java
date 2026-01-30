package lab8;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final int id;
    private double balance;

    // Lock riêng cho từng đối tượng Account (Dùng cho Version 3)
    private final Lock lock = new ReentrantLock();

    public Account(int id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    // Phương thức này dùng để mô phỏng lỗi (ghi đè trực tiếp)
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Getter cho Lock
    public Lock getLock() {
        return lock;
    }

    @Override
    public String toString() {
        return String.format("Acc[%d]: %.2f", id, balance);
    }
}