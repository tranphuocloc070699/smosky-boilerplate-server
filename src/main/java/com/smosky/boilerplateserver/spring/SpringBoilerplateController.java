package com.smosky.boilerplateserver.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spring")
@RequiredArgsConstructor
public class SpringBoilerplateController {

  private final SpringDependencyRepository repository;

  final int size = 100 * 1024 * 1024;
  final ExchangeStrategies strategies = ExchangeStrategies.builder()
      .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
      .build();
  @GetMapping("")
  public Object hello() {
    String[] array = new String[]{"/home/loctran/.nvm/versions/node/v21.2.0/bin/npm","init","-y"};
    try {
      Process process = new ProcessBuilder(array).redirectErrorStream(true).start();

      InputStream is = process.getInputStream();
      InputStream error = process.getErrorStream();
      process.getOutputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(error));


      String line;
      StringBuilder output = new StringBuilder();
//      while ((line = reader.readLine()) != null) {
//        output.append(line).append("\n");
//        System.out.println(line);
//      }

      List<String> arr = readOutput(is);
      for (var item : arr) {
        System.out.println(item);
        output.append(item).append("\n");
      }
      return output;
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  @PostMapping("")
  public Object boilerplate() {
    String randomName= UUID.randomUUID().toString();
    String apiUrl = "https://start.spring.io/starter.zip";
    String queryParams = "bootVersion=3.2.1&type=maven-project&packaging=jar&jvmVersion=17" +
        "&groupId=com.loctran&artifactId=demo&name=demo&description=description" +
        "&dependencies=lombok,web,data-jpa,postgresql";

    String urlString = apiUrl + "?" + queryParams;
    WebClient webClient = WebClient.create();
    byte[] responseBody = webClient.get()
        .uri(urlString)
        .retrieve()
        .bodyToMono(byte[].class)
        .block();

    if (responseBody != null) {
      // Save the response to a file (my-project.zip)
      try {
        java.nio.file.Files.write(java.nio.file.Path.of("my-project"+".zip"), responseBody);

        System.out.println("Download successful!");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Failed to download.");
    }

    return true;
  }

  @PostMapping("/dependency")
  public Object createDependency(@RequestBody SpringDependency dto) {

    return repository.save(dto);
  }


  private List<String> readOutput(InputStream inputStream) throws IOException {
    try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
      return output.lines()
          .collect(Collectors.toList());

    }
  }
}
