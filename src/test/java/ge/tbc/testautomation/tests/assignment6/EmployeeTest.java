package ge.tbc.testautomation.tests.assignment6;

import com.example.springboot.soap.interfaces.AddEmployeeRequest;
import com.example.springboot.soap.interfaces.EmployeeInfo;
import ge.tbc.testautomation.soapservice.Marshall;
import ge.tbc.testautomation.soapservice.SoapServiceSender;
import io.qameta.allure.*;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Epic("Employee Service")
public class EmployeeTest {
    String url = "http://localhost:8087/ws/employees.wsdl";

    String addAction = "";
    String getAction = "";

    private final long id = 12345L;

    @Story("Add employee")
    @Description("Adds an employee and asserts success message")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void addEmployee() throws Exception {
        EmployeeInfo info = new EmployeeInfo();
        info.setEmployeeId(id);
        info.setName("David Devidze");
        info.setDepartment("IT");
        info.setPhone("+9951234567890");
        info.setAddress("Rustaveli ave");
        info.setSalary(new BigDecimal("5000.00"));
        info.setEmail("david.devidze@example.com");

        LocalDate birth = LocalDate.of(1990, 1, 15);
        XMLGregorianCalendar birthDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(birth.toString());
        info.setBirthDate(birthDate);

        AddEmployeeRequest request = new AddEmployeeRequest();
        request.setEmployeeInfo(info);

        String requestBody = Marshall.marshallSoapRequest(request);

        Response response = SoapServiceSender.send(url, addAction, requestBody)
                .then().log().all()
                .statusCode(200)
                .extract().response();

        XmlPath xml = new XmlPath(response.asString());
        String resultMessage = xml.getString("Envelope.Body.addEmployeeResponse.serviceStatus.message");

        assertThat("Add response should contain success message", resultMessage, equalTo("Content Added Successfully"));
    }

    @Story("Get employee by id")
    @Description("Retrieves employee by id and validates fields")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getEmployeeById() {
        long expectedId = id;

        String getRequest = String.format("""
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sch="http://interfaces.soap.springboot.example.com">
              <soapenv:Header/>
              <soapenv:Body>
                <sch:getEmployeeByIdRequest>
                  <sch:employeeId>%d</sch:employeeId>
                </sch:getEmployeeByIdRequest>
              </soapenv:Body>
            </soapenv:Envelope>""", expectedId);

        Response response = SoapServiceSender.send(url, getAction, getRequest)
                .then().log().all()
                .statusCode(200)
                .extract().response();

        XmlPath xml = new XmlPath(response.asString());

        Long employeeId = xml.getLong("Envelope.Body.getEmployeeByIdResponse.employeeInfo.employeeId");
        String name = xml.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.name");
        String department = xml.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.department");
        String email = xml.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.email");
        String salary = xml.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.salary");
        String birthDate = xml.getString("Envelope.Body.getEmployeeByIdResponse.employeeInfo.birthDate");

        assertThat(employeeId, notNullValue());
        assertThat(employeeId, equalTo(expectedId));
        assertThat(name, equalTo("David Devidze"));
        assertThat(department, equalTo("IT"));
        assertThat(email, equalTo("david.devidze@example.com"));
        assertThat(salary, equalTo("5000.00"));
        assertThat(birthDate, equalTo("1990-01-15"));
    }
}
