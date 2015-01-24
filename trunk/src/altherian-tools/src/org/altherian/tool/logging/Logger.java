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

package org.altherian.tool.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Logger {

   private static String logFileName;

   private static LogLevel maxLevel = LogLevel.Info;
   private static Boolean printSql;

   private static SimpleDateFormat formater;

   private static PrintStream output = System.out;

   static {
      printSql = false;
      formater = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss.SSS");
   }

   private Logger() {
      // static methods only
   }

   public static void setLevel(LogLevel level) {
      if (level != maxLevel) {
         maxLevel = level;
         debug("Changed LogLevel to " + maxLevel);
      }
   }

   public static LogLevel getLevel() {
      return maxLevel;
   }

   public static boolean isLevel(LogLevel level) {
      return (maxLevel == null) || (level.getLevel() <= maxLevel.getLevel());
   }

   public static void track() {
      put("", LogLevel.Tracking);
   }

   public static void verbose(Object o) {
      put(o, LogLevel.Verbose);
   }

   public static void sql(String query) {
      if (printSql) {
         put(query, LogLevel.Verbose);
      }
   }

   public static void debug(Object o) {
      put(o, LogLevel.Debug);
   }

   public static void warning(Object o) {
      put(o, LogLevel.Warning);
   }

   public static void info(Object o) {
      put(o, LogLevel.Info);
   }

   public static void exception(Throwable e) {
      put(e, LogLevel.Exception);
      e.printStackTrace(output);
   }

   public static void fatalException(Object o) {
      put(o, LogLevel.FatalException);
   }

   public static void error(Object o) {
      put(o, LogLevel.Error);
   }

   public static void raw(Object o) {
      put(o, LogLevel.Raw);
   }

   public static void log(String fileName) throws IOException {
      if (fileName != null) {
         System.out.println("Configure filename: " + fileName);
         logFileName = fileName;

         File file = new File(logFileName);
         if (!file.exists()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
               throw new IOException("Unable to create dirs to the log file: " + file.getParentFile().getAbsolutePath());
            }
            file.createNewFile();
         }

         try {
            file.setReadable(true, false);
         } catch (SecurityException e) {
            throw new IOException("Unable to set read permission to everyone on the log file");
         }

         try {
            file.getParentFile().setReadable(true, false);
         } catch (SecurityException e) {
            throw new IOException("Unable to set read permission to everyone on the log folder");
         }

         Calendar now = new GregorianCalendar();
         FileOutputStream fos = new FileOutputStream(file, true);
         output = new PrintStream(fos);
         output.println("+============================================================================+");
         output.println(String.format("+           Log start on %1$td/%1$tm/%1$tY @ %1$tH:%1$tM:%1$tS           +", now));
         output.println("+============================================================================+");
         output.flush();
      }

   }

   public static void log(String fileName, int history) throws IOException {
      if (history < 1) {
         throw new IllegalArgumentException("History count must be greater than 0");
      }

      for (int i = history; i > 0; i--) {
         File log = new File(fileName + "." + i);
         if (log.exists()) {
            if (i == history) {
               log.delete();
            } else {
               int j = i + 1;
               log.renameTo(new File(fileName + "." + j));
            }
         }
      }

      File log = new File(fileName);
      if (log.exists()) {
         log.renameTo(new File(fileName + ".1"));
      }

      log(fileName);
   }

   private static void put(Object o, LogLevel type) {
      if (isLevel(type)) {
         if (o == null) {
            o = new String("[ ! [NULL] ! ]");
         }

         String time = formater.format(new Date());
         String output = "";
         switch (type) {
            case Raw:
               output = o.toString();
               break;
            case FatalException:
               output = time + " |FatalExcept| " + Thread.currentThread().getName() + " | " + getCalling(false) + " | " + o;
               break;
            case Exception:
               output = time + " |E X C E P T I O N| " + Thread.currentThread().getName() + " | " + getCalling(false);
               break;
            case Error:
               output = time + " |ERROR ERROR| " + o;
               break;
            case Warning:
               output = time + " |  WARNING  | " + o;
               break;
            case Info:
               output = time + " |   Info    | " + o;
               break;
            case Verbose:
               output = time + " |  verbose  | " + o;
               break;
            case Debug:
               output = time + " |   debug   | " + Thread.currentThread().getName() + " | " + getCalling(false) + " | " + o;
               break;
            case Tracking:
               output = time + " | tracking  | " + Thread.currentThread().getName() + " | " + getCalling(true);
               break;
            default:
               output = time + " | UNKNWON | " + o;
         }

         put(output);
      }
   }

   private static void put(String s) {
      output.println(s);
   }

   private static String getCalling(boolean methodName) {
      int depth = 4;
      if (methodName) {
         StackTraceElement e[] = Thread.currentThread().getStackTrace();
         if ((e != null) && (e.length >= depth)) {
            StackTraceElement s = e[depth];
            if (s != null) {
               String finalValue = s.getClassName().substring((s.getClassName().lastIndexOf(".") + 1));
               finalValue = finalValue + "." + s.getMethodName() + "():" + s.getLineNumber();
               return finalValue;
            }
         }
         return null;
      } else {
         return sun.reflect.Reflection.getCallerClass(depth + 1).getSimpleName();
      }
   }

   public static void destroy() {
      if (logFileName != null) {
         Logger.output.close();
      }
   }

}
