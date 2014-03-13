package org.altherian.hboxd.vbox4_2.ws.setting.storage;

import org.altherian.hbox.constant.MediumAttribute;
import org.altherian.hbox.exception.HyperboxRuntimeException;
import org.altherian.hboxd.settings.StringSetting;
import org.altherian.hboxd.settings._Setting;
import org.altherian.hboxd.vbox4_2.ws.setting._MediumSettingAction;

import org.virtualbox_4_2.IMedium;

public class MediumParentUuidSettingAction implements _MediumSettingAction {
   
   @Override
   public String getSettingName() {
      return MediumAttribute.ParentUUID.toString();
   }
   
   @Override
   public void set(IMedium medium, _Setting setting) {
      throw new HyperboxRuntimeException("Read-only setting");
   }
   
   @Override
   public _Setting get(IMedium medium) {
      if (medium.getParent() != null) {
         return new StringSetting(MediumAttribute.ParentUUID, medium.getParent().getId());
      } else {
         return new StringSetting(MediumAttribute.ParentUUID, "");
      }
   }
   
}
