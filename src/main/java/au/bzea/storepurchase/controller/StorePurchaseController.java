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
import au.bzea.storepurchase.repository.TransactionRepository;
import au.bzea.storepurchase.service.Currencies;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@RestController
public class StorePurchaseController {
    private static Logger logger = Logger.getLogger(StorePurchaseController.class.getName());

    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/currencies")
    public static String testCurrencies() {
        try {
            List<String> cc = Currencies.getCurrencies();
            return cc.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return e.getMessage();
        }
        
    }
    
    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) {
        System.out.println(id);
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            System.out.println("Present: " + transaction.isPresent());
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
        try {

            List<Transaction> transactions = new ArrayList<Transaction>();
            transactionRepository.findAll().forEach(transactions::add);

            System.out.println("test");
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //return "";
    }

    @PostMapping("/transactions")
	public ResponseEntity<Transaction> createTutorial(@RequestBody Transaction transaction) {
		try {
            System.out.println(transaction.getUsdAmount());
			Transaction _transaction = transactionRepository
					.save(new Transaction(
                        transaction.getId(), 
                        transaction.getTransactionDate(), 
                        transaction.getDescription(), 
                        transaction.getUsdAmount()));
            System.out.println(_transaction.toString());
			return new ResponseEntity<>(_transaction, HttpStatus.CREATED);
		} catch (Exception e) {
            System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
