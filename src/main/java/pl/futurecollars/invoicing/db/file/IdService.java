package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.utils.FilesService;

public class IdService {

  private final Path idFilePath;
  private final FilesService filesService;

  private long nextId = 1;

  public IdService(Path idFilePath, FilesService fileService) {
    this.idFilePath = idFilePath;
    this.filesService = fileService;
    try {
      List<String> lines = filesService.readAllLines(idFilePath);
      if (lines.isEmpty()) {
        filesService.writeToFile(idFilePath, "1");
      } else {
        nextId = Integer.parseInt(lines.get(0));
      }
    } catch (IOException ex) {
      throw new RuntimeException("Failed to initialize id database", ex);
    }
  }

  public long getNextIdAndIncrement() {
    try {
      filesService.writeToFile(idFilePath, String.valueOf(nextId + 1));
      return nextId++;
    } catch (IOException e) {
      throw new RuntimeException("Failed to save last id file: " + e.getMessage());
    }
  }
}
