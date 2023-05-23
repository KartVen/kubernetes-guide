package pl.kartven.kubernetesguideapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class KubernetesGuideAppApplication {
	private final AtomicInteger counter = new AtomicInteger(0);
	@Value("${spring.application.name}")
	private String appName;

	public static void main(String[] args) {
		SpringApplication.run(KubernetesGuideAppApplication.class, args);
	}

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<CounterInfo> getCounter() {
		String instanceName = String.valueOf(ManagementFactory.getRuntimeMXBean().getName());
		int counterValue = counter.incrementAndGet();

		return ResponseEntity.ok(new CounterInfo(appName, instanceName, counterValue));
	}

	public static class CounterInfo {
		String app;
		@JsonProperty("instance")
		String instanceName;
		@JsonProperty("counter")
		Integer counterValue;

		public CounterInfo(String app, String instanceName, Integer counterValue) {
			this.app = app;
			this.instanceName = instanceName;
			this.counterValue = counterValue;
		}

		public String getApp() {
			return app;
		}

		public String getInstanceName() {
			return instanceName;
		}

		public Integer getCounterValue() {
			return counterValue;
		}
	}

}
