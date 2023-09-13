package au.bzea.storepurchase.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RequestTransaction {
    
    private String transactionDate;

    private String description;

    private String usdAmount;

    public RequestTransaction(String transactionDate, String description, String usdAmount) {
        this.transactionDate = transactionDate;
        this.description = description;
        this.usdAmount = usdAmount;
    }

    @Override
	public String toString() {
		return "Transaction [date=" + transactionDate + ", desc=" + description + ", Amount=" + usdAmount + "]";
	}
}
