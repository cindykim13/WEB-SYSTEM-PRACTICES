package lab8;

public class Bank {
    private final Account[] accounts;
    private final ExecutionMode mode; // Lưu lại mode được chọn

    public Bank(int numAccounts, double initialBalance, ExecutionMode mode) {
        this.accounts = new Account[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            accounts[i] = new Account(i, initialBalance);
        }
        this.mode = mode; // Nhận mode từ bên ngoài
    }

    // --- BỘ ĐIỀU PHỐI (DISPATCHER) ---
    // Phương thức public duy nhất, gọi đến logic private tương ứng
    public void transfer(int fromId, int toId, double amount) {
        switch (mode) {
            case V0_UNSAFE:
                transferV0_Unsafe(fromId, toId, amount); // Thêm case này
                break;
            case V1_SYNC_METHOD:
                transferV1_SyncMethod(fromId, toId, amount);
                break;
            case V2_SYNC_BLOCK:
                transferV2_SyncBlock(fromId, toId, amount);
                break;
            case V3_REENTRANT_LOCK:
                transferV3_ReentrantLock(fromId, toId, amount);
                break;
        }
    }
    // VERSION 0: Logic Lỗi (Copy từ thành quả Giai đoạn 2)
    private void transferV0_Unsafe(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        if (from.getBalance() >= amount) {
            try {
                // MÔ PHỎNG LỖI RACE CONDITION (Lost Update)
                // Tách rời Read và Write, chèn độ trễ vào giữa

                // Rút tiền
                double currentFrom = from.getBalance();
                Thread.sleep(10); // Cố tình dừng để thread khác chen vào đọc dữ liệu cũ
                from.setBalance(currentFrom - amount);

                // Nạp tiền
                double currentTo = to.getBalance();
                // Thread.sleep(10); // Có thể sleep hoặc không
                to.setBalance(currentTo + amount);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // --- VERSION 1: SYNCHRONIZED METHOD ---
    private synchronized void transferV1_SyncMethod(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];
        if (from.getBalance() >= amount) {
            try {
                from.setBalance(from.getBalance() - amount);
                Thread.sleep(1);
                to.setBalance(to.getBalance() + amount);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    // --- VERSION 2: SYNCHRONIZED BLOCK + LOCK ORDERING ---
    private void transferV2_SyncBlock(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];
        Account firstLock = (from.getId() < to.getId()) ? from : to;
        Account secondLock = (from.getId() < to.getId()) ? to : from;
        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.getBalance() >= amount) {
                    try {
                        from.setBalance(from.getBalance() - amount);
                        Thread.sleep(1);
                        to.setBalance(to.getBalance() + amount);
                    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            }
        }
    }

    // --- VERSION 3: REENTRANTLOCK ---
    private void transferV3_ReentrantLock(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];
        Account firstLock = (from.getId() < to.getId()) ? from : to;
        Account secondLock = (from.getId() < to.getId()) ? to : from;

        firstLock.getLock().lock();
        try {
            secondLock.getLock().lock();
            try {
                if (from.getBalance() >= amount) {
                    try {
                        from.setBalance(from.getBalance() - amount);
                        Thread.sleep(1);
                        to.setBalance(to.getBalance() + amount);
                    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            } finally {
                secondLock.getLock().unlock();
            }
        } finally {
            firstLock.getLock().unlock();
        }
    }

    // Phương thức tiện ích không đổi
    public double getTotalBalance() {
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        return total;
    }
}