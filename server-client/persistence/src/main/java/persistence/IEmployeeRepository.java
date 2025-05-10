package persistence;

import models.Employee;

import java.util.Optional;

public interface IEmployeeRepository extends ICrudRepository<Long, Employee> {
    Optional<Employee> findBy(String username, String password);
}
