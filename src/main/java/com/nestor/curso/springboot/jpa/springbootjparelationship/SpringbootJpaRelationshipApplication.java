package com.nestor.curso.springboot.jpa.springbootjparelationship;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Address;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Client;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Invoice;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.ClientRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.InvoiceRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}


	@Transactional
	public void oneToManyFindById() {
		Optional<Client> optionalClient = clientRepository.findById(2L);
		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El verjel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);
			
			Set<Address> addresses = new HashSet<>();
			addresses.add(address1);
			addresses.add(address2);
			client.setAddresses(addresses);
	
			clientRepository.save(client);
	
			System.out.println(client);
		});
		
	}

	@Transactional
	public void removeAddressFindById() {
		Optional<Client> optionalClient = clientRepository.findById(2L);
		optionalClient.ifPresent(client -> {
			Address address1 = new Address("El verjel", 1234);
			Address address2 = new Address("Vasco de Gama", 9875);
	
			Set<Address> addresses = new HashSet<>();
			addresses.add(address1);
			addresses.add(address2);
			client.setAddresses(addresses);
	
			clientRepository.save(client);
	
			System.out.println(client);

			Optional<Client> optionalClient2 = clientRepository.findOneWithAddresses(2L);
			optionalClient2.ifPresent(c-> {
				c.getAddresses().remove(address2);
				clientRepository.save(c);
				System.out.println(c);
		});
	});
		
	}

	@Transactional
	public void oneToMany(){
		Client client = new Client("Fran", "Moras");

		Address address1 = new Address("El verjel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println(client);
	}
	@Transactional
	public void removeAddress(){
		Client client = new Client("Fran", "Moras");

		Address address1 = new Address("El verjel", 1234);
		Address address2 = new Address("Vasco de Gama", 9875);

		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		clientRepository.save(client);

		System.out.println(client);

		Optional<Client> optionalClient = clientRepository.findById(3L);
		optionalClient.ifPresent(c-> {
			c.getAddresses().remove(address1);
			clientRepository.save(c);
			System.out.println(c);
		});
	}


	@Transactional
	public void manyToOne() {

		Client client = new Client("John", "Doe");
		clientRepository.save(client);

		Invoice invoice = new Invoice("compras de oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceDB = invoiceRepository.save(invoice);
		System.out.println(invoiceDB);
	}

	@Transactional
	public void manyToOneFindByIdClient() {
		
		Optional<Client> optionalClient = clientRepository.findById(1L);

		if (optionalClient.isPresent()) {
			Client client = optionalClient.orElseThrow();

			Invoice invoice = new Invoice("compras de oficina", 2000L);
			invoice.setClient(client);
			Invoice invoiceDB = invoiceRepository.save(invoice);
			System.out.println(invoiceDB);
		}
	}

	@Transactional
	public void oneToManyInvoiceBidireccional() {
		Client client = new Client("Fran", "Moras");

		Invoice invoice1 = new Invoice("compras de la casa", 5000L);
		Invoice invoice2 = new Invoice("compras de oficina", 8000L);

		/*
		(En la clase Client hemos añadido un método para ahorrar estas líneas de código, aunque esto también estaría bien)
		List<Invoice> invoices = new ArrayList<>();
		invoices.add(invoice1);
		invoices.add(invoice2);
		 */
		client.addInvoice(invoice1).addInvoice(invoice2);

		clientRepository.save(client); // solo guardamos el cliente porque las facturas se guardan automáticamente (lo tenemos en cascada)

		System.out.println(client);

	}
	@Transactional
	public void oneToManyInvoiceBidireccionalFindById() {
		Optional<Client>  optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client -> {
	
			Invoice invoice1 = new Invoice("compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("compras de oficina", 8000L);

			client.addInvoice(invoice1).addInvoice(invoice2);
	
			clientRepository.save(client); // solo guardamos el cliente porque las facturas se guardan automáticamente (lo tenemos en cascada)
	
			System.out.println(client);
		});

	}
		/*
		(En la clase Client hemos añadido un método para ahorrar estas líneas de código, aunque esto también estaría bien)
		List<Invoice> invoices = new ArrayList<>();
		invoices.add(invoice1);
		invoices.add(invoice2);
		 */
		@Transactional
		public void removeInvoiceBidireccionalFindById() {
			Optional<Client>  optionalClient = clientRepository.findOne(1L);
	
			optionalClient.ifPresent(client -> {
		
				Invoice invoice1 = new Invoice("compras de la casa", 5000L);
				Invoice invoice2 = new Invoice("compras de oficina", 8000L);
	
				client.addInvoice(invoice1).addInvoice(invoice2);
		
				clientRepository.save(client); // solo guardamos el cliente porque las facturas se guardan automáticamente (lo tenemos en cascada)
		
				System.out.println(client);
			});
	
			Optional<Client>  optionalClientDb = clientRepository.findOne(1L);

			optionalClientDb.ifPresent(client -> {
				Invoice invoice3 = new Invoice("compras de la casa", 5000L);
				invoice3.setId(1L);

				Optional<Invoice> invoiceOptional = Optional.of(invoice3); // invoiceRepository.findById(2L);
				invoiceOptional.ifPresent(invoice -> {
					client.removeInvoice(invoice);
					// client.getInvoices().remove(invoice);
					// invoice.setClient(null);
					clientRepository.save(client); 
					System.out.println(client);
				});
			});

		}

		@Transactional
		public void removeInvoiceBidireccional() {

			Client client = new Client("Fran", "Moras");
			//Optional<Client>  optionalClient = Optional.of(new Client("Fran", "Moras"));
	
		//	optionalClient.ifPresent(client -> {
		
				Invoice invoice1 = new Invoice("compras de la casa", 5000L);
				Invoice invoice2 = new Invoice("compras de oficina", 8000L);
	
				client.addInvoice(invoice1).addInvoice(invoice2);
		
				clientRepository.save(client); // solo guardamos el cliente porque las facturas se guardan automáticamente (lo tenemos en cascada)
		
				System.out.println(client);
		//	});
	
			Optional<Client>  optionalClientDb = clientRepository.findOne(3L);

			optionalClientDb.ifPresent(clientDb -> {
				// Invoice invoice3 = new Invoice("compras de la casa", 5000L);
				// invoice3.setId(1L);

				Optional<Invoice> invoiceOptional =   invoiceRepository.findById(2L); //Optional.of(invoice3);
				invoiceOptional.ifPresent(invoice -> {
					clientDb.removeInvoice(invoice);
					// client.getInvoices().remove(invoice);
					// invoice.setClient(null);
					clientRepository.save(clientDb); 
					System.out.println(clientDb);
				});
			});

		}




	@Override
	public void run(String... args) throws Exception {
		// manyToOne();
		//oneToMany();
		//oneToManyFindById();
		//removeAddress();
		//removeAddressFindById();
		//oneToManyInvoiceBidireccional();
		//oneToManyInvoiceBidireccionalFindById();
		//removeInvoiceBidireccionalFindById();
		removeInvoiceBidireccional();
	}

}
