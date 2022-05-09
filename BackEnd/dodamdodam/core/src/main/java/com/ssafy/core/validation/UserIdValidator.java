package com.ssafy.core.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class UserIdValidator implements ConstraintValidator<UserId, String> {

    final int USER_ID_MAX = 20;
    final int USER_ID_MIN = 4;

    @Override
    public void initialize(UserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {

        if (userId == null) {
            return false;
        } else if (userId.length() < USER_ID_MIN || userId.length() > USER_ID_MAX) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("아이디는 최소 {0}자 이상, 최대 {1}자 이하여야 합니다.", USER_ID_MIN, USER_ID_MAX)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
