package ru.gav19770210.stage2task05.service;

import ru.gav19770210.stage2task05.request.CreateProductRequest;
import ru.gav19770210.stage2task05.response.CreateProductResponse;

public interface CreateProductService {
    CreateProductResponse createProduct(CreateProductRequest createProductRequest);
}
