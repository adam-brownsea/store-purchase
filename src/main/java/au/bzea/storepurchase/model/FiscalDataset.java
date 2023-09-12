package au.bzea.storepurchase.model;

import java.util.List;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter @Setter @NoArgsConstructor
public class FiscalDataset {
    private List<CurrencyRate> data;
    private FiscalMeta meta;
    private FiscalLinks links;

}
