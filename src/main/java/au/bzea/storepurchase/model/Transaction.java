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

@Entity
@Table(name = "transaction")
@Getter @Setter @NoArgsConstructor
public class Transaction {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_date", nullable = false)
    private Date transactionDate;

    @Column(name = "description")
    private String description;

    @Column(name = "usd_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal usdAmount;

    @Transient
    private String currency;

    @Transient
    private BigDecimal amount; 

    @Transient
    private BigDecimal exchangeRate; 

    public Transaction(Date transactionDate, String description, BigDecimal usdAmount) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.usdAmount = usdAmount;
    }

    @Override
	public String toString() {
		return "Transaction [id=" + id + ", date=" + transactionDate.toString() + ", desc=" + description + ", Amount=" + usdAmount + "]";
	}
}
