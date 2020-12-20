package com.zertificon.address.management.zertificon_address_management;

import com.zertificon.address.management.zertificon_address_management.api.AddressController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ZertificonAddressManagementApplicationTests {

	@Autowired
	private AddressController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
