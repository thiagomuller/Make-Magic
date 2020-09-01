package com.thiagomuller.hpapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.thiagomuller.hpapi.model.Character;

public interface CharacterRepository extends CrudRepository<Character, Integer>{
	
	@Query("SELECT c FROM Character c where houseId= :houseId")
	public List<Character> findCharactersByHouseId(@Param("houseId") String houseId);
	
	@Query("SELECT c FROM Character c where name = :name")
	public Optional<Character> findCharactersByName(@Param("name") String name);
}