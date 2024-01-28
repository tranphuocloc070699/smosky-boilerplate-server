package com.smosky.boilerplateserver.util;

public class StringUtils {

  public static String capitalizeFirstLetter(String input) {
    if (input == null || input.isEmpty()) {
      return input; // Return unchanged if input is null or empty
    }
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }
}
