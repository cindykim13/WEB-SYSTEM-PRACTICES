package lab8;

public class Account {
    private int id;
    private double balance;

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

    // Các phương thức thay đổi trạng thái không an toàn (Non-thread-safe)
    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    @Override
    public String toString() {
        return String.format("Acc[%d]: %.2f", id, balance);
    }
}