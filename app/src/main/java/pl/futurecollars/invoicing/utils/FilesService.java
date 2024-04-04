package pl.futurecollars.invoicing.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FilesService {

  public void appendLineToFile(Path path, String line) throws IOException {
    // Use UTF-8 encoding to write the line to the file, appending it and creating the file if it does not exist
    Files.write(path, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
  }

  public void writeToFile(Path path, String line) throws IOException {
    // Use UTF-8 encoding to write the line to the file, truncating the file if it exists and creating it if not
    Files.write(path, line.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void writeLinesToFile(Path path, List<String> lines) throws IOException {
    // Use UTF-8 encoding to write lines to the file, truncating the file if it exists and creating it if not
    Files.write(path, lines, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public List<String> readAllLines(Path path) throws IOException {
    // Use UTF-8 encoding to read all lines from the file
    return Files.readAllLines(path, StandardCharsets.UTF_8);
  }
}
