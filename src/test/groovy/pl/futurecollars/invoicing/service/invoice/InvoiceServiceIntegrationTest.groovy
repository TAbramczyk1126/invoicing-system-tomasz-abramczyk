package pl.futurecollars.invoicing.service.invoice

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.helpers.TestHelpers.invoice

class InvoiceServiceIntegrationTest extends Specification {

    private InvoiceService service;
    private List<Invoice> invoices;

    def setup() {
        Database db = new InMemoryDatabase();
        service = new InvoiceService(db);

        invoices = (1..12).collect { invoice(it) }
    }

    def "should save invoices returning sequential id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect({ service.save(it) })

        then:
        ids == (1L..invoices.size()).collect()
        ids.forEach({ assert service.getById(it).isPresent() })
        ids.forEach({ assert service.getById(it).get().getId() == it })
        ids.forEach({ assert service.getById(it).get() == invoices.get((int)it - 1) })
    }

    def "get by id returns empty optional when there is no invoice with given id"() {
        expect:
        !service.getById(1).isPresent()
    }

    def "get all returns empty collection if there were no invoices"() {
        expect:
        service.getAll().isEmpty()
    }

    def "get all returns all invoices in the database, deleted invoice is not returned"() {
        given:
        invoices.forEach({ service.save(it) })

        expect:
        service.getAll().size() == invoices.size()
        service.getAll().forEach({ assert it == invoices.get((int) it.getId() - 1) })

        when:
        service.delete(1)

        then:
        service.getAll().size() == invoices.size() - 1
        service.getAll().forEach({ assert it == invoices.get((int)it.getId() - 1) })
        service.getAll().forEach({ assert it.getId() != 1 })
    }

    def "can delete all invoices"() {
        given:
        invoices.forEach({ service.save(it) })

        when:
        invoices.forEach({ service.delete(it.getId()) })

        then:
        service.getAll().isEmpty()
    }

    def "deleting not existing invoice is not causing any error"() {
        given:
        def service = new InvoiceService(new InMemoryDatabase())

        when:
        def deletedInvoice = service.delete(123)

        then:
        deletedInvoice == Optional.empty()
    }

    def "it's possible to update the invoice"() {
        given:
        long id = service.save(invoices.get(0))

        when:
        service.update(id, invoices.get(1))

        then:
        service.getById(id).get() == invoices.get(1)
    }

    def "updating not existing invoice throws exception"() {
        given:
        InvoiceService service = new InvoiceService(new InMemoryDatabase())
        Invoice updatedInvoice = new Invoice()

        when:
        Optional<Invoice> result = service.update(123, updatedInvoice)
        then:
        result == Optional.empty()
    }
}
