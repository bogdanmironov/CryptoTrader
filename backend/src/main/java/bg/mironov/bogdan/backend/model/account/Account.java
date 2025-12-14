package bg.mironov.bogdan.backend.model.account;

import java.math.BigDecimal;
import java.util.UUID;

public record Account (
    long id,
    UUID publicId,
    BigDecimal balance
){
}
