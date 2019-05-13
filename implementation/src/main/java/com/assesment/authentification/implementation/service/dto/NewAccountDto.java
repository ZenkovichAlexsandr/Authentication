package com.assesment.authentification.implementation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountDto implements Dto {
    @NotBlank(message = "Username can not be blank")
    @NotEmpty(message = "Username can not be empty")
    private String username;
    @NotBlank(message = "Password can not be blank")
    @Length(min = 6, message = "Password should be more than 6 symbols")
    private String password;
    @Pattern(regexp = "[0-9]{8}", message = "Account Number should contains 8 numbers")
    private String accountNumber;
}
