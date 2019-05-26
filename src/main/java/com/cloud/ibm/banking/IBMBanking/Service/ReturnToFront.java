package com.cloud.ibm.banking.IBMBanking.Service;

        import java.util.UUID;

public class ReturnToFront {
    private boolean ifSuccess;
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setIfSuccess(boolean ifSuccess) {
        this.ifSuccess = ifSuccess;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
