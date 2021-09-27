package com.bridgelabz.employeepayrollservice;

import java.time.LocalDate;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double basic_pay;
	public LocalDate startDate;

	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.basic_pay = salary;
	}

	public EmployeePayrollData(int id, String name, Double salary, LocalDate startDate) {
		this(id, name, salary);
		this.basic_pay = salary;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [id=" + id + ", name=" + name + ", basic_pay=" + basic_pay + ", startDate="
				+ startDate + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		EmployeePayrollData that = (EmployeePayrollData) obj;
		return id == that.id && Double.compare(that.basic_pay, this.basic_pay) == 0
				&& name.compareTo(that.name) == 0 && startDate == that.startDate;
	}


}
