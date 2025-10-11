package ge.tbc.testautomation.db.mapper;

import ge.tbc.testautomation.db.models.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EmployeeMapper {

    @Insert("INSERT INTO employee (employee_id, name, department, email, salary) " +
            "VALUES (#{employee_id}, #{name}, #{department}, #{email}, #{salary})")
    void insertEmployee(Employee employee);

    @Select("SELECT * FROM employee WHERE employee_id = #{employee_id}")
    Employee selectEmployeeById(@Param("employee_id") Long employee_id);

    @Update("UPDATE employee SET name=#{name}, department=#{department}, email=#{email}, salary=#{salary} WHERE employee_id=#{employee_id}")
    void updateEmployee(Employee employee);

    @Delete("DELETE FROM employee WHERE employee_id=#{employee_id}")
    void deleteEmployee(@Param("employee_id") Long employee_id);

    @Select("SELECT * FROM employee")
    List<Employee> selectAll();
}
