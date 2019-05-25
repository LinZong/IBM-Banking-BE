package com.cloud.ibm.banking.IBMBanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class IbmBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbmBankingApplication.class, args);
	}
}

