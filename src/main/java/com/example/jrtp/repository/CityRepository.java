package com.example.jrtp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jrtp.entity.CityEntity;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {

	List<CityEntity> findByStateStateId(Integer stateId);
}
