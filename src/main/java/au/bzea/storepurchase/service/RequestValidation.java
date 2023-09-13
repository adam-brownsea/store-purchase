package au.bzea.storepurchase.service;

import au.bzea.storepurchase.model.RequestTransaction;
import java.util.List;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class RequestValidation {
    private static Logger logger = Logger.getLogger(RequestValidation.class.getName());

    public static List<String> validate(RequestTransaction request) {
        List<String> errors = new ArrayList<String>();

        // Can't check if transaction empty as has required fields
        if (request == null) {
            logger.warning("request empty");
            errors.add("Empty request");
            return errors;
        }

        LocalDate transactionDate = null;
        if (request.getTransactionDate() == null ) {
            // check transaction date is provided
            logger.warning("date not provided");
            errors.add("Transaction date is required");
        } else {
            try {
                // try to format the date
                transactionDate = LocalDate.parse(request.getTransactionDate());        

                // check transaction date not in future
                if (transactionDate.isAfter(LocalDate.now())) {
                    logger.warning("future date");
                    errors.add("Transaction date cannot be in the future");
                }
            } catch (Exception e) {
                logger.warning("invalid date");
                errors.add("Transaction date is not valid date");
            }

        }

        // Description <= size 50
        if (request.getDescription() != null && request.getDescription().length() > 50) {
            logger.warning("description too long");
            errors.add("Description is greater than 50 characters");
        }

        // USD Amount 
        if (request.getUsdAmount() == null ) {
            // check USD amount  is provided
            logger.warning("date not provided");
            errors.add("USD amount is not provided");
        }
        String amount = request.getUsdAmount();
        try {
            BigDecimal usdAmount = new BigDecimal(amount);
            logger.info("is amount: " + usdAmount.toString());
            if (amount.contains(".") 
                && amount.substring(amount.indexOf(".") + 1).length() > 2) {
                logger.warning("too many decimals");
                errors.add("USD Amount has more than 2 decimals");        
            }
        } catch (Exception e) {
            logger.warning("invalid decimal");
            errors.add("USD Amount is not dollar amount");
        }
        
        return errors;
    }
}
