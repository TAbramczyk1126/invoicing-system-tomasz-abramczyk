package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Car {

  @ApiModelProperty(value = "Car registration number", required = true, example = "WM 12345")
  private String registrationNumber;

  @ApiModelProperty(value = "Specifies if car is used for personal reason", required = true, example = "true")
  private boolean personalUse;
}
