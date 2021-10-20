package com.joao.victor.parcel.simulator.v1.service;

import com.joao.victor.parcel.simulator.v1.dtos.BuyProductRequest;
import com.joao.victor.parcel.simulator.v1.dtos.BuyProductResponse;
import com.joao.victor.parcel.simulator.v1.dtos.PaymentTermsRequest;
import com.joao.victor.parcel.simulator.v1.entities.PaymentTermsEntity;
import com.joao.victor.parcel.simulator.v1.entities.ProductEntity;
import com.joao.victor.parcel.simulator.v1.repository.PaymentTermsRepository;
import com.joao.victor.parcel.simulator.v1.repository.ProductRepository;
import com.joao.victor.parcel.simulator.v1.utils.CalculateInterestForMonth;
import com.joao.victor.parcel.simulator.v1.utils.CheckInterestRateSelic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SaleService {

    @Autowired private ProductRepository productRepository;

    @Autowired private PaymentTermsRepository termsRepository;

    public ProductEntity saveProd(ProductEntity prod) {
        prod.setCreatedAt(LocalDateTime.now());
        return productRepository.save(prod);
    }

    public PaymentTermsEntity savePay(PaymentTermsEntity terms) {
        terms.setCreatedAt(LocalDateTime.now());
        return termsRepository.save(terms);
    }

    public List<BuyProductResponse> buyProduct(BuyProductRequest request) throws Exception {
        ProductEntity product = fromProductRequestToProductEntity(request);
        request.setId(product.getId());
        if(request.getPaymentTerms().getInputValue() >= request.getValue()){
            return productResponse(request, true);
        }
        return productResponse(request, false);
    }
    
    public List<BuyProductResponse> productResponse(BuyProductRequest req, boolean inCash) throws Exception {

        List<BuyProductResponse> res = new ArrayList<>();

        //TODO: Caso o pagamento tenha sido avista
        if (inCash){
            BuyProductResponse r = new BuyProductResponse();
            r.setId(req.getId());
            r.setName(req.getName());
            r.setParcelNumber(1);
            r.setRateInterestMonth(CheckInterestRateSelic.check());
            r.setValue(new BigDecimal(req.getValue()));
            res.add(r);
            return res;
        }

        //TODO: Caso o pagamento tenha sido a prazo sem juros
        if (req.getPaymentTerms().getNumberInstallments() < 7){
            for (int i = 0; i < req.getPaymentTerms().getNumberInstallments(); i++) {
                BuyProductResponse response = new BuyProductResponse();
                response.setId(req.getId());
                response.setName(req.getName());
                response.setParcelNumber(i+1);
                response.setRateInterestMonth(new BigDecimal("0"));
                BigDecimal numberInstallments = new BigDecimal(req.getPaymentTerms().getNumberInstallments());
                response.setValue(new BigDecimal(req.getValue() - req.getPaymentTerms().getInputValue())
                        .divide(numberInstallments, 2, RoundingMode.HALF_UP));
                res.add(response);
            }
        }
        if (req.getPaymentTerms().getNumberInstallments() > 6){
            BigDecimal resultFinal = CalculateInterestForMonth.
                    calculateInterestForMonth(new BigDecimal(req.getValue()), req.getPaymentTerms().getNumberInstallments());
            for (int i = 0; i < req.getPaymentTerms().getNumberInstallments(); i++) {
                BuyProductResponse response = new BuyProductResponse();
                response.setId(req.getId());
                response.setName(req.getName());
                response.setParcelNumber(i+1);
                response.setRateInterestMonth(CheckInterestRateSelic.check());
                BigDecimal numberInstallments = new BigDecimal(req.getPaymentTerms().getNumberInstallments());
                response.setValue(resultFinal.subtract(new BigDecimal(req.getPaymentTerms().getInputValue()))
                        .divide(numberInstallments, 2, RoundingMode.HALF_UP) );
                res.add(response);
            }
        }
        return res;
    }

    public ProductEntity fromProductRequestToProductEntity(BuyProductRequest request) throws Exception {
        ProductEntity product = new ProductEntity();
        product.setName(request.getName());
        product.setValue(new BigDecimal(request.getValue()));
        product.setCode(request.getCode());
        product.setPaymentTermsEntity(fromPaymentTermsRequestToPaymentTermsEntity(request.getPaymentTerms()));
        return saveProd(product);
    }

    public PaymentTermsEntity fromPaymentTermsRequestToPaymentTermsEntity(PaymentTermsRequest request) throws Exception {
        PaymentTermsEntity paymentTerms = new PaymentTermsEntity();
        paymentTerms.setInputValue(new BigDecimal(request.getInputValue()));
        paymentTerms.setNumberInstallments(request.getNumberInstallments());
        paymentTerms.setInterestRate(CheckInterestRateSelic.check());
        paymentTerms.setCreatedAt(LocalDateTime.now());
        return savePay(paymentTerms);
    }
}
