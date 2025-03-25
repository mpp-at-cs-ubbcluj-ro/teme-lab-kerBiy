package service;

import model.Employee;
import repository.IEmployeeRepository;

public class EmployeeService {
    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository repo) {
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