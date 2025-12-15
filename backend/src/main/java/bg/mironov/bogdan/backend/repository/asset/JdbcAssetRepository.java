package bg.mironov.bogdan.backend.repository.asset;

import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.asset.NewAsset;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class JdbcAssetRepository implements AssetRepository {
    private final JdbcClient jdbc;

    public JdbcAssetRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Asset> findByAccountId(long accountId) {
        return jdbc.sql("""
                    SELECT id, account_id, symbol, quantity FROM asset
                    WHERE account_id = ? ORDER BY symbol
                """)
            .param(accountId)
            .query(Asset.class)
            .list();
    }

    @Override
    public Asset findByAccountAndSymbolForUpdate(long accountId, String symbol) {
        return jdbc.sql("""
                    SELECT id, account_id, symbol, quantity FROM asset
                    WHERE account_id = ? AND symbol = ?
                    FOR UPDATE
                """)
            .params(accountId, symbol)
            .query(Asset.class)
            .optional()
            .orElse(null);
    }

    @Override
    public Asset findByAccountAndSymbol(long accountId, String symbol) {
        return jdbc.sql("""
                    SELECT id, account_id, symbol, quantity FROM asset
                    WHERE account_id = ? AND symbol = ?
                """)
            .params(accountId, symbol)
            .query(Asset.class)
            .optional()
            .orElse(null);
    }

    @Override
    public void updateQuantity(long assetId, BigDecimal newQuantity) {
        jdbc.sql("""
                    UPDATE asset SET quantity = ? WHERE id = ?
                """)
            .params(newQuantity, assetId)
            .update();
    }

    @Override
    public void insert(NewAsset newAsset) {
        jdbc.sql("""
                    INSERT INTO asset (account_id, symbol, quantity)
                    VALUES (?, ?, ?)
                """)
            .params(newAsset.accountId(), newAsset.symbol(), newAsset.quantity())
            .update();
    }
}
