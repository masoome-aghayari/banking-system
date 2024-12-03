package ir.azkivaam.banking_system.service;

import ir.azkivaam.banking_system.domain.dto.BankAccountCreationResponse;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.domain.dto.BranchDto;
import ir.azkivaam.banking_system.domain.dto.PersonDto;
import ir.azkivaam.banking_system.domain.entity.BankAccount;
import ir.azkivaam.banking_system.domain.entity.Branch;
import ir.azkivaam.banking_system.domain.entity.Person;
import ir.azkivaam.banking_system.exceptions.AccountCreationException;
import ir.azkivaam.banking_system.exceptions.BankAccountException;
import ir.azkivaam.banking_system.exceptions.BranchNotFoundException;
import ir.azkivaam.banking_system.mapper.BankAccountMapper;
import ir.azkivaam.banking_system.observer.impl.BankAccountObserverManager;
import ir.azkivaam.banking_system.repository.BankAccountRepository;
import ir.azkivaam.banking_system.repository.BranchRepository;
import ir.azkivaam.banking_system.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private BankAccountRepository accountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private BankAccountObserverManager bankAccountObserverManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ExecutorService notificationExecutor = Executors.newFixedThreadPool(2);
        accountService = new AccountServiceImpl(branchRepository,
                                                bankAccountObserverManager,
                                                bankAccountMapper,
                                                notificationExecutor,
                                                accountRepository);
    }


    @ParameterizedTest
    @MethodSource("provideTestCasesForCreateAccount")
    void createAccount_shouldSucceed_whenBranchExists(BankAccount bankAccount,
                                                      BankAccountDto bankAccountDto) {

        String branchDtoCode = bankAccountDto.getBranch().getCode();
        when(bankAccountMapper.toEntity(bankAccountDto)).thenReturn(bankAccount);
        when(branchRepository.existsByCode(branchDtoCode)).thenReturn(true);
        when(branchRepository.findByCode(branchDtoCode)).thenReturn(bankAccount.getBranch());
        when(accountRepository.save(bankAccount)).thenReturn(bankAccount);
        when(bankAccountMapper.toDto(bankAccount)).thenReturn(bankAccountDto);
        when(bankAccountMapper.toResponse(bankAccountDto)).thenReturn(new BankAccountCreationResponse());

        BankAccountCreationResponse response = accountService.createAccount(bankAccountDto);

        assertNotNull(response);
        verify(branchRepository).existsByCode(branchDtoCode);
        verify(accountRepository).save(bankAccount);
        verify(bankAccountObserverManager, times(1)).notifyObservers(any());
    }


    @ParameterizedTest
    @MethodSource("provideTestCasesForCreateAccount")
    void createAccount_shouldThrowException_whenBranchDoesNotExist(BankAccount bankAccount,
                                                                   BankAccountDto bankAccountDto) {
        when(bankAccountMapper.toEntity(bankAccountDto)).thenReturn(bankAccount);
        when(branchRepository.existsByCode(anyString())).thenReturn(false);

        AccountCreationException exception =
                assertThrows(AccountCreationException.class, () -> accountService.createAccount(bankAccountDto));
        assertTrue(exception.getCause() instanceof BranchNotFoundException);
        verify(bankAccountObserverManager, times(1)).notifyObservers(any());
    }

    static Stream<Arguments> provideTestCasesForCreateAccount() {
        String branchCode = "11111";
        Long balance = 10_000_000L;

        BranchDto branchDto = BranchDto.builder()
                                       .code(branchCode)
                                       .name("karimkhan")
                                       .build();
        Branch branch = new Branch();
        branch.setCode(branchCode);
        branch.setName(branchDto.getName());
        branch.setId(1L);

        PersonDto personDto = PersonDto.builder()
                                       .birthDate("1374")
                                       .nationalId("4310928838")
                                       .sureName("masoome")
                                       .lastname("aghayari")
                                       .build();
        Person person = new Person();
        person.setId(1L);
        person.setNationalId(personDto.getNationalId());
        person.setSureName(personDto.getSureName());
        person.setLastname(personDto.getLastname());
        person.setBirthDate(personDto.getBirthDate());

        BankAccountDto bankAccountDto = BankAccountDto.builder()
                                                      .branch(branchDto)
                                                      .person(personDto)
                                                      .balance(balance)
                                                      .build();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBranch(branch);
        bankAccount.setPerson(person);
        bankAccount.setBalance(balance);
        return Stream.of(Arguments.of(bankAccount, bankAccountDto));
    }

    @Test
    void getBalance_shouldReturnBalance_whenAccountExists() {
        String accountNumber = "1234567890";
        BankAccount entity = new BankAccount();
        entity.setBalance(10_000_000L);

        when(accountRepository.findByAccountNumberForUpdate(accountNumber)).thenReturn(Optional.of(entity));

        Long balance = accountService.getBalance(accountNumber);

        assertEquals(10_000_000L, balance);
    }

    @Test
    void getBalance_shouldThrowException_whenAccountDoesNotExist() {
        String accountNumber = "123456789";
        when(accountRepository.findByAccountNumberForUpdate(accountNumber)).thenReturn(Optional.empty());
        assertThrows(BankAccountException.class, () -> accountService.getBalance(accountNumber));
    }
}
