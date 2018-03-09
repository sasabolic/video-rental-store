package com.example.videorentalstore.rental.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class CreateRentalRequestListJsonTests {

	@Autowired
	private JacksonTester<CreateRentalRequestList> json;

	@Test
	public void testDeserialize() throws Exception {
		String content = "[{\"film_id\":1,\"days_rented\":10}]";

		assertThat(this.json.parse(content)).isEqualTo(new CreateRentalRequestList(Arrays.asList(new CreateRentalRequest(1L, 10))));
		assertThat(this.json.parseObject(content).getCreateRentalRequests()).isEqualTo(Arrays.asList(new CreateRentalRequest(1L, 10)));
	}

}