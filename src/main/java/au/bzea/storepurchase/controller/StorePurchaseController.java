package au.bzea.storepurchase.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
import au.bzea.storepurchase.repository.TransactionRepository;
import au.bzea.storepurchase.service.Currencies;
import au.bzea.storepurchase.service.RequestValidation;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;


@RestController
public class StorePurchaseController {
    private static Logger logger = Logger.getLogger(StorePurchaseController.class.getName());

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) {
        logger.info("Start getTransactionById: " + id);
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            logger.info("Present: " + transaction.isPresent());
                return transaction.isPresent() ? new ResponseEntity<>(transaction.get(), HttpStatus.OK) 
            : new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //return "";
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        logger.info("Start getAllTransactions");
        try {
            List<Transaction> transactions = new ArrayList<Transaction>();
            transactionRepository.findAll().forEach(transactions::add);

            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactions")
	public ResponseEntity<?> CreateTransaction(@RequestBody RequestTransaction request) {
        logger.info("Start CreateTransaction");

        List<String> errors = RequestValidation.validate(request);
        if (errors.size() > 0) {

            RestError restError = new RestError();
            restError.setMessage("Input validation errors");
            restError.setErrors(errors);
            restError.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntityBuilder.build(restError);
        }

		try {
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
            System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
