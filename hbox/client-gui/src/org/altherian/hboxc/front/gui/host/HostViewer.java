package org.altherian.hboxc.front.gui.host;

import net.miginfocom.swing.MigLayout;

import org.altherian.hbox.comm.output.host.HostOutput;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui._Refreshable;
import org.altherian.hboxc.front.gui.workers._HostReceiver;
import org.altherian.helper.swing.JTextFieldUtils;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class HostViewer implements _Refreshable, _HostReceiver {
   
   private String srvId;
   private HostOutput host;
   
   private JLabel hostnameLabel;
   private JTextField hostnameValue;
   private JLabel memPercLabel;
   private JProgressBar memPercValue;
   private JLabel memUsedLabel;
   private JTextField memUsedValue;
   private JLabel memFreeLabel;
   private JTextField memFreeValue;
   private JLabel memTotalLabel;
   private JTextField memTotalValue;
   
   
   private JPanel panel;
   
   public HostViewer() {
      hostnameLabel = new JLabel("Hostname");
      memPercLabel = new JLabel("Memory Usage");
      memUsedLabel = new JLabel("Memory Used");
      memFreeLabel = new JLabel("Memory Free");
      memTotalLabel = new JLabel("Memory Total");
      
      hostnameValue = JTextFieldUtils.createNonEditable();
      memPercValue = new JProgressBar(JProgressBar.HORIZONTAL, 0, 10000);
      memUsedValue = JTextFieldUtils.createNonEditable();
      memFreeValue = JTextFieldUtils.createNonEditable();
      memTotalValue = JTextFieldUtils.createNonEditable();
      
      panel = new JPanel(new MigLayout("ins 0"));
      panel.add(hostnameLabel);
      panel.add(hostnameValue, "growx,pushx,wrap");
      panel.add(memPercLabel);
      panel.add(memPercValue, "growx,pushx,wrap");
      panel.add(memUsedLabel);
      panel.add(memUsedValue, "growx,pushx,wrap");
      panel.add(memFreeLabel);
      panel.add(memFreeValue, "growx,pushx,wrap");
      panel.add(memTotalLabel);
      panel.add(memTotalValue, "growx,pushx,wrap");
      
   }
   
   public void show(String srvId) {
      clear();
      this.srvId = srvId;
      this.host = Gui.getReader().getServerReader(srvId).getHost();
      update();
   }
   
   @Override
   public void refresh() {
      show(srvId);
   }
   
   private void update() {
      Long memUsed = host.getMemorySize() - host.getMemoryAvailable();
      hostnameValue.setText(host.getHostname());
      memPercValue.setStringPainted(true);
      memPercValue.setValue((int) Math.ceil(((1 - (host.getMemoryAvailable().doubleValue() / host.getMemorySize().doubleValue())) * 10000)));
      
      memPercValue.setString(memUsed + "MB / " + host.getMemorySize() + " MB (" + memPercValue.getString() + ")");
      memUsedValue.setText(memUsed.toString() + " MB");
      memFreeValue.setText(host.getMemoryAvailable().toString() + " MB");
      memTotalValue.setText(host.getMemorySize().toString() + " MB");
   }
   
   private void clear() {
      hostnameValue.setText(null);
      memPercValue.setValue(0);
      memPercValue.setString(null);
      memPercValue.setStringPainted(false);
      memUsedValue.setText(null);
      memFreeValue.setText(null);
      memTotalValue.setText(null);
   }
   
   public JComponent getComponent() {
      return panel;
   }
   
   @Override
   public void loadingStarted() {
      clear();
   }
   
   @Override
   public void loadingFinished(boolean isSuccessful, String message) {
      if (isSuccessful) {
         update();
      } else {
         hostnameValue.setText("Error when loading host info: " + message);
      }
   }
   
   @Override
   public void put(HostOutput hostOut) {
      host = hostOut;
   }
   
}
