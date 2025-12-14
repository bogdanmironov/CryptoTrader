package bg.mironov.bogdan.backend.repository.asset;

import bg.mironov.bogdan.backend.model.asset.Asset;
import bg.mironov.bogdan.backend.model.asset.NewAsset;

import java.math.BigDecimal;
import java.util.List;

public interface AssetRepository {
    List<Asset> findByAccountId(long accountId);

    Asset findByAccountAndSymbolForUpdate(long accountId, String symbol);

    void updateQuantity(long assetId, BigDecimal newQuantity);

    void insert(NewAsset newAsset);

}
