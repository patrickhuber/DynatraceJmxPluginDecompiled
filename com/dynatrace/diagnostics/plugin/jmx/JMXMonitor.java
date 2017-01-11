 package com.dynatrace.diagnostics.plugin.jmx;
 
 import com.dynatrace.diagnostics.pdk.Monitor;
 import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
 import com.dynatrace.diagnostics.pdk.Status;
 import com.dynatrace.diagnostics.pdk.Status.StatusCode;
 import com.dynatrace.diagnostics.plugin.jmx.helper.HelperUtils;
 import java.util.logging.Logger;

 public class JMXMonitor
   extends JMXExecutor
   implements Monitor
 {
   public static final Logger log = Logger.getLogger(JMXMonitor.class.getName());

   public Status setup(MonitorEnvironment env)
     throws Exception
   {
     try
     {
       status = super.setup(env);
     } catch (Exception e) { Status status;
       String msg = "JMXMonitor class: setup method: exception was thrown '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       return new Status(Status.StatusCode.ErrorInfrastructure, msg, msg, e); }
     Status status;
     return status;
   }
 
   public Status execute(MonitorEnvironment env)
     throws Exception
   {
     try
     {
       status = super.execute(env);
     } catch (Exception e) { Status status;
       String msg = "JMXMonitor class: execute method: exception was thrown '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       return new Status(Status.StatusCode.ErrorInfrastructure, msg, msg, e); }
     Status status;
     return status;
   }

 
   public void teardown(MonitorEnvironment env)
     throws Exception
   {
     super.teardown(env);
   }
 }
