package ru.gav19770210.stage2task05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gav19770210.stage2task05.entity.AccountState;
import ru.gav19770210.stage2task05.entity.AgreementEntity;
import ru.gav19770210.stage2task05.entity.TppProductEntity;
import ru.gav19770210.stage2task05.entity.TppProductRegisterEntity;
import ru.gav19770210.stage2task05.repository.*;
import ru.gav19770210.stage2task05.request.CreateProductRequest;
import ru.gav19770210.stage2task05.response.CreateProductResponse;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class CreateProductServiceImp implements CreateProductService {
    private TppProductRepo productRepo;
    private TppRefProductRegisterTypeRepo productRegisterTypeRepo;
    private TppProductRegisterRepo productRegisterRepo;
    private TppRefProductClassRepo productClassRepo;
    private GetAccountNumberService getAccountNumberService;
    private AgreementRepo agreementRepo;

    @Autowired
    public void setProductRepo(TppProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Autowired
    public void setProductRegisterTypeRepo(TppRefProductRegisterTypeRepo productRegisterTypeRepo) {
        this.productRegisterTypeRepo = productRegisterTypeRepo;
    }

    @Autowired
    public void setProductClassRepo(TppRefProductClassRepo productClassRepo) {
        this.productClassRepo = productClassRepo;
    }

    @Autowired
    public void setProductRegisterRepo(TppProductRegisterRepo productRegisterRepo) {
        this.productRegisterRepo = productRegisterRepo;
    }

    @Autowired
    public void setGetAccountNumberService(GetAccountNumberService getAccountNumberService) {
        this.getAccountNumberService = getAccountNumberService;
    }

    @Autowired
    public void setAgreementRepo(AgreementRepo agreementRepo) {
        this.agreementRepo = agreementRepo;
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest createProductRequest) {
        var createProductResponse = new CreateProductResponse();
        var productId = createProductRequest.getInstanceId();
        TppProductEntity productEntity;

        if (productId == null) {
            var existProductEntity = productRepo.getByNumber(createProductRequest.getContractNumber());
            if (existProductEntity != null) {
                throw new IllegalArgumentException("Параметр ContractNumber (Номер договора) <"
                        + createProductRequest.getContractNumber() + "> уже существует для ЭП с ИД <"
                        + existProductEntity.getId() + ">.");
            }

            var productRegisterTypeEntities = productRegisterTypeRepo.findAllByProductClassCodeAndAccountType(
                    createProductRequest.getProductCode(), "Клиентский");
            if (productRegisterTypeEntities.isEmpty()) {
                throw new NoResultException("Код продукта <" + createProductRequest.getProductCode()
                        + "> не найден в Каталоге типов регистров <tpp_ref_product_register_type>.");
            }

            productEntity = new TppProductEntity();
            productEntity.setProductCodeId(productClassRepo.getByValue(createProductRequest.getProductCode()));
            productEntity.setClientId(getClientByMdmCode(createProductRequest.getMdmCode()));
            productEntity.setType(createProductRequest.getProductType());
            productEntity.setNumber(createProductRequest.getContractNumber());
            productEntity.setPriority(Long.valueOf(createProductRequest.getPriority()));
            productEntity.setDateOfConclusion(createProductRequest.getContractDate());
            productEntity.setStartDateTime(new Date());
            productEntity.setDays(0L);
            productEntity.setPenaltyRate(BigDecimal.valueOf(createProductRequest.getInterestRatePenalty()));
            productEntity.setNso(BigDecimal.valueOf(createProductRequest.getMinimalBalance()));
            productEntity.setThresholdAmount(BigDecimal.valueOf(createProductRequest.getThresholdAmount()));
            productEntity.setInterestRateType(createProductRequest.getRateType());
            productEntity.setTaxRate(BigDecimal.valueOf(createProductRequest.getTaxPercentageRate()));
            productEntity.setReasonClose(null);
            productEntity.setState("OPEN");

            for (var productRegisterTypeEntity : productRegisterTypeEntities) {
                var accountEntity = getAccountNumberService.getAccountNumber(
                        createProductRequest.getBranchCode(),
                        createProductRequest.getIsoCurrencyCode(),
                        createProductRequest.getMdmCode(),
                        "00",
                        productRegisterTypeEntity.getValue());

                var productRegisterEntity = new TppProductRegisterEntity(
                        productEntity,
                        productRegisterTypeEntity.getValue(),
                        accountEntity,
                        createProductRequest.getIsoCurrencyCode(),
                        AccountState.OPEN,
                        accountEntity.getAccountNumber());

                productRegisterRepo.save(productRegisterEntity);

                createProductResponse.getData().getRegisterId().add(productRegisterEntity.getId());
            }
        } else {
            var existProductEntity = productRepo.findById(productId);
            if (existProductEntity.isEmpty()) {
                throw new NoResultException("Экземпляр продукта с ИД <"
                        + productId + "> не найден.");
            }
            productEntity = existProductEntity.get();

            for (var agreement : createProductRequest.getInstanceAgreement()) {
                for (var agreementEntity : productEntity.getAgreements()) {
                    if (agreementEntity.getNumber().equals(agreement.getNumber())) {
                        throw new IllegalArgumentException("Параметр Number (Номер Дополнительного соглашения) <"
                                + agreementEntity.getNumber() + "> уже существует для ЭП с ИД <" + productId + ">.");
                    }
                }

                var agreementsEntity = new AgreementEntity();

                agreementsEntity.setProductId(productEntity);
                agreementsEntity.setGeneralAgreementId(agreement.getGeneralAgreementId());
                agreementsEntity.setSupplementaryAgreementId(agreement.getSupplementaryAgreementId());
                agreementsEntity.setArrangementType(agreement.getArrangementType());
                agreementsEntity.setSchedulerJobId(agreement.getSchedulerJobId());
                agreementsEntity.setNumber(agreement.getNumber());
                agreementsEntity.setCoefficient(BigDecimal.valueOf(agreement.getCoefficient()));
                agreementsEntity.setNumber(agreement.getNumber());
                agreementsEntity.setOpeningDate(agreement.getOpeningDate());
                agreementsEntity.setClosingDate(agreement.getClosingDate());
                agreementsEntity.setCancelDate(agreement.getCancelDate());
                agreementsEntity.setValidityDuration(agreement.getValidityDuration());
                agreementsEntity.setCancellationReason(agreement.getCancellationReason());
                agreementsEntity.setStatus("OPEN");
                agreementsEntity.setInterestCalculationDate(agreement.getInterestCalculationDate());
                agreementsEntity.setInterestRate(BigDecimal.valueOf(agreement.getInterestRate()));
                agreementsEntity.setCoefficient(BigDecimal.valueOf(agreement.getCoefficient()));
                agreementsEntity.setCoefficientAction(agreement.getCoefficientAction());
                agreementsEntity.setMinimumInterestRate(BigDecimal.valueOf(agreement.getMinimumInterestRate()));
                agreementsEntity.setMinimumInterestRateCoefficient(BigDecimal.valueOf(agreement.getMinimumInterestRateCoefficient()));
                agreementsEntity.setMinimumInterestRateCoefficientAction(agreement.getMinimumInterestRateCoefficientAction());
                agreementsEntity.setMaximalInterestRate(BigDecimal.valueOf(agreement.getMaximalInterestRate()));
                agreementsEntity.setMaximalInterestRateCoefficient(BigDecimal.valueOf(agreement.getMaximalInterestRateCoefficient()));
                agreementsEntity.setMaximalInterestRateCoefficientAction(agreement.getMaximalInterestRateCoefficientAction());

                productEntity.getAgreements().add(agreementsEntity);
                agreementRepo.save(agreementsEntity);

                createProductResponse.getData().getSupplementaryAgreementId().add(agreementsEntity.getId());
            }

        }
        productRepo.save(productEntity);

        createProductResponse.getData().setInstanceId(productEntity.getId());

        return createProductResponse;
    }

    /**
     * Заглушка для получения ИД клиента
     *
     * @param mdmCode Код Клиента
     * @return ИД клиента
     */
    private Long getClientByMdmCode(String mdmCode) {
        if (mdmCode != null)
            return 1234567890L;
        return null;
    }
}
