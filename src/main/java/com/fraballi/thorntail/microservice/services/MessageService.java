package com.fraballi.thorntail.microservice.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.fraballi.thorntail.microservice.config.ConfigProvider;
import com.fraballi.thorntail.microservice.domain.Message;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Stateless
public class MessageService {

   @Inject
   private ConfigProvider configProvider;

   public MessageService() {
   }

   public Message getMessage() {
      final ConfigProvider.Configuration configuration = configProvider.getConfiguration();
      final ConfigProvider.Configuration.Application application = configuration.getApplication();
      final ConfigProvider.Configuration.Project project = configuration.getProject();
      return Message.builder().name(application.getName()).message(application.getMessage()).stage(project.getStage()).build();
   }
}
