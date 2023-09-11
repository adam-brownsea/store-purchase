package au.bzea.storepurchase.repository;

import au.bzea.storepurchase.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
