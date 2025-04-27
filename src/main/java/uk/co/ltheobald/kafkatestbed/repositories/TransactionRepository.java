package uk.co.ltheobald.kafkatestbed.repositories;

import org.springframework.data.repository.CrudRepository;
import uk.co.ltheobald.kafkatestbed.entities.TransactionEntity;

import java.util.UUID;

public interface TransactionRepository extends CrudRepository<TransactionEntity, UUID> {
}