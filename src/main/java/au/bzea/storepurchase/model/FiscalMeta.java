package au.bzea.storepurchase.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@Entity
@Getter @Setter @NoArgsConstructor
public class FiscalMeta {
    private String count;
    private CurrencyRate labels;
    private CurrencyRate dataTypes;
    private CurrencyRate dataFormats;
    @JsonProperty("total-count")
    private String totalCount;
    @JsonProperty("total-pages")
    private String totalPages;
}
