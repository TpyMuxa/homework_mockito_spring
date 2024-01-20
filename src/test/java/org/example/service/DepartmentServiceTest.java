package org.example.service;

import org.example.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    private final Collection<Employee> employees = List.of(
            new Employee("Ivan", "Ivanov", 1, 10_000),
            new Employee("Petr", "Petrov", 1, 20_000),
            new Employee("Ivan", "Petrov", 2, 30_000),
            new Employee("Petr", "Ivanov", 2, 40_000),
            new Employee("Andrey", "Andreev", 3, 50_000)
    );

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    public void beforeEach() {
        when(employeeService.findAll()).thenReturn(employees);
    }

    @Test
    void getMaxSalaryPositiveTest() {
        int expected = 40_000;
        assertThat(departmentService.getMaxSalary(2)).isEqualTo(expected);
    }

    @Test
    void getMaxSalaryNegativeTest() {
        assertThat(departmentService.getMaxSalary(4)).isNull();
    }

    @Test
    void getMinSalaryPositiveTest() {
        int expected = 10_000;
        assertThat(departmentService.getMinSalary(1)).isEqualTo(expected);
    }

    @Test
    void getMinSalaryNegativeTest() {
        assertThat(departmentService.getMinSalary(4)).isNull();
    }

    @Test
    void getSumOfSalariesPositiveTest() {
        int expected = 30_000;
        assertThat(departmentService.getSumOfSalaries(1)).isEqualTo(expected);
    }

    @Test
    void getSumOfSalariesNegativeTest() {
        assertThat(departmentService.getSumOfSalaries(4)).isEqualTo(0);
    }

    @Test
    void getEmployeesFromDepartmentPositiveTest() {
        assertThat(departmentService.getEmployeesFromDepartment(2)).containsExactlyInAnyOrder(
                new Employee("Ivan", "Petrov", 2, 30_000),
                new Employee("Petr", "Ivanov", 2, 40_000)
        );
    }

    @Test
    void getEmployeesFromDepartmentNegativeTest() {
        assertThat(departmentService.getEmployeesFromDepartment(2)).containsExactlyInAnyOrder(
                new Employee("Ivan", "Petrov", 2, 30_000),
                new Employee("Petr", "Ivanov", 2, 40_000)
        );
    }

    @Test
    void getEmployeesGroupedByDepartment() {
        assertThat(departmentService.getEmployeesGroupedByDepartment()).containsExactlyInAnyOrderEntriesOf(
                Map.of(
                        1, List.of(new Employee("Ivan", "Ivanov", 1, 10_000), new Employee("Petr", "Petrov", 1, 20_000)),
                        2, List.of(new Employee("Ivan", "Petrov", 2, 30_000), new Employee("Petr", "Ivanov", 2, 40_000)),
                        3, List.of(new Employee("Andrey", "Andreev", 3, 50_000))
                )
        );
    }
}
