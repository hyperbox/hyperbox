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

package org.altherian.hboxc.front.gui.vm.edit;

import net.miginfocom.swing.MigLayout;
import org.altherian.hbox.comm.Command;
import org.altherian.hbox.comm.HypervisorTasks;
import org.altherian.hbox.comm.Request;
import org.altherian.hbox.comm.in.MachineIn;
import org.altherian.hbox.comm.out.hypervisor.MachineOut;
import org.altherian.hbox.constant.EntityType;
import org.altherian.hboxc.front.gui.Gui;
import org.altherian.hboxc.front.gui.MainView;
import org.altherian.hboxc.front.gui.builder.IconBuilder;
import org.altherian.hboxc.front.gui.worker.receiver._MachineReceiver;
import org.altherian.hboxc.front.gui.workers.MachineGetWorker;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VmEditDialog {
   
   private final String GENERAL = "General";
   private final String SYSTEM = "System";
   private final String CONSOLE = "Console";
   private final String DISPLAY = "Display";
   private final String STORAGE = "Storage";
   private final String AUDIO = "Audio";
   private final String NETWORK = "Network";
   
   private JDialog mainDialog;
   private JProgressBar refreshProgress;
   
   private JSplitPane split;
   
   private JPanel buttonsPanel;
   private JButton saveButton;
   private JButton cancelButton;
   
   private JScrollPane leftPane;
   private JScrollPane rightPane;
   
   private DefaultListModel listModel;
   private JList itemList;
   
   private JPanel sectionPanels;
   private CardLayout layout;
   
   private GeneralVmEdit generalEdit;
   private SystemVmEdit systemEdit;
   private ConsoleVmEdit consoleEdit;
   private DisplayVmEdit displayEdit;
   private StorageVmEdit storageEdit;
   private AudioVmEdit audioEdit;
   private NetworkVmEdit networkEdit;
   
   private MachineIn mIn;
   
   public VmEditDialog() {
      generalEdit = new GeneralVmEdit();
      systemEdit = new SystemVmEdit();
      consoleEdit = new ConsoleVmEdit();
      displayEdit = new DisplayVmEdit();
      storageEdit = new StorageVmEdit();
      audioEdit = new AudioVmEdit();
      networkEdit = new NetworkVmEdit();
      
      mainDialog = new JDialog(MainView.getMainFrame());
      mainDialog.setIconImage(IconBuilder.getTask(HypervisorTasks.MachineModify).getImage());
      mainDialog.setModalityType(ModalityType.DOCUMENT_MODAL);
      mainDialog.setSize(1000, 600);
      
      layout = new CardLayout();
      sectionPanels = new JPanel(layout);
      
      sectionPanels.add(generalEdit.getComp(), GENERAL);
      sectionPanels.add(systemEdit.getComp(), SYSTEM);
      sectionPanels.add(consoleEdit.getComp(), CONSOLE);
      sectionPanels.add(displayEdit.getComp(), DISPLAY);
      sectionPanels.add(storageEdit.getComp(), STORAGE);
      sectionPanels.add(audioEdit.getComp(), AUDIO);
      sectionPanels.add(networkEdit.getComp(), NETWORK);
      
      listModel = new DefaultListModel();
      listModel.addElement(GENERAL);
      listModel.addElement(SYSTEM);
      listModel.addElement(CONSOLE);
      listModel.addElement(DISPLAY);
      listModel.addElement(STORAGE);
      listModel.addElement(AUDIO);
      listModel.addElement(NETWORK);
      
      itemList = new JList(listModel);
      itemList.setCellRenderer(new LabelCellRenderer());
      itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      itemList.addListSelectionListener(new ListSelect());
      if (!listModel.isEmpty()) {
         itemList.setSelectedValue(listModel.getElementAt(0), true);
      }
      
      leftPane = new JScrollPane(itemList);
      leftPane.setMinimumSize(new Dimension(100, 50));
      rightPane = new JScrollPane(sectionPanels);
      rightPane.setMinimumSize(new Dimension(300, 100));
      
      split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
      split.setBorder(null);
      
      saveButton = new JButton(new SaveAction());
      cancelButton = new JButton(new CancelAction());
      refreshProgress = new JProgressBar();
      refreshProgress.setVisible(false);
      
      buttonsPanel = new JPanel(new MigLayout());
      buttonsPanel.add(saveButton);
      buttonsPanel.add(cancelButton);
      buttonsPanel.add(refreshProgress, "growx,pushx");
      
      mainDialog.getContentPane().setLayout(new MigLayout());
      mainDialog.getContentPane().add(split, "grow,push,wrap");
      mainDialog.getContentPane().add(buttonsPanel, "grow x");
      
   }
   
   private void show(MachineOut mOut) {
      mIn = new MachineIn(mOut);
      MachineGetWorker.execute(new MachineReceiver(), mOut);
      mainDialog.setTitle(mOut.getName() + " - Settings");
      mainDialog.setLocationRelativeTo(mainDialog.getParent());
      mainDialog.setVisible(true);
   }
   
   public static void edit(MachineOut mOut) {
      new VmEditDialog().show(mOut);
   }
   
   private void hide() {
      itemList.clearSelection();
      mainDialog.setVisible(false);
      mainDialog.dispose();
      mIn = null;
   }
   
   public void save() {
      generalEdit.save();
      systemEdit.save();
      consoleEdit.save();
      displayEdit.save();
      storageEdit.save();
      audioEdit.save();
      networkEdit.save();
      
      Gui.post(new Request(Command.VBOX, HypervisorTasks.MachineModify, mIn));
      
      hide();
   }
   
   public void cancel() {
      hide();
   }
   
   @SuppressWarnings("serial")
   private class SaveAction extends AbstractAction {
      
      public SaveAction() {
         super("Save");
         setEnabled(true);
      }
      
      @Override
      public void actionPerformed(ActionEvent arg0) {
         VmEditDialog.this.save();
      }
      
   }
   
   @SuppressWarnings("serial")
   private class CancelAction extends AbstractAction {
      
      public CancelAction() {
         super("Cancel");
         setEnabled(true);
      }
      
      @Override
      public void actionPerformed(ActionEvent e) {
         VmEditDialog.this.cancel();
      }
      
   }
   
   private class ListSelect implements ListSelectionListener {
      
      @Override
      public void valueChanged(ListSelectionEvent lsEv) {
         if (itemList.getSelectedValue() != null) {
            layout.show(sectionPanels, itemList.getSelectedValue().toString());
         }
      }
      
   }
   
   @SuppressWarnings("serial")
   private class LabelCellRenderer extends DefaultListCellRenderer {
      
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         
         if (label.getText() == GENERAL) {
            label.setIcon(IconBuilder.getEntityType(EntityType.Machine));
         } else if (label.getText() == SYSTEM) {
            label.setIcon(IconBuilder.getEntityType(EntityType.CPU));
         } else if (label.getText() == CONSOLE) {
            label.setIcon(IconBuilder.getEntityType(EntityType.Console));
         } else if (label.getText() == DISPLAY) {
            label.setIcon(IconBuilder.getEntityType(EntityType.Display));
         } else if (label.getText() == STORAGE) {
            label.setIcon(IconBuilder.getEntityType(EntityType.HardDisk));
         } else if (label.getText() == AUDIO) {
            label.setIcon(IconBuilder.getEntityType(EntityType.Audio));
         } else if (label.getText() == NETWORK) {
            label.setIcon(IconBuilder.getEntityType(EntityType.Network));
         } else {
            label.setIcon(null);
         }
         
         return label;
      }
      
   }
   
   private class MachineReceiver implements _MachineReceiver {
      
      @Override
      public void loadingStarted() {
         refreshProgress.setIndeterminate(true);
         refreshProgress.setVisible(true);
      }
      
      @Override
      public void loadingFinished(boolean isFinished, String message) {
         refreshProgress.setVisible(false);
         refreshProgress.setIndeterminate(false);
      }
      
      @Override
      public void put(final MachineOut mOut) {
         if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
               
               @Override
               public void run() {
                  put(mOut);
               }
            });
         } else {
            generalEdit.update(mOut, mIn);
            systemEdit.update(mOut, mIn);
            consoleEdit.update(mOut, mIn);
            displayEdit.update(mOut, mIn);
            storageEdit.update(mOut, mIn);
            audioEdit.update(mOut, mIn);
            networkEdit.update(mOut, mIn);
         }
         
      }
      
   }
   
}
