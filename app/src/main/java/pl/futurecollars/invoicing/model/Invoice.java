package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

  private static int nextId = 1;

  private int id;
  private LocalDate date;
  private Company buyer;
  private Company seller;
  private List<InvoiceEntry> entries;

  public Invoice(LocalDate date, Company buyer, Company seller, List<InvoiceEntry> entries) {
    this.id = nextId++;
    this.date = date;
    this.buyer = buyer;
    this.seller = seller;
    this.entries = entries;
  }
}
