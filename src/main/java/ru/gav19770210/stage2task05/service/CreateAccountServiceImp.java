package ru.gav19770210.stage2task05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gav19770210.stage2task05.entity.AccountState;
import ru.gav19770210.stage2task05.entity.TppProductRegisterEntity;
import ru.gav19770210.stage2task05.repository.TppProductRegisterRepo;
import ru.gav19770210.stage2task05.repository.TppProductRepo;
import ru.gav19770210.stage2task05.repository.TppRefProductRegisterTypeRepo;
import ru.gav19770210.stage2task05.request.CreateAccountRequest;
import ru.gav19770210.stage2task05.response.CreateAccountResponse;

import javax.persistence.NoResultException;

@Service
public class CreateAccountServiceImp implements CreateAccountService {
    private TppProductRegisterRepo productRegisterRepo;
    private TppProductRepo productRepo;
    private TppRefProductRegisterTypeRepo productRegisterTypeRepo;
    private GetAccountNumberService getAccountNumberService;

    @Autowired
    public void setRegistryTypeRepo(TppProductRegisterRepo registerRepo) {
        this.productRegisterRepo = registerRepo;
    }

    @Autowired
    public void setProductRepo(TppProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Autowired
    public void setProductRegisterTypeRepo(TppRefProductRegisterTypeRepo productRegisterTypeRepo) {
        this.productRegisterTypeRepo = productRegisterTypeRepo;
    }

    @Autowired
    public void setGetAccountNumberService(GetAccountNumberService getAccountNumberService) {
        this.getAccountNumberService = getAccountNumberService;
    }

    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        var productId = createAccountRequest.getInstanceId();

        var existProductEntity = productRepo.findById(productId);
        if (existProductEntity.isEmpty()) {
            throw new NoResultException("Экземпляр продукта с ИД <" + productId + "> не найден.");
        }
        var productEntity = existProductEntity.get();

        var productRegisterTypeEntity = productRegisterTypeRepo.getByValue(createAccountRequest.getRegistryTypeCode());
        if (productRegisterTypeEntity == null) {
            throw new NoResultException("Тип регистра <" + createAccountRequest.getRegistryTypeCode()
                    + "> не найден в Каталоге регистров <tpp_ref_product_register_type>.");
        }

        if (productRegisterRepo.existsByProductIdAndRegisterType(productEntity, createAccountRequest.getRegistryTypeCode())) {
            throw new IllegalArgumentException("Параметр registryTypeCode (Тип регистра) <"
                    + createAccountRequest.getRegistryTypeCode()
                    + "> уже существует для ЭП с ИД <" + productId + ">.");
        }

        var accountEntity = getAccountNumberService.getAccountNumber(
                createAccountRequest.getBranchCode(),
                createAccountRequest.getCurrencyCode(),
                createAccountRequest.getMdmCode(),
                createAccountRequest.getPriorityCode(),
                createAccountRequest.getRegistryTypeCode()
        );

        var productRegisterEntity = new TppProductRegisterEntity(
                productEntity,
                createAccountRequest.getRegistryTypeCode(),
                accountEntity,
                createAccountRequest.getCurrencyCode(),
                AccountState.OPEN,
                accountEntity.getAccountNumber());

        productRegisterRepo.save(productRegisterEntity);

        var createAccountResponse = new CreateAccountResponse();
        createAccountResponse.getData().setAccountId(productRegisterEntity.getId().toString());

        return createAccountResponse;
    }
}
