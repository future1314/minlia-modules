package com.minlia.module.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation that check the cellphone
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { CellphoneValidator.class })
public @interface Cellphone {

    String message() default "请输入11位有效手机号码";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
