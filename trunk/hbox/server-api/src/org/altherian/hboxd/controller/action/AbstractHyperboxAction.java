package org.altherian.hboxd.controller.action;

import org.altherian.hbox.exception.HyperboxRuntimeException;

public abstract class AbstractHyperboxAction implements _HyperboxAction {
   
   @Override
   public Class<?>[] getRequiredClasses() {
      return new Class<?>[] {};
   }
   
   @Override
   public Enum<?>[] getRequiredEnums() {
      return new Enum<?>[] {};
   }
   
   @Override
   public String[] getRequiredData() {
      return new String[] {};
   }
   
   @Override
   public void pause() {
      throw new HyperboxRuntimeException("This operation is not supported");
   }
   
   @Override
   public boolean isCancelable() {
      return false;
   }
   
   @Override
   public void cancel() {
      throw new HyperboxRuntimeException("This operation is not supported");
   }
   
}
