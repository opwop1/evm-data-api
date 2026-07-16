package link.vtcm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * @author zhangmj
 * @since 2025/7/1
 */
@Configuration
@MapperScan("${mybatis-plus.mapper-package}")
public class MybatisPlusConfig {
}
