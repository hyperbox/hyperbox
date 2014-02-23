package org.altherian.hboxc.front.gui.security.perm;

import org.altherian.hbox.comm.input.PermissionInput;
import org.altherian.hboxc.front.gui.utils.AbstractListTableModel;

@SuppressWarnings("serial")
public class PermissionTableModel extends AbstractListTableModel<PermissionInput> {
   
   private final String itemTypeCol = "Item Type";
   private final String itemCol = "Item";
   private final String actionCol = "Action";
   private final String accessCol = "Access";
   
   @Override
   protected void addColumns() {
      addColumn(itemTypeCol);
      addColumn(itemCol);
      addColumn(actionCol);
      addColumn(accessCol);
   }
   
   @Override
   protected Object getValueAt(PermissionInput obj, String columnName) {
      if (columnName.contentEquals(itemTypeCol)) {
         return obj.getItemTypeId();
      } else if (columnName.contentEquals(itemCol)) {
         return obj.getItemId();
      } else if (columnName.contentEquals(actionCol)) {
         return obj.getActionId();
      } else if (columnName.contentEquals(accessCol)) {
         return obj.isAllowed() ? "Grant" : "Deny";
      } else {
         return "";
      }
   }
   
}
