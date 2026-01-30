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

    // VERSION 1: SYNCHRONIZED METHOD (Coarse-grained Locking)
    // Từ khóa synchronized khóa toàn bộ đối tượng Bank (this)
    public synchronized void transfer(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        if (from.getBalance() >= amount) {
            try {
                // Logic cũ gây lỗi, nay đã được bọc trong synchronized

                // 1. Rút tiền
                double currentFrom = from.getBalance();
                Thread.sleep(1); // Mô phỏng độ trễ xử lý
                from.setBalance(currentFrom - amount);

                // 2. Nạp tiền
                double currentTo = to.getBalance();
                // Thread.sleep(1);
                to.setBalance(currentTo + amount);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    /*
    // VERSION 2: SYNCHRONIZED BLOCK + LOCK ORDERING
    public void transfer(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        // --- CƠ CHẾ CHỐNG DEADLOCK: LOCK ORDERING ---
        // Luôn khóa tài khoản có ID nhỏ hơn trước
        Account firstLock = (from.getId() < to.getId()) ? from : to;
        Account secondLock = (from.getId() < to.getId()) ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                // CRITICAL SECTION: Chỉ 2 tài khoản này bị khóa, các tài khoản khác vẫn hoạt động
                if (from.getBalance() >= amount) {
                    try {
                        double currentFrom = from.getBalance();
                        // Dù có sleep bao lâu, Thread khác muốn đụng vào 2 acc này cũng phải chờ
                        // Nhưng Thread đụng vào acc khác thì thoải mái -> Hiệu năng cao
                        Thread.sleep(1);
                        from.setBalance(currentFrom - amount);

                        double currentTo = to.getBalance();
                        to.setBalance(currentTo + amount);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    */

    /*
    // VERSION 3: REENTRANT LOCK (Explicit Lock)
    public void transfer(int fromId, int toId, double amount) {
        Account from = accounts[fromId];
        Account to = accounts[toId];

        // Vẫn phải áp dụng Lock Ordering để tránh Deadlock
        Account firstLock = (from.getId() < to.getId()) ? from : to;
        Account secondLock = (from.getId() < to.getId()) ? to : from;

        // BẮT ĐẦU KHÓA
        firstLock.getLock().lock(); // Khóa 1
        try {
            secondLock.getLock().lock(); // Khóa 2
            try {
                // CRITICAL SECTION
                if (from.getBalance() >= amount) {
                    try {
                         double currentFrom = from.getBalance();
                         Thread.sleep(1);
                         from.setBalance(currentFrom - amount);

                         double currentTo = to.getBalance();
                         to.setBalance(currentTo + amount);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                // QUAN TRỌNG: Phải unlock trong finally
                secondLock.getLock().unlock();
            }
        } finally {
            // QUAN TRỌNG: Phải unlock trong finally
            firstLock.getLock().unlock();
        }
    }
    */
    public double getTotalBalance() {
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        return total;
    }
}