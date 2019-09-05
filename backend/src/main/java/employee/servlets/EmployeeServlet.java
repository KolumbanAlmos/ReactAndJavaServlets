package employee.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import employee.services.EmployeeService;
import employee.dtos.EmployeeDTO;
import employee.utils.EmployeeConverter;
import employee.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/benovative/employee", asyncSupported = true)
public class EmployeeServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServlet.class);

    private static final long serialVersionUID = 1L;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeConverter employeeConverter;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Processes GET requests. The endpoint can be called with or without the "email" query param.
     * If called with the "email" query param filled, then it will return only the Employee with the given e-mail address.
     * Otherwise it will return all the Employees.
     * @param request - the request that can contain an "email" query param.
     * @param response - a response containing an Employee or a list of Employees based on the request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            response.setContentType("application/json");

            String email = request.getParameter("email");
            if(email == null || email.isEmpty()) {
                List<Employee> employees = employeeService.findAll();

                mapper.writeValue(response.getOutputStream(), employeeConverter.convert(employees));
            } else {
                Employee employee = employeeService.findByEmail(email);

                if(employee == null){
                    logger.error(Constants.EMPLOYEE_DOES_NOT_EXIST_ERROR_MESSAGE);
                    throw new IllegalArgumentException(Constants.EMPLOYEE_DOES_NOT_EXIST_ERROR_MESSAGE);
                }

                mapper.writeValue(response.getOutputStream(), employeeConverter.convert(employee));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Processes POST requests. Saves an Employee into the database with the given data coming in a json body.
     * If the Employee with the given e-mail address already exists then it works as an update.
     * @param request - request containing the Employee json body
     * @param response -
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(request.getInputStream()));
            String json = br.readLine();

            EmployeeDTO employeeDTO = mapper.readValue(json, EmployeeDTO.class);
            if(!employeeDTO.isValid()){
                logger.error(Constants.EMAIL_VALUE_NOT_FILLED_ERROR_MESSAGE);
                throw new IllegalArgumentException(Constants.EMAIL_VALUE_NOT_FILLED_ERROR_MESSAGE);
            }

            Employee existingEmployee = employeeService.findByEmail(employeeDTO.getEmail());

            if(existingEmployee != null){
                employeeService.save(employeeConverter.convert(existingEmployee, employeeDTO));
            } else {
                employeeService.save(employeeConverter.convert(employeeDTO));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Processes PUT requests. Updates the Employee with the given e-mail address.
     * Also updates the e-mail address if the "newEmail" query param is filled.
     * @param request - the request containing the Employee json body. It can also contain the "newEmail" query param.
     * @param response -
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response){
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(request.getInputStream()));
            String json = br.readLine();

            EmployeeDTO employeeDTO = mapper.readValue(json, EmployeeDTO.class);

            Employee existingEmployee = employeeService.findByEmail(employeeDTO.getEmail());
            if(existingEmployee == null){
                logger.error(Constants.EMPLOYEE_DOES_NOT_EXIST_ERROR_MESSAGE);
                throw new IllegalArgumentException(Constants.EMPLOYEE_DOES_NOT_EXIST_ERROR_MESSAGE);
            }

            String newEmail = request.getParameter("newEmail");
            if(newEmail != null && !newEmail.isEmpty()){
                employeeDTO.setEmail(newEmail);
            }

            Employee updatedEmployee = employeeConverter.convert(existingEmployee, employeeDTO);
            employeeService.save(updatedEmployee);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Processes DELETE requests. The Employee with the given e-mail address will be deleted from the database.
     * @param request - the request containing the "email" query param.
     * @param response -
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        String email = request.getParameter("email");

        if(email == null || email.isEmpty()){
            logger.error(Constants.MISSING_EMAIL_QUERY_PARAM_ERROR_MESSAGE);
            throw new IllegalArgumentException(Constants.MISSING_EMAIL_QUERY_PARAM_ERROR_MESSAGE);
        }

        employeeService.deleteByEmail(email);
    }
}
