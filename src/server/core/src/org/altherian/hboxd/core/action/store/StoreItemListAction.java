/*
 * Hyperbox - Enterprise Virtualization Manager
 * Copyright (C) 2013 Maxime Dor
 * 
 * http://hyperbox.altherian.org
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.altherian.hboxd.core.action.store;

import org.altherian.hbox.comm.Answer;
import org.altherian.hbox.comm.AnswerType;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HyperboxTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.StoreIn;
import org.altherian.hbox.comm.in.StoreItemIn;
import org.altherian.hbox.comm.out.StoreItemOut;
import org.altherian.hboxd.comm.io.factory.StoreItemIoFactory;
import org.altherian.hboxd.core._Hyperbox;
import org.altherian.hboxd.core.action.AbstractHyperboxMultiTaskAction;
import org.altherian.hboxd.session.SessionContext;
import org.altherian.hboxd.store._StoreItem;
import java.util.Arrays;
import java.util.List;

public final class StoreItemListAction extends AbstractHyperboxMultiTaskAction {

   @Override
   public List<String> getRegistrations() {
      return Arrays.asList(Command.HBOX.getId() + HyperboxTasks.StoreItemList.getId());
   }

   @Override
   public boolean isQueueable() {
      return false;
   }

   @Override
   public void run(Request request, _Hyperbox hbox) {
      StoreIn sIn = request.get(StoreIn.class);
      StoreItemIn siIn;
      if (request.has(StoreItemIn.class)) {
         siIn = request.get(StoreItemIn.class);
      } else {
         siIn = new StoreItemIn();
      }
      List<_StoreItem> siList = hbox.getStoreManager().getStore(sIn.getId()).getItem(siIn.getPath()).listItems();
      List<StoreItemOut> siOutList = StoreItemIoFactory.get(siList);
      for (StoreItemOut siOut : siOutList) {
         SessionContext.getClient().putAnswer(new Answer(request, AnswerType.DATA, siOut));
      }
   }

}
