package org.example.service;

import org.example.exception.EmployeeAlreadyAddedException;
import org.example.exception.EmployeeNotFoundException;
import org.example.exception.EmployeeStorageIsFullException;
import org.example.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

public class EmployeeServiceTest {

    private final EmployeeService employeeService = new EmployeeService(new ValidationService());

    @AfterEach
    public void afterEach() {
        Collection<Employee> copy = new ArrayList<>(employeeService.findAll());
        for (Employee employee : copy) {
            employeeService.remove(employee.getFirstName(), employee.getLastName());
        }
    }

    @BeforeEach
    public void beforeEach() {
        employeeService.add("Ivan", "Ivanov", 1, 10_000);
        employeeService.add("Petr", "Petrov", 1, 20_000);
        employeeService.add("Ivan", "Petrov", 2, 30_000);
        employeeService.add("Petr", "Ivanov", 2, 40_000);
    }

    @Test
    void addPositiveTest() {
        Employee expected = new Employee("Alexey", "Alexeev", 3, 50_000);

        employeeService.add("Alexey", "Alexeev", 3, 50_000);
        assertThatNoException()
                .isThrownBy(() -> employeeService.find("Alexey", "Alexeev"));
        assertThat(employeeService.find("Alexey", "Alexeev")).isEqualTo(expected);
        assertThat(employeeService.findAll()).contains(expected);
    }

    @Test
    void addNegative1Test() {
        for (int i = 0; i < 6; i++) {
            char c = (char) ('a' + i);
            employeeService.add("Alexey" + c, "Alexeev" + c, 3, 50_000);
        }
        assertThat(employeeService.findAll()).hasSize(10);

        assertThatExceptionOfType(EmployeeStorageIsFullException.class)
                .isThrownBy(() -> employeeService.add("Alexey", "Alexeev", 3, 50_000));
    }

    @Test
    void addNegative2Test() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10_000);
        assertThat(employeeService.findAll()).contains(expected);

        assertThatExceptionOfType(EmployeeAlreadyAddedException.class)
                .isThrownBy(() -> employeeService.add("Ivan", "Ivanov", 1, 10_000));
    }

    @Test
    void removePositiveTest() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10_000);
        assertThat(employeeService.findAll()).contains(expected);

        employeeService.remove("Ivan", "Ivanov");

        assertThat(employeeService.findAll()).doesNotContain(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Ivan", "Ivanov"));
    }

    @Test
    void removeNegativeTest() {
        Employee expected = new Employee("Andrey", "Andreev", 1, 10_000);
        assertThat(employeeService.findAll()).doesNotContain(expected);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.remove("Andrey", "Andreev"));
    }

    @Test
    void findPositiveTest() {
        Employee expected = new Employee("Ivan", "Ivanov", 1, 10_000);
        assertThat(employeeService.findAll()).contains(expected);
        assertThat(employeeService.find("Ivan", "Ivanov")).isEqualTo(expected);
    }

    @Test
    void findNegativeTest() {
        Employee employee = new Employee("Andrey", "Andreev", 1, 10_000);
        assertThat(employeeService.findAll()).doesNotContain(employee);
        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(() -> employeeService.find("Andrey", "Andreev"));
    }

    @Test
    void findAll() {
        assertThat(employeeService.findAll()).containsExactlyInAnyOrder(
                new Employee("Ivan", "Ivanov", 1, 10_000),
                new Employee("Petr", "Ivanov", 2, 40_000),
                new Employee("Ivan", "Petrov", 2, 30_000),
                new Employee("Petr", "Petrov", 1, 20_000)
        );
    }
}