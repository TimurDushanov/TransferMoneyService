package ru.netology.transfermoneyservice.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferAndConfirmResponse {
    @NotBlank(message = "номер операции не может быть пуст")
    @Pattern(regexp = "^[0-9]*$")
    private String operationId;
}
