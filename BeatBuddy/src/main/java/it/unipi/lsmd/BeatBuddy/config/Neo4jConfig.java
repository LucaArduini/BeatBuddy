package it.unipi.lsmd.BeatBuddy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "it.unipi.lsmd.BeatBuddy.repository.Neo4j")
public class Neo4jConfig {
    // Empty class body - configuration is defined by annotations
}