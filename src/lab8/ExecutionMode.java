package lab8;

/**
 * Enum định danh các phiên bản giải pháp Thread Safety.
 * Dùng để cấu hình cho BankSimulation.
 */
public enum ExecutionMode {
    V0_UNSAFE("Version 0: Unsafe (Non-Atomic / Race Condition)"), // Thêm dòng này
    V1_SYNC_METHOD("Version 1: Synchronized Method (Khóa Thô)"),
    V2_SYNC_BLOCK("Version 2: Synchronized Block + Lock Ordering (Khóa Mịn)"),
    V3_REENTRANT_LOCK("Version 3: ReentrantLock (Khóa Tường Minh)");

    private final String description;

    ExecutionMode(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}