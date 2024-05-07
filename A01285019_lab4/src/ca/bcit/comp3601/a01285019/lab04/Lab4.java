
package ca.bcit.comp3601.a01285019.lab04;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.bcit.comp3601.a01285019.lab04.data.CompareByJoinedDate;
import ca.bcit.comp3601.a01285019.lab04.data.Customer;
import ca.bcit.comp3601.a01285019.lab04.io.CustomerReader;
import ca.bcit.comp3601.a01285019.lab04.io.CustomerReport;

/**
 *
 *
 */
public class Lab4 {

	private static final Instant startTime = Instant.now();
	private static final File customerFile = new File("./customers.txt");
	private static Scanner textReader = null;

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		System.out.println(startTime);

		try {
			textReader = new Scanner(customerFile);
			String customerData = textReader.useDelimiter("\\Z").next(); // Read file content into a String
			customerData = customerData.substring(1, customerData.length() - 1);
			if (customerData.isEmpty()) {
				System.err.println("Input data is missing. Expecting customer data.");
				printEndTimeAndDuration();
				System.exit(-1);
			}

			new Lab4().run(customerData);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			printEndTimeAndDuration();
			System.exit(-1);
		} finally {
			if (textReader != null) {
				textReader.close(); // Close the Scanner
			}
		}
	}

	public static void printEndTimeAndDuration() {
		Instant endTime = Instant.now();
		System.out.println(endTime);

		// print the duration
		System.out.println(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
	}

	/**
	 * Populate the customers and print them out.
	 */
	private void run(String customerData) {
		try {

			Map<Long, Customer> customers = CustomerReader.read(customerData);

			// sort the customers by joined date
			List<Customer> sortedCustomers = new ArrayList<>(customers.values());
			Collections.sort(sortedCustomers, new CompareByJoinedDate());

			CustomerReport.write(sortedCustomers);

		} catch (ApplicationException e) {
			System.err.println("Cannot Proceed. Exiting after printing the End time and Duration");
			// TODO Log the error
		} finally {
			printEndTimeAndDuration();
		}
	}

}
