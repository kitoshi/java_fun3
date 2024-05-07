package ca.bcit.comp3601.a01285019.lab04.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.bcit.comp3601.a01285019.lab04.data.Customer;

public class CustomerDao extends Dao {
	public static final String TABLE_NAME = DbConstants.CUSTOMER_TABLE_NAME;

	private static final Logger LOG = LogManager.getLogger();

	protected CustomerDao(Database database, String tableName) {
		super(database, tableName);
	}

	@Override
	public void create() throws SQLException {
		String sql = String.format("create table %s(" // 1
				+ "%s BIGINT, " // 2
				+ "%s VARCHAR(64), " // 3
				+ "%s VARCHAR(64), " // 4
				+ "%s VARCHAR(64), " // 5
				+ "%s VARCHAR(64), " // 6
				+ "%s VARCHAR(7), " // 7
				+ "%s VARCHAR(20), " // 8
				+ "%s VARCHAR(64), " // 9
				+ "%s DATE, " // 10
				+ "primary key (%s) )", // 11
				tableName, // 1
				Fields.CUSTOMER_ID.getName(), // 2
				Fields.FIRST_NAME.getName(), // 3
				Fields.LAST_NAME.getName(), // 4
				Fields.STREET.getName(), // 5
				Fields.CITY.getName(), // 6
				Fields.POSTAL_CODE.getName(), // 7
				Fields.STREET.getName(), // 8
				Fields.PHONE.getName(), // 9
				Fields.EMAIL_ADDRESS.getName(), // 10
				Fields.JOINED_DATE.getName()); // 11
		LOG.debug(sql);
		super.create(sql);
	}

	public void add(CustomerDao customer) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("insert into %s values(" // 1 tableName
					+ "'%s', " // 2 StudentId
					+ "'%s', " // 3 FirstName
					+ "'%s', " // 4 Mi
					+ "'%s', " // 5 LastName
					+ "'%s', " // 6 Phone
					+ "'%s', " // 7 BirthDate
					+ "'%s', " // 8 Street
					+ "'%s', " // 9 ZipCode
					+ "'%s')", // 10 DeptID
					tableName, // 1
					student.getStudentId(), // 2
					student.getFirstName(), // 3
					student.getMi(), // 4
					student.getLastName(), // 5
					student.getPhone(), // 6
					student.getBirthDateString(), // 7
					student.getStreet(), // 8
					student.getZipCode(), // 9
					student.getDeptID()); // 10
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}

	public CustomerDao getCustomer(String customerID) throws SQLException, Exception {
		Connection connection;
		Statement statement = null;
		Customer customer = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.STUDENT_ID.getName(),
					studentId);
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// get the Student
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new Exception(String.format("Expected one result, got %d", count));
				}
				student = new Student();
				student.setStudentId(resultSet.getString(Fields.STUDENT_ID.getName()));
				student.setFirstName(resultSet.getString(Fields.FIRST_NAME.getName()));
				student.setMi(resultSet.getString(Fields.MI.getName()));
				student.setLastName(resultSet.getString(Fields.LAST_NAME.getName()));
				student.setPhone(resultSet.getString(Fields.PHONE.getName()));
				student.setBirthDate(resultSet.getString(Fields.BIRTHDATE.getName()));
				student.setStreet(resultSet.getString(Fields.STREET.getName()));
				student.setZipCode(resultSet.getString(Fields.ZIP_CODE.getName()));
				student.setDeptID(resultSet.getString(Fields.DEPT_ID.getName()));
			}
		} finally {
			close(statement);
		}

		return student;
	}

	public void update(Student student) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.STUDENT_ID.getName(), student.getStudentId(), //
					Fields.FIRST_NAME.getName(), student.getFirstName(), //
					Fields.MI.getName(), student.getMi(), //
					Fields.LAST_NAME.getName(), student.getLastName(), //
					Fields.PHONE.getName(), student.getPhone(), //
					Fields.BIRTHDATE.getName(), student.getBirthDate(), //
					Fields.STREET.getName(), student.getStreet(), //
					Fields.ZIP_CODE.getName(), student.getZipCode(), //
					Fields.DEPT_ID.getName(), student.getDeptID(), //
					Fields.STUDENT_ID.getName(), student.getStudentId());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.STUDENT_ID.getName(),
					customer.getId());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public enum Fields {

		CUSTOMER_ID("customerId", "BIGINT", 9, 1), //
		FIRST_NAME("firstName", "VARCHAR", 64, 2), //
		LAST_NAME("lastName", "VARCHAR", 64, 3), //
		STREET("street", "VARCHAR", 64, 4), //
		CITY("city", "VARCHAR", 64, 5), //
		POSTAL_CODE("postalCode", "VARCHAR", 7, 6), //
		PHONE("phone", "VARCHAR", 20, 7), //
		EMAIL_ADDRESS("emailAddress", "VARCHAR", 64, 8), //
		JOINED_DATE("joinedDate", "DATE", 4, 9); //

		private final String name;
		private final String type;
		private final int length;
		private final int column;

		Fields(String name, String type, int length, int column) {
			this.name = name;
			this.type = type;
			this.length = length;
			this.column = column;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public int getLength() {
			return length;
		}

		public int getColumn() {
			return column;
		}
	}
}
