package ch.zhaw.gpi.kis;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Patient {
    @Id
    private Long insuranceNumber;
    private String lastName;
    private String firstName;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private Long plz;
    private String insuranceType;

    public Long getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(Long insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthdate) {
        this.birthDate = birthdate;
    }

    public Long getPlz() {
        return plz;
    }

    public void setPlz(Long plz) {
        this.plz = plz;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }
}
