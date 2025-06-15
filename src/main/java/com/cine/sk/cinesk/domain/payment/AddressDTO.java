package com.cine.sk.cinesk.domain.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String zipCode;
}
