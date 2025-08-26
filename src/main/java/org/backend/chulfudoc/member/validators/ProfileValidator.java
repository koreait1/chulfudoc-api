package org.backend.chulfudoc.member.validators;

import org.backend.chulfudoc.member.controllers.RequestProfile;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class ProfileValidator implements Validator {
    @Override
    public void validate(Object target, Errors errors) {
        RequestProfile form = (RequestProfile) target;
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        if (StringUtils.hasText(password))
            ValidationUtils.rejectIfEmptyOrWhitespace(errors,"confirmPassword", "NotBlank");

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }
}
