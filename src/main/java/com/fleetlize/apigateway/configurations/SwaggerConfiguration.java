package com.fleetlize.apigateway.configurations;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration implements SwaggerResourcesProvider {

  private final RouteLocator routeLocator;

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(PathSelectors.any()).build()
        .pathMapping("/");
  }

  @Override
  public List<SwaggerResource> get() {

    return routeLocator.getRoutes()
        .stream()
        .map(route -> swaggerResource(route.getId(),
            route.getFullPath().replace("**", "v2/api-docs")))
        .collect(Collectors.toList());

  }

  private SwaggerResource swaggerResource(final String name,
      final String location) {
    final SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(name);
    swaggerResource.setLocation(location);
    return swaggerResource;
  }
}
