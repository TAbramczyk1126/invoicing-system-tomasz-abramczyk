package pl.futurecollars.invoicing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.InvoiceService
import spock.lang.Specification

@SpringBootTest
class InvoiceApplicationTest extends Specification {

    @Autowired
    private InvoiceService invoiceService;

    def "invoice service is created"() {
        expect:
        invoiceService

    }
    def "should return no invoices when database is empty"() {
        given:

        when:
        List<Invoice> invoices = invoiceService.getAll()

        then:
        invoices.isEmpty()
        invoices.size() == 0
    }
}
