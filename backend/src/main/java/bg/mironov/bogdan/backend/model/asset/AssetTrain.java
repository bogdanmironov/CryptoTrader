package bg.mironov.bogdan.backend.model.asset;

import java.math.BigDecimal;

public class AssetTrain {
    private String symbol;
    private BigDecimal quantity;

    public AssetTrain(String symbol) {
        this(symbol, BigDecimal.ZERO);
    }

    public AssetTrain(String symbol, BigDecimal quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Asset toAsset() {
        return new Asset(-1, -1, symbol, quantity);
    }
}
