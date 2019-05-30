package com.cloud.ibm.banking.IBMBanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class IbmBankingApplication {

	public static void main(String[] args) {
		String logo = "\n" +
				" __  .______   .___  ___.    .______        ___      .__   __.  __  ___  __  .__   __.   _______ \n" +
				"|  | |   _  \\  |   \\/   |    |   _  \\      /   \\     |  \\ |  | |  |/  / |  | |  \\ |  |  /  _____|\n" +
				"|  | |  |_)  | |  \\  /  |    |  |_)  |    /  ^  \\    |   \\|  | |  '  /  |  | |   \\|  | |  |  __  \n" +
				"|  | |   _  <  |  |\\/|  |    |   _  <    /  /_\\  \\   |  . `  | |    <   |  | |  . `  | |  | |_ | \n" +
				"|  | |  |_)  | |  |  |  |    |  |_)  |  /  _____  \\  |  |\\   | |  .  \\  |  | |  |\\   | |  |__| | \n" +
				"|__| |______/  |__|  |__|    |______/  /__/     \\__\\ |__| \\__| |__|\\__\\ |__| |__| \\__|  \\______| \n" +
				"                                                                                                 \n";

		SpringApplication.run(IbmBankingApplication.class, args);
		System.out.println(logo);
	}
}

