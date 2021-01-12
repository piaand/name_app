package com.example.name_application;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
* Tests NameApplication with in-memory database connection.
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
class NameApplicationTests {
	private final Name name;
	private final Name name2;
	private final Name name3;
	private final Name name4;
	private final Name name5;

	public NameApplicationTests() {
		this.name = new Name(7L, "Pia");
		this.name2 = new Name(10L, "Lasse");
		this.name3 = new Name(3L, "Hanna");
		this.name4 = new Name(1L, "Maaret");
		this.name5 = new Name(8L, "Kalle");
	}

	@Autowired
	NameRepository nameRepository;

	@Autowired
	NameService nameService;

	@Before
	public void initTestDB() {
		//Save 5 names to repository
		nameRepository.save(name);
		nameRepository.save(name2);
		nameRepository.save(name3);
		nameRepository.save(name4);
		nameRepository.save(name5);

	}

	@After
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
	void contextLoads() {

	}

}
