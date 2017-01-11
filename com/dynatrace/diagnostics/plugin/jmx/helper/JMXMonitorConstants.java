 package com.dynatrace.diagnostics.plugin.jmx.helper;

 import com.dynatrace.diagnostics.pdk.Status;
 import com.dynatrace.diagnostics.pdk.Status.StatusCode;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.regex.Pattern;




 public abstract interface JMXMonitorConstants
 {
   public static final String CONFIG_IS_TASK = "isTask";
   public static final String CONFIG_PLUGIN_ID = "pluginId";
   public static final String CONFIG_IS_CUSTOM_JMX_URL = "isCustomJmxUrlInd";
   public static final String CONFIG_JMX_HOST = "jmxHost";
   public static final String CONFIG_JMX_PORT = "jmxPort";
   public static final String CONFIG_IS_AUTHENTICATION_ON = "isAuthenticationOn";
   public static final String CONFIG_USER = "user";
   public static final String CONFIG_PASSWORD = "password";
   public static final String CONFIG_URL_PATH = "urlPath";
   public static final String CONFIG_CUSTOM_JMX_URL = "customJmxUrl";
   public static final String CONFIG_JMX_SERVER_TYPE = "jmxServerType";
   public static final String CONFIG_INCLUDE_MBEAN_PATTERN = "includeMBeansPatterns";
   public static final String CONFIG_EXCLUDE_MBEAN_PATTERN = "excludeMBeansPatterns";
   public static final String CONFIG_DT_SERVER = "dtServer";
   public static final String CONFIG_DT_PORT = "dtPort";
   public static final String CONFIG_DT_PROTOCOL = "dtProtocol";
   public static final String CONFIG_IS_DT_AUTH = "isDtAuth";
   public static final String CONFIG_DT_USER = "dtUser";
   public static final String CONFIG_DT_PASSWORD = "dtPassword";
   public static final String CONFIG_DIR_JAR = "dirJar";
   public static final String CONFIG_JAR_VERSION = "version";
   public static final String JMX_SERVICE_URL_1 = "service:jmx:rmi://";
   public static final String JMX_SERVICE_URL_2 = "/jndi/rmi://";
   public static final String JMX_SERVICE_URL_3 = "/jmxrmi";
   public static final String JMX_REMOTE_CREDENTIALS = "jmx.remote.credentials";
   public static final String DEFAULT_ENCODING = System.getProperty("file.encoding", "UTF-8");
   public static final String NEW_LINE = System.getProperty("line.separator");
   public static final Status STATUS_SUCCESS = new Status(Status.StatusCode.Success);
   public static final String JAR_FILE_NAME = "com.dynatrace.diagnostics.plugin.jmx.metricgroup.";
   public static final String JAR_FILE_NAME_SUFFIX = ".jar";
   public static final String PLUGIN_XML_FILE_NAME = "plugin.xml";
   public static final String UNIT_SECOND = "s";
   public static final String UNIT_MILLISECOND = "ms";
   public static final String UNIT_NANOSECOND = "ns";
   public static final String UNIT_MINUTE = "min";
   public static final int MANIFEST_FILE_RECORD_LENGTH = 70;
   public static final int MAX_SIZE_ARCHIVE_DATA = 100;
   public static final String JAVA_VERSION = System.getProperty("java.version");
   public static final String JAVA_VENDOR = "(" + System.getProperty("java.vendor") + ")";

   public static final String JMX_SERVICE_URL_JBOSS = "service:jmx:remoting-jmx://";

   public static final String START_OF_PLUGIN_XML_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE +
     "<?eclipse version=\"3.2\"?>" + NEW_LINE +
     "<!-- plugin.xml file written by dynaTrace Client 6.2.0 -->" + NEW_LINE +
     "<plugin>" + NEW_LINE;
   public static final String MG_LINE_1_0 = "  <extension point=\"com.dynatrace.diagnostics.pdk.monitormetricgroup\" id=\"com.dynatrace.diagnostics.plugin.jmx.monitor.";
   public static final String MG_LINE_1_1 = "\" name=\"";
   public static final String MG_LINE_1_2 = "\">";
   public static final String MG_LINE_2 = "    <metricgroup monitorid=\"com.dynatrace.diagnostics.plugin.jmx.monitor\">";
   public static final String MG_LINE_3_0 = "    <metric name=\"";
   public static final String MG_LINE_3_1 = "\" unit=\"";
   public static final String MG_LINE_3_2 = "\" description=\"My metric description ...\" />";
   public static final String MG_LINE_4 = "    </metricgroup>";
   public static final String MG_LINE_5 = "    <information>";
   public static final String MG_LINE_6 = "      <description value=\"Describes metric groups and metrics which will be captured by the JMX Enhanced Monitor plugin\" />";
   public static final String MG_LINE_7 = "    </information>";
   public static final String MG_LINE_8 = "  </extension>";
   public static final String REST_OF_MG_LINES = "    </metricgroup>" + NEW_LINE + "    <information>" + NEW_LINE +
     "      <description value=\"Describes metric groups and metrics which will be captured by the JMX Enhanced Monitor plugin\" />" + NEW_LINE + "    </information>" + NEW_LINE +
     "  </extension>" + NEW_LINE;

   public static final String END_OF_PLUGIN_XML_FILE = "</plugin>";

   public static final String BUNDLE_SYMBOLIC_NAME = "com.dynatrace.diagnostics.plugin.jmx.metricgroup.";
   public static final String MANIFEST_MF_1 = "Manifest-Version: 1.0" + NEW_LINE +
     "Bundle-Vendor: Dynatrace" + NEW_LINE +
     "Bundle-ClassPath: ." + NEW_LINE +
     "Bundle-Version: ";
   public static final String MANIFEST_MF_2 = NEW_LINE + "Bundle-Name: CF Metric Group Plugin ";
   public static final String MANIFEST_MF_2_1 = "Created-By: " + JAVA_VERSION + " " + JAVA_VENDOR;
   public static final String MANIFEST_MF_3 = NEW_LINE + "Bundle-ManifestVersion: 2" + NEW_LINE +
     "Bundle-SymbolicName: ";
   public static final String MANIFEST_MF_4 = ";" + "singleton:=true" + NEW_LINE;

   public static final String JAR_COMMAND_0 = "jar -cvfm ";

   public static final Map<String, Pattern> UNIT_PATTERNS_MAP_CF = Collections.unmodifiableMap(
     new HashMap()
   {
     private static final long serialVersionUID = -3495315942841640533L;
   }
     );









   public static final Map<String, Pattern> UNIT_PATTERNS_MAP_JVM = Collections.unmodifiableMap(
     new HashMap()
   {
     private static final long serialVersionUID = 6128041299839588544L;
   }
     );
 }


/* Location:              C:\Users\PRH7261\Downloads\com.dynatrace.diagnostics.plugin.jmx_1.0.9.jar!\com\dynatrace\diagnostics\plugin\jmx\helper\JMXMonitorConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */