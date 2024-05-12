package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
@Slf4j
public class FileBasedDatabase implements Database {

  private final Path databasePath;
  private final IdService idService;
  private final FilesService filesService;
  private final JsonService jsonService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndIncrement());
      filesService.appendLineToFile(databasePath, jsonService.toJson(invoice));
      return invoice.getId();
    } catch (IOException e) {
      log.info("Problem z zapisaniem danych");
      throw new RuntimeException("Failed to save invoice: " + e.getMessage());
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return filesService.readAllLines(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, Invoice.class))
          .findFirst();
    } catch (IOException ex) {
      log.info("Problem ze znalezieniem danych po id: {}", id);
      throw new RuntimeException("Database failed to get invoice with id: " + id, ex);
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return filesService.readAllLines(databasePath)
          .stream()
          .map(line -> jsonService.toObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      log.info("Problem z odczytaniem danych");
      throw new RuntimeException("Failed to read invoices from file", ex);
    }
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    try {
      List<String> allInvoices = filesService.readAllLines(databasePath);
      var invoicesWithoutInvoiceWithGivenId = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());
      updatedInvoice.setId(id);
      invoicesWithoutInvoiceWithGivenId.add(jsonService.toJson(updatedInvoice));
      filesService.writeLinesToFile(databasePath, invoicesWithoutInvoiceWithGivenId);
      allInvoices.removeAll(invoicesWithoutInvoiceWithGivenId);
      return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(allInvoices.get(0), Invoice.class));
    } catch (IOException ex) {
      log.info("Problem z aktualizacja danych po id: {}", id);
      throw new RuntimeException("Failed to update invoice with id: " + id, ex);
    }
  }

  @Override
  public Optional<Invoice> delete(int id) {
    try {
      var allInvoices = filesService.readAllLines(databasePath);
      var invoicesExceptDeleted = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());
      filesService.writeLinesToFile(databasePath, invoicesExceptDeleted);
      allInvoices.removeAll(invoicesExceptDeleted);
      return allInvoices.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(allInvoices.get(0), Invoice.class));
    } catch (IOException ex) {
      log.info("Problem z usunieciem danych po id: {}", id);
      throw new RuntimeException("Failed to delete invoice with id: " + id, ex);
    }
  }
}
