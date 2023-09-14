package au.bzea.storepurchase.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CurrencyRate {

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDesc;

    @JsonProperty("exchange_rate")
    private String exchangeRate;
    
    @JsonProperty("effective_date")
    private String effectiveDate;

    public CurrencyRate(String countryCurrencyDesc, String exchangeRate, String effectiveDate) {
        this.countryCurrencyDesc = countryCurrencyDesc;
        this.exchangeRate = exchangeRate;
        this.effectiveDate = effectiveDate;
    }

    @Override
	public String toString() {
		return "countryCurrencyDesc=" + countryCurrencyDesc + ", exchangeRate=" + exchangeRate + ", effectiveDate=" + effectiveDate;
	}
}
