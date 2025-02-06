package net.ninjadev.resilience.service;

import net.ninjadev.resilience.entity.Account;
import net.ninjadev.resilience.entity.ResilienceUser;
import net.ninjadev.resilience.entity.transaction.RecurringTransaction;
import net.ninjadev.resilience.entity.transaction.Transaction;
import net.ninjadev.resilience.entity.transaction.TransactionFrequency;
import net.ninjadev.resilience.repository.AccountRepository;
import net.ninjadev.resilience.repository.ResilienceUserRepository;
import net.ninjadev.resilience.repository.transaction.RecurringTransactionRepository;
import net.ninjadev.resilience.repository.transaction.TransactionRepository;
import net.ninjadev.resilience.request.AddAccountRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AutoCloseable closeable;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RecurringTransactionRepository recurringTransactionRepository;

    @Mock
    private ResilienceUserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFindById_AccountExists() {
        Account mockAccount = new Account();
        mockAccount.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

        Optional<Account> result = accountService.findById("1");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_AccountDoesNotExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.findById("1");

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testAdd_AccountDoesNotExist() {
        AddAccountRequest request = new AddAccountRequest();
        request.setAccountNumber("12345");
        Account newAccount = new Account();
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        Optional<Account> result = accountService.add(request);

        assertTrue(result.isPresent());
        verify(accountRepository, times(1)).findByAccountNumber("12345");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testAdd_AccountAlreadyExists() {
        AddAccountRequest request = new AddAccountRequest();
        request.setAccountNumber("12345");
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(new Account()));

        Optional<Account> result = accountService.add(request);

        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findByAccountNumber("12345");
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testAddUserToAccount_UserAndAccountValid() {
        ResilienceUser user = new ResilienceUser();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Account account = new Account();
        account.setUsers(new HashSet<>());
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.addUserToAccount(user, account);

        assertTrue(account.getUsers().contains(user));
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testAddUserToAccount_UserNotFound() {
        ResilienceUser user = new ResilienceUser();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Account account = new Account();
        account.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.addUserToAccount(user, account);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
        verify(accountRepository, never()).save(account);
    }

    @Test
    void testGetByUserId_UserExists() {
        ResilienceUser user = new ResilienceUser();
        user.setId(1L);

        Account mockAccount = new Account();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUsers(Set.of(user))).thenReturn(List.of(mockAccount));

        List<Account> result = accountService.getByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(mockAccount, result.get(0));
        verify(accountRepository, times(1)).findByUsers(Set.of(user));
    }

    @Test
    void testGetByUserId_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        List<Account> result = accountService.getByUserId(1L);

        assertTrue(result.isEmpty());
        verify(accountRepository, never()).findByUsers(any());
    }

    @Test
    void testGetPendingBalance_ValidAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Transaction transaction = new Transaction();
        transaction.setAmount(50.0);

        when(transactionRepository.findPendingTransactionsByAccountId(1L))
                .thenReturn(List.of(transaction));

        double result = accountService.getPendingBalance(1L);

        assertEquals(150.0, result);
        verify(accountRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).findPendingTransactionsByAccountId(1L);
    }

    @Test
    void testGetBalanceAsOfDate_ValidAccount() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1);
        LocalDate endDate = today.plusYears(1);
        LocalDate datePlusThreeMonths = startDate.plusMonths(3);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(100.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.findPendingTransactionsByAccountId(1L))
                .thenReturn(Collections.emptyList());

        RecurringTransaction recurring = new RecurringTransaction();
        recurring.setAmount(50.0);
        recurring.setFrequency(TransactionFrequency.MONTHLY);
        recurring.setStartDate(startDate);
        recurring.setEndDate(endDate);

        when(recurringTransactionRepository.findAll()).thenReturn(List.of(recurring));

        double result = accountService.getBalanceAsOfDate(1L, datePlusThreeMonths);

        assertEquals(250.0, result, 0.01);
        verify(accountRepository, times(2)).findById(1L);
        verify(transactionRepository, times(1)).findPendingTransactionsByAccountId(1L);
        verify(recurringTransactionRepository, times(1)).findAll();

    }

    @Test
    void testUpdateAccount() {
        Account account = new Account();
        accountService.update(account);

        verify(accountRepository, times(1)).save(account);
    }
}
