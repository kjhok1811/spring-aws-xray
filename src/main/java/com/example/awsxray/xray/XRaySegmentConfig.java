package com.example.awsxray.xray;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.slf4j.SLF4JSegmentListener;
import com.amazonaws.xray.strategy.ContextMissingStrategy;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.net.URL;

@Configuration
public class XRaySegmentConfig {
    @Value("${aws.xray.fixed-segment-name}")
    private String fixedSegmentName;

    @Value("${aws.xray.prefix-log-name}")
    private String prefixLogName;

    @Value("${aws.xray.sampling-rules-json}")
    private String samplingRulesJson;

    @PostConstruct
    public void init() {
        AWSXRay.beginSegment(fixedSegmentName);

        URL ruleFile = getClass().getResource(samplingRulesJson);

        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new EC2Plugin());
        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
        builder.withSegmentListener(new SLF4JSegmentListener(prefixLogName));
        builder.withContextMissingStrategy(new IgnoreContextMissingStrategy());
        AWSXRay.setGlobalRecorder(builder.build());

        AWSXRay.endSegment();
    }

    @Bean
    public Filter tracingFilter() {
        return new AWSXRayServletFilter(fixedSegmentName);
    }

    private class IgnoreContextMissingStrategy implements ContextMissingStrategy {
        @Override
        public void contextMissing(String message, Class<? extends RuntimeException> exceptionClass) {}
    }
}
