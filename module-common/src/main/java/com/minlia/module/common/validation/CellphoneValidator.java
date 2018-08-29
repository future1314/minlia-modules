package com.minlia.module.common.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Annotation that check the cellphone
 * Created by garen on 2017/6/30.
 */
public class CellphoneValidator implements ConstraintValidator<Cellphone, String> {

    @Override
    public void initialize(Cellphone cellphone) {

    }

    @Override
    public boolean isValid(String cellphone, ConstraintValidatorContext ctx) {
        if (StringUtils.isNotEmpty(cellphone)) {
            //如果因为现有的号码不能满足市场需求，电信服务商会增大号码范围。所以一般情况下我们只要验证手机号码为11位，且以1开头。
            return Pattern.matches("^1[0-9]{10}$",cellphone);
        } else {
            return true;
        }
    }

}
