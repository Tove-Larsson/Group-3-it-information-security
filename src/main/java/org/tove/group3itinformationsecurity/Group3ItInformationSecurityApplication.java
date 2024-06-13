package org.tove.group3itinformationsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tove.group3itinformationsecurity.utils.MaskingUtils;

@SpringBootApplication
public class Group3ItInformationSecurityApplication {

	public static void main(String[] args) {

		/*

		String socket = MaskingUtils.anonymize("a@test.com");
		System.out.println(socket);

		String socket1 = MaskingUtils.anonymize("aa@test.com");
		System.out.println(socket1);

		String socket2 = MaskingUtils.anonymize("aaa@test.com");
		System.out.println(socket2);

		String socket3 = MaskingUtils.anonymize("aaaatest.com");
		System.out.println(socket3);

		 */


		SpringApplication.run(Group3ItInformationSecurityApplication.class, args);
	}

}


