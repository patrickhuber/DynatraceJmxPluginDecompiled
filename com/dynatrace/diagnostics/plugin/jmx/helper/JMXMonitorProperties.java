 package com.dynatrace.diagnostics.plugin.jmx.helper;

 import java.util.Arrays;
 import java.util.List;
 import java.util.Map;
 import javax.management.MBeanServerConnection;
 import javax.management.remote.JMXConnector;
 import javax.management.remote.JMXServiceURL;













 public class JMXMonitorProperties
 {
   private boolean isTask;
   private String pluginId;
   private String bundleSymbolicName;
   private boolean customJmxUrlInd;
   private String jmxHost;
   private long jmxPort;
   private boolean isAuthenticationOn;
   private String user;
   private String password;
   private String urlPath;
   private String customJmxUrl;
   private String jmxServerType;
   private List<String> includeMBeansPatterns;
   private List<String> excludeMBeansPatterns;
   private String workDir;
   private String dirJar;
   private String archiveDir;
   private String jarFileName;
   private String jarAbsFileName;
   private String manifestAbsFileName;
   private String manifest;
   private String pluginXml;
   private String jarVersion;
   private String[] jarCommand;
   private String shellScriptContent;
   private String jmxServiceUrlString;
   private JMXServiceURL jmxServiceUrl;
   private Map<String, String[]> credentialsMap;
   private JMXConnector jmxConnector;
   private MBeanServerConnection jmxConnection;
   private String dtRestUrl;
   private int httpStatusCode;

   public boolean isTask()
   {
     return this.isTask;
   }

   public void setTask(boolean isTask) { this.isTask = isTask; }

   public String getPluginId() {
     return this.pluginId;
   }

   public void setPluginId(String pluginId) { this.pluginId = pluginId; }

   public String getBundleSymbolicName() {
     return this.bundleSymbolicName;
   }

   public void setBundleSymbolicName(String bundleSymbolicName) { this.bundleSymbolicName = bundleSymbolicName; }

   public boolean isCustomJmxUrlInd() {
     return this.customJmxUrlInd;
   }

   public void setCustomJmxUrlInd(boolean customJmxUrlInd) { this.customJmxUrlInd = customJmxUrlInd; }

   public String getJmxHost() {
     return this.jmxHost;
   }

   public void setJmxHost(String jmxHost) { this.jmxHost = jmxHost; }

   public long getJmxPort() {
     return this.jmxPort;
   }

   public void setJmxPort(long rmiPort) { this.jmxPort = rmiPort; }

   public boolean isAuthenticationOn() {
     return this.isAuthenticationOn;
   }

   public void setAuthenticationOn(boolean isAuthenticationOn) { this.isAuthenticationOn = isAuthenticationOn; }

   public String getUser() {
     return this.user;
   }

   public void setUser(String user) { this.user = user; }

   public String getPassword() {
     return this.password;
   }

   public void setPassword(String password) { this.password = password; }

   public String getUrlPath() {
     return this.urlPath;
   }

   public void setUrlPath(String urlPath) { this.urlPath = urlPath; }

   public String getCustomJmxUrl() {
     return this.customJmxUrl;
   }

   public void setCustomJmxUrl(String customJmxUrl) { this.customJmxUrl = customJmxUrl; }

   public String getJmxServerType() {
     return this.jmxServerType;
   }

   public void setJmxServerType(String jmxServerType) { this.jmxServerType = jmxServerType; }

   public List<String> getIncludeMBeansPatterns() {
     return this.includeMBeansPatterns;
   }

   public void setIncludeMBeansPatterns(List<String> includeMBeansPatterns) { this.includeMBeansPatterns = includeMBeansPatterns; }

   public List<String> getExcludeMBeansPatterns() {
     return this.excludeMBeansPatterns;
   }

   public void setExcludeMBeansPatterns(List<String> excludeMBeansPatterns) { this.excludeMBeansPatterns = excludeMBeansPatterns; }

   public String getWorkDir() {
     return this.workDir;
   }

   public void setWorkDir(String workDir) { this.workDir = workDir; }

   public String getArchiveDir() {
     return this.archiveDir;
   }

   public void setArchiveDir(String archiveDir) { this.archiveDir = archiveDir; }

   public String getDirJar() {
     return this.dirJar;
   }

   public void setDirJar(String jarFile) { this.dirJar = jarFile; }

   public String getJarFileName() {
     return this.jarFileName;
   }

   public void setJarFileName(String jarFileName) { this.jarFileName = jarFileName; }

   public String getJarAbsFileName() {
     return this.jarAbsFileName;
   }

   public void setJarAbsFileName(String jarAbsFileName) { this.jarAbsFileName = jarAbsFileName; }

   public String getJarVersion() {
     return this.jarVersion;
   }

   public void setJarVersion(String jarVersion) { this.jarVersion = jarVersion; }

   public String[] getJarCommand() {
     return this.jarCommand;
   }

   public void setJarCommand(String[] jarCommand) { this.jarCommand = jarCommand; }





































   public String getShellScriptContent()
   {
     return this.shellScriptContent;
   }

   public void setShellScriptContent(String shellScriptContent) { this.shellScriptContent = shellScriptContent; }

   public String getManifestAbsFileName() {
     return this.manifestAbsFileName;
   }

   public void setManifestAbsFileName(String manifestAbsFileName) { this.manifestAbsFileName = manifestAbsFileName; }

   public String getManifest() {
     return this.manifest;
   }

   public void setManifest(String manifest) { this.manifest = manifest; }

   public String getPluginXml() {
     return this.pluginXml;
   }

   public void setPluginXml(String pluginXml) { this.pluginXml = pluginXml; }

   public String getJmxServiceUrlString() {
     return this.jmxServiceUrlString;
   }

   public void setJmxServiceUrlString(String jmxServiceUrlString) { this.jmxServiceUrlString = jmxServiceUrlString; }

   public JMXServiceURL getJmxServiceUrl() {
     return this.jmxServiceUrl;
   }

   public void setJmxServiceUrl(JMXServiceURL jmxServiceUrl) { this.jmxServiceUrl = jmxServiceUrl; }

   public Map<String, String[]> getCredentialsMap() {
     return this.credentialsMap;
   }

   public void setCredentialsMap(Map<String, String[]> credentialsMap) { this.credentialsMap = credentialsMap; }

   public JMXConnector getJmxConnector() {
     return this.jmxConnector;
   }

   public void setJmxConnector(JMXConnector jmxConnector) { this.jmxConnector = jmxConnector; }

   public MBeanServerConnection getJmxConnection() {
     return this.jmxConnection;
   }

   public void setJmxConnection(MBeanServerConnection jmxConnection) { this.jmxConnection = jmxConnection; }






   public String getDtRestUrl()
   {
     return this.dtRestUrl;
   }

   public void setDtRestUrl(String dtRestUrl) { this.dtRestUrl = dtRestUrl; }

   public int getHttpStatusCode() {
     return this.httpStatusCode;
   }

   public void setHttpStatusCode(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }

   public String toString()
   {
     StringBuilder builder = new StringBuilder();
     builder.append("JMXMonitorProperties [isTask=");
     builder.append(this.isTask);
     builder.append(", pluginId=");
     builder.append(this.pluginId == null ? "-" : this.pluginId);
     builder.append(", bundleSymbolicName=");
     builder.append(this.bundleSymbolicName == null ? "" : this.bundleSymbolicName);
     builder.append(", customJmxUrlInd=");
     builder.append(this.customJmxUrlInd);
     builder.append(", jmxHost=");
     builder.append(this.jmxHost);
     builder.append(", jmxPort=");
     builder.append(this.jmxPort);
     builder.append(", isAuthenticationOn=");
     builder.append(this.isAuthenticationOn);
     builder.append(", user=");
     builder.append((this.user == null) || (this.user.isEmpty()) ? "-" : this.user);
     builder.append(", password=");
     builder.append((this.password == null) || (this.password.isEmpty()) ? "-" : this.password);
     builder.append(", urlPath=");
     builder.append((this.urlPath == null) || (this.urlPath.isEmpty()) ? "-" : this.urlPath);
     builder.append(", customJmxUrl=");
     builder.append((this.customJmxUrl == null) || (this.customJmxUrl.isEmpty()) ? "-" : this.customJmxUrl);
     builder.append(", jmxServerType=");
     builder.append((this.jmxServerType == null) || (this.jmxServerType.isEmpty()) ? "-" : this.jmxServerType);
     builder.append(", includeMBeansPatterns=");
     builder.append(Arrays.toString(this.includeMBeansPatterns.toArray()));
     builder.append(", excludeMBeansPatterns=");
     builder.append(Arrays.toString(this.excludeMBeansPatterns.toArray()));
     builder.append(", workDir=");
     builder.append(this.workDir);
     builder.append(", dirJar=");
     builder.append(this.dirJar);
     builder.append(", archiveDir=");
     builder.append(this.archiveDir);
     builder.append(", jarFileName=");
     builder.append(this.jarFileName);
     builder.append(", jarAbsFileName=");
     builder.append(this.jarAbsFileName);
     builder.append(", jarVersion=");
     builder.append(this.jarVersion);
     builder.append(", jarCommand=");
     builder.append(Arrays.toString(this.jarCommand));













     builder.append(", shellScriptContent=");
     builder.append(this.shellScriptContent);
     builder.append(", manifestAbsFileName=");
     builder.append(this.manifestAbsFileName);
     builder.append(", manifest=");
     builder.append(this.manifest);
     builder.append(", manifest=");
     builder.append(this.manifest);
     builder.append(", pluginXml=");
     builder.append(this.pluginXml);
     builder.append(", jmxServiceUrlString=");
     builder.append((this.jmxServiceUrlString == null) || (this.jmxServiceUrlString.isEmpty()) ? "-" : this.jmxServiceUrlString);
     builder.append(", jmxServiceUrl=");
     builder.append(this.jmxServiceUrl);
     builder.append(", credentialsMap=");
     builder.append(this.credentialsMap);
     builder.append(", jmxConnector=");
     builder.append(this.jmxConnector);
     builder.append(", jmxConnection=");
     builder.append(this.jmxConnection);



     builder.append(", dtRestUrl=");
     builder.append(this.dtRestUrl == null ? "-" : this.dtRestUrl);
     builder.append("]");
     return builder.toString();
   }
 }

