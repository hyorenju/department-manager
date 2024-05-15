package vn.edu.vnua.department.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.vnua.department.common.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ExecutorService executor(){
        return Executors.newFixedThreadPool(Constants.ThreadsNumConstant.MAX_THREADS);
    }
}
