package com.fraballi.thorntail.microservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

   private String name;

   private String stage;

   private String message;
}
