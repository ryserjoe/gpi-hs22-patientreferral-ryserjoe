package ch.zhaw.gpi.referralprocessapplication;

import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.JsonParser;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Named;
import java.text.SimpleDateFormat;

@Named("readInformationFromKis")
public class GetAppointmentDateRangeDelegate implements JavaDelegate {
    private static final SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String MEDICAL_CASE_GET_END_POINT = "http://localhost:8090/api/medicalCases/{id}";

    @Autowired
    private RestTemplate kisRestClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ResponseEntity<String> medicalCaseResponse = kisRestClient.exchange(MEDICAL_CASE_GET_END_POINT, HttpMethod.GET, null, String.class, delegateExecution.getVariable("case_id"));
        JsonObject medicalCase = JsonParser.parseString(medicalCaseResponse.getBody()).getAsJsonObject();
        delegateExecution.setVariable("case_appointment_earliest", yearMonthDayFormat.parse(medicalCase.get("dateEarliest").toString().replace("\"","")));
        delegateExecution.setVariable("case_appointment_latest", yearMonthDayFormat.parse(medicalCase.get("dateLatest").toString().replace("\"","")));
    }
}
