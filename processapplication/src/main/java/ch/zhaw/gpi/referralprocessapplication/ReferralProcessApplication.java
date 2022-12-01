package ch.zhaw.gpi.referralprocessapplication;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Hauptklasse für die Prozessapplikation, welche diese mit allen Abhängigkeiten
 * ausführt. Basis ist das SpringBoot-Framework, welches von Camunda erweitert
 * wurde.
 *
 * Zusammengefasst werden dabei folgende Schritte durchlaufen: 
 * 1. Tomcat initialisieren 2. Camunda REST API aktivieren 3.
 * Camunda Job Executor initialisieren 4. Camunda Process Engine inklusive
 * Datenbank initialisieren gemäss application.properties
 * 5. Sofern noch nicht vorhanden, den Demo-Admin-User erstellen gemäss
 * application.properties 6. Die Webapps (Tasklist, Admin, Cockpit) deployen 7.
 * Gefundene Prozesse (z.B. ExampleProcess) deployen 8. Alle Komponenten starten
 * => unter localhost:8080 ist Tomcat erreichbar *
 */
@SpringBootApplication
@EnableProcessApplication
public class ReferralProcessApplication {

    @Bean(name = "kisRestClient")
    public RestTemplate getKisRestClient(RestTemplateBuilder builder){
        return builder.build();
    }

    /**
     * Haupt-Methode, welche beim Run-Befehl eine
     * Camunda-Prozessapplikation erstellt.
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ReferralProcessApplication.class, args);
    }
}
