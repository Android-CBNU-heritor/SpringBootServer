package com.example.foodmart;

import com.example.foodmart.File.FIleProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FIleProperties.class
})
public class FoodmartApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodmartApplication.class, args);
    }

}
