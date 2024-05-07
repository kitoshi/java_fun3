package ca.bcit.comp3601.a01285019.lab04.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bcit.comp3601.a01285019.lab04.ApplicationException;
import ca.bcit.comp3601.a01285019.lab04.data.Customer;
import ca.bcit.comp3601.a01285019.lab04.util.Validator;


public class CustomerReader {

	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	private static Logger LOG = LogManager.getLogger(CustomerReader.class);
	
	/**
	 * private constructor to prevent instantiation
	 */
	private CustomerReader() {
	}

	/**
	 * Read the customer input data.
	 * 
	 * @param data
	 *            The input data.
	 * @return A list of customers.
	 * @throws ApplicationException
	 */
	public static Map<Long, Customer> read(String data) throws ApplicationException {
		Map<Long, Customer> customers = new HashMap<>();
		String[] rows = data.split(RECORD_DELIMITER);

		for (String row : rows) {
			Customer customer = readCustomerString(row);
			customers.put(customer.getId(), customer);
		}

		return customers;
	}

	/**
	 * Parse a Customer data string into a Customer object;
	 * 
	 * @param row
	 * @throws ApplicationException
	 */
	private static Customer readCustomerString(String data) throws ApplicationException {
		String[] elements = data.split(FIELD_DELIMITER);
		if (elements.length != Customer.ATTRIBUTE_COUNT) {
			String errorMsg = String.format("Expected %d but got %d: %s", Customer.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements));
			LOG.error(errorMsg);
			throw new ApplicationException(errorMsg);
		}

		int index = 0;
		long id = Integer.parseInt(elements[index++]);
		String firstName = elements[index++];
		String lastName = elements[index++];
		String street = elements[index++];
		String city = elements[index++];
		String postalCode = elements[index++];
		String phone = elements[index++];
		// should the email validation be performed here or in the customer class? I'm leaning towards putting it here.
		String emailAddress = elements[index++];
		if (!Validator.validateEmail(emailAddress)) {
			String errorMsg = String.format("Invalid email: %s", emailAddress);
			LOG.error(errorMsg);
			throw new ApplicationException(errorMsg);
		}
		String yyyymmdd = elements[index];
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			String errorMsg = String.format("Invalid joined date: %s for customer %d", yyyymmdd, id);
			LOG.error(errorMsg);
			throw new ApplicationException();
		}
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(4, 6));
		int day = Integer.parseInt(yyyymmdd.substring(6, 8));

		return new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city).setPostalCode(postalCode)
				.setEmailAddress(emailAddress).setJoinedDate(year, month, day).build();
	}

}
