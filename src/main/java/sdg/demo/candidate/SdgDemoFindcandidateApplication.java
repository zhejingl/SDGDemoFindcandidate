package sdg.demo.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;

@SpringBootApplication
public class SdgDemoFindcandidateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdgDemoFindcandidateApplication.class, args);
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
