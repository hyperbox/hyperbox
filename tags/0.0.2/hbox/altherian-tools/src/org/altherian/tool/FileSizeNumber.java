package org.altherian.tool;

public class FileSizeNumber {
   
   private long byteSize;
   private long humanSize;
   private String humanUnit;
   
   public FileSizeNumber(long byteSize) {
      this.byteSize = byteSize;
      evaluate();
   }
   
   public FileSizeNumber(String byteSize) {
      this(Long.parseLong(byteSize));
   }
   
   private void evaluate() {
      if (byteSize >= 1125899906842624l) {
         humanUnit = "PB";
         humanSize = byteSize / 1125899906842624l;
      }
      else if (byteSize >= 1099511627776l) {
         humanUnit = "TB";
         humanSize = byteSize / 1099511627776l;
      }
      else if (byteSize >= 1073741824l) {
         humanUnit = "GB";
         humanSize = byteSize / 1073741824l;
      }
      else if (byteSize >= 1048576l) {
         humanUnit = "MB";
         humanSize = byteSize / 1048576l;
      }
      else if (byteSize >= 1024) {
         humanUnit = "KB";
         humanSize = byteSize / 1024;
      }
      else {
         humanUnit = "B";
         humanSize = byteSize;
      }
   }
   
   public long getHumanSize() {
      return humanSize;
   }
   
   public String getHumanUnit() {
      return humanUnit;
   }

}
