package com.cloud.ibm.banking.IBMBanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class IbmBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbmBankingApplication.class, args);
		LogBanner();
	}

	public static void LogBanner()
	{
		String logo = "  ___  ____   __  __   ____                 _     _                 ____   _                _             _ \n" +
				" |_ _|| __ ) |  \\/  | | __ )   __ _  _ __  | | __(_) _ __    __ _  / ___| | |_  __ _  _ __ | |_  ___   __| |\n" +
				"  | | |  _ \\ | |\\/| | |  _ \\  / _` || '_ \\ | |/ /| || '_ \\  / _` | \\___ \\ | __|/ _` || '__|| __|/ _ \\ / _` |\n" +
				"  | | | |_) || |  | | | |_) || (_| || | | ||   < | || | | || (_| |  ___) || |_| (_| || |   | |_|  __/| (_| |\n" +
				" |___||____/ |_|  |_| |____/  \\__,_||_| |_||_|\\_\\|_||_| |_| \\__, | |____/  \\__|\\__,_||_|    \\__|\\___| \\__,_|\n" +
				"                                                            |___/                                           ";

		System.out.println(logo);
	}
}

