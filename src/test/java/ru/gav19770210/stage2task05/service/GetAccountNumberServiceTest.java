package ru.gav19770210.stage2task05.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.gav19770210.stage2task05.entity.AccountEntity;
import ru.gav19770210.stage2task05.entity.AccountPoolEntity;
import ru.gav19770210.stage2task05.repository.AccountPoolRepo;
import ru.gav19770210.stage2task05.repository.AccountRepo;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GetAccountNumberServiceTest {
    @MockBean
    AccountPoolRepo accountPoolRepo;
    @MockBean
    AccountRepo accountRepo;
    @InjectMocks
    GetAccountNumberServiceImp getAccountNumberService;
    Long accountPoolId;
    String branchCode;
    String currencyCode;
    String mdmCode;
    String priorityCode;
    String registerTypeCode;

    @BeforeEach
    public void initBeforeEach() {
        accountPoolId = 11L;
        branchCode = "0022";
        currencyCode = "800";
        mdmCode = "15";
        priorityCode = "00";
        registerTypeCode = "03.012.002_47533_ComSoLd";
    }

    @Test
    @DisplayName("Проверка отсутствия в пула счетов с заданными параметрами")
    public void checkNotExistAccountPool() {
        System.out.println("Initialize data");

        var accountPoolEntity = new AccountPoolEntity();
        accountPoolEntity.setId(accountPoolId);

        System.out.println("Initialize mocks");

        given(accountPoolRepo.getByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode))
                .willReturn(null);

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class, () -> getAccountNumberService.getAccountNumber(
                        branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode),
                "Не прошла проверка отсутствия пула счетов с заданными параметрами");
    }

    @Test
    @DisplayName("Проверка отсутствия в пуле счетов не занятого счёта")
    public void checkNotExistAccountNotBusyInPool() {
        System.out.println("Initialize data");

        var accountPoolEntity = new AccountPoolEntity();
        accountPoolEntity.setId(accountPoolId);

        System.out.println("Initialize mocks");

        given(accountPoolRepo.getByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode))
                .willReturn(accountPoolEntity);

        given(accountRepo.getFirstByAccountPoolIdAndBusy(accountPoolId, false))
                .willReturn(null);

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class, () -> getAccountNumberService.getAccountNumber(
                        branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode),
                "Не прошла проверка отсутствия в пуле счетов не занятого счёта");
    }

    @Test
    @DisplayName("Проверка существования в пуле счетов не занятого счёта")
    public void checkExistAccountNotBusyInPool() {
        System.out.println("Initialize data");

        var accountPoolEntity = new AccountPoolEntity();
        accountPoolEntity.setId(accountPoolId);

        List<AccountEntity> accountEntities = new ArrayList<>();
        var accountNumber = "12345";
        var accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setAccountPoolId(accountPoolId);
        accountEntity.setAccountNumber(accountNumber);
        accountEntity.setBusy(false);
        accountEntities.add(accountEntity);

        System.out.println("Initialize mocks");

        given(accountPoolRepo.getByBranchCodeAndCurrencyCodeAndMdmCodeAndPriorityCodeAndRegisterTypeCode(
                branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode))
                .willReturn(accountPoolEntity);

        given(accountRepo.getFirstByAccountPoolIdAndBusy(accountPoolId, false))
                .willReturn(accountEntities.stream().filter(x -> !x.isBusy()).findFirst().get());

        System.out.println("Validate service");

        var accountEntity2 = getAccountNumberService.getAccountNumber(
                branchCode, currencyCode, mdmCode, priorityCode, registerTypeCode);
        Assertions.assertEquals(accountEntity, accountEntity2,
                "Из пула счетов получен не тот номер счёта");
        Assertions.assertEquals(0, accountEntities.stream().filter(x -> !x.isBusy()).count(),
                "В пуле счетов полученный счёт не пометился как занятый");
    }
}
