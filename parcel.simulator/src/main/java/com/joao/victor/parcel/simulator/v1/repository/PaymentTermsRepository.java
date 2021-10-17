package com.joao.victor.parcel.simulator.v1.repository;

import com.joao.victor.parcel.simulator.v1.entities.PaymentTermsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTermsEntity, Long> {
}
