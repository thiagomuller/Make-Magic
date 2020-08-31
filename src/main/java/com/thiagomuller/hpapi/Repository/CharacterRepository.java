package com.thiagomuller.hpapi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.thiagomuller.hpapi.Model.Character;

public interface CharacterRepository extends CrudRepository<Character, Integer>{
	
	@Query("SELECT c FROM Character c where houseId= :houseId")
	public List<Character> findCharactersByHouseId(@Param("houseId") String houseId);
}