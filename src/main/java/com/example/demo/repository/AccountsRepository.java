package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Account;

@Transactional(readOnly = true)
public interface AccountsRepository extends JpaRepository<Account, Long>{

	Optional<Account> findById(String id);
		
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	@Query("SELECT a FROM Account a WHERE a.id = ?1")
	Optional<Account> getAccountForUpdate(String id);
	
}
