 package com.dynatrace.diagnostics.plugin.jmx.helper;

 public enum JMX_SERVER_TYPE {
   CF_OPS_METRICS_TOOL("Cloud Foundry Ops Metrics Tool"),
   JVM("JVM"),
   WEBSPHERE("WebSphere"),
   WEBLOGIC("Weblogic"),
   JBOSS("JBoss"),
   CUSTOM_JMX_URL("Custom_Jmx_Url");

   private String jmxServerType;

   private JMX_SERVER_TYPE(String jmxServerType) {
     this.jmxServerType = jmxServerType;
   }


   public String jmxServerType() { return this.jmxServerType; }

   public static JMX_SERVER_TYPE valueOfIgnoreCase(String name) {
     JMX_SERVER_TYPE[] arrayOfJMX_SERVER_TYPE;
     int j = (arrayOfJMX_SERVER_TYPE = values()).length; for (int i = 0; i < j; i++) { JMX_SERVER_TYPE jmxST = arrayOfJMX_SERVER_TYPE[i];
       if (jmxST.jmxServerType.equalsIgnoreCase(name)) {
         return jmxST;
       }
     }
     throw new IllegalArgumentException("There is no value with name '" + name + "' in the enum " + JMX_SERVER_TYPE.class.getName());
   }


   public String toString()
   {
     return this.jmxServerType;
   }
 }