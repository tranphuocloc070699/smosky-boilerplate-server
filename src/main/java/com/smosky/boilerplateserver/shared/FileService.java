package com.smosky.boilerplateserver.shared;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

  /**
   * Directory Structure to json
   */
  public  Node getNode(File node) throws IOException {
    if (node.isDirectory()) {
      return new Node(UUID.randomUUID().toString(),node.getName(), node.getPath(), "directory", null, getDirList(node),true,false);
    } else {
      return new Node(UUID.randomUUID().toString(),node.getName(), node.getPath(), "file",
          readFileContent(node.toPath()), new ArrayList<>(),false,false);
    }
  }

  private  List<Node> getDirList(File node) {
    List<Node> nodeList = new ArrayList<>();
    for (File n : node.listFiles()) {
      try {
        nodeList.add(getNode(n));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return nodeList;
  }


  private String readFileContent(Path filePath) throws IOException {
    String extension = getFileExtension(filePath.toString());

    if (extension != null) {
      switch (extension.toLowerCase()) {
        case "zip":
        case "jar":
        case "war":
          return "This content cannot read normally";
        default:
          return Files.readString(filePath);
      }
    }

    return "Unknown";
  }

  private  String getFileExtension(String filePath) {
    if (filePath != null && filePath.lastIndexOf(".") != -1) {
      return filePath.substring(filePath.lastIndexOf(".") + 1);
    }
    return null;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class Node {
    private String id;
    private String name;
    private String location;
    private String type;
    private String content;
    private List<Node> nodeList;
    private Boolean isExpanded;
    private Boolean isSelected;
  }
}
