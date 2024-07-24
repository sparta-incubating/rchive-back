package kr.sparta.rchive.global.config;

import io.swagger.annotations.Example;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class SwaggerExampleHolder {

    private Example holder;
    private String name;
    private HttpStatus status;
}
