package ge.tbc.testautomation.tests.assignment7;

import ge.tbc.testautomation.db.models.Employee;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.List;

import static ge.tbc.testautomation.config.DataBaseConfig.dbMapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Feature("Employee SOAP & DB Integration")
public class EmployeeDbTest {

    @Story("Insert Employee via DB and validate")
    @Test
    public void testInsertEmployee() {
        Long generatedId = System.currentTimeMillis();
        Employee newEmp = new Employee(generatedId, "John", "IT", "john@example.com", 5000.0);
        dbMapper().insertEmployee(newEmp);
        Allure.step("Inserted Employee into DB: " + newEmp);

        Employee dbEmp = dbMapper().selectEmployeeById(generatedId);
        assertThat(dbEmp, notNullValue());
        assertThat(dbEmp.getName(), is("John"));
        assertThat(dbEmp.getDepartment(), is("IT"));
        assertThat(dbEmp.getEmail(), is("john@example.com"));
        assertThat(dbEmp.getSalary(), is(5000.0));
        Allure.step("Verified Employee inserted and selected by ID: " + dbEmp);
    }

    @Story("Select employee by id and validate")
    @Test
    public void testSelectEmployeeById() {
        Long generatedId = System.currentTimeMillis();
        Employee emp = new Employee(generatedId, "Alice", "HR", "alice@example.com", 4500.0);
        dbMapper().insertEmployee(emp);

        Employee dbEmp = dbMapper().selectEmployeeById(generatedId);
        assertThat(dbEmp, notNullValue());
        assertThat(dbEmp.getName(), is("Alice"));
        assertThat(dbEmp.getDepartment(), is("HR"));
        assertThat(dbEmp.getEmail(), is("alice@example.com"));
        assertThat(dbEmp.getSalary(), is(4500.0));
        Allure.step("Selected Employee by ID and verified all fields: " + dbEmp);
    }

    @Story("Update employee and validate")
    @Test
    public void testUpdateEmployee() {
        Long generatedId = System.currentTimeMillis();
        Employee emp = new Employee(generatedId, "Bob", "Marketing", "bob@example.com", 4000.0);
        dbMapper().insertEmployee(emp);

        emp.setDepartment("Sales");
        emp.setSalary(4200.0);
        dbMapper().updateEmployee(emp);
        Allure.step("Updated Employee department and salary: " + emp);

        Employee updatedEmp = dbMapper().selectEmployeeById(generatedId);
        assertThat(updatedEmp, notNullValue());
        assertThat(updatedEmp.getDepartment(), is("Sales"));
        assertThat(updatedEmp.getSalary(), is(4200.0));
        Allure.step("Verified updated Employee in DB: " + updatedEmp);
    }

    @Story("Delete employee and validate")
    @Test
    public void testDeleteEmployee() {
        Long generatedId = System.currentTimeMillis();
        Employee emp = new Employee(generatedId, "Eve", "QA", "eve@example.com", 3900.0);
        dbMapper().insertEmployee(emp);

        dbMapper().deleteEmployee(generatedId);
        Employee deletedEmp = dbMapper().selectEmployeeById(generatedId);
        assertThat(deletedEmp, nullValue());
        Allure.step("Deleted Employee and verified absence in DB: " + generatedId);
    }

    @Test
    public void testSelectAllEmployees() {
        List<Employee> employees = dbMapper().selectAll();
        assertThat(employees, notNullValue());
        assertThat(employees, everyItem(notNullValue()));
        Allure.step("Selected all employees, count: " + employees.size());
    }
}
