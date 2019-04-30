package com.rsun.config;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebMvc
@ComponentScan(value = "com.rsun.web", includeFilters = @ComponentScan.Filter(Controller.class))
@PropertySource(value = {"classpath:config.properties"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MvcWebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private static final String UTF8 = "UTF-8";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        // @ResponseBody注解靠HttpMessageConverter解析
        List<HttpMessageConverter<?>> converters = adapter.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {  // 移除默认编码为ISO8859-1的字符串解析器
                iterator.remove();
            }
        }
        converters.add(new StringHttpMessageConverter(Charset.forName(UTF8)));  // 字符串才使用UTF-8解析
        converters.add(new MappingJackson2HttpMessageConverter());  // 解析json
        adapter.setMessageConverters(converters);
        return adapter;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    //配置静态资源的处理
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        //对静态资源的请求转发到容器缺省的servlet，而不使用DispatcherServlet
        configurer.enable();
    }

    //因为配置了拦截器，所以springMVC的dispatcherServlet就会启动，原来静态资源加载就会出问题了
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //处理静态资源访问
        registry.addResourceHandler("/static").addResourceLocations("/static/");   //根目录下的任何静态资源被请求时，都到/static/目录下去找静态资源
        super.addResourceHandlers(registry);    //registry要加载父类的registry里面
    }

    /**
     * STEP 1 - 创建模版解析器
     */
    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/view/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(UTF8);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }


    /**
     * STEP 2 - 创建模版引擎
     * 并为模板引擎注入模板解析器
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        Set<IDialect> dialects = new HashSet<>();
        dialects.add(new LayoutDialect());
        templateEngine.setAdditionalDialects(dialects);
        return templateEngine;
    }

    /**
     * STEP 3 - 注册 Thymeleaf 视图解析器
     * 并为解析器注入模板引擎
     */
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(UTF8);
        viewResolver.setContentType("text/html; charset=utf-8");
        return viewResolver;
    }


//    Freemarker 相关
//    @Bean
//    public FreeMarkerConfigurer freemarkerConfig() {
//        FreeMarkerConfigurer config = new FreeMarkerConfigurer();
//        config.setTemplateLoaderPath("/WEB-INF/ftl/");
//        config.setDefaultEncoding("utf-8");
//        Properties properties = new Properties();
//        properties.setProperty("locale", "zh_CN");
//        properties.setProperty("datetime_format", "yyyy-MM-dd HH:mm:ss");
//        properties.setProperty("date_format", "yyyy-MM-dd");
//        properties.setProperty("number_format", "#.##");
//        properties.setProperty("template_exception_handler", "rethrow");
//        properties.setProperty("object_wrapper", "beans");
//        config.setFreemarkerSettings(properties);
//        return config;
//    }

//    //配置视图解析器
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
////        registry.jsp("/WEB-INF/view/", ".jsp");
//        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
//        viewResolver.setCache(false);
////        viewResolver.setPrefix("/WEB-INF/ftl/");
//        viewResolver.setSuffix(".ftl");
//        viewResolver.setContentType("text/html;charset=UTF-8");
//        viewResolver.setRequestContextAttribute("request");
//        viewResolver.setExposeRequestAttributes(true);
//        viewResolver.setExposeSessionAttributes(true);
//        viewResolver.setExposeSpringMacroHelpers(true);
//        registry.viewResolver(viewResolver);
//    }

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(applicationContext.getBean("templateEngine", SpringTemplateEngine.class));
//        viewResolver.setCharacterEncoding("UTF-8");
//        registry.viewResolver(viewResolver);
//    }
}
