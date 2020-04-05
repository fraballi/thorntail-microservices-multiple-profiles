package com.fraballi.thorntail.microservice.controllers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fraballi.thorntail.microservice.domain.Message;
import com.fraballi.thorntail.microservice.services.MessageService;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Stateless
@Path("message")
public class MessageController {

   @Inject
   private MessageService service;

   public MessageController() {
   }

   @GET
   @Produces(value = MediaType.APPLICATION_JSON)
   public Message get() {
      return service.getMessage();
   }
}
