package com.nestor.curso.springboot.jpa.springbootjparelationship;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Client;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Invoice;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.ClientRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.InvoiceRepository;


@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

		public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}

	public void manyToOne() {
		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		Invoice invoice = new Invoice("compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);
		System.out.println(invoiceDB);
	}

	public void manyToOneFindByIdClient() {

		Optional<Client> optionalClient = clientRepository.findById(1L);
	
		if(optionalClient.isPresent()){
			Client client = optionalClient.orElseThrow();
		
		Invoice invoice = new Invoice("compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);
		System.out.println(invoiceDB);
		}
	}

		@Override
		public void run(String... args) throws Exception {
			//manyToOne();
			manyToOneFindByIdClient();
		}		

}


