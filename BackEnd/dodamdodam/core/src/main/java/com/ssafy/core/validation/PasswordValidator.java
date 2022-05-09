package com.ssafy.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    final int USER_PWD_MAX = 20;
    final int USER_PWD_MIN = 8;

    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null) {
            return false;
        } else if (password.length() < USER_PWD_MIN || password.length() > USER_PWD_MAX) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("비밀번호는 최소 {0}자 이상, 최대 {1}자 이하여야 합니다.", USER_PWD_MIN, USER_PWD_MAX)
            ).addConstraintViolation();

            return false;
        }
        return true;
    }
}
