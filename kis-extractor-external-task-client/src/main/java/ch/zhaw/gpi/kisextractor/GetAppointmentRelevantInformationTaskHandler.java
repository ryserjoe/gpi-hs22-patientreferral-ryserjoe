package ch.zhaw.gpi.kisextractor;

import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.JsonParser;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Map;

@Component
@ExternalTaskSubscription("GetScheduledAppointmentRelevantInformation")
public class GetAppointmentRelevantInformationTaskHandler implements ExternalTaskHandler {
    private static final SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String MEDICAL_CASE_GET_END_POINT = "http://localhost:8090/api/medicalCases/{id}";

    @Autowired
    private RestTemplate kisRestClient;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            ResponseEntity<String> medicalCaseResponse = kisRestClient.exchange(MEDICAL_CASE_GET_END_POINT, HttpMethod.GET, null, String.class, (Long) externalTask.getVariable("case_id"));
            JsonObject medicalCase = JsonParser.parseString(medicalCaseResponse.getBody()).getAsJsonObject();
            externalTaskService.complete(externalTask,
                    Map.of("case_appointment_earliest", yearMonthDayFormat.parse(medicalCase.get("dateEarliest").toString().replace("\"", "")),
                            "case_appointment_latest", yearMonthDayFormat.parse(medicalCase.get("dateLatest").toString().replace("\"", ""))));
        } catch (HttpClientErrorException e) {
            externalTaskService.handleFailure(externalTask, "Fall-ID nicht gefunden", e.getMessage(), 0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
