package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.service.TaxCalculatorResult;
import pl.futurecollars.invoicing.model.Company;

@RequestMapping(value = "tax", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"tax-controller"})
public interface TaxCalculatorApi {

  @ApiOperation(value = "Get company incomes, costs, vat nad taxes to pay")
  @PostMapping
  TaxCalculatorResult calculateTaxes(@RequestBody Company company);
}
