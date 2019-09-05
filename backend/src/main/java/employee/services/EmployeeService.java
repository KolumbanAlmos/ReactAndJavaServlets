package employee.services;

import employee.entities.Employee;
import employee.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email);
    }

    public void save(Employee employee){
        employeeRepository.save(employee);
    }

    public void deleteByEmail(String email){
        employeeRepository.deleteByEmail(email);
    }

}
