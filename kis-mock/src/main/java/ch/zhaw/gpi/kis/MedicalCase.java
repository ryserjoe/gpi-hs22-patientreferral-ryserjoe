package ch.zhaw.gpi.kis;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MedicalCase {
    @Id
    @GeneratedValue
    private Long caseId;
    @ManyToOne
    @JsonProperty(value = "patient")
    private Patient patient;
    private String reasons;
    private Long referrerId;
    private boolean isEmergency;
    @Temporal(TemporalType.DATE)
    private Date dateDesired;
    private String responsibleDepartment;
    @Temporal(TemporalType.DATE)
    private Date dateEarliest;
    @Temporal(TemporalType.DATE)
    private Date dateLatest;

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public Long getReferrerId() {
        return referrerId;
    }

    public void setReferrerId(Long referrerId) {
        this.referrerId = referrerId;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public Date getDateDesired() {
        return dateDesired;
    }

    public void setDateDesired(Date dateDesired) {
        this.dateDesired = dateDesired;
    }

    public String getResponsibleDepartment() {
        return responsibleDepartment;
    }

    public void setResponsibleDepartment(String responsibleDepartment) {
        this.responsibleDepartment = responsibleDepartment;
    }

    public Date getDateEarliest() {
        return dateEarliest;
    }

    public void setDateEarliest(Date dateEarliest) {
        this.dateEarliest = dateEarliest;
    }

    public Date getDateLatest() {
        return dateLatest;
    }

    public void setDateLatest(Date dateLatest) {
        this.dateLatest = dateLatest;
    }
}
