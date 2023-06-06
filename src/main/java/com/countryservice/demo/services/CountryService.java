package com.countryservice.demo.services;

//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.countryservice.demo.beans.Country;
//import com.countryservice.demo.controllers.AddResponse;
import com.countryservice.demo.repositories.CountryRepository;

@Service
@Component
public class CountryService {
	
	@Autowired
	CountryRepository countryrep;
	
	public List<Country> getAllCountries() 
	{
		List<Country> countries = countryrep.findAll();
		return countries;
	}
	
	public Country getCountrybyID(int id) 
	{
		List<Country>countries=countryrep.findAll();
		Country country =null;
		
		for(Country con:countries)
		{
			if(con.getId()==id)
				country=con;
		}
		return country;
	}
	
	public Country getCountrybyName(String countryName) 
	{
		List<Country>countries=countryrep.findAll();
		Country country =null;
		
		for(Country con:countries)
		{
			if(con.getCountryName().equalsIgnoreCase(countryName))
				country=con;
		}
		return country;
	}
	
	public Country addCountry(Country country) 
	{
		country.setId(getMaxId());
		countryrep.save(country);
		return country;
	}
		//Utility method to get max id 
		public int getMaxId()
		{
			return countryrep.findAll().size()+1;
		}
		
		public Country updateCountry(Country country) 
		{
			countryrep.save(country);
			return country;
		}
		
		public void deleteCountry(Country country)
		{
			countryrep.delete(country);
		}		
	
}