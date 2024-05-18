package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

  @JsonIgnore
  @ApiModelProperty(value = "Company id (generated by application)", required = true, example = "1")
  private int id;

  @ApiModelProperty(value = "Tax identification number", required = true, example = "123-123-22-22")
  private String taxIdentificationNumber;

  @ApiModelProperty(value = "Company address", required = true, example = "ul. Cedrowa 16, 05-074 Hipolitów")
  private String address;

  @ApiModelProperty(value = "Company name", required = true, example = "Przykładowa nazwa Sp.z o.o.")
  private String name;

  @Builder.Default
  @ApiModelProperty(value = "Pension insurance amount", required = true, example = "150.25")
  private BigDecimal pensionInsurance = BigDecimal.ZERO;

  @Builder.Default
  @ApiModelProperty(value = "Health insurance amount", required = true, example = "299.99")
  private BigDecimal healthInsurance = BigDecimal.ZERO;

}
