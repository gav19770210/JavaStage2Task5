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
import ru.gav19770210.stage2task05.entity.AgreementEntity;
import ru.gav19770210.stage2task05.entity.TppProductEntity;
import ru.gav19770210.stage2task05.entity.TppRefProductRegisterTypeEntity;
import ru.gav19770210.stage2task05.repository.TppProductRepo;
import ru.gav19770210.stage2task05.repository.TppRefProductRegisterTypeRepo;
import ru.gav19770210.stage2task05.request.CreateProductRequest;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CreateProductServiceTest {
    @MockBean
    private TppProductRepo productRepo;
    @MockBean
    private TppRefProductRegisterTypeRepo productRegisterTypeRepo;
    @InjectMocks
    private CreateProductServiceImp createProductService;

    private Long instanceId;
    private String contractNumber;
    private String productClassCode;
    private String agreementNumber;

    @BeforeEach
    public void initBeforeEach() {
        instanceId = 11L;
        contractNumber = "666";
        productClassCode = "03.012.002";
        agreementNumber = "888";
    }

    @Test
    @DisplayName("Проверка на отсутствия ЭП с заданным номером <ContractNumber>")
    public void checkNotExistByContractNumber() {
        System.out.println("Initialize data");

        var createProductRequest = new CreateProductRequest();
        createProductRequest.setInstanceId(null);
        createProductRequest.setContractNumber(contractNumber);

        var productEntity = new TppProductEntity();
        productEntity.setId(instanceId);

        System.out.println("Initialize mocks");

        given(productRepo.getByNumber(createProductRequest.getContractNumber()))
                .willReturn(productEntity);

        System.out.println("Validate service");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> createProductService.createProduct(createProductRequest),
                "Не сработала проверка на отсутствие ЭП с заданным номером <ContractNumber>");
    }

    @Test
    @DisplayName("Проверка на существование типов регистров по коду продукта и типу счета")
    public void checkExistRegisterTypeByProductClassCodeAndAccountType() {
        System.out.println("Initialize data");

        var createProductRequest = new CreateProductRequest();
        createProductRequest.setInstanceId(null);
        createProductRequest.setContractNumber(contractNumber);
        createProductRequest.setProductCode(productClassCode);

        List<TppRefProductRegisterTypeEntity> productRegisterTypeEntities = new ArrayList<>();

        System.out.println("Initialize mocks");

        given(productRepo.getByNumber(createProductRequest.getContractNumber()))
                .willReturn(null);
        given(productRegisterTypeRepo.findAllByProductClassCodeAndAccountType(
                createProductRequest.getProductCode(),
                "Клиентский"))
                .willReturn(productRegisterTypeEntities);

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class,
                () -> createProductService.createProduct(createProductRequest),
                "Не сработала проверка на существование типов регистров по коду продукта и типу счета");
    }

    @Test
    @DisplayName("Проверка на существование ЭП с заданным ИД <InstanceId>")
    public void checkExistInstanceId() {
        System.out.println("Initialize data");

        var createProductRequest = new CreateProductRequest();
        createProductRequest.setInstanceId(instanceId);

        System.out.println("Initialize mocks");

        given(productRepo.findById(createProductRequest.getInstanceId()))
                .willReturn(Optional.empty());

        System.out.println("Validate service");

        Assertions.assertThrows(NoResultException.class,
                () -> createProductService.createProduct(createProductRequest),
                "Не сработала проверка на существование ЭП с заданным ИД <InstanceId>");
    }

    @Test
    @DisplayName("Проверка на отсутствия ДС с заданным номером <Number>")
    public void checkNotExistAgreementsByNumber() {
        System.out.println("Initialize data");

        var createProductRequest = new CreateProductRequest();
        createProductRequest.setInstanceId(instanceId);

        var requestAgreements = new CreateProductRequest.Agreement[1];
        requestAgreements[0] = new CreateProductRequest.Agreement();
        requestAgreements[0].setNumber(agreementNumber);
        createProductRequest.setInstanceAgreement(requestAgreements);

        var agreementEntity = new AgreementEntity();
        agreementEntity.setNumber(agreementNumber);

        var productEntity = new TppProductEntity();
        productEntity.setId(instanceId);
        productEntity.getAgreements().add(agreementEntity);

        System.out.println("Initialize mocks");

        given(productRepo.findById(createProductRequest.getInstanceId()))
                .willReturn(Optional.of(productEntity));

        System.out.println("Validate service");

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> createProductService.createProduct(createProductRequest),
                "Не сработала проверка на отсутствие ДС с заданным номером <Number>");
    }
}
