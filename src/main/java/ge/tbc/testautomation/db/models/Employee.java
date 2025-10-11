package ge.tbc.testautomation.db.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class Employee {
    private Long employee_id;
    private String name;
    private String department;
    private String email;
    private Double salary;
}
