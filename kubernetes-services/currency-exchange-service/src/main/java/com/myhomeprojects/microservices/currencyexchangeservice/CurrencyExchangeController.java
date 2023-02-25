package com.myhomeprojects.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyExchangeController {

	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	@Autowired
	private Environment environment;
	
	@Autowired
	private CurrencyExchangeRepository currencyExchangeRepository;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchnageValue(@PathVariable String from, @PathVariable String to) {
		logger.info("retrieveExchnageValue called with from:"+from+" to:"+to);
		CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
		
		if(currencyExchange == null)
			throw new RuntimeException("Unable to Find data ");
		
		
		
		String port = environment.getProperty("local.server.port");
		//Kubernetes change
		String host = environment.getProperty("HOSTNAME");
		String version = "v1";
		
		
		currencyExchange.setEnvironment(port+" "+version+" "+host);
		
		return currencyExchange;
	}
	
}
