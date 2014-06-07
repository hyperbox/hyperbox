package org.altherian.hboxd.controller.action;

import org.altherian.hbox.comm.AnswerType;

import javax.swing.AbstractAction;

/**
 * Skeleton structure for a multi-task action.<br/>
 * This class will handle the start, finish & failed return type.</br> The following are used :
 * <ul>
 * <li>Start : AnswerType.STARTED</li>
 * <li>Finish : AnswerType.COMPLETED</li>
 * <li>Failed : AnswerType.FAILED</li>
 * </ul>
 * 
 * @see AnswerType
 * @see _Action
 * @see AbstractAction
 * @author noteirak
 */
public abstract class AbstractHyperboxMultiTaskAction extends AbstractHyperboxAction {
   
   @Override
   public AnswerType getStartReturn() {
      return AnswerType.STARTED;
   }
   
   @Override
   public AnswerType getFinishReturn() {
      return AnswerType.COMPLETED;
   }
   
   @Override
   public AnswerType getFailReturn() {
      return AnswerType.FAILED;
   }
   
}
