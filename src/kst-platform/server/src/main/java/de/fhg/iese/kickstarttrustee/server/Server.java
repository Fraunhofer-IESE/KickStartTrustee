package de.fhg.iese.kickstarttrustee.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "de.fhg.iese.kickstarttrustee")
public class Server {
  public static void main(String[] args) {
    SpringApplication.run(Server.class, args);
  }
}
