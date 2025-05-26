package com.example.jrtp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jrtp.entity.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer>{

}
