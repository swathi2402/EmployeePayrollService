package com.bridgelabz.employeepayrollservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null)
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public EmployeePayrollDBService() {

	}

	private Connection getConnection() throws EmployeePayrollException {
		String jdbcURL = "jdbc:mysql://localhost:3306/employee_service?useSSL=false";
		String userName = "root";
		String password = "swathi*123";
		Connection connection = null;

		System.out.println("Connecting to database" + jdbcURL);
		try {
			connection = DriverManager.getConnection(jdbcURL, userName, password);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Error in Database loading");
		}
		System.out.println("Connection is successfull" + connection);

		return connection;
	}

	public void writeToDatabase() throws EmployeePayrollException {
		String sql = "INSERT INTO employee_payroll (name, gender, basic_pay, start) VALUES ('Mark', 'M', 200000.0, '2020-09-23');";
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);

		} catch (SQLSyntaxErrorException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNKOWN_DATABASE,
					"Error in databse");
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
	}

	public List<EmployeePayrollData> readData() throws EmployeePayrollException {
		String sql = "SELECT id, name, basic_pay, start FROM employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("basic_pay");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}

		} catch (SQLSyntaxErrorException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNKOWN_DATABASE,
					"Error in databse");
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
		try {
			return this.updateEmployeeDataUsingStatement(name, salary);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
		String sql = String.format("UPDATE employee_payroll SET basic_pay= %.2f WHERE name ='%s';", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) throws EmployeePayrollException {
		List<EmployeePayrollData> employeePayrollList = null;
		if (this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws EmployeePayrollException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				Double salary = resultSet.getDouble("basic_pay");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return employeePayrollList;
	}

	private void prepareStatementForEmployeeData() throws EmployeePayrollException {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT id, name, basic_pay, start FROM employee_payroll WHERE name=?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
	}

	public List<EmployeePayrollData> getEmployeesFromDateRange(String date) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT id, name, basic_pay, start FROM employee_payroll WHERE start BETWEEN CAST('%s' AS DATE) AND DATE(NOW());",
				date);
		List<EmployeePayrollData> employeesListInDateRange = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeesListInDateRange = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return employeesListInDateRange;
	}

	private double excecuteStatement(String sql) throws EmployeePayrollException {
		double result = 0.0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				result =  resultSet.getDouble(2);

		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return result;
	}

	public double getSumOfSalariesBasedOnGender(char gender) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT gender, SUM(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		return excecuteStatement(sql);
	}

	public double getAverageOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT gender, AVG(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		return excecuteStatement(sql);
	}

	public int getCountBasedOnGender(char gender) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT gender, COUNT(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		int count = 0;
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				count = resultSet.getInt(2);
			}

		} catch (SQLException e) {
			throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQL_EXCEPTION,
					"Syntax error in sql statement");
		}
		return count;
	}

	public double getMinimunOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT gender, MIN(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		return excecuteStatement(sql);

	}

	public double getMaximunOfSalaryBasedOnGender(char gender) throws EmployeePayrollException {
		String sql = String.format(
				"SELECT gender, MAX(basic_pay) FROM employee_payroll WHERE gender = '%c' GROUP BY gender;", gender);

		return excecuteStatement(sql);
	}

}
