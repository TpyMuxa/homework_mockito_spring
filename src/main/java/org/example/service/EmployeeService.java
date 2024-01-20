package org.example.service;

import org.example.exception.EmployeeAlreadyAddedException;
import org.example.exception.EmployeeNotFoundException;
import org.example.exception.EmployeeStorageIsFullException;
import org.example.model.Employee;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {

    private static final int MAX_EMPLOYEES = 10;
    private final Map<String, Employee> employees = new HashMap<>();

    private final ValidationService validationService;

    public EmployeeService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public void add(String firstName, String lastName, int department, int salary) {
        if (employees.size() == MAX_EMPLOYEES) {
            throw new EmployeeStorageIsFullException();
        }
        firstName = validationService.validate(firstName);
        lastName = validationService.validate(lastName);

        String key = buildKey(firstName, lastName);
        if (employees.containsKey(key)) {
            throw new EmployeeAlreadyAddedException();
        }

        employees.put(key, new Employee(firstName, lastName, department, salary));
    }

    public Employee remove(String firstName, String lastName) {
        String key = buildKey(firstName, lastName);
        Employee employee = employees.remove(key);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return employee;
    }

    public Employee find(String firstName, String lastName) {
        String key = buildKey(firstName, lastName);
        Employee employee = employees.get(key);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return employee;
    }

    public Collection<Employee> findAll() {

        return Collections.unmodifiableCollection(employees.values());
    }

    private String buildKey(String name, String surname) {
        return name + " " + surname;
    }

    public void changeDepartment(Employee employee,
                                 int newDepartment) {
        String key = buildKey(employee.getFirstName(), employee.getLastName());
        if (employees.containsKey(key)) {
            Employee emp = employees.get(key);
            emp.setDepartment(newDepartment);
        }
    }

    public void printEmployeesByDepartment() {

        Stream.iterate(1, department -> department + 1)
                .limit(5)
                .forEach(department -> {
                    System.out.println("Сотрудники из отдела " + department + ":");
                    employees.values().stream()
                            .filter(employee -> employee.getDepartment() == department)
                            .forEach(employee -> System.out.println(employee.getLastName() + " " + employee.getFirstName()));
                });
    }

    public void indexSalariesForDepartment(double index, int department) {

        employees.values().stream()
                .filter(employee -> employee.getDepartment() == department)
                .forEach(employee -> employee.setSalary((int) (employee.getSalary() + employee.getSalary() * index / 100)));
    }

    public double averageSalaryForDepartment(int department) {

        return employees.values().stream()
                .filter(employee -> employee.getDepartment() == department)
                .mapToInt(Employee::getSalary)
                .average()
                .orElse(0);
    }

    public double totalSalariesForDepartment(int department) {

        return employees.values().stream()
                .filter(employee -> employee.getDepartment() == department)
                .mapToInt(Employee::getSalary)
                .sum();
    }

    public void printAllEmployeesFromDepartment(int department) {

        employees.values().stream()
                .filter(employee -> employee.getDepartment() == department)
                .forEach(employee -> System.out.printf(
                        "ФИО: %s %s, ЗП: %d%n, отдел: %d",
                        employee.getLastName(),
                        employee.getFirstName(),
                        employee.getSalary(),
                        employee.getDepartment()
                ));
    }
}