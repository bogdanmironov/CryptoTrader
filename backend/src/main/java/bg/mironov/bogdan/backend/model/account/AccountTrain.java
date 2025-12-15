package bg.mironov.bogdan.backend.model.account;

import java.math.BigDecimal;

public class AccountTrain {
    private BigDecimal balance;

    public AccountTrain(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account toAccount() {
        return new Account(-1, null, balance);
    }
}
