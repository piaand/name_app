package com.example.name_application;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


/**
* Tests NameApplication with in-memory database connection.
 */

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class NameApplicationTests {
	private final Name name;
	private final Name name2;
	private final Name name3;
	private final Name name4;
	private final Name name5;
	private final String firstName = "Pia";
	private final String firstName2 = "Lasse";
	private final Long amount = 7L;
	private final Long amount2 = 10L;
	private static final Logger LOGGER = Logger.getLogger(NameApplication.class.getName());
	private static final String logFileName = "./logs/testNameApplication.log";
	private static final File logFile = new File(logFileName);

	public NameApplicationTests() {
		this.name = new Name(amount, firstName);
		this.name2 = new Name(amount2, firstName2);
		this.name3 = new Name(3L, "Hanna");
		this.name4 = new Name(1L, "Maaret");
		this.name5 = new Name(8L, "Kalle");

	}

	private	Long returnLongFromJson(JsonNode node) {
		try {
			String result = node.get("totalAmount").toString();
			Long number = Long.parseLong(result);
			return number;
		} catch(Exception e) {
			LOGGER.severe("Conversion of JsonNode "+ node + " to number didn't succeed. Error: " + e);
			return null;
		}
	}

	@Autowired
	NameRepository nameRepository;

	@Autowired
	NameService nameService;

	@Autowired
	LogConfigurations config;

	@BeforeAll
	static void init() {
		if (logFile.exists()) {
			logFile.delete();
		}
	}

	@BeforeEach
	public void initTestDB() {
		config.setConfig();
		LOGGER.info("Saving db entries for next test.");
		//Save 5 names to repository
		nameRepository.save(name);
		nameRepository.save(name2);
		nameRepository.save(name3);
		nameRepository.save(name4);
		nameRepository.save(name5);

	}

	@AfterEach
	public void cleanTestDB() {
		LOGGER.info("Tearing down db entries after test.");
		nameRepository.deleteAll();
	}

	@Test
	void testCreateLogFile() {
		LOGGER.info("Run test testCreateLogFile");
		boolean exists = logFile.exists();
		assertTrue(exists);
	}

	@Test
	public void testTotalAmount() {
		LOGGER.info("Run test testTotalAmount");
		Long amountRepository = nameRepository.findTotalAmount();
		JsonNode amountService = nameService.getTotalAmountOfNames();
		Long amount = returnLongFromJson(amountService);
		assertEquals(amountRepository, amount);
	}

	@Test
	public void testTotalAmountEmptyDB() {
		LOGGER.info("Run test testTotalAmountEmptyDB");
		nameRepository.deleteAll();
		JsonNode amountService = nameService.getTotalAmountOfNames();
		Long amount = returnLongFromJson(amountService);
		assertSame(0L, amount);
	}

	@Test
	public void testGetGivenNameAmount() {
		LOGGER.info("Run test testGetGivenNameAmount");
		Name queryResult = nameRepository.findByName(firstName);
		Assertions.assertNotNull(queryResult);
		JsonNode node = nameService.getGivenNameAmount(firstName);
		Long result = returnLongFromJson(node);
		assertEquals(queryResult.getName(), firstName);
		assertEquals(queryResult.getAmount(), amount);
		assertEquals(queryResult.getAmount(), result);
	}

	@Test()
	public void testGetGivenNameNotFound() {
		LOGGER.info("Run test testGetGivenNameNotFound");
		Name queryResult = nameRepository.findByName("Matilda");
		Assertions.assertNull(queryResult);
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			JsonNode result = nameService.getGivenNameAmount("Matilda");
		});
	}

	@Test
	public void testGetAllNamesAndAmountsNotFound() {
		LOGGER.info("Run test testGetAllNamesAndAmountsNotFound");
		nameRepository.deleteAll();
		List<Name> listNames = nameRepository.findAllByOrderByAmountDesc();
		assertTrue(listNames.isEmpty());
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			JsonNode node = nameService.getAllNamesAndAmounts();
		});
	}

	@Test
	public void testGetAllNamesSortedAlphabeticallyNotFound() {
		LOGGER.info("Run test testGetAllNamesSortedAlphabeticallyNotFound");
		nameRepository.deleteAll();
		List<Name> listNames = nameRepository.findAllByOrderByNameAsc();
		assertTrue(listNames.isEmpty());
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			JsonNode node = nameService.getAllNamesSortedAlphabetically();
		});
	}

	@Test
	public void testGetAllNamesAndAmounts() {
		LOGGER.info("Run test testGetAllNamesAndAmounts");
		List<Name> listNames = nameRepository.findAllByOrderByAmountDesc();
		assertFalse(listNames.isEmpty());

		if (listNames.size() > 1) {
			JsonNode node = nameService.getAllNamesAndAmounts().get("data");
			String firstAmountString = node.get(0).get("amount").toString();
			String secondAmountString = node.get(1).get("amount").toString();
			Integer firstAmount = Integer.parseInt(firstAmountString);
			Integer secondAmount = Integer.parseInt(secondAmountString);
			assertTrue(firstAmount >= secondAmount);
		}

	}

	@Test
	public void testGetAllNamesSortedAlphabetically() {
		LOGGER.info("Run test testGetAllNamesSortedAlphabetically");
		List<Name> listNames = nameRepository.findAllByOrderByNameAsc();
		assertFalse(listNames.isEmpty());

		if(listNames.size() > 1) {
			JsonNode node = nameService.getAllNamesSortedAlphabetically().get("data");
			String firstName = node.get(0).toString();
			String secondName = node.get(1).toString();

			assertTrue(firstName.compareTo(secondName) <= 0);
		}

	}
}
