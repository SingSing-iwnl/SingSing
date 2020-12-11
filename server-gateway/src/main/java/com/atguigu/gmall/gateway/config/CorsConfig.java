package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    //制作跨域
    @Bean
    public CorsWebFilter corsWebFilter(){
        //创建对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //设置跨域的相关参数
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        //返回当前对象
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        //配置那些需要过滤的路径
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //CorsConfigurationSource 接口
        return  new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
