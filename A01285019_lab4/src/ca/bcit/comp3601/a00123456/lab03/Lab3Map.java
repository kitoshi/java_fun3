
package ca.bcit.comp3601.a00123456.lab03;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.bcit.comp3601.a00123456.lab03.data.CompareByJoinedDate;
import ca.bcit.comp3601.a00123456.lab03.data.Customer;
import ca.bcit.comp3601.a00123456.lab03.io.CustomerReader;
import ca.bcit.comp3601.a00123456.lab03.io.CustomerReport;

/**
 * To demonstrate knowledge Generics and Collections
 * 
 * Create a commandline program which reads customer data, creates Customer objects, adds them to a collection, and prints a simple Customer report
 * sorted by joined date
 *
 */
public class Lab3Map {

	private static final Instant startTime = Instant.now();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(startTime);

		if (args.length == 0) {
			System.err.println("Input data is missing. Expecting customer data.");
			printEndTimeAndDuration();
			System.exit(-1);
		}

		new Lab3Map().run(args[0]);
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
			System.err.println( "Cannot Proceed. Exiting after printing the End time and Duration") ;
			//TODO Log the error
		} finally {
			printEndTimeAndDuration();
		}
	}

}
