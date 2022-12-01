package ch.zhaw.gpi.referralprocessapplication;

import camundajar.impl.com.google.gson.JsonElement;
import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.JsonParser;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Named("createPatientAndMedicalCaseAdapter")
public class CreatePatientAndMedicalCaseDelegate implements JavaDelegate {
    private static final SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String PATIENT_GET_END_POINT = "http://localhost:8090/api/patients/{patInsuranceNumber}";
    private static final String PATIENT_POST_END_POINT = "http://localhost:8090/api/patients";
    private static final String MEDICAL_CASE_POST_END_POINT = "http://localhost:8090/api/medicalCases";

    @Autowired
    private RestTemplate kisRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Long patient = createOrUpdatePatient(delegateExecution);
        createNewCase(delegateExecution, patient);
    }

    private void createNewCase(DelegateExecution delegateExecution, Long patient) {
        JsonObject medicalCase = new JsonObject();
        medicalCase.addProperty("reasons", getVariable(delegateExecution, "case_ref_reasons"));
        medicalCase.addProperty("referrerId", getLongVariable(delegateExecution, "case_ref_id"));
        medicalCase.addProperty("emergency", getBooleanVariable(delegateExecution, "case_is_emergency"));
        medicalCase.addProperty("dateDesired", yearMonthDayFormat.format(getDateVariable(delegateExecution, "case_desired_date")));
        medicalCase.addProperty("responsibleDepartment", getVariable(delegateExecution, "case_department"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(medicalCase.toString(), httpHeaders);
        ResponseEntity<Void> exchange = kisRestClient.exchange(MEDICAL_CASE_POST_END_POINT, HttpMethod.POST, httpEntity, Void.class);
        String location = exchange.getHeaders().get("Location").get(0);
        Long caseId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        httpHeaders.setContentType(MediaType.valueOf("text/uri-list"));
        httpEntity = new HttpEntity<>(PATIENT_POST_END_POINT + "/" + patient, httpHeaders);
        kisRestClient.exchange(MEDICAL_CASE_POST_END_POINT + "/" + caseId + "/patient", HttpMethod.PUT, httpEntity, Void.class);

        delegateExecution.setVariable("case_id", caseId);
    }

    private Long createOrUpdatePatient(DelegateExecution delegateExecution) {
        Long patInsuranceNumber = getLongVariable(delegateExecution, "pat_insurance_number");
        return findPatient(patInsuranceNumber).orElseGet(() -> createPatient(delegateExecution));
    }

    private Optional<Long> findPatient(Long patInsuranceNumber) {
        try {
            ResponseEntity<String> patient = kisRestClient.exchange(PATIENT_GET_END_POINT, HttpMethod.GET, null, String.class, patInsuranceNumber);
            String  location = JsonParser.parseString(patient.getBody()).getAsJsonObject().get("_links").getAsJsonObject().get("patient").getAsJsonObject().get("href").toString();
            return Optional.of(Long.valueOf(location.substring(location.lastIndexOf("/") + 1)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Long createPatient(DelegateExecution delegateExecution) {
        JsonObject patient = new JsonObject();
        patient.addProperty("insuranceNumber", getLongVariable(delegateExecution, "pat_insurance_number"));
        patient.addProperty("insuranceType", getVariable(delegateExecution, "pat_insurance_type"));
        patient.addProperty("firstName", getVariable(delegateExecution, "pat_firstname"));
        patient.addProperty("lastName", getVariable(delegateExecution, "pat_lastname"));
        patient.addProperty("birthDate", yearMonthDayFormat.format(getDateVariable(delegateExecution, "pat_birthday")));
        patient.addProperty("plz", getLongVariable(delegateExecution, "pat_plz"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(patient.toString(), httpHeaders);
        kisRestClient.exchange(PATIENT_POST_END_POINT, HttpMethod.POST, httpEntity, Void.class);
        return getLongVariable(delegateExecution, "pat_insurance_number");
    }

    private String getVariable(DelegateExecution delegateExecution, String name) {
        Object variable = delegateExecution.getVariable(name);
        return (String) variable;
    }

    private Date getDateVariable(DelegateExecution delegateExecution, String name) {
        Object variable = delegateExecution.getVariable(name);
        return (Date) variable;
    }

    private Boolean getBooleanVariable(DelegateExecution delegateExecution, String name) {
        Object variable = delegateExecution.getVariable(name);
        return (Boolean) variable;
    }

    private Long getLongVariable(DelegateExecution delegateExecution, String name) {
        Object variable = delegateExecution.getVariable(name);
        return (Long) variable;
    }
}
