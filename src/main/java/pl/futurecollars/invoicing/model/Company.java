package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

  @ApiModelProperty(value = "Tax identification number", required = true, example = "123-123-22-22")
  private String taxIdentifications;
  @ApiModelProperty(value = "Company address", required = true, example = "ul. Cedrowa 16, 05-074 Hipolitów")
  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "Przykładowa nazwa Sp.z o.o.")
  private String name;

  public Company(String taxIdentifications, String address, String name) {
    this.taxIdentifications = taxIdentifications;
    this.address = address;
    this.name = name;
  }
}
