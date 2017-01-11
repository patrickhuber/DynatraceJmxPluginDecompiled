 package com.dynatrace.diagnostics.plugin.jmx.helper;

 import java.io.ByteArrayOutputStream;
 import java.io.PrintStream;
 import java.io.UnsupportedEncodingException;
 import java.util.logging.Level;
 import java.util.logging.Logger;

 public class HelperUtils
   implements JMXMonitorConstants
 {
   private static final Logger log = Logger.getLogger(HelperUtils.class.getName());

   public static String getExceptionAsString(Exception e) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering getExceptionAsString method");
     }
     String msg;
     if ((msg = e.getMessage()) == null) {
       msg = "-";
     }
     return


       new StringBuilder(String.valueOf(e.getClass().getCanonicalName())).append(" exception occurred. Message = '").toString() + msg + "'; Stacktrace is '" + getStackTraceAsString(e) + "'";
   }

   public static String getStackTraceAsString(Exception e) {
     String returnString = "";
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering getStackTraceAsString method");
     }
     ByteArrayOutputStream ba = new ByteArrayOutputStream();
     try {
       e.printStackTrace(new PrintStream(ba, true, DEFAULT_ENCODING));
       returnString = ba.toString(DEFAULT_ENCODING);
     } catch (UnsupportedEncodingException e1) {
       log.finer("getStackTraceAsString method: UnsupportedEncodingException ; message is '" + e1.getMessage() + "'");
     }
     return returnString;
   }
 }


/* Location:              C:\Users\PRH7261\Downloads\com.dynatrace.diagnostics.plugin.jmx_1.0.9.jar!\com\dynatrace\diagnostics\plugin\jmx\helper\HelperUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */