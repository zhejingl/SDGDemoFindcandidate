package sdg.demo.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class SdgDemoFindcandidateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdgDemoFindcandidateApplication.class, args);
		System.out.println("SDGDemo Application is up and running: candidate service");
	}
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
	/*
	@Bean
	public io.opentracing.Tracer tracer() {
		return Configuration.fromEnv().getTracer();
	}
	*/
}
