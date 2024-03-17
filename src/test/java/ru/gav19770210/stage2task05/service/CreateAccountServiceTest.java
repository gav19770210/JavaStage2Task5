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
import ru.gav19770210.stage2task05.entity.TppProductEntity;
import ru.gav19770210.stage2task05.entity.TppRefProductRegisterTypeEntity;
import ru.gav19770210.stage2task05.repository.TppProductRegisterRepo;
import ru.gav19770210.stage2task05.repository.TppProductRepo;
import ru.gav19770210.stage2task05.repository.TppRefProductRegisterTypeRepo;
import ru.gav19770210.stage2task05.request.CreateAccountRequest;

import javax.persistence.NoResultException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CreateAccountServiceTest {
    @MockBean
    private TppProductRepo productRepo;
    @MockBean
    private TppProductRegisterRepo productRegisterRepo;
    @MockBean
    private TppRefProductRegisterTypeRepo productRegisterTypeRepo;
    @InjectMocks
    private CreateAccountServiceImp createAccountService;
    private Long instanceId;
    private String registryTypeCode;

    @BeforeEach
    public void initBeforeEach() {
        instanceId = 11L;
        registryTypeCode = "03.012.002_47533_ComSoLd";
    }

    @Test
    @DisplayName("Проверка на существование ЭП с заданным ИД <InstanceId>")
    public void checkExistInstanceId() {
        System.out.println("Initialize data");

        var createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setInstanceId(instanceId);

        System.out.println("Initialize mocks");

        given(productRepo.findById(createAccountRequest.getInstanceId()))
                .willReturn(Optional.empty());

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class,
                () -> createAccountService.createAccount(createAccountRequest),
                "Не сработала проверка на существование ЭП с заданным ИД <InstanceId>");
    }

    @Test
    @DisplayName("Проверка существования типа регистра <registryTypeCode>")
    public void checkExistRegisterType() {
        System.out.println("Initialize data");

        var productEntity = new TppProductEntity();
        productEntity.setId(instanceId);

        var createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setInstanceId(productEntity.getId());
        createAccountRequest.setRegistryTypeCode(registryTypeCode);

        System.out.println("Initialize mocks");

        given(productRepo.findById(createAccountRequest.getInstanceId()))
                .willReturn(Optional.of(productEntity));
        given(productRegisterTypeRepo.getByValue(createAccountRequest.getRegistryTypeCode()))
                .willReturn(null);

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class,
                () -> createAccountService.createAccount(createAccountRequest),
                "Не сработала проверка на существование типа регистра <registryTypeCode>");
    }

    @Test
    @DisplayName("Проверка отсутствия регистра с типом <registryTypeCode> для ЭП с ИД <InstanceId>")
    public void checkNotExistProductIdAndRegisterType() {
        System.out.println("Initialize data");

        var productEntity = new TppProductEntity();
        productEntity.setId(instanceId);

        var createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setInstanceId(productEntity.getId());
        createAccountRequest.setRegistryTypeCode(registryTypeCode);

        var registerTypeEntity = new TppRefProductRegisterTypeEntity();
        registerTypeEntity.setValue(registryTypeCode);

        System.out.println("Initialize mocks");

        given(productRepo.findById(createAccountRequest.getInstanceId()))
                .willReturn(Optional.of(productEntity));
        given(productRegisterTypeRepo.getByValue(createAccountRequest.getRegistryTypeCode()))
                .willReturn(registerTypeEntity);
        given(productRegisterRepo.existsByProductIdAndRegisterType(productEntity, createAccountRequest.getRegistryTypeCode()))
                .willReturn(true);

        System.out.println("Validate service");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> createAccountService.createAccount(createAccountRequest),
                "Не сработала проверка отсутствия регистра с типом <registryTypeCode> для ЭП с ИД <InstanceId>");
    }
}
