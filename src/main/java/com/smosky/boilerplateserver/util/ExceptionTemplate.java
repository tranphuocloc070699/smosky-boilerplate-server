package com.smosky.boilerplateserver.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExceptionTemplate {

  public static void writePackageToExceptionFile(String exceptionDir, String exceptionPackagePath,
      String responseDtoPath) {

    /*AlreadyExistException*/
    StringBuilder alreadyExistFileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new FileReader(exceptionDir + "/AlreadyExistException.java"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        alreadyExistFileContent.append(line).append(System.lineSeparator());
      }
      alreadyExistFileContent.insert(0, exceptionPackagePath + System.lineSeparator());

      try (BufferedWriter writer = new BufferedWriter(
          new FileWriter(exceptionDir + "/AlreadyExistException.java"))) {
        writer.write(alreadyExistFileContent.toString());
      }

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    /*ConflictException*/
    StringBuilder conflictExceptionFileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new FileReader(exceptionDir + "/ConflictException.java"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        conflictExceptionFileContent.append(line).append(System.lineSeparator());
      }
      conflictExceptionFileContent.insert(0, exceptionPackagePath + System.lineSeparator());

      try (BufferedWriter writer = new BufferedWriter(
          new FileWriter(exceptionDir + "/ConflictException.java"))) {

        writer.write(conflictExceptionFileContent.toString());
      }

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    /*ResourceNotFoundException*/
    StringBuilder resourceNotFoundExceptionFileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new FileReader(exceptionDir + "/ResourceNotFoundException.java"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        resourceNotFoundExceptionFileContent.append(line).append(System.lineSeparator());
      }
      resourceNotFoundExceptionFileContent.insert(0, exceptionPackagePath + System.lineSeparator());

      try (BufferedWriter writer = new BufferedWriter(
          new FileWriter(exceptionDir + "/ResourceNotFoundException.java"))) {
        writer.write(resourceNotFoundExceptionFileContent.toString());

      }

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    /*GlobalExceptionHandler*/
    StringBuilder globalExceptionFileContent = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new FileReader(exceptionDir + "/GlobalExceptionHandler.java"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        globalExceptionFileContent.append(line).append(System.lineSeparator());

      }
      globalExceptionFileContent.insert(0, exceptionPackagePath + ";" + System.lineSeparator());
      /*+2 because exceptionPackagePath.length + ";"*/
      globalExceptionFileContent.insert(exceptionPackagePath.length() + 2,
          "import " + responseDtoPath + ";" + System.lineSeparator());
      try (BufferedWriter writer = new BufferedWriter(
          new FileWriter(exceptionDir + "/GlobalExceptionHandler.java"))) {
        writer.write(globalExceptionFileContent.toString());

      }

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
