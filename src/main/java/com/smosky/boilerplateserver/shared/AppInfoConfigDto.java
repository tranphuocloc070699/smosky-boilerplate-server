package com.smosky.boilerplateserver.shared;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "boilerplate-server")
@Getter
@Setter
public class AppInfoConfigDto {
  private String message;
  private Map<String, String> contactDetails;
  private List<String> onCallSupport;
}