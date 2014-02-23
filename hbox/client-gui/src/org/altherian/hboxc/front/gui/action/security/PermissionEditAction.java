package org.altherian.hboxc.front.gui.action.security;

import org.altherian.hboxc.front.gui.security.user._UserSelector;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class PermissionEditAction extends AbstractAction {
   
   private _UserSelector usrSelect;
   
   public PermissionEditAction(_UserSelector usrSelect) {
      this.usrSelect = usrSelect;
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      usrSelect.getServerId();
   }
   
}
