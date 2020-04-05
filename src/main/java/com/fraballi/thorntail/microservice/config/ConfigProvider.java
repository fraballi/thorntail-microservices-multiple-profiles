package com.fraballi.thorntail.microservice.config;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.util.Strings;
import org.yaml.snakeyaml.Yaml;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Startup
@Singleton
@Data
@Log4j2
public class ConfigProvider {

   private static final String PROJECT_DEFAULTS = "project-defaults.yml";

   private static final String THORNTAIL_PROJECT_STAGE_PROPERTY = "thorntail.project.stage";

   private static final String DEFAULT_CONFIGURATION = "configuration";

   @Getter(AccessLevel.PRIVATE)
   private final Map<String, Configuration> properties = new HashMap<>();

   private Configuration configuration;

   public ConfigProvider() {
   }

   public Configuration getConfiguration() {
      return properties.get(DEFAULT_CONFIGURATION);
   }

   /**
    * Loads properties for current configuration. Follows parent 'projects-defaults.yml', looking for a 'profile' then access any child profile
    * within the format: 'projects-<profile>.yml' to inject secondary properties. This case is for demo purposes where extra properties need to be
    * loaded besides ENV vars.
    */
   @PostConstruct
   public void setProperties() {
      final Yaml yaml = new Yaml();
      final ClassLoader classLoader = getClass().getClassLoader();
      configuration = yaml.loadAs(classLoader.getResourceAsStream(PROJECT_DEFAULTS), Configuration.class);

      if (nonNull(configuration) && nonNull(configuration.getApplication())) {
         final Configuration.Application application = configuration.getApplication();
         log.info("Application Name: {}", application.getName());

         final Configuration.Swarm swarm = configuration.getSwarm();
         if (nonNull(swarm) && nonNull(swarm.getHttp())) {
            log.info("Application Port: {}", swarm.getHttp().getPort());
         }

         String profile = null;
         final Properties systemProperties = System.getProperties();
         final String systemStage = String.valueOf(systemProperties.get(THORNTAIL_PROJECT_STAGE_PROPERTY));
         if (isNotEmpty(systemStage)) {
            profile = systemStage;
            if (nonNull(configuration.getProject())) {
               configuration.getProject().setStage(profile);
            } else {
               final Configuration.Project project = new Configuration.Project();
               project.setStage(profile);
               configuration.setProject(project);
            }
            log.info("Active Profile: {}", profile);
         }

         if (isNotEmpty(profile)) {
            configuration = yaml.loadAs(classLoader.getResourceAsStream(String.format("project-%s.yml", profile)), Configuration.class);

            final Configuration.Project project = configuration.getProject();
            if (Strings.isEmpty(project.getStage())) {
               project.setStage(profile);
            }
         }
      }
      properties.put(DEFAULT_CONFIGURATION, configuration);
   }

   @Data
   public static class Configuration {

      @NotNull
      private Project project;

      @NotNull
      private Swarm swarm;

      @NotNull
      private Application application;

      @Data
      public static class Project {

         @NotBlank
         private String stage;
      }

      @Data
      public static class Swarm {

         @NotNull
         private Http http;

         @Data
         public static class Http {

            private int port;
         }
      }

      @Data
      public static class Application {

         @NotBlank
         private String name;

         private String profile;

         private String message;
      }
   }
}
