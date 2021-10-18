package com.joao.victor.parcel.simulator.v1.service;

import com.joao.victor.parcel.simulator.v1.dtos.BuyProductRequest;
import com.joao.victor.parcel.simulator.v1.dtos.BuyProductResponse;
import com.joao.victor.parcel.simulator.v1.dtos.UserResponse;
import com.joao.victor.parcel.simulator.v1.entities.UserEntity;
import com.joao.victor.parcel.simulator.v1.enums.TypeBuy;
import com.joao.victor.parcel.simulator.v1.repository.UserRepository;
import com.joao.victor.parcel.simulator.v1.utils.CheckInterestRateSelic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService implements UserDetailsService {

    private CheckInterestRateSelic rateSelic;

    @Autowired
    private UserRepository userRepository;

    public @Valid @NotBlank UserEntity findUserByLogin(String login) {
        return userRepository.findByLogin(login).get();
    }

    public BuyProductResponse buyProduct(BuyProductRequest request){
        BuyProductResponse response = new BuyProductResponse();
        if(request.getPaymentTerms().
                checkInCash(request.getPaymentTerms().getInputValue(),
                        request.getValue()) == TypeBuy.IN_CASH.getCod()){
            response.setInCash(true);
            response.setParcelNumber(1);
            response.setRateInterestMonth(new BigDecimal("0"));
            response.setParcelNumber(1);
            response.setValue(request.getValue());
        }else{
            if (request.getPaymentTerms().getNumberInstallments() < 7){
                response.setInCash(false);
                response.setParcelNumber(response.getParcelNumber());
                response.setRateInterestMonth(new BigDecimal("0"));
                response.setParcelNumber(1);
                response.setValue(request.getValue());
            }
        }
        return productResponse(response);
    }

    public BuyProductResponse productResponse(BuyProductResponse response){
        BuyProductResponse r = new BuyProductResponse();
        r.setValue(response.getValue());
        r.setInCash(response.isInCash());
        r.setParcelNumber(response.getParcelNumber());
        r.setRateInterestMonth(response.getRateInterestMonth());
        return r;
    }
    
    public List<BuyProductResponse> productResponseInstallments(BuyProductRequest req) throws Exception {

        List<BuyProductResponse> res = new ArrayList<>();

        for (int i = 0; i < req.getPaymentTerms().getNumberInstallments(); i++) {
            if (req.getPaymentTerms().getNumberInstallments() < 7){
                BuyProductResponse response = new BuyProductResponse();
                response.setInCash(false);
                response.setParcelNumber(i+1);
                response.setRateInterestMonth(new BigDecimal("0"));
                BigDecimal numberInstallments = new BigDecimal(req.getPaymentTerms().getNumberInstallments());
                response.setValue(response.getValue().divide(numberInstallments));
                res.add(response);
            }else{
                BuyProductResponse response = new BuyProductResponse();
                response.setInCash(false);
                response.setParcelNumber(i+1);
                response.setRateInterestMonth(rateSelic.check());
                response.setValue(req.getPaymentTerms().
                        calculateInterestForMonth(req.getValue(),
                                req.getPaymentTerms().getNumberInstallments()));
            }
        }
        return res;
    }
    public UserResponse fromUserEntityToUserResponse(@Valid @NotBlank UserEntity user) {
        userRepository.findById(user.getId());
        UserResponse res = new UserResponse();

        res.setId(user.getId());
        res.setLogin(user.getLogin());
        res.setName(user.getName());

        return res;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByLogin(login);
        if (!user.isPresent())
            throw new UsernameNotFoundException("User not found, please check your credentials.");
        return new User(user.get().getLogin(), user.get().getPassword(),
                true, true, true, true, new ArrayList<>());
    }
}
