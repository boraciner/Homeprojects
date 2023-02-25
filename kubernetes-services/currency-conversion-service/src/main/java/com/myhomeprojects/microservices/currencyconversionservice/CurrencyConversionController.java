package com.myhomeprojects.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	private Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from , @PathVariable String to , @PathVariable BigDecimal quantity) {
		logger.info("calculateCurrencyConversion called with from:"+from+" to:"+to);
		HashMap<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://currency-exchange:8001/currency-exchange/from/{from}/to/{to}",CurrencyConversion.class,uriVariables );
		CurrencyConversion currencyConversion = responseEntity.getBody();
		
		
		return new CurrencyConversion(1001L,from,to,currencyConversion.getConversionMultiple(),quantity,quantity.multiply(currencyConversion.getConversionMultiple()) ,currencyConversion.getEnvironment()+" rest template");
	}
	
	//http://localhost:8100/currency-conversion-feign/from/USD/to/INR/quantity/10
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from , @PathVariable String to , @PathVariable BigDecimal quantity) {
		logger.info("calculateCurrencyConversionFeign called with from:"+from+" to:"+to);
		CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchnageValue(from, to);
		
		return new CurrencyConversion(1001L,from,to,currencyConversion.getConversionMultiple(),quantity,quantity.multiply(currencyConversion.getConversionMultiple()) ,currencyConversion.getEnvironment()+" feign");
	}
}
