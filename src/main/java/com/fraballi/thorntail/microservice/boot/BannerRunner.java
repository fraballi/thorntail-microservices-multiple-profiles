package com.fraballi.thorntail.microservice.boot;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.fraballi.thorntail.microservice.config.ConfigProvider;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;

import lombok.extern.log4j.Log4j2;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Log4j2
@Startup
@Singleton
public class BannerRunner {
   @Inject
   private ConfigProvider configProvider;

   @PostConstruct
   public void init() {
      final ConfigProvider.Configuration configuration = configProvider.getConfiguration();
      try {
         showBanner(configuration.getProject().getStage());
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void showBanner(final String profile) throws IOException {
      Preconditions.checkNotNull(profile);
      if ("prod".equals(profile)) {
         banner("static/banner-prod.txt");
      } else {
         banner("static/banner-dev.txt");
      }
   }

   private static void banner(final String resource) throws IOException {
      final ClassLoader classLoader = ConfigProvider.class.getClassLoader();
      try (InputStream is = classLoader.getResourceAsStream(resource)) {
         ByteSource byteSource = new ByteSource() {

            @Override
            public InputStream openStream() {
               return is;
            }
         };
         log.info("ENVIRONMENT \n\n \t{}", byteSource.asCharSource(StandardCharsets.UTF_8).read());
      }
   }
}
