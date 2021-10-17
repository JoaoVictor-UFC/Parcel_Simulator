package com.joao.victor.parcel.simulator.v1.repository;

import com.joao.victor.parcel.simulator.v1.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
}
