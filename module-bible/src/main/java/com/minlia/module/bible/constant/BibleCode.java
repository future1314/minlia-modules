package com.minlia.module.bible.constant;


import com.minlia.cloud.code.Code;
import com.minlia.cloud.i18n.Lang;

/**
 * Created by will on 6/21/17.
 * API响应码
 */
public class BibleCode {

    public enum Exception implements Code {

        TEST(-1, "TEST");

        private int code;
        private String i18nKey;

        Exception(int code, String i18nKey) {
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
        public String message() {
            return Lang.get(this.i18nKey);
        }

    }

    public enum Message implements Code {

        /**
         * 存在字典子项无法删除
         */
        COULD_NOT_DELETE_HAS_CHILDREN(100600,"system.common.exception.100600"),

        /**
         * 字典父级不存在
         */
        PARENT_NOT_EXISTS(100601,"system.common.exception.100601");

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
        public String message() {
            return Lang.get(this.i18nKey);
        }

    }

}
