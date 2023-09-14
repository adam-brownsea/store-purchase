package au.bzea.storepurchase.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.bzea.storepurchase.model.RequestTransaction;
import au.bzea.storepurchase.model.Transaction;
import au.bzea.storepurchase.repository.TransactionRepository;

@Service
public class TransactionService {
    private static Logger logger = Logger.getLogger(TransactionService.class.getName());

    @Autowired
    TransactionRepository transactionRepository;
    
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Retrieve transaction
    public Transaction retrieve(Long id){
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            logger.info("Present: " + transaction.isPresent());
            return transaction.isPresent() ? transaction.get() :  null;
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    // Store transaction
    public Transaction create(RequestTransaction request) {
       // Store transaction
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
			return _transaction;
		} catch (Exception e) {
            System.out.println(e.getMessage());
			return new Transaction();
		}
    }
}
