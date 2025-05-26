package com.example.jrtp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jrtp.entity.StateEntity;

public interface StateRepository extends JpaRepository<StateEntity, Integer> {

	List<StateEntity> findByCountryCountryId(Integer countryId);
}
