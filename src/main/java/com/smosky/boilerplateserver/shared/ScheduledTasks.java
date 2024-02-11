package com.smosky.boilerplateserver.shared;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*@Component*/
public class ScheduledTasks {
  @Scheduled(cron = "0 * * * * *") // Cron expression for running every minute
  public void execute() {
  deleteAllFilesInFolder("zip");
  deleteAllFilesInFolder("zip-response");
    try {
      deleteFolders("extract-zip");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private  void deleteFolders(String directory) throws IOException {
    Path dir = Paths.get(directory); //path to the directory
    Files
        .walk(dir) // Traverse the file tree in depth-first order
        .sorted(Comparator.reverseOrder())
        .forEach(path -> {
          try {
            System.out.println("Deleting: " + path);
            Files.delete(path);  //delete each file or directory
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  private void deleteAllFilesInFolder(String directoryPath) {
    File directory = new File(directoryPath);

    // Get a list of all files in the directory
    File[] files = directory.listFiles();

    if (files != null) {
      // Iterate through the files and delete each one
      for (File file : files) {
        if (file.isFile()) {
          // Delete the file
          boolean deleted = file.delete();
          if (deleted) {
            System.out.println("Deleted file: " + file.getName());
          } else {
            System.err.println("Failed to delete file: " + file.getName());
          }
        }
      }
    } else {
      System.err.println("Failed to list files in the directory.");
    }


  }
}
