/*
 * Hyperbox0 - Enterprise Virtualization Manager
 * Copyright (C) 2014 Maxime Dor
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

package org.altherian.hboxd.factory;

import org.altherian.hboxd.exception.module.ModuleInvalidDescriptorFileException;
import org.altherian.hboxd.module.Module;
import org.altherian.hboxd.module._Module;
import org.altherian.tool.AxStrings;
import org.altherian.tool.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModuleFactory {
   
   private ModuleFactory() {
      throw new RuntimeException("Not allowed");
   }
   
   public static _Module get(File descriptorFile) {
      // TODO put the hardcoded values into config variables
      try {
         Logger.debug("Start module creation: " + descriptorFile.getAbsolutePath());
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder;
         dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(descriptorFile);
         doc.getDocumentElement().normalize();
         
         XPath xPath = XPathFactory.newInstance().newXPath();
         if (!"module".equals(doc.getDocumentElement().getNodeName())) {
            throw new ModuleInvalidDescriptorFileException("Invalid module descriptor file, invalid root tag: " + doc.getDocumentElement().getNodeName());
         } else {
            String id = (String) xPath.evaluate("/module/id", doc, XPathConstants.STRING);
            Logger.debug("id: " + id);
            String path = (String) xPath.evaluate("/module/path", doc, XPathConstants.STRING);
            Logger.debug("path: " + path);
            String name = (String) xPath.evaluate("/module/name", doc, XPathConstants.STRING);
            Logger.debug("name: " + name);
            String desc = (String) xPath.evaluate("/module/desc", doc, XPathConstants.STRING);
            Logger.debug("desc: " + desc);
            String version = (String) xPath.evaluate("/module/version", doc, XPathConstants.STRING);
            Logger.debug("version: " + version);
            String vendor = (String) xPath.evaluate("/module/vendor", doc, XPathConstants.STRING);
            Logger.debug("vendor: " + vendor);
            String url = (String) xPath.evaluate("/module/url", doc, XPathConstants.STRING);
            Logger.debug("url: " + url);
            NodeList providerNodeList = (NodeList) xPath.evaluate("/module/providers/provider", doc, XPathConstants.NODESET);
            Map<String,String> providers = new HashMap<String,String>();
            Logger.debug("providers: " + providerNodeList.getLength());
            for (int i = 0; i < providerNodeList.getLength(); i++) {
               String type = (String) xPath.evaluate("type", providerNodeList.item(i), XPathConstants.STRING);
               Logger.debug("type #" + i + ": " + type);
               String imp = (String) xPath.evaluate("impl", providerNodeList.item(i), XPathConstants.STRING);
               Logger.debug("imp #" + i + ": " + imp);
               if (!AxStrings.isEmpty(type) && !AxStrings.isEmpty(imp)) {
                  Logger.debug("Added to providers map.");
                  providers.put(type, imp);
               }
            }
            Logger.debug("providers done.");
            
            File modPath = new File(AxStrings.isEmpty(path) ? id : path);
            Logger.debug("mod raw path: " + path);
            Logger.debug("mod path: " + modPath.getAbsolutePath());
            if (!modPath.isAbsolute()) {
               modPath = new File(descriptorFile.getAbsoluteFile().getParent() + File.separator + path).getAbsoluteFile();
            }
            Logger.debug("mod abs path: " + modPath.getPath());
            Logger.debug("mod path read: " + modPath.canRead());
            if (!modPath.canRead()) {
               throw new ModuleInvalidDescriptorFileException("Module base path is not readable: "+modPath.getAbsolutePath());
            }
            
            Logger.debug("Found a valid module for " + descriptorFile.getAbsolutePath());
            Logger.debug("End Module creation.");
            return new Module(id, descriptorFile, modPath, name, version, vendor, desc, url, providers);
         }
      } catch (ParserConfigurationException e) {
         throw new ModuleInvalidDescriptorFileException("Error when reading/parsing " + descriptorFile.getAbsolutePath() + ": " + e.getMessage());
      } catch (SAXException e) {
         throw new ModuleInvalidDescriptorFileException("Error when reading/parsing " + descriptorFile.getAbsolutePath() + ": " + e.getMessage());
      } catch (IOException e) {
         throw new ModuleInvalidDescriptorFileException("Error when reading/parsing " + descriptorFile.getAbsolutePath() + ": " + e.getMessage());
      } catch (XPathExpressionException e) {
         throw new ModuleInvalidDescriptorFileException("Error when reading/parsing " + descriptorFile.getAbsolutePath() + ": " + e.getMessage());
      }
   }
   
}
