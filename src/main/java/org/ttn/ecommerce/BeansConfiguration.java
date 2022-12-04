//package org.ttn.ecommerce;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
//
//import java.util.Locale;
//
//@Configuration
//public class BeansConfiguration {
//
//     AcceptHeaderLocaleResolver resolver;
//     ResourceBundleMessageSource messageSource;
//
//     @Bean
//     public AcceptHeaderLocaleResolver localeResolver() {
//         resolver = new AcceptHeaderLocaleResolver();
//         resolver.setDefaultLocale(Locale.US);
//         return resolver;
//     }
//
//     @Bean
//     public ResourceBundleMessageSource messageSource() {
//         messageSource = new ResourceBundleMessageSource();
//         messageSource.setBasename("welcome");
//         return messageSource;
//     }
//  }