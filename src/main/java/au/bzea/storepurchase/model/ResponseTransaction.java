package au.bzea.storepurchase.model;

import java.sql.Date;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;

@Getter @Setter @NoArgsConstructor
public class ResponseTransaction {
    
    private Long id;

    private Date transactionDate;

    private String description;

    private BigDecimal usdAmount;

    private String currency;

    private BigDecimal amount; 

    private BigDecimal exchangeRate; 

    public ResponseTransaction(Long id, Date transactionDate, String description, BigDecimal usdAmount) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.description = description;
        this.usdAmount = usdAmount;
    }

    @Override
	public String toString() {
		return "ResponseTransaction [id=" + id + ", date=" + transactionDate.toString() + ", desc=" + description + ", Amount=" + usdAmount + "]";
	}
}
