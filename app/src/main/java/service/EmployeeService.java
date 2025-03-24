package service;

import model.Employee;
import repository.EmployeeRepository;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository repo) {
        this.employeeRepository = repo;
    }

    public Employee login(String username, String password) {
        for (Employee e : employeeRepository.findAll()) {
            if (e.getUsername().equals(username) && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null;
    }
}