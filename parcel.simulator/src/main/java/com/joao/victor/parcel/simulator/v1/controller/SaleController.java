package com.joao.victor.parcel.simulator.v1.controller;

import com.joao.victor.parcel.simulator.errorExceptions.MessageErrorCustom;
import com.joao.victor.parcel.simulator.v1.dtos.BuyProductRequest;
import com.joao.victor.parcel.simulator.v1.dtos.BuyProductResponse;
import com.joao.victor.parcel.simulator.v1.entities.ProductEntity;
import com.joao.victor.parcel.simulator.v1.service.SaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(value = "Sale-Service", tags = { "Sale-Service" })
@RestController
@Valid
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @ApiOperation(value = "buy a product",notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED", response = BuyProductResponse.class),
            @ApiResponse(code = 401, message = "UNAUTHORIZATED", response = MessageErrorCustom.class),
            @ApiResponse(code = 403, message = "FORBIDDEN", response = MessageErrorCustom.class),
            @ApiResponse(code = 404, message = "NOT FOUND", response = MessageErrorCustom.class)
    })
    @PostMapping(consumes = { "application/json", "application/xml" })
    public ResponseEntity<List<BuyProductResponse>> buyProduct(@RequestBody @Valid BuyProductRequest req) throws Exception {
        return ResponseEntity.ok().body(saleService.buyProduct(req));
    }
}
