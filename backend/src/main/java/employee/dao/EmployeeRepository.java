package employee.dao;

import employee.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    public Employee findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Employee e WHERE e.email = :email")
    public void deleteByEmail(@Param("email") String email);
}
