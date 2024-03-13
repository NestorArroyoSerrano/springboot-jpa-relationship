package com.nestor.curso.springboot.jpa.springbootjparelationship;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Address;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Client;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.ClientDetails;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Course;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Invoice;
import com.nestor.curso.springboot.jpa.springbootjparelationship.entities.Student;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.ClientDetailsRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.ClientRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.CourseRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.InvoiceRepository;
import com.nestor.curso.springboot.jpa.springbootjparelationship.repositories.StudentRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ClientDetailsRepository clientDetailsRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

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

		@Transactional
		public void OneToOne() {
			ClientDetails clientDetails = new ClientDetails(true, 5000);
			clientDetailsRepository.save(clientDetails);

			Client client = new Client("Erba", "Pura");
			client.setClientDetails(clientDetails);
			clientRepository.save(client);

			System.out.println(client);

		}

		@Transactional
		public void OneToOneFindById() {
			ClientDetails clientDetails = new ClientDetails(true, 5000);
			clientDetailsRepository.save(clientDetails);

			Optional<Client> clientOptional = clientRepository.findOne(2L); // new Client("Erba", "Pura");
			clientOptional.ifPresent(client -> {
			client.setClientDetails(clientDetails);
			clientRepository.save(client);

			System.out.println(client);

			});
			
		}

		@Transactional
		public void OneToOneBidireccionalFindById() {
			
			Optional <Client> clientOptional = clientRepository.findOne(1L);
			clientOptional.ifPresent(client-> {
				ClientDetails clientDetails = new ClientDetails(true, 5000);

				client.setClientDetails(clientDetails);
				
				clientRepository.save(client);
	
				System.out.println(client);
			});

		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToMany() {

			Student student1 = new Student("Jano", "Pura");
			Student student2 = new Student("Erba", "Doe");

			Course course1 = new Course("Curso de JAVA master", "Andres");
			Course course2 = new Course("Curso de Spring Boot", "Andres");

			student1.setCourses(Set.of(course1, course2));
			student2.setCourses(Set.of(course1));

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyBidireccionalFind() {

			Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
			Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

			Student student1 = studentOptional1.get();
			Student student2 = studentOptional2.get();

			Course course1 = courseRepository.findOneWithStudents(1L).get();
			Course course2 = courseRepository.findOneWithStudents(2L).get();

			//student1.setCourses(Set.of(course1, course2));
			//student2.setCourses(Set.of(course2));

			student1.addCourse(course1);
			student1.addCourse(course2);
			student1.addCourse(course2);

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyRemoveBidireccionalFind() {

			Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
			Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

			Student student1 = studentOptional1.get();
			Student student2 = studentOptional2.get();

			Course course1 = courseRepository.findOneWithStudents(1L).get();
			Course course2 = courseRepository.findOneWithStudents(2L).get();

			//student1.setCourses(Set.of(course1, course2));
			//student2.setCourses(Set.of(course2));

			student1.addCourse(course1);
			student1.addCourse(course2);
			student1.addCourse(course2);

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

			Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(1L);
			if(studentOptionalDb.isPresent()) {

				Student studentDb = studentOptionalDb.get();
				Optional<Course> courseOptionalDb = courseRepository. findOneWithStudents(1L);
				
				if(courseOptionalDb.isPresent()) {
					Course courseDb = courseOptionalDb.get();
					studentDb.removeCourse(courseDb);

					studentRepository.save(studentDb); //Aquí actualiza en cascada
					System.out.println(studentDb);
					
		}
	}

}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyFind() {

			Optional<Student> studentOptional1 = studentRepository.findById(1L);
			Optional<Student> studentOptional2 = studentRepository.findById(2L);

			Student student1 = studentOptional1.get();
			Student student2 = studentOptional2.get();

			Course course1 = courseRepository.findById(1L).get();
			Course course2 = courseRepository.findById(2L).get();

			student1.setCourses(Set.of(course1, course2));
			student2.setCourses(Set.of(course2));

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyRemoveFind() {

			Optional<Student> studentOptional1 = studentRepository.findById(1L);
			Optional<Student> studentOptional2 = studentRepository.findById(2L);

			Student student1 = studentOptional1.get();
			Student student2 = studentOptional2.get();

			Course course1 = courseRepository.findById(1L).get();
			Course course2 = courseRepository.findById(2L).get();

			student1.setCourses(Set.of(course1, course2));
			student2.setCourses(Set.of(course2));

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

			Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(1L);
			if(studentOptionalDb.isPresent()) {

				Student studentDb = studentOptionalDb.get();
				Optional<Course> courseOptionalDb = courseRepository.findById(2L);
				
				if(courseOptionalDb.isPresent()) {
					Course courseDb = courseOptionalDb.get();
					studentDb.getCourses().remove(courseDb);

					studentRepository.save(studentDb); //Aquí actualiza en cascada
					System.out.println(studentDb);
					
				}
			}
		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyRemove() {

			Student student1 = new Student("Jano", "Pura");
			Student student2 = new Student("Erba", "Doe");

			Course course1 = new Course("Curso de JAVA master", "Andres");
			Course course2 = new Course("Curso de Spring Boot", "Andres");

			student1.setCourses(Set.of(course1, course2));
			student2.setCourses(Set.of(course1));

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

			Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(3L);
			if(studentOptionalDb.isPresent()) {

				Student studentDb = studentOptionalDb.get();
				Optional<Course> courseOptionalDb = courseRepository.findById(3L);
				
				if(courseOptionalDb.isPresent()) {
					Course courseDb = courseOptionalDb.get();
					studentDb.getCourses().remove(courseDb);

					studentRepository.save(studentDb); //Aquí actualiza en cascada
					System.out.println(studentDb);
					
				}
			}
		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyBidireccional() {

			Student student1 = new Student("Jano", "Pura");
			Student student2 = new Student("Erba", "Doe");

			Course course1 = new Course("Curso de JAVA master", "Andres");
			Course course2 = new Course("Curso de Spring Boot", "Andres");

		//	student1.setCourses(Set.of(course1, course2));
		//	student2.setCourses(Set.of(course1));

			student1.addCourse(course1);
			student1.addCourse(course2);
			student1.addCourse(course2);

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

		}

		@SuppressWarnings("null")
		@Transactional
		public void manyToManyBidireccionalRemove() {

			Student student1 = new Student("Jano", "Pura");
			Student student2 = new Student("Erba", "Doe");

			Course course1 = new Course("Curso de JAVA master", "Andres");
			Course course2 = new Course("Curso de Spring Boot", "Andres");

		//	student1.setCourses(Set.of(course1, course2));
		//	student2.setCourses(Set.of(course1));

			student1.addCourse(course1);
			student1.addCourse(course2);
			student1.addCourse(course2);

			studentRepository.saveAll(List.of(student1,student2));

			System.out.println(student1);
			System.out.println(student2);

			Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(3L);
			if(studentOptionalDb.isPresent()) {

				Student studentDb = studentOptionalDb.get();
				Optional<Course> courseOptionalDb = courseRepository. findOneWithStudents(3L);
				
				if(courseOptionalDb.isPresent()) {
					Course courseDb = courseOptionalDb.get();
					studentDb.removeCourse(courseDb);

					studentRepository.save(studentDb); //Aquí actualiza en cascada
					System.out.println(studentDb);
					

		}
	}
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
		//removeInvoiceBidireccional();
		//OneToOne();
		//OneToOneFindById();
		//OneToOneBidireccionalFindById();
		//manyToMany();
		//manyToManyFind();
		//manyToManyRemoveFind();
		//manyToManyRemove();
		//manyToManyBidireccional();
		//manyToManyBidireccionalRemove();
		//manyToManyBidireccionalFind();
		manyToManyRemoveBidireccionalFind();
	}

}
