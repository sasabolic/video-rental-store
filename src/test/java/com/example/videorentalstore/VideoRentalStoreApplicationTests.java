package com.example.videorentalstore;

import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.film.web.FilmController;
import com.example.videorentalstore.invoice.web.CustomerInvoiceController;
import com.example.videorentalstore.rental.web.CustomerRentalController;
import com.example.videorentalstore.rental.web.RentalController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoRentalStoreApplicationTests {

	@Autowired
	private CustomerController customerController;

	@Autowired
	private FilmController filmController;

	@Autowired
	private CustomerInvoiceController customerInvoiceController;

	@Autowired
	private CustomerRentalController customerRentalController;

	@Autowired
	private RentalController rentalController;

	@Test
	public void whenContextLoadThenControllersCreated() {
		assertThat(customerController).isNotNull();
		assertThat(filmController).isNotNull();
		assertThat(customerInvoiceController).isNotNull();
		assertThat(customerRentalController).isNotNull();
		assertThat(rentalController).isNotNull();
	}

}
