package org.xman.jeefw.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.xman.jeefw.config.beetl.ClasspathBeetlConfig;
import org.xman.jeefw.config.beetl.WebInfoBeetlConfig;

import java.util.regex.Pattern;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan({
        "org.xman.jeefw.web",
        "org.xman.jeefw.config"
})
@PropertySource({
        "classpath:application.properties"
})
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 将 @Value 映射为具体类型的配置解析器。
     *
     * @return 配置解析器
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setNullValue("");
        configurer.setIgnoreUnresolvablePlaceholders(true);

        return configurer;
    }

    /**
     * 默认格式转换服务支持常用的转换与格式化方式,
     * 包括 JSR-354 Money & Currency, JSR-310 Date-Time and/or Joda-Time 等。
     *
     * @return 转换服务
     */
    @Bean
    public static ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        // 增加自定义的转换
        conversionService.addConverter(
                String.class,
                Pattern.class,
                (Converter<String, Pattern>) Pattern::compile
        );

        return conversionService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");

        return source;
    }
}
