package employee.util;

import employee.dto.EmployeeDTO;
import employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeConverter {
    /**
     * Converts a list of Employee entities to a list of EmployeeDTOs.
     * @param employees - a list of Employee entities to be converted into data transfer objects
     * @return list of data transfer objects
     */
    public List<EmployeeDTO> convert(List<Employee> employees){
        return employees.stream().map(this::convert).collect(Collectors.toList());
    }

    /**
     * Converts an Employee entity into an EmployeeDTO.
     * @param employee - the entity to be converted into a data transfer object
     * @return a data transfer object
     */
    public EmployeeDTO convert(Employee employee){
        return new EmployeeDTO(employee.getFirstName(), employee.getLastName(), employee.getEmail());
    }

    /**
     * Converts an EmployeeDTO into an Employee entity.
     * @param employeeDTO - the data transfer object to be converted into an entity
     * @return an Employee entity
     */
    public Employee convert(EmployeeDTO employeeDTO){
        return new Employee(employeeDTO.getFirstName(), employeeDTO.getLastName(), employeeDTO.getEmail());
    }

    /**
     * Converts an existing EmployeeDTO into an Employee entity.
     * @param employee - the entity. It is needed because of its ID.
     * @param employeeDTO - the data transfer object to be converted
     * @return an Employee entity
     */
    public Employee convert(Employee employee, EmployeeDTO employeeDTO){
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());

        return employee;
    }
}
