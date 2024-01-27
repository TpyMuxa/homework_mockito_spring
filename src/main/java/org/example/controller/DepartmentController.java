package org.example.controller;

import org.example.model.Employee;
import org.example.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {

        this.departmentService = departmentService;
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesFromDepartment(@PathVariable("id") int department) {
        return departmentService.getEmployeesFromDepartment(department);
    }

    @GetMapping("/{id}/salary/sum")
    public int getSumOfSalaries(@PathVariable("id") int department) {
        return departmentService.getSumOfSalaries(department);
    }
    @GetMapping("/{id}/salary/max")
    public Integer getMaxSalary(@PathVariable("id") int department) {
        return departmentService.getMaxSalary(department);
    }

    @GetMapping("/{id}/salary/min")
    public Integer getMinSalary(@RequestParam("departmentId") int department) {
        return departmentService.getMinSalary(department);
    }

    @GetMapping("/employees")
    public Map<Integer, List<Employee>> getEmployeesGroupedByDepartment() {
        return departmentService.getEmployeesGroupedByDepartment();
    }
}