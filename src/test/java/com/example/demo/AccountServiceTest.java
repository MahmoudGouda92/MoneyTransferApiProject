package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import com.example.demo.exception.AccountNotExistException;
import com.example.demo.exception.OverDraftException;
import com.example.demo.exception.SystemException;
import com.example.demo.model.Account;
import com.example.demo.model.TransferRequest;
import com.example.demo.repository.AccountsRepository;
import com.example.demo.service.AccountsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@Mock
	AccountsRepository accRepo;
	
	@InjectMocks
	AccountsService accService;
	
	@Test
	public void testRetrieveBalance() {
		when(accRepo.findById("1")).thenReturn(Optional.of(new Account("1","Gouda", BigDecimal.ONE)));
		
		assertEquals(BigDecimal.ONE, accService.retrieveBalances("1").getBalance());
	}
	
	@Test(expected = AccountNotExistException.class)
	public void testRetrieveBalanceFromInvalidAccount() {
		when(accRepo.findById("1")).thenReturn(Optional.empty());
		
		accService.retrieveBalances("1");
	}
	
	@Test
	public void testTransferBalance() throws Exception, Exception, Exception {
		String accountFromId = "1";
		String accountFromTo = "2";
		BigDecimal amount = new BigDecimal(10);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId,"Gouda", BigDecimal.TEN);
		Account accTo = new Account(accountFromId,"Gouda", BigDecimal.TEN);
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		
		accService.transferBalances(request);
		
		assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}
	
	@Test(expected = OverDraftException.class)
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException, SystemException {
		String accountFromId = "1";
		String accountFromTo = "2";
		BigDecimal amount = new BigDecimal(20);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId,"Gouda", BigDecimal.TEN);
		Account accTo = new Account(accountFromId,"Gouda", BigDecimal.TEN);
		
		when(accRepo.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accRepo.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		
		accService.transferBalances(request);
	}
}
