package org.altherian.hbox.comm.output.security;

import org.altherian.hbox.comm.io.BooleanSettingIO;
import org.altherian.hbox.comm.io.StringSettingIO;
import org.altherian.hbox.comm.output.ObjectOutput;
import org.altherian.hbox.constant.PermissionAttributes;

public class PermissionOutput extends ObjectOutput {
   
   private String userId;
   
   protected PermissionOutput() {
      // used for (de)serialisation
   }
   
   public PermissionOutput(String userId, String itemTypeId, String actionId, boolean isAllowed) {
      this.userId = userId;
      setSetting(new StringSettingIO(PermissionAttributes.ItemType, itemTypeId));
      setSetting(new StringSettingIO(PermissionAttributes.Action, actionId));
      setSetting(new BooleanSettingIO(PermissionAttributes.IsGranted, isAllowed));
   }
   
   public PermissionOutput(String userId, String itemTypeId, String actionId, String itemId, boolean isAllowed) {
      this(userId, itemTypeId, actionId, isAllowed);
      setSetting(new StringSettingIO(PermissionAttributes.Item, itemId));
   }
   
   /**
    * @return the userId
    */
   public String getUserId() {
      return userId;
   }
   
   /**
    * @return the itemTypeId
    */
   public String getItemTypeId() {
      return getSetting(PermissionAttributes.ItemType).getString();
   }
   
   /**
    * @return the actionId
    */
   public String getActionId() {
      return getSetting(PermissionAttributes.Action).getString();
   }
   
   /**
    * @return the itemId
    */
   public String getItemId() {
      return getSetting(PermissionAttributes.Item).getString();
   }
   
   /**
    * @return the isAllowed
    */
   public boolean isAllowed() {
      return getSetting(PermissionAttributes.IsGranted).getBoolean();
   }
   
}
