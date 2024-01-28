package com.smosky.boilerplateserver.util;

import java.io.BufferedWriter;
import java.io.IOException;

public class CustomWriter {

  private final BufferedWriter bufferedWriter;

  public CustomWriter(BufferedWriter writer) {
    this.bufferedWriter = writer;
  }


  public void write(String... content) throws IOException {
    bufferedWriter.write(String.join("", content));
    bufferedWriter.newLine();
  }

  public void write(Integer line, String... content) throws IOException {
    bufferedWriter.write(String.join("", content));
    for (int i = 0; i < line; i++) {
      bufferedWriter.newLine();
    }
  }

}
