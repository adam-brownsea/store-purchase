package au.bzea.storepurchase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import au.bzea.storepurchase.model.Transaction;
import au.bzea.storepurchase.exceptions.ResponseEntityBuilder;
import au.bzea.storepurchase.exceptions.RestError;
import au.bzea.storepurchase.model.RequestTransaction;
import au.bzea.storepurchase.model.ResponseTransaction;
import au.bzea.storepurchase.service.FiscalDataService;
import au.bzea.storepurchase.service.Helper;
import au.bzea.storepurchase.service.RequestValidation;
import au.bzea.storepurchase.repository.TransactionRepository;
import au.bzea.storepurchase.service.TransactionService;

import java.util.logging.Logger;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@RestController
public class StorePurchaseController {

    private static Logger logger = Logger.getLogger(StorePurchaseController.class.getName());

    private final TransactionRepository transactionRepository;
    private final FiscalDataService fiscalDataService;

    public StorePurchaseController(TransactionRepository transactionRepository, FiscalDataService fiscalDataService) {
        this.transactionRepository = transactionRepository;
        this.fiscalDataService = fiscalDataService;
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable("id") Long id) {
        return getTransactionById(id, null);
    }

    @GetMapping("/transactions/{id}/{countryCurrency}")
    public ResponseEntity<?> getTransactionById(@PathVariable("id") Long id, 
        @PathVariable("countryCurrency") String countryCurrency) {

        logger.info("Start getTransactionById: " + id);
        
        try {
            // Validate currencies
            countryCurrency = countryCurrency != null ? Helper.decodeString(countryCurrency) : null;
            if (countryCurrency != null && countryCurrency.length() != 0 && !RequestValidation.validateCurrency(countryCurrency)) {
                logger.warning("Invalid country currency: " + countryCurrency);
                return ResponseEntityBuilder.build(new RestError(HttpStatus.BAD_REQUEST, "Invalid country currency"));
            }

            // Get transaction
            TransactionService tranService = new TransactionService(transactionRepository);
            Transaction transaction = tranService.retrieve(id);
            if (transaction == null) {
                RestError restError = new RestError(HttpStatus.BAD_REQUEST, "Transaction not found!");
                return ResponseEntityBuilder.build(restError);
            }

            // Setup response
            ResponseTransaction response = new ResponseTransaction();
            response.setId(transaction.getId());
            response.setTransactionDate(transaction.getTransactionDate());
            response.setDescription(transaction.getDescription());
            response.setUsdAmount(transaction.getUsdAmount());
            response.setCurrency(countryCurrency);

            if (countryCurrency == null || countryCurrency.length() == 0) {
                // No currency then assume USD and use same amount
                response.setCurrency("United States-Dollar");
                response.setAmount(response.getUsdAmount());
                response.setExchangeRate(new BigDecimal(1.00));
            } else {

                //Get conversion rate and calculate amount
                LocalDate transactionDate = null;
                if (transaction.getTransactionDate() != null ) {
                    transactionDate = transaction.getTransactionDate().toLocalDate();
                
                    // Get rate
                    BigDecimal rate = fiscalDataService.getRate(countryCurrency, transactionDate);
                        response.setExchangeRate(rate);
                    // if rate is zero, not rate found so return error.
                    logger.info("Rate: " + rate);
                    if (rate.equals(new BigDecimal(0))) {
                        RestError restError = new RestError(HttpStatus.BAD_REQUEST, "Rate not found for transaction date!");
                        return ResponseEntityBuilder.build(restError);
            }


                    // Calculate result and round
                    BigDecimal amount = rate.multiply(transaction.getUsdAmount());
                    response.setAmount(Helper.roundAmount(amount));
                }
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return new ResponseEntity<>("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactions")
	public ResponseEntity<?> CreateTransaction(@RequestBody RequestTransaction request) {
        logger.info("Start CreateTransaction");
        
        try {
            // Validate Request
            List<String> errors = RequestValidation.validatePostRequest(request);
            if (errors.size() > 0) {

                RestError restError = new RestError(HttpStatus.BAD_REQUEST, "Input validation errors", errors);
                return ResponseEntityBuilder.build(restError);
            }

            // Store transaction		
            System.out.println(request.getUsdAmount());
            Date transactionDate = Date.valueOf(request.getTransactionDate());  
            String description = request.getDescription();
            BigDecimal usdAmount = new BigDecimal(request.getUsdAmount());

			Transaction _transaction = transactionRepository
					.save(new Transaction(transactionDate, 
                        description, 
                        usdAmount));
            System.out.println(_transaction.toString());
			return new ResponseEntity<>(_transaction, HttpStatus.CREATED);
		} catch (Exception e) {
            logger.severe(e.getMessage());
			return new ResponseEntity<>("Error occured", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
