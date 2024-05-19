package pl.futurecollars.invoicing.controller

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Unroll
import java.time.LocalDate

import static pl.futurecollars.invoicing.helpers.TestHelpers.company

@Unroll
class TaxCalculatorControllerIntegrationTest extends AbstractControllerTest {

    def "zeros are returned when there are no invoices in the system"() {
        when:
        def taxCalculatorResponse = calculateTax(company(0))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
        taxCalculatorResponse.pensionInsurance == 0
        taxCalculatorResponse.incomeMinusCostsMinusPensionInsurance == 0.00
        taxCalculatorResponse.incomeMinusCostsMinusPensionInsuranceRounded == 0
        taxCalculatorResponse.incomeTax == 0.00
        taxCalculatorResponse.healthInsurancePaid == 0
        taxCalculatorResponse.healthInsuranceToSubtract == 0
        taxCalculatorResponse.incomeTaxMinusHealthInsurance == 0
        taxCalculatorResponse.finalIncomeTax == 0
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "zeros are returned when tax id is not matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax(company(-14))

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 0
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax(company(5))
        println "Response for company(5): $taxCalculatorResponse"

        then:
        taxCalculatorResponse.income == 15000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 15000
        taxCalculatorResponse.collectedVat == 1200.0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 1200.0

        when:
        taxCalculatorResponse = calculateTax(company(10))
        println "Response for company(10): $taxCalculatorResponse"

        then:
        taxCalculatorResponse.income == 55000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 55000
        taxCalculatorResponse.collectedVat == 4400.0
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 4400.0

        when:
        taxCalculatorResponse = calculateTax(company(15))
        println "Response for company(15): $taxCalculatorResponse"

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 15000
        taxCalculatorResponse.incomeMinusCosts == -15000
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 1200.0
        taxCalculatorResponse.vatToReturn == -1200.0
    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addUniqueInvoices(15)

        when:
        def taxCalculatorResponse = calculateTax(company(12))

        then:
        taxCalculatorResponse.income == 78000
        taxCalculatorResponse.costs == 3000
        taxCalculatorResponse.incomeMinusCosts == 75000
        taxCalculatorResponse.collectedVat == 6240.0
        taxCalculatorResponse.paidVat == 240.0
        taxCalculatorResponse.vatToReturn == 6000.0
    }
    def "tax is calculated correctly when car is used for personal purposes"() {
        given:
        def invoice = Invoice.builder()
                .date(LocalDate.now())
                .number("no number :)")
                .seller(company(1))
                .buyer(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .vatValue(BigDecimal.valueOf(23.45))
                                .vatRate(Vat.VAT_23)
                                .netPrice(BigDecimal.valueOf(100))
                                .quantity(1.0)
                                .expensionRelatedToCar(
                                        Car.builder()
                                                .personalUse(true)
                                                .registrationNumber("KWI 555234")
                                                .build()
                                )
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoice)

        when:
        def taxCalculatorResponse = calculateTax(invoice.getSeller())

        then: "no proportion - it applies only when you are the buyer"
        taxCalculatorResponse.income == 100
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.incomeMinusCosts == 100
        taxCalculatorResponse.collectedVat == 23.45
        taxCalculatorResponse.paidVat == 0
        taxCalculatorResponse.vatToReturn == 23.45

        when:
        taxCalculatorResponse = calculateTax(invoice.getBuyer())

        then: "proportion applied - it applies when you are the buyer"
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 111.73
        taxCalculatorResponse.incomeMinusCosts == -111.73
        taxCalculatorResponse.collectedVat == 0
        taxCalculatorResponse.paidVat == 11.72
        taxCalculatorResponse.vatToReturn == -11.72
    }
}
