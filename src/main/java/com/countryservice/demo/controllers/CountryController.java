package com.countryservice.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.countryservice.demo.beans.Country;
import com.countryservice.demo.services.CountryService;

@RestController
public class CountryController {
	
	@Autowired      
	CountryService countryService ;
	
	@GetMapping("/getcountries")        
	public ResponseEntity <List<Country>> getContries()
	{
		try
		{
			List<Country> countries = countryService.getAllCountries();
			return new ResponseEntity<List<Country>> (countries, HttpStatus.FOUND);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getcountries/{id}")       
	public ResponseEntity<Country> getCountryById(@PathVariable(value="id")int id)
	{
		try
		{
			Country country = countryService.getCountrybyID(id);
			return new ResponseEntity<Country>(country,HttpStatus.FOUND);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getcountries/countryname") 
	public ResponseEntity<Country> getCountryByName(@RequestParam(value="name")String countryName)
	{
		try
		{
		Country country = countryService.getCountrybyName(countryName);
		return new ResponseEntity<Country>(country,HttpStatus.FOUND);
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addcountry")
	public ResponseEntity<Country> addCountry(@RequestBody Country country)
	{
		try
		{
			country =countryService.addCountry(country);
			return new ResponseEntity<Country>(country,HttpStatus.CREATED);	
		}
		catch(NoSuchElementException e)
		{
			return new ResponseEntity<>(HttpStatus.CONFLICT);	
		}
	}

	@PutMapping("/updatecountry/{id}")
	public ResponseEntity<Country> updateCountry(@PathVariable(value="id") int id, @RequestBody Country country)
	{
		try
		{
			Country existCountry=countryService.getCountrybyID(id);
		
			existCountry.setCountryName(country.getCountryName());
			existCountry.setCountryCapital(country.getCountryCapital());
		
			Country updated_country = countryService.updateCountry(existCountry);
			return new ResponseEntity<Country>(updated_country,HttpStatus.OK);	
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.CONFLICT);	
		}
	}
	//deletecountry
	@DeleteMapping(path = "/deletecountry/{id}")
	public ResponseEntity<Country> deleteCountry(@PathVariable("id")int id)
	{
	Country country = null;
		{
			try
			{
				country = countryService.getCountrybyID(id);
				countryService.deleteCountry(country);
			}
			catch(NoSuchElementException e)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);	
			}
				return new ResponseEntity<Country>(country, HttpStatus.OK);
		}
	}
}