package uk.co.lexisnexis.risk.search.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching(proxyTargetClass = true)
public class CompanySearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanySearchServiceApplication.class, args);
	}

}
