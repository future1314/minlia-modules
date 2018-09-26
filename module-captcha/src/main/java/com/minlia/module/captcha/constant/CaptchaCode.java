package com.minlia.module.captcha.constant;

import com.minlia.cloud.code.Code;
import com.minlia.cloud.i18n.Lang;

/**
 * Created by garen on 2018/1/22.
 */
public class CaptchaCode {

    public enum Message implements Code{

        /**
         * 手机号码格式有误:请输入11位有效手机号码
         */
        CELLPHONE_WRONG_FORMAT(100300,"system.captcha.exception.100300"),

        /**
         * 验证码已失效，请重新发送验证码
         */
        CAPTCHA_EXPIRED(100301,"system.captcha.exception.100301"),

        /**
         * 验证码错误
         */
        CAPTCHA_ERROR(100302,"system.captcha.exception.100302"),

        /**
         * 验证码多次验证失败，请重新发送验证码
         */
        CAPTCHA_REPETITIOUS_ERROR(100303,"system.captcha.exception.100303"),

        /**
         * 验证码已使用，请重新发送
         */
        ALREADY_USED(100304,"system.captcha.exception.100304"),

        /**
         * 短信模板没找到, 请先配置
         */
        TEMPLATE_NOT_FOUND(100305,"system.captcha.exception.100305"),

        /**
         * 短信模板没找到, 请先配置
         */
        SEND_ONE_TIME(100306,"system.captcha.exception.100306"),

        /**
         * 一分钟只能发一次，请勿多次发送
         */
        ONCE_PER_MINUTE(100307,"system.captcha.exception.100307"),

        /**
         * 验证码发送失败
         */
        SEND_FAILURE(100308,"system.captcha.exception.100308"),

        /**
         * 验证码找不到
         */
        NOT_FOUND(100309,"system.captcha.exception.100309");

        private int code;
        private String i18nKey;

        Message(int code, String i18nKey) {
            this.code = code;
            this.i18nKey = i18nKey;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public String i18nKey() {
            return i18nKey;
        }

        @Override
        public String message(){
            return Lang.get(this.i18nKey);
        }

    }

}