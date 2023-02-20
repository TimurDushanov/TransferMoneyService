package ru.netology.transfermoneyservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Deprecated
public class CheckTillDateValidator implements ConstraintValidator<CheckTillDate, String> {
    //    private LocalDate minTillDate;
    private String minTillDate;

    @Override
    public void initialize(CheckTillDate checkTillDate) {
//        minTillDate = LocalDate.parse(DateTimeFormatter.ofPattern("MMyy")
//                .format(LocalDateTime.now()));
        minTillDate = DateTimeFormatter.ofPattern("MMyy")
                .format(LocalDateTime.now());
    }

    @Override
    public boolean isValid(String enteredValue,
                           ConstraintValidatorContext constraintValidatorContext) {
        String enter = enteredValue.substring(0, 1);
        return Integer.parseInt(enteredValue) > Integer.parseInt(minTillDate)
                && Integer.parseInt(enter) > 0 && Integer.parseInt(enter) <= 12;
//        LocalDate localDate = LocalDate.parse(enteredValue, DateTimeFormatter.ofPattern("MMyy"));
//        return localDate.isAfter(minTillDate);
    }
}
