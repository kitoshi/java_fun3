
package ca.bcit.comp3601.a01285019.lab04.data;

import java.util.Comparator;

public class CompareByJoinedDate implements Comparator<Customer> {
	@Override
	public int compare(Customer customer1, Customer customer2) {
		return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
	}
}
