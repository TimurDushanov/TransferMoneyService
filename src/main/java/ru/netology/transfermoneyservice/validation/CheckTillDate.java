package ru.netology.transfermoneyservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckTillDateValidator.class)
public @interface CheckTillDate {

//    public LocalDate value() default;
    public String message() default "Неправильный срок действия вашей карты";

    public Class<?>[] groups() default {};
    public Class<? extends Payload> [] payload() default {};
}
