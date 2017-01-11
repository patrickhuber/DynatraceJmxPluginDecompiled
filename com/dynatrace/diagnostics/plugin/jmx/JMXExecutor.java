 package com.dynatrace.diagnostics.plugin.jmx;
 
 import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
 import com.dynatrace.diagnostics.pdk.MonitorMeasure;
 import com.dynatrace.diagnostics.pdk.PluginEnvironment;
 import com.dynatrace.diagnostics.pdk.PluginEnvironment.Host;
 import com.dynatrace.diagnostics.pdk.Status;
 import com.dynatrace.diagnostics.pdk.Status.StatusCode;
 import com.dynatrace.diagnostics.pdk.TaskEnvironment;
 import com.dynatrace.diagnostics.plugin.jmx.helper.AttrEntityHelper;
 import com.dynatrace.diagnostics.plugin.jmx.helper.HelperUtils;
 import com.dynatrace.diagnostics.plugin.jmx.helper.JMXMonitorConstants;
 import com.dynatrace.diagnostics.plugin.jmx.helper.JMXMonitorProperties;
 import com.dynatrace.diagnostics.plugin.jmx.helper.JMX_SERVER_TYPE;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.text.NumberFormat;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import javax.management.Attribute;
 import javax.management.AttributeList;
 import javax.management.MBeanAttributeInfo;
 import javax.management.MBeanInfo;
 import javax.management.MBeanServerConnection;
 import javax.management.ObjectName;
 import javax.management.openmbean.CompositeDataSupport;
 import javax.management.openmbean.CompositeType;
 import javax.management.remote.JMXConnector;
 import javax.management.remote.JMXConnectorFactory;
 import javax.management.remote.JMXServiceURL;
 import org.apache.commons.compress.archivers.ArchiveOutputStream;
 import org.apache.commons.compress.archivers.ArchiveStreamFactory;
 import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
 import org.apache.commons.io.FileUtils;
 import org.apache.commons.io.IOUtils;
 import org.apache.commons.io.filefilter.FileFilterUtils;
 import org.apache.commons.lang3.StringUtils;
 
 
 public class JMXExecutor
   implements JMXMonitorConstants
 {
   JMXMonitorProperties pp;
   public static Logger log = null;
   
   public Status setup(PluginEnvironment env) throws Exception {
     if ((env instanceof TaskEnvironment)) {
       log = JMXTask.log;
     } else {
       log = JMXMonitor.log;
     }
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering setup method");
     }
     try {
       this.pp = setConfiguration(env);
       
       if (log.isLoggable(Level.FINER)) {
         log.finer("setup method: the configuration property file is " + this.pp.toString());
       }
     } catch (Exception e) {
       String msg = "setConfiguration method: '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       return new Status(Status.StatusCode.ErrorInfrastructure, msg, msg, e);
     }
     
     return STATUS_SUCCESS;
   }
   
   public Status execute(PluginEnvironment env) throws Exception {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering execute method");
     }
     
     try
     {
       MBeanServerConnection con;
       if ((con = this.pp.getJmxConnection()) == null) { JMXConnector jmxc;
         if ((jmxc = this.pp.getJmxConnector()) != null) {
           try {
             jmxc.close();
           } catch (Exception localException1) {}
         }
         this.pp.setJmxConnector(jmxc = JMXConnectorFactory.connect(this.pp.getJmxServiceUrl(), this.pp.getCredentialsMap()));
         con = jmxc.getMBeanServerConnection();
       }
     } catch (Exception e) {
       String msg = "execute method: exception was thrown '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       this.pp.setJmxConnection(null);
       JMXConnector jmxc; if ((jmxc = this.pp.getJmxConnector()) != null) {
         try {
           jmxc.close();
         } catch (Exception localException2) {}
         this.pp.setJmxConnector(null);
       }
       
       return new Status(Status.StatusCode.ErrorInfrastructure, msg, msg, e);
     }
     MBeanServerConnection con;
     if ((env instanceof TaskEnvironment)) {
       processJMXTask(env, this.pp);
       return STATUS_SUCCESS;
     }
     try
     {
       processMBeans(env, con, this.pp);
     } catch (Exception e) {
       String msg = "execute method: exception was thrown '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       this.pp.setJmxConnection(null);
       JMXConnector jmxc; if ((jmxc = this.pp.getJmxConnector()) != null) {
         try {
           jmxc.close();
         } catch (Exception localException3) {}
         this.pp.setJmxConnector(null);
       }
       
       return new Status(Status.StatusCode.ErrorInfrastructure, msg, msg, e);
     }
     
     return STATUS_SUCCESS;
   }
   
   public void teardown(PluginEnvironment env) throws Exception {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering teardown method");
     }
     
     JMXConnector jmxc;
     if ((jmxc = this.pp.getJmxConnector()) != null) {
       try {
         jmxc.close();
       } catch (Exception localException) {}
       this.pp.setJmxConnector(null);
     }
   }
   
   public void processJMXTask(PluginEnvironment env, JMXMonitorProperties props) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering processJMXTask method");
     }
     try
     {
       MBeanServerConnection con;
       if ((con = this.pp.getJmxConnection()) == null) { JMXConnector jmxc;
         if ((jmxc = this.pp.getJmxConnector()) != null) {
           try {
             jmxc.close();
           } catch (Exception localException1) {}
         }
         props.setJmxConnector(jmxc = JMXConnectorFactory.connect(this.pp.getJmxServiceUrl(), this.pp.getCredentialsMap()));
         con = jmxc.getMBeanServerConnection();
       }
       processMBeans(env, con, this.pp);
     } catch (Exception e) {
       String msg = "processJMXTask method: exception was thrown '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       throw new RuntimeException(msg, e);
     }
     MBeanServerConnection con;
   }
   
   public void processMBeans(PluginEnvironment env, MBeanServerConnection con, JMXMonitorProperties props) throws Exception { if (log.isLoggable(Level.FINER)) {
       log.finer("Entering processMBeans method");
     }
     
     List<Set<ObjectName>> list = new ArrayList();
     if (!props.getIncludeMBeansPatterns().isEmpty()) {
       for (String line : props.getIncludeMBeansPatterns()) {
         if (log.isLoggable(Level.FINER)) {
           log.finer("processMBeans: IncludeMBeansPatterns: line is '" + line + "'");
         }
         if (!line.trim().isEmpty())
         {
 
           list.add(con.queryNames(new ObjectName(line), null)); }
       }
     } else {
       list.add(con.queryNames(null, null));
     }
     
     List<Set<ObjectName>> elist = new ArrayList();
     for (String line : props.getExcludeMBeansPatterns()) {
       if (log.isLoggable(Level.FINER)) {
         log.finer("processMBeans: ExcludeMBeansPatterns: line is '" + line + "'");
       }
       if (!line.trim().isEmpty())
       {
 
         elist.add(con.queryNames(new ObjectName(line), null)); }
     }
     StringBuilder sb = null;
     if (props.isTask())
       sb = new StringBuilder(START_OF_PLUGIN_XML_FILE);
     Iterator localIterator4;
     for (Iterator localIterator3 = list.iterator(); localIterator3.hasNext(); 
         
         localIterator4.hasNext())
     {
       Object set = (Set)localIterator3.next();
       
       localIterator4 = ((Set)set).iterator(); continue;ObjectName name = (ObjectName)localIterator4.next();
       Iterator localIterator6; for (Iterator localIterator5 = elist.iterator(); localIterator5.hasNext(); 
           localIterator6.hasNext())
       {
         Set<ObjectName> eset = (Set)localIterator5.next();
         localIterator6 = eset.iterator(); continue;ObjectName ename = (ObjectName)localIterator6.next();
         if (ename.apply(name))
         {
           if (!log.isLoggable(Level.FINER)) break;
           log.finer("processMBeans method: object '" + name.getCanonicalName() + 
             "' was skipped because it matches an exclusion object '" + ename.getCanonicalName() + "'");
           
           break;
         }
       }
       
       MBeanInfo info = con.getMBeanInfo(name);
       MBeanAttributeInfo[] mbas = info.getAttributes();
       List<String> l = new ArrayList();
       Object localObject1; int j = (localObject1 = mbas).length; for (int i = 0; i < j; i++) { MBeanAttributeInfo mba = localObject1[i];
         l.add(mba.getName());
       }
       if (l.size() > 0)
       {
         StringBuilder sb1 = new StringBuilder();
         if (props.isTask()) {
           sb1.append(buildMGEntry(name, props.getPluginId()));
         }
         boolean metricsInd = false;
         for (localObject1 = l.iterator(); ((Iterator)localObject1).hasNext();) { String s = (String)((Iterator)localObject1).next();
           String[] aNames = { s };
           AttributeList aList = null;
           try {
             aList = con.getAttributes(name, aNames);
           } catch (Exception e) {
             if (!log.isLoggable(Level.FINER)) continue; }
           log.finer("processMBeans method: name '" + 
             s + "' is skipped because the con.getAttributes method threw the following exception '" + HelperUtils.getExceptionAsString(e) + "'");
           
           continue;
           
           if (aList == null) {
             if (log.isLoggable(Level.FINER)) {
               log.finer("processMBeans method: name '" + 
                 s + "' is skipped because getAttributes method returned AttributrList which is null.");
             }
           }
           else {
             for (Attribute att : aList.asList())
               if (!props.isTask()) {
                 buildMeasure((MonitorEnvironment)env, name, att, props.getPluginId());
               }
               else {
                 AttrEntityHelper ah = buildAttrEntry(name, att, props.getPluginId(), props.getJmxServerType());
                 if (ah.isMetric()) {
                   metricsInd = true;
                   sb1.append(ah.getMetricString());
                 }
               }
           }
         }
         if (props.isTask())
         {
           sb1.append(REST_OF_MG_LINES);
         }
         if (metricsInd) {
           sb.append(sb1.toString());
         }
       }
     }
     
 
 
     if (props.isTask()) {
       sb.append("</plugin>");
       if (sb.toString().indexOf("    <metric name=\"".trim()) == -1) {
         String msg = "processMBeans method: no metrics were added to the plugin.xml file. Check Include/Exclude MBeans Patterns parameters";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
       zipMGDir(props);
       buildMGJar(sb.toString(), props);
       cleanupArchiveDir(props);
     }
   }
   
   public void buildMGJar(String pluginXml, JMXMonitorProperties props) throws Exception {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering buildMGJar method");
     }
     
 
     File file = new File(props.getDirJar());
     
     FileUtils.deleteQuietly(file);
     
     String dir = props.getDirJar() + "build" + File.separator;
     file = new File(dir);
     if (!file.mkdirs()) {
       String msg = "buildMGJar method: File.mkdirs method returned 'false' for the '" + dir + "' directory";
       log.severe(msg);
       throw new RuntimeException(msg);
     }
     
     File output = new File(dir, props.getJarFileName());
     OutputStream out = new FileOutputStream(output);
     ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("jar", out);
     
     os.putArchiveEntry(new ZipArchiveEntry("plugin.xml"));
     IOUtils.copy(IOUtils.toInputStream(pluginXml), os);
     os.closeArchiveEntry();
     
     os.putArchiveEntry(new ZipArchiveEntry("META-INF/MANIFEST.MF"));
     IOUtils.copy(IOUtils.toInputStream(props.getManifest()), os);
     os.closeArchiveEntry();
     
     os.putArchiveEntry(new ZipArchiveEntry("src/dummy"));
     IOUtils.copy(IOUtils.toInputStream(""), os);
     os.closeArchiveEntry();
     
     os.putArchiveEntry(new ZipArchiveEntry("lib/dummy"));
     IOUtils.copy(IOUtils.toInputStream(""), os);
     os.closeArchiveEntry();
     
     os.close();
   }
   
   public AttrEntityHelper buildAttrEntry(ObjectName oName, Attribute attribute, String pluginId, String jmxServerType) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering buildAttrEntry method");
     }
     
     boolean metricNameInd = false;
     StringBuilder sb = new StringBuilder();
     String mgName = getMGName(pluginId, oName);
     String name = mgName + "." + attribute.getName();
     Object value = attribute.getValue();
     
 
     if ((value instanceof CompositeDataSupport)) {
       CompositeDataSupport cds = (CompositeDataSupport)value;
       
       for (String key : cds.getCompositeType().keySet()) {
         if ((cds.get(key) instanceof Number)) {
           metricNameInd = true;
           String cdsName = name + "." + key;
           String unit = getUnit(cdsName, JMX_SERVER_TYPE.valueOfIgnoreCase(jmxServerType));
           sb.append("    <metric name=\"").append(cdsName).append("\" unit=\"").append(unit)
             .append("\" description=\"My metric description ...\" />").append(NEW_LINE);
           if (log.isLoggable(Level.FINER)) {
             log.finer("buildAttrEntry method: measure name is '" + cdsName + ", measure value is value '" + format(cds.get(key)) + "'");
           }
         } else {
           log.warning("buildAttrEntry method: domain: " + oName.getDomain() + 
             ", keys: " + oName.getCanonicalKeyPropertyListString() + 
             ", attribute: " + name + "." + key + 
             ", value: " + cds.get(key) + 
             " is ignored because of non-numeric value");
         }
       }
     } else if ((value instanceof Number)) {
       metricNameInd = true;
       String unit = getUnit(name, JMX_SERVER_TYPE.valueOfIgnoreCase(jmxServerType));
       sb.append("    <metric name=\"").append(name).append("\" unit=\"").append(unit)
         .append("\" description=\"My metric description ...\" />").append(NEW_LINE);
       if (log.isLoggable(Level.FINER)) {
         log.finer("buildAttrEntry method: domain: " + oName.getDomain() + 
           ", keys: " + oName.getCanonicalKeyPropertyListString() + 
           ", attribute: " + name + ", value: " + (value == null ? "null" : value.toString()));
       }
     }
     else {
       log.warning("buildAttrEntry method: domain: " + oName.getDomain() + 
         ", keys: " + oName.getCanonicalKeyPropertyListString() + 
         ", attribute: " + name + ", value: " + (value == null ? "null" : value.toString()) + 
         " is ignored because of non-numeric value");
     }
     return new AttrEntityHelper(sb.toString(), metricNameInd);
   }
   
   public String getUnit(String name, JMX_SERVER_TYPE serverType) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering getUnit method: name is '" + name + "', serverType is '" + serverType + "'");
     }
     
     switch (serverType) {
     case CF_OPS_METRICS_TOOL: 
       Set<Map.Entry<String, Pattern>> entries = UNIT_PATTERNS_MAP_CF.entrySet();
       for (Map.Entry<String, Pattern> entry : entries) {
         if (((Pattern)entry.getValue()).matcher(name).matches()) {
           if (log.isLoggable(Level.FINER)) {
             log.finer("getUnit method: found match for attribute '" + name + "' is '" + (String)entry.getKey() + "'");
           }
           if (((String)entry.getKey()).equals("s"))
           {
             Pattern p = (Pattern)UNIT_PATTERNS_MAP_CF.get("ms");
             if (p.matcher(name).matches()) {
               return "ms";
             }
             p = (Pattern)UNIT_PATTERNS_MAP_CF.get("ns");
             if (p.matcher(name).matches()) {
               return "ns";
             }
           }
           return (String)entry.getKey();
         }
       }
       break;
     case CUSTOM_JMX_URL: 
     case WEBLOGIC: 
     case WEBSPHERE: 
       Set<Map.Entry<String, Pattern>> entries = UNIT_PATTERNS_MAP_JVM.entrySet();
       for (Map.Entry<String, Pattern> entry : entries) {
         if (((Pattern)entry.getValue()).matcher(name).matches()) {
           if (log.isLoggable(Level.FINER)) {
             log.finer("getUnit method: found match for attribute '" + name + "' is '" + (String)entry.getKey() + "'");
           }
           if (((String)entry.getKey()).equals("s"))
           {
             Pattern p = (Pattern)UNIT_PATTERNS_MAP_JVM.get("ms");
             if (p.matcher(name).matches()) {
               return "ms";
             }
             p = (Pattern)UNIT_PATTERNS_MAP_JVM.get("ns");
             if (p.matcher(name).matches()) {
               return "ns";
             }
           }
           return (String)entry.getKey();
         }
       }
       break;
     case JBOSS: case JVM: default: 
       String msg = "getUnit method: the JMX_SERVER_TYPE '" + serverType.name() + "' is not implemented yet.";
       log.severe(msg);
       throw new UnsupportedOperationException(msg);
     }
     
     Set<Map.Entry<String, Pattern>> entries;
     return "number";
   }
   
   public String buildMGEntry(ObjectName name, String pluginId) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering buildMGEntry method");
     }
     String mg = getMGName(pluginId, name);
     
     String v = transformString(mg);
     StringBuilder sb = new StringBuilder();
     sb.append("  <extension point=\"com.dynatrace.diagnostics.pdk.monitormetricgroup\" id=\"com.dynatrace.diagnostics.plugin.jmx.monitor.").append(v).append("\" name=\"").append(mg).append("\">").append(NEW_LINE);
     sb.append("    <metricgroup monitorid=\"com.dynatrace.diagnostics.plugin.jmx.monitor\">").append(NEW_LINE);
     return sb.toString();
   }
   
   public static String transformString(String source) {
     String v = source.replaceAll("[^A-Za-z0-9\\.]", "o");
     String[] as = v.split("\\.");
     StringBuilder sbId = new StringBuilder();
     String[] arrayOfString1; int j = (arrayOfString1 = as).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
       if (!s.isEmpty())
       {
 
         if (Character.isDigit(s.charAt(0))) {
           s = "a" + s;
         }
         sbId.append(".").append(s);
       } }
     if (sbId.length() == 0) {
       throw new RuntimeException("transformString method: source '" + source + "' has wrong format. It should be either 'Plugin Instance Id' or 'Host'.'Port' combination.");
     }
     v = sbId.toString().substring(1).toLowerCase();
     return v;
   }
   
   public String getMGName(String pluginId, ObjectName name) {
     if (log.isLoggable(Level.FINER))
       log.finer("Entering getMGName method");
     StringBuilder sb;
     StringBuilder sb;
     if (!pluginId.isEmpty()) {
       sb = new StringBuilder(pluginId).append(".").append(name.getDomain());
     } else {
       sb = new StringBuilder(name.getDomain());
     }
     String keyList = name.getKeyPropertyListString();
     String[] as = keyList.split(",");
     String[] arrayOfString1;
     int j = (arrayOfString1 = as).length; for (int i = 0; i < j; i++) { String key = arrayOfString1[i];
       String[] av = key.split("=");
       sb.append(".").append(av[0].replaceAll(" ", ""))
         .append(".").append(av[1].replaceAll(" ", ""));
     }
     
     return sb.toString().replace("\"", "");
   }
   
   public void buildMeasure(MonitorEnvironment env, ObjectName oName, Attribute attribute, String pluginId) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering buildMeasure method");
     }
     String mgName = getMGName(pluginId, oName);
     String attrName = mgName + "." + attribute.getName();
     Object value = attribute.getValue();
     Double d = Double.valueOf(NaN.0D);
     
     if ((value instanceof CompositeDataSupport))
     {
       CompositeDataSupport cds = (CompositeDataSupport)value;
       
       for (String key : cds.getCompositeType().keySet()) { Object obj;
         if (((obj = cds.get(key)) instanceof Number)) {
           String cdsName = attrName + "." + key;
           d = Double.valueOf(((Number)obj).doubleValue());
           if (log.isLoggable(Level.FINER)) {
             log.finer("buildMeasure method: measure name is '" + cdsName + ", measure value is value '" + d + "'");
           }
           
           populateMeasure(env, mgName, cdsName, d);
         }
         else if (log.isLoggable(Level.FINER)) {
           log.finer("buildMeasure method: measure: " + mgName + 
             ", attribute: " + attrName + "." + key + 
             ", value: " + cds.get(key) + 
             " is ignored because of non-numeric value");
         }
       }
     }
     else if ((value instanceof Number)) {
       d = Double.valueOf(((Number)value).doubleValue());
       
       populateMeasure(env, mgName, attrName, d);
     }
   }
   
   public void populateMeasure(MonitorEnvironment env, String mgName, String metricName, Double d)
   {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering populateMeasure method: metric group name is '" + mgName + "', metric name is '" + metricName + "', value = " + d);
     }
     Collection<MonitorMeasure> measures;
     if ((measures = env.getMonitorMeasures(mgName, metricName)) != null) {
       for (MonitorMeasure measure : measures) {
         log.finer("populateMeasure method: Populating measure '" + metricName + "' for metric group '" + mgName + "'");
         measure.setValue(d.doubleValue());
       }
     }
   }
   
   public String format(Object value) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering format method");
     }
     if (value == null)
       return "null";
     if ((value instanceof String))
       return (String)value;
     if ((value instanceof Number)) {
       NumberFormat f = NumberFormat.getInstance();
       f.setMaximumFractionDigits(2);
       f.setGroupingUsed(false);
       return f.format(value); }
     if ((value instanceof Object[])) {
       return Integer.toString(Arrays.asList((Object[])value).size());
     }
     return value.toString();
   }
   
   public JMXMonitorProperties setConfiguration(PluginEnvironment env) {
     if (log.isLoggable(Level.FINER)) {
       log.finer("Entering setConfiguration method");
     }
     
 
     JMXMonitorProperties props = new JMXMonitorProperties();
     props.setTask(env.getConfigBoolean("isTask").booleanValue());
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_IS_TASK parameter is '" + props.isTask() + "'");
     }
     if ((props.isTask()) && 
       (!(env instanceof TaskEnvironment))) {
       String msg = "setConfiguration message: plugin should be configured as a Task because isTask indicator is set to '" + props.isTask() + "'";
       log.severe(msg);
       throw new RuntimeException(msg);
     }
     
     props.setCustomJmxUrlInd(env.getConfigBoolean("isCustomJmxUrlInd").booleanValue());
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_IS_CUSTOM_JMX_URL parameter is '" + env.getConfigBoolean("isCustomJmxUrlInd") + "'");
     }
     if (props.isCustomJmxUrlInd()) { String v;
       props.setCustomJmxUrl((v = env.getConfigString("customJmxUrl")) != null ? v.trim() : "");
       if (props.getCustomJmxUrl().isEmpty()) {
         String msg = "setConfiguration message: 'customJmxUrl' configuration parameter is empty. Expected non-empty custom Jmx Url string.";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
     }
     if (props.isTask()) { String v;
       props.setJmxHost(v = (v = env.getConfigString("jmxHost")) != null ? v.trim() : "");
       if (v.isEmpty()) {
         String msg = "setConfiguration method: the JMX Host when plugin is configured as a task must not be empty";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
     } else {
       props.setJmxHost("");
       if (!props.isCustomJmxUrlInd()) {
         props.setJmxHost(env.getHost().getAddress().trim());
         if (log.isLoggable(Level.FINER)) {
           log.finer("setConfiguration method: JMX Host is '" + props.getJmxHost() + "'");
         }
       }
     }
     
     props.setJmxPort(0L);
     if (!props.isCustomJmxUrlInd()) {
       props.setJmxPort(env.getConfigLong("jmxPort").longValue());
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: CONFIG_JMX_PORT parameter is '" + props.getJmxPort() + "'");
       }
     }
     props.setPluginId(v = (v = env.getConfigString("pluginId")) == null ? "" : v.trim());
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_PLUGIN_ID parameter is '" + props.getPluginId() + "'");
     }
     if ((props.isCustomJmxUrlInd()) && (props.getPluginId().isEmpty())) {
       String msg = "setConfiguration method: the 'Plugin Instance Id' parameter must be set if the custom JMX service URL is used.";
       log.severe(msg);
       throw new RuntimeException(msg);
     }
     if (!v.isEmpty()) {
       if (!StringUtils.isAlphanumeric(v)) {
         String msg = "setConfiguration method: the pluginId is '" + v + "'. It contains non-alphanumeric characters.";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
     } else {
       props.setPluginId(props.getJmxHost() + "." + props.getJmxPort());
       if ((v = props.getPluginId()).trim().isEmpty()) {
         String msg = "setConfiguration method: the pluginId is '" + v + "'. It should not be empty.";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
     }
     props.setBundleSymbolicName("com.dynatrace.diagnostics.plugin.jmx.metricgroup." + transformString(props.getPluginId()));
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: BUNDLE_SYMBOLIC_NAME parameter is '" + props.getBundleSymbolicName() + "'");
     }
     props.setAuthenticationOn(env.getConfigBoolean("isAuthenticationOn").booleanValue());
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_IS_AUTHENTICATION_ON parameter is '" + props.isAuthenticationOn() + "'");
     }
     if (props.isAuthenticationOn()) {
       props.setUser((v = env.getConfigString("user")) != null ? v.trim() : "");
       props.setPassword(env.getConfigPassword("password"));
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: CONFIG_USER parameter is '" + props.getUser() + "'");
         log.finer("setConfiguration method: CONFIG_PASSWORD parameter is '" + props.getPassword() + "'");
       }
     }
     
     JMX_SERVER_TYPE jmxST;
     if (!props.isCustomJmxUrlInd()) {
       props.setJmxServerType((v = env.getConfigString("jmxServerType")) != null ? v.trim() : "");
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: CONFIG_JMX_SERVER_TYPE parameter is '" + props.getJmxServerType() + "'");
       }
       try {
         JMX_SERVER_TYPE jmxST = JMX_SERVER_TYPE.valueOfIgnoreCase(v);
         if (log.isLoggable(Level.FINER)) {
           log.finer("setConfiguration method: jmxST name is '" + jmxST.name() + "', ordinal is " + jmxST.ordinal() + ", jmxST is '" + jmxST.toString() + "'");
         }
       } catch (IllegalArgumentException localIllegalArgumentException) {
         String msg = "setConfiguration method: Incorrect value of the JMX SERVER TYPE parameter '" + v + "'";
         log.severe(msg);
         throw new RuntimeException(msg); }
       JMX_SERVER_TYPE jmxST;
       if ((jmxST != JMX_SERVER_TYPE.CF_OPS_METRICS_TOOL) && (jmxST != JMX_SERVER_TYPE.JVM) && (jmxST != JMX_SERVER_TYPE.JBOSS)) {
         String msg = "setConfiguration method: the JMX_SERVER_TYPE '" + jmxST.name() + "' is not implemented yet.";
         log.severe(msg);
         throw new UnsupportedOperationException(msg);
       }
     } else {
       jmxST = JMX_SERVER_TYPE.CUSTOM_JMX_URL;
       props.setJmxServerType(JMX_SERVER_TYPE.CUSTOM_JMX_URL.jmxServerType());
     }
     
     String v = (v = env.getConfigString("includeMBeansPatterns")) != null ? v.trim() : "";
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_INCLUDE_MBEAN_PATTERN parameter is '" + v + "'");
     }
     try {
       if (!v.isEmpty()) {
         props.setIncludeMBeansPatterns(IOUtils.readLines(IOUtils.toInputStream(v), DEFAULT_ENCODING));
       } else {
         props.setIncludeMBeansPatterns(new ArrayList());
       }
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: IncludeMBeansPatterns parameter is '" + props.getIncludeMBeansPatterns() + "'");
       }
     } catch (IOException e) {
       String msg = "setConfiguration method: exception was thrown when executing the props.setIncludeMBeansPatterns method '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       throw new RuntimeException(msg, e);
     }
     
     v = (v = env.getConfigString("excludeMBeansPatterns")) != null ? v.trim() : "";
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: CONFIG_EXCLUDE_MBEAN_PATTERN parameter is '" + v + "'");
     }
     try {
       if (!v.isEmpty()) {
         props.setExcludeMBeansPatterns(IOUtils.readLines(IOUtils.toInputStream(v), DEFAULT_ENCODING));
       } else {
         props.setExcludeMBeansPatterns(new ArrayList());
       }
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: ExcludeMBeansPatterns parameter is '" + props.getExcludeMBeansPatterns() + "'");
       }
     } catch (IOException e) {
       String msg = "setConfiguration method: exception was thrown when executing the props.setExcludeMBeansPatterns method '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       throw new RuntimeException(msg, e);
     }
     if (props.isTask()) {
       props.setJarVersion(v = (v = env.getConfigString("version")) != null ? v.trim() : "");
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: CONFIG_JAR_VERSION parameter is '" + props.getJarVersion() + "'");
       }
       if (v.isEmpty()) {
         String msg = "setConfiguration method: version of the jar file must not be empty";
         log.severe(msg);
         throw new RuntimeException(msg);
       }
       v = (v = env.getConfigString("dirJar")) != null ? v.trim() : "";
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: CONFIG_DIR_JAR parameter is '" + v + "'");
       }
       if (v.isEmpty()) {
         String msg = "setConfiguration method: the directory for the jar file is the Dynatrace Collector home directory";
         log.info(msg);
         v = "." + File.separator;
       }
       if (!v.endsWith(File.separator)) {
         v = v + File.separator;
       }
       props.setWorkDir(v);
       props.setDirJar(v + "mg" + File.separator);
       props.setArchiveDir(v + "archive" + File.separator);
       props.setPluginXml(props.getDirJar() + "plugin.xml");
       props.setJarFileName("com.dynatrace.diagnostics.plugin.jmx.metricgroup." + props.getPluginId() + "_" + props.getJarVersion() + ".jar");
       props.setJarAbsFileName(v + "build" + File.separator + props.getJarFileName());
       props.setManifestAbsFileName(props.getDirJar() + "META-INF" + File.separator + "MANIFEST.MF");
       props.setManifest(chopRecords(MANIFEST_MF_1 + props.getJarVersion() + MANIFEST_MF_2 + props.getPluginId() + 
         NEW_LINE + MANIFEST_MF_2_1 + MANIFEST_MF_3 + props.getBundleSymbolicName() + MANIFEST_MF_4));
       
       props.setShellScriptContent("cd " + props.getDirJar() + NEW_LINE + 
         "jar -cvfm build" + File.separator + props.getJarFileName() + 
         " META-INF" + File.separator + "MANIFEST.MF .");
       String[] cmd = new String[1];
       String shellSuffix = ".sh";
       if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
         shellSuffix = ".bat";
       }
       cmd[0] = (props.getWorkDir() + "export" + shellSuffix);
       props.setJarCommand(cmd);
       if (log.isLoggable(Level.FINER)) {
         log.finer("setConfiguration method: props is '" + props.toString() + "'");
       }
     }
     
     if (!props.isCustomJmxUrlInd())
       switch (jmxST) {
       case CF_OPS_METRICS_TOOL: 
       case CUSTOM_JMX_URL: 
         props.setJmxServiceUrlString("service:jmx:rmi://" + props.getJmxHost() + 
           ":" + props.getJmxPort() + "/jndi/rmi://" + 
           props.getJmxHost() + ":" + props.getJmxPort() + 
           "/jmxrmi");
         break;
       case WEBLOGIC: 
         props.setJmxServiceUrlString("service:jmx:remoting-jmx://" + props.getJmxHost() + 
           ":" + props.getJmxPort());
         break;
       case JBOSS: case JVM: default: 
         String msg = "setConfiguration method: JMX Server Type '" + jmxST.name() + "' is not implemented yet";
         log.severe(msg);
         throw new RuntimeException(msg);
         
         break; } else {
       props.setJmxServiceUrlString(props.getCustomJmxUrl());
     }
     if (log.isLoggable(Level.FINER)) {
       log.finer("setConfiguration method: jmxServiceUrlString is '" + props.getJmxServiceUrlString() + "'");
     }
     try {
       props.setJmxServiceUrl(new JMXServiceURL(props.getJmxServiceUrlString()));
       Map<String, String[]> map = new HashMap();
       if (props.isAuthenticationOn()) {
         String[] auth = { props.getUser(), props.getPassword() };
         map.put("jmx.remote.credentials", auth);
       }
       props.setCredentialsMap(map);
       props.setJmxConnector(JMXConnectorFactory.connect(props.getJmxServiceUrl(), map));
       props.setJmxConnection(props.getJmxConnector().getMBeanServerConnection());
     } catch (Exception e) {
       String msg = "setConfiguration method: '" + HelperUtils.getExceptionAsString(e) + "'";
       log.severe(msg);
       throw new RuntimeException(msg, e);
     }
     
     return props;
   }
   
   public static String chopRecords(String in) {
     String[] lines = in.split(NEW_LINE);
     StringBuilder sb = new StringBuilder();
     String[] arrayOfString1; int j = (arrayOfString1 = lines).length; for (int i = 0; i < j; i++) { String line = arrayOfString1[i];
       while (line.length() > 70) {
         sb.append(line.substring(0, 70)).append(NEW_LINE);
         line = " " + line.substring(70);
       }
       
       sb.append(line).append(NEW_LINE);
     }
     
     return sb.toString();
   }
   
   public static void zipMGDir(JMXMonitorProperties props) throws IOException {
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss.SSS");
     
     File f = new File(props.getArchiveDir());
     if (!f.exists()) {
       f.mkdirs();
     }
     f = new File(props.getDirJar());
     if (!f.exists()) {
       f.mkdirs();
       return;
     }
     String in = props.getDirJar().substring(0, props.getDirJar().length() - 1);
     long time = new Date().getTime();
     String timestamp = simpleDateFormat.format(Long.valueOf(time));
     String zipArchiveOut = props.getArchiveDir() + new File(in).getName() + "_" + 
       props.getJarVersion() + "_" + timestamp + ".zip";
     createZip(in, zipArchiveOut);
   }
   

   public static void createZip(String directoryPath, String zipPath)
     throws IOException
   {
     // Byte code:
     //   0: aconst_null
     //   1: astore_2
     //   2: aconst_null
     //   3: astore_3
     //   4: aconst_null
     //   5: astore 4
     //   7: new 417	java/io/FileOutputStream
     //   10: dup
     //   11: new 387	java/io/File
     //   14: dup
     //   15: aload_1
     //   16: invokespecial 392	java/io/File:<init>	(Ljava/lang/String;)V
     //   19: invokespecial 419	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
     //   22: astore_2
     //   23: new 1233	java/io/BufferedOutputStream
     //   26: dup
     //   27: aload_2
     //   28: invokespecial 1235	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
     //   31: astore_3
     //   32: new 1238	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream
     //   35: dup
     //   36: aload_3
     //   37: invokespecial 1240	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:<init>	(Ljava/io/OutputStream;)V
     //   40: astore 4
     //   42: aload 4
     //   44: aload_0
     //   45: ldc_w 462
     //   48: invokestatic 1241	com/dynatrace/diagnostics/plugin/jmx/JMXExecutor:addFileToZip	(Lorg/apache/commons/compress/archivers/zip/ZipArchiveOutputStream;Ljava/lang/String;Ljava/lang/String;)V
     //   51: goto +101 -> 152
     //   54: astore 5
     //   56: new 62	java/lang/StringBuilder
     //   59: dup
     //   60: ldc_w 1245
     //   63: invokespecial 66	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   66: aload 5
     //   68: invokestatic 81	com/dynatrace/diagnostics/plugin/jmx/helper/HelperUtils:getExceptionAsString	(Ljava/lang/Exception;)Ljava/lang/String;
     //   71: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   74: ldc 87
     //   76: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   79: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   82: astore 6
     //   84: getstatic 16	com/dynatrace/diagnostics/plugin/jmx/JMXExecutor:log	Ljava/util/logging/Logger;
     //   87: aload 6
     //   89: invokevirtual 89	java/util/logging/Logger:severe	(Ljava/lang/String;)V
     //   92: aload 5
     //   94: athrow
     //   95: astore 7
     //   97: aload 4
     //   99: ifnull +12 -> 111
     //   102: aload 4
     //   104: invokevirtual 1247	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:finish	()V
     //   107: goto +4 -> 111
     //   110: pop
     //   111: aload 4
     //   113: ifnull +12 -> 125
     //   116: aload 4
     //   118: invokevirtual 1250	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:close	()V
     //   121: goto +4 -> 125
     //   124: pop
     //   125: aload_3
     //   126: ifnull +11 -> 137
     //   129: aload_3
     //   130: invokevirtual 1251	java/io/BufferedOutputStream:close	()V
     //   133: goto +4 -> 137
     //   136: pop
     //   137: aload_2
     //   138: ifnull +11 -> 149
     //   141: aload_2
     //   142: invokevirtual 1252	java/io/FileOutputStream:close	()V
     //   145: goto +4 -> 149
     //   148: pop
     //   149: aload 7
     //   151: athrow
     //   152: aload 4
     //   154: ifnull +12 -> 166
     //   157: aload 4
     //   159: invokevirtual 1247	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:finish	()V
     //   162: goto +4 -> 166
     //   165: pop
     //   166: aload 4
     //   168: ifnull +12 -> 180
     //   171: aload 4
     //   173: invokevirtual 1250	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:close	()V
     //   176: goto +4 -> 180
     //   179: pop
     //   180: aload_3
     //   181: ifnull +11 -> 192
     //   184: aload_3
     //   185: invokevirtual 1251	java/io/BufferedOutputStream:close	()V
     //   188: goto +4 -> 192
     //   191: pop
     //   192: aload_2
     //   193: ifnull +11 -> 204
     //   196: aload_2
     //   197: invokevirtual 1252	java/io/FileOutputStream:close	()V
     //   200: goto +4 -> 204
     //   203: pop
     //   204: return
     // Line number table:
     //   Java source line #864	-> byte code offset #0
     //   Java source line #865	-> byte code offset #2
     //   Java source line #866	-> byte code offset #4
     //   Java source line #869	-> byte code offset #7
     //   Java source line #870	-> byte code offset #23
     //   Java source line #871	-> byte code offset #32
     //   Java source line #872	-> byte code offset #42
     //   Java source line #873	-> byte code offset #51
     //   Java source line #874	-> byte code offset #56
     //   Java source line #875	-> byte code offset #84
     //   Java source line #876	-> byte code offset #92
     //   Java source line #877	-> byte code offset #95
     //   Java source line #878	-> byte code offset #97
     //   Java source line #879	-> byte code offset #111
     //   Java source line #880	-> byte code offset #125
     //   Java source line #881	-> byte code offset #137
     //   Java source line #882	-> byte code offset #149
     //   Java source line #878	-> byte code offset #152
     //   Java source line #879	-> byte code offset #166
     //   Java source line #880	-> byte code offset #180
     //   Java source line #881	-> byte code offset #192
     //   Java source line #883	-> byte code offset #204
     // Local variable table:
     //   start	length	slot	name	signature
     //   0	205	0	directoryPath	String
     //   0	205	1	zipPath	String
     //   1	196	2	fOut	FileOutputStream
     //   3	182	3	bOut	java.io.BufferedOutputStream
     //   5	167	4	tOut	org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
     //   54	39	5	e	Exception
     //   82	6	6	msg	String
     //   95	55	7	localObject	Object
     //   110	1	8	localException1	Exception
     //   124	1	9	localException2	Exception
     //   136	1	10	localException3	Exception
     //   148	1	11	localException4	Exception
     //   165	1	12	localException5	Exception
     //   179	1	13	localException6	Exception
     //   191	1	14	localException7	Exception
     //   203	1	15	localException8	Exception
     // Exception table:
     //   from	to	target	type
     //   7	51	54	java/lang/Exception
     //   7	95	95	finally
     //   97	107	110	java/lang/Exception
     //   111	121	124	java/lang/Exception
     //   125	133	136	java/lang/Exception
     //   137	145	148	java/lang/Exception
     //   152	162	165	java/lang/Exception
     //   166	176	179	java/lang/Exception
     //   180	188	191	java/lang/Exception
     //   192	200	203	java/lang/Exception
   }
   

   private static void addFileToZip(org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream zOut, String path, String base)
     throws IOException
   {
     // Byte code:
     //   0: new 387	java/io/File
     //   3: dup
     //   4: aload_1
     //   5: invokespecial 392	java/io/File:<init>	(Ljava/lang/String;)V
     //   8: astore_3
     //   9: new 62	java/lang/StringBuilder
     //   12: dup
     //   13: aload_2
     //   14: invokestatic 482	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
     //   17: invokespecial 66	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   20: aload_3
     //   21: invokevirtual 1221	java/io/File:getName	()Ljava/lang/String;
     //   24: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   27: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   30: astore 4
     //   32: new 431	org/apache/commons/compress/archivers/zip/ZipArchiveEntry
     //   35: dup
     //   36: aload_3
     //   37: aload 4
     //   39: invokespecial 1263	org/apache/commons/compress/archivers/zip/ZipArchiveEntry:<init>	(Ljava/io/File;Ljava/lang/String;)V
     //   42: astore 5
     //   44: aload_0
     //   45: aload 5
     //   47: invokevirtual 1266	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:putArchiveEntry	(Lorg/apache/commons/compress/archivers/ArchiveEntry;)V
     //   50: aload_3
     //   51: invokevirtual 1267	java/io/File:isFile	()Z
     //   54: ifeq +48 -> 102
     //   57: aconst_null
     //   58: astore 6
     //   60: new 1270	java/io/FileInputStream
     //   63: dup
     //   64: aload_3
     //   65: invokespecial 1272	java/io/FileInputStream:<init>	(Ljava/io/File;)V
     //   68: astore 6
     //   70: aload 6
     //   72: aload_0
     //   73: invokestatic 448	org/apache/commons/io/IOUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)I
     //   76: pop
     //   77: aload_0
     //   78: invokevirtual 1273	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:closeArchiveEntry	()V
     //   81: goto +13 -> 94
     //   84: astore 7
     //   86: aload 6
     //   88: invokestatic 1274	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
     //   91: aload 7
     //   93: athrow
     //   94: aload 6
     //   96: invokestatic 1274	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
     //   99: goto +79 -> 178
     //   102: aload_0
     //   103: invokevirtual 1273	org/apache/commons/compress/archivers/zip/ZipArchiveOutputStream:closeArchiveEntry	()V
     //   106: aload_3
     //   107: invokevirtual 1278	java/io/File:listFiles	()[Ljava/io/File;
     //   110: astore 6
     //   112: aload 6
     //   114: ifnull +64 -> 178
     //   117: aload 6
     //   119: dup
     //   120: astore 10
     //   122: arraylength
     //   123: istore 9
     //   125: iconst_0
     //   126: istore 8
     //   128: goto +43 -> 171
     //   131: aload 10
     //   133: iload 8
     //   135: aaload
     //   136: astore 7
     //   138: aload_0
     //   139: aload 7
     //   141: invokevirtual 1282	java/io/File:getAbsolutePath	()Ljava/lang/String;
     //   144: new 62	java/lang/StringBuilder
     //   147: dup
     //   148: aload 4
     //   150: invokestatic 482	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
     //   153: invokespecial 66	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
     //   156: ldc_w 1285
     //   159: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
     //   162: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
     //   165: invokestatic 1241	com/dynatrace/diagnostics/plugin/jmx/JMXExecutor:addFileToZip	(Lorg/apache/commons/compress/archivers/zip/ZipArchiveOutputStream;Ljava/lang/String;Ljava/lang/String;)V
     //   168: iinc 8 1
     //   171: iload 8
     //   173: iload 9
     //   175: if_icmplt -44 -> 131
     //   178: return
     // Line number table:
     //   Java source line #886	-> byte code offset #0
     //   Java source line #887	-> byte code offset #9
     //   Java source line #888	-> byte code offset #32
     //   Java source line #890	-> byte code offset #44
     //   Java source line #892	-> byte code offset #50
     //   Java source line #893	-> byte code offset #57
     //   Java source line #895	-> byte code offset #60
     //   Java source line #896	-> byte code offset #70
     //   Java source line #897	-> byte code offset #77
     //   Java source line #898	-> byte code offset #81
     //   Java source line #899	-> byte code offset #86
     //   Java source line #900	-> byte code offset #91
     //   Java source line #899	-> byte code offset #94
     //   Java source line #902	-> byte code offset #99
     //   Java source line #903	-> byte code offset #102
     //   Java source line #904	-> byte code offset #106
     //   Java source line #906	-> byte code offset #112
     //   Java source line #907	-> byte code offset #117
     //   Java source line #908	-> byte code offset #138
     //   Java source line #907	-> byte code offset #168
     //   Java source line #912	-> byte code offset #178
     // Local variable table:
     //   start	length	slot	name	signature
     //   0	179	0	zOut	org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
     //   0	179	1	path	String
     //   0	179	2	base	String
     //   8	99	3	f	File
     //   30	119	4	entryName	String
     //   42	4	5	zipEntry	ZipArchiveEntry
     //   58	37	6	fInputStream	java.io.FileInputStream
     //   110	8	6	children	File[]
     //   84	8	7	localObject	Object
     //   136	4	7	child	File
     //   126	50	8	i	int
     //   123	53	9	j	int
     //   120	12	10	arrayOfFile1	File[]
     // Exception table:
     //   from	to	target	type
     //   60	84	84	finally
   }
   
   public void cleanupArchiveDir(JMXMonitorProperties props)
   {
     Collection<File> collection = FileUtils.listFiles(new File(props.getArchiveDir()), FileFilterUtils.suffixFileFilter(".zip"), null);
     if (collection.size() <= 100) {
       return;
     }
     List<File> files = new ArrayList(collection);
     Collections.sort(files, new Comparator() {
       public int compare(File o1, File o2) {
         return -o1.getName().compareTo(o2.getName());
       }
     });
     for (int i = 100; i < files.size(); i++) {
       FileUtils.deleteQuietly((File)files.get(i));
     }
   }
 }


/* Location:              C:\Users\PRH7261\Downloads\com.dynatrace.diagnostics.plugin.jmx_1.0.9.jar!\com\dynatrace\diagnostics\plugin\jmx\JMXExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */