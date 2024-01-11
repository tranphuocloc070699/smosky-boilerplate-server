package com.smosky.boilerplateserver.spring;

import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/spring")
@RequiredArgsConstructor
public class SpringBoilerplateController {

  private final SpringDependencyRepository repository;
  private final PropertyRepository propertyRepository;
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
  public Object boilerplate(@RequestBody Dependency dependency) {
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
        /* Write .zip file from start.spring.io to folder */
        Files.write(Path.of("my-project"+".zip"), responseBody);

        /* Extract .zip file */
        ZipFile zipFile = new ZipFile("my-project.zip");
        zipFile.extractAll("extract-project");

        /*
        * Validate value
        * */

       /* Write config to application.properties */
        File file = new File("extract-project/src/main/resources/application.properties");
        writeArrayListToFile(dependency.getProperties(),file.toPath().toString());

        System.out.println("file existed:" + file);
        return file.exists();


      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Failed to download.");
    }
    return true;
  }

  private static void writeArrayListToFile(List<Property> arrayList, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (Property line : arrayList) {
        // Write each line followed by a newline character
        writer.write(line.getName()+"="+line.getValue());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception based on your requirements
    }
  }

  @PostMapping("/dependency")
  public Object createDependency(@RequestBody Dependency dto) {

    return repository.save(dto);
  }


  private List<String> readOutput(InputStream inputStream) throws IOException {
    try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
      return output.lines()
          .collect(Collectors.toList());

    }
  }
}
