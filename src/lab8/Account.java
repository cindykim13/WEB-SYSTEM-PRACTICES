package lab8;

public class Account {
    private int id;
    private double balance;

    public Account(int id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() { return id; }

    // Phương thức đọc
    public double getBalance() { return balance; }

    // --- THÊM MỚI: Phương thức ghi đè (Setter) ---
    // Cho phép thiết lập số dư trực tiếp, phục vụ việc mô phỏng lỗi
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Giữ nguyên các hàm cũ nếu muốn, nhưng ta sẽ không dùng chúng trong Bank nữa
    public void withdraw(double amount) { this.balance -= amount; }
    public void deposit(double amount) { this.balance += amount; }
}