package com.sign.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MM
 * @create 2019-05-17 16:42
 **/
@EnableSwagger2
@ComponentScan(basePackages = {"com.sign"})
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sign.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("对外开放接口API 文档")
                .description("HTTP对外开放接口")
                .termsOfServiceUrl("http://127.0.0.1")
                .contact("MM")
                .version("1.1")
                .build();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig fj = new FastJsonConfig();

        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                //    输出key是包含双引号
//                SerializerFeature.QuoteFieldNames,
                //    是否输出为null的字段,若为null 则显示该字段
//                SerializerFeature.WriteMapNullValue,
                //    数值字段如果为null，则输出为0
//                SerializerFeature.WriteNullNumberAsZero,
                //     List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                //    字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                //    Boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                //    Date的日期转换器
                SerializerFeature.WriteDateUseDateFormat,
                //    循环引用
                SerializerFeature.DisableCircularReferenceDetect,
        };

        fj.setSerializerFeatures(serializerFeatures);
        //3、中文乱码解决方案
        List<MediaType> fastMedisTypes = new ArrayList<>();
        //设定Json格式且编码为utf-8
        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastMedisTypes.add(MediaType.APPLICATION_JSON);
        //兼容mappingJackson...
        fastMedisTypes.add(new MediaType("application", "*+json"));
//        new MediaType()
        fjc.setSupportedMediaTypes(fastMedisTypes);
        for (int i = 0; i < converters.size(); i++) {
//            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
//                converters.set(i, fjc);
//            }
        }
//        fjc.setFastJsonConfig(fj);
//        converters.add(0, fjc);
    }
}
