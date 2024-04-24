package pl.futurecollars.invoicing.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.file.IdService;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.JsonService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

  @Bean
  public IdService idService(FilesService fileService,
                             @Value("${invoicing-system.database.directory}") String databaseDirectory,
                             @Value("${invoicing-system.database.id.file}") String idFile
  ) throws IOException {
    Path idFilePath = Files.createTempFile(databaseDirectory, idFile);
    return new IdService(idFilePath, fileService);
  }

  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
  @Bean
  public Database fileBasedDatabase(IdService idService,
                                    FilesService fileService,
                                    JsonService jsonService,
                                    @Value("${invoicing-system.database.directory}") String databaseDirectory,
                                    @Value("${invoicing-system.database.invoices.file}") String invoicesFile) throws IOException {
//    log.trace("Used file database - trace");
//    log.debug("Used file database - debug");
    log.info("Used file database - info");
//    log.warn("Used file database - warning");
//    log.error("Used file database - error");
    Path databaseFilePath = Files.createTempFile(databaseDirectory, invoicesFile);
    return new FileBasedDatabase(databaseFilePath, idService, fileService, jsonService);
  }

  @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
  @Bean
  public Database inMemoryDatabase() {
    log.info("Used in memory database");
    return new InMemoryDatabase();
  }
}