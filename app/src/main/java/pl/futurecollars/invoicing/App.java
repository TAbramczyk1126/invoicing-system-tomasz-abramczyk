/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package pl.futurecollars.invoicing;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.JsonService;

public class App {

  public String getGreeting() {
    return "Hello World!";
  }

  public static void main(String[] args) throws IOException {
    System.out.println(new App().getGreeting());

    Database db = new InMemoryDatabase();
    InvoiceService service = new InvoiceService(db);

    Company buyer = new Company("1111111", "ul. Bukowinska 24d/7 02-703 Warszawa, Polska", "iCode Trust Sp. z o.o");
    Company seller = new Company("552-168-66-00", "32-005 Niepolomice, Nagietkowa 19", "Piotr Kolacz Development");

    Company buyer1 = new Company("123-111-22-22", "ul. Cedrowa 24 02-703 Halinow, Polska", "Test 2");

    List<InvoiceEntry> products = List.of(new InvoiceEntry("Programming course", BigDecimal.valueOf(10000), BigDecimal.valueOf(2300), Vat.VAT_23));

    Invoice invoice = new Invoice(LocalDate.now(), buyer, seller, products);
    Invoice invoice2 = new Invoice(LocalDate.now(), buyer1, seller, products);

    int id = service.save(invoice);
    int id1 = service.save(invoice2);

    Invoice invoiceFromDb = service.getById(id).orElseThrow(() -> new RuntimeException());

    System.out.println(service.getAll());

    service.delete(id);

    service.save(invoice);

    JsonService jsonService = new JsonService();
    String jsonData = jsonService.toJson(invoice);

    FilesService fileService = new FilesService();
    fileService.writeToFile(Path.of("invoice.json"), jsonData);

    System.out.println("Invoice data saved to invoice.json file.");
  }
}
