package com.pusky.onlineshopmockup.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {

   private static ResourceBundleMessageSource messageSource;

   Translator(ResourceBundleMessageSource messageSource) {
      Translator.messageSource = messageSource;
   }

   public static String translate(String msgCode) {
      return messageSource.getMessage(msgCode, null, LocaleContextHolder.getLocale());
   }
}