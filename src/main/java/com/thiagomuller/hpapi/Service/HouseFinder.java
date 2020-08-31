package com.thiagomuller.hpapi.Service;


import java.util.List;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;

public interface HouseFinder {
	public List<String> findAllHouses() throws NoHousesFoundException;
}
