package org.altherian.hboxc.front.gui;

public enum Config {
   
   MainFramePosY("gui.mainframe.pos.y"),
   MainFramePosX("gui.mainframe.pos.x"),
   MainFrameWidth("gui.mainframe.width"),
   MainFrameHeight("gui.mainframe.height"),
   MainFrameState("gui.mainframe.state");
   
   private String id;
   
   private Config(String id) {
      this.id = id;
   }
   
   public String getId() {
      return id;
   }
   
}
