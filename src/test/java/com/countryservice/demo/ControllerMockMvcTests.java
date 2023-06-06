package com.countryservice.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.CountryController;
import com.countryservice.demo.services.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(OrderAnnotation.class)
@ComponentScan(basePackages = "com.restservices.demo")
@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = {ControllerMockMvcTests.class})
public class ControllerMockMvcTests 
{
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	CountryService countryService;
	
	@InjectMocks
	CountryController countryController;
	
	List<Country> mycountries;
	Country country;
	
	@BeforeEach
	public void setup()
	{
		mockMvc=MockMvcBuilders.standaloneSetup(countryController).build();
	}
	
	@Test
	@Order(1)
	public void test_getAllCountries() throws Exception
	{
		mycountries = new ArrayList<Country>();
		mycountries.add(new Country(1,"India","Delhi"));
		mycountries.add(new Country(2,"USA","Washington"));
		
		when(countryService.getAllCountries()).thenReturn(mycountries);
		
		this.mockMvc.perform(get("/getcountries"))
		     		.andExpect(status().isFound())
		     		.andDo(print());
	}

	@Test
	@Order(2)
	public void test_getCountrybyID() throws Exception
	{
		country = new Country(2,"USA","Washington");
		
		int countryID=2;
		when(countryService.getCountrybyID(countryID)).thenReturn(country);
		
		this.mockMvc.perform(get("/getcountries/{id}",countryID))
			.andExpect(status().isFound())
			.andExpect(MockMvcResultMatchers.jsonPath(".id").value(2))
			.andExpect(MockMvcResultMatchers.jsonPath(".countryName").value("USA"))
			.andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value("Washington"))
			.andDo(print());
	}
	
	@Test
	@Order(3)
	public void test_getCountrybyName() throws Exception
	{
		country = new Country(2,"USA","Washington");
		
		String countryName="USA";
		when(countryService.getCountrybyName(countryName)).thenReturn(country);
		
		this.mockMvc.perform(get("/getcountries/countryname").param("name", "USA"))
			.andExpect(status().isFound())
			.andExpect(MockMvcResultMatchers.jsonPath(".id").value(2))
			.andExpect(MockMvcResultMatchers.jsonPath(".countryName").value("USA"))
			.andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value("Washington"))
			.andDo(print());
	}
	
	@Test
	@Order(4)
	public void test_addCountry() throws Exception
	{
		country = new Country(3,"Germany","Berlin");
		
		when(countryService.addCountry(country)).thenReturn(country);
		
		ObjectMapper mapper=new ObjectMapper(); //convert java to json
		String jsonbody = mapper.writeValueAsString(country);
		
		this.mockMvc.perform(post("/addcountry")
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
			
		
	}
	
	@Test
	@Order(5)
	public void test_upadteCountry() throws Exception
	{
		country = new Country(3,"Japan","Tokyo");
		int countryID =3;
		
		when(countryService.getCountrybyID(countryID)).thenReturn(country);//Mocking
		when(countryService.updateCountry(country)).thenReturn(country);
		
		ObjectMapper mapper=new ObjectMapper(); //convert java to json
		String jsonbody = mapper.writeValueAsString(country);
		
		this.mockMvc.perform(put("/updatecountry/{id}",countryID)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath(".countryName").value("Japan"))
				.andExpect(MockMvcResultMatchers.jsonPath(".countryCapital").value("Tokyo"))
				.andDo(print());
	}
	
	@Test
	@Order(6)
	public void test_deleteCountry() throws Exception 
	{
		country = new Country(3, "japan", "Tokyo");
		int countryID=3;
		
		when(countryService.getCountrybyID(countryID)).thenReturn(country); //Mocking
		
		this.mockMvc.perform(delete("/deletecountry/{id}",countryID))
		.andExpect(status().isOk());
	}
}
