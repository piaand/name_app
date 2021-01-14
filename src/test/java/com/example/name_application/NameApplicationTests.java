package com.example.name_application;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
* Tests NameApplication with in-memory database connection.
 */

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class NameApplicationTests {
	private final Name name;
	private final Name name2;
	private final Name name3;
	private final Name name4;
	private final Name name5;
	private final String firstName = "Pia";
	private final Long amount = 7L;
	private static final Logger logger = Logger.getLogger(NameApplication.class.getName());

	public NameApplicationTests() {
		this.name = new Name(amount, firstName);
		this.name2 = new Name(10L, "Lasse");
		this.name3 = new Name(3L, "Hanna");
		this.name4 = new Name(1L, "Maaret");
		this.name5 = new Name(8L, "Kalle");
	}

	@Autowired
	NameRepository nameRepository;

	@Autowired
	NameService nameService;

	@BeforeEach
	public void initTestDB() {
		//Save 5 names to repository
		nameRepository.save(name);
		nameRepository.save(name2);
		nameRepository.save(name3);
		nameRepository.save(name4);
		nameRepository.save(name5);

	}

	@AfterEach
	public void cleanTestDB() {
		nameRepository.deleteAll();
	}

	@Test
	public void testTotalAmount() {
		Long amountRepository = nameRepository.findTotalAmount();
		Long amountService = nameService.getTotalAmountOfNames();
		assertEquals(amountRepository, amountService);
	}

	@Test
	public void testTotalAmountEmptyDB() {
		nameRepository.deleteAll();
		Long amountService = nameService.getTotalAmountOfNames();
		assertSame(0L, amountService);
	}

	@Test
	void contextLoads() { }

	@Test
	public void testGetGivenNameAmount() {
		Name queryResult = nameRepository.findByName(firstName);
		Assertions.assertNotNull(queryResult);
		Long result = nameService.getGivenNameAmount(firstName);
		assertEquals(queryResult.getName(), firstName);
		assertEquals(queryResult.getAmount(), amount);
		assertEquals(queryResult.getAmount(), result);
	}

}
