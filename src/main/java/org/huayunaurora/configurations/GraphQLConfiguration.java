package org.huayunaurora.configurations;

import graphql.scalars.ExtendedScalars;
import org.huayunaurora.loginAndRegistration.exceptions.CustomGraphQLErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfiguration {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.UUID);
    }

    @Bean
    public CustomGraphQLErrorHandler customGraphQLErrorHandler() {
        return new CustomGraphQLErrorHandler();

    }

}
