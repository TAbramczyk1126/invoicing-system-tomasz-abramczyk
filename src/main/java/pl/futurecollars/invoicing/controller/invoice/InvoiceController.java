package pl.futurecollars.invoicing.controller.invoice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.invoice.InvoiceService;

@Slf4j
@RestController
public class InvoiceController implements InvoiceApi {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @Override
  public long add(@RequestBody Invoice invoice) {
    log.info("Used postMapping update - info");
    return invoiceService.save(invoice);
  }

  @Override
  public List<Invoice> getAll() {
    return invoiceService.getAll();
  }

  @Override
  public ResponseEntity<Invoice> getById(@PathVariable int id) {
    log.info("Used getMapping update - info");
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) {
    log.info("Used putMapping update - info");
    return invoiceService.update(id, invoice)
        .map(name -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> deleteById(@PathVariable int id) {
    log.info("Used deleteMapping update - info");
    return invoiceService.delete(id)
        .map(name -> ResponseEntity.noContent().build())
        .orElse(ResponseEntity.notFound().build());
  }
}
