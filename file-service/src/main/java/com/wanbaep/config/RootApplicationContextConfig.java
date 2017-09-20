package com.wanbaep.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "com.wanbaep"
})
@Import(DbConfig.class)
public class RootApplicationContextConfig {
}
