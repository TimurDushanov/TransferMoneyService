package ru.netology.transfermoneyservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Amount {
    @NotBlank(message = "указана недопустимая сумма перевода")
    @Min(1)
    private float value;
    @NotBlank(message = "валюта должна быть указана")
    private String currency;

}
