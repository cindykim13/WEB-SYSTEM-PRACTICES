package lab8;

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

    public void transfer(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        // Chúng ta thao tác trực tiếp trên biến local để mở rộng "Race Window"
        // Mô phỏng chính xác quy trình Load-Store của CPU

        if (from.getBalance() >= amount) {
            try {
                // ---------------------------------------------------------
                // BƯỚC 1: RÚT TIỀN (SOURCE) - CỐ TÌNH GÂY LỖI
                // ---------------------------------------------------------

                // 1. READ: Đọc dữ liệu từ RAM vào "CPU Register" (biến local)
                double currentFromBalance = from.getBalance();

                // 2. CONTEXT SWITCH FORCED: Ngừng lại để luồng khác chen vào
                // 10ms là khoảng thời gian "vô tận" với CPU, đảm bảo va chạm.
                Thread.sleep(10);

                // 3. MODIFY & WRITE: Tính toán và ghi lại vào RAM
                double newFromBalance = currentFromBalance - amount;
                from.setBalance(newFromBalance);

                // ---------------------------------------------------------
                // BƯỚC 2: NẠP TIỀN (DESTINATION) - CỐ TÌNH GÂY LỖI
                // ---------------------------------------------------------

                // 1. READ
                double currentToBalance = to.getBalance();

                // 2. CONTEXT SWITCH FORCED
                // Không cần sleep ở đây cũng được, nhưng thêm vào cho chắc chắn lỗi
                // Thread.sleep(10);

                // 3. MODIFY & WRITE
                double newToBalance = currentToBalance + amount;
                to.setBalance(newToBalance);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        return total;
    }
}