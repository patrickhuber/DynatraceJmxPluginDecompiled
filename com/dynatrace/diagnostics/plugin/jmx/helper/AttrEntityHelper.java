 package com.dynatrace.diagnostics.plugin.jmx.helper;

 public class AttrEntityHelper
 {
   public AttrEntityHelper(String metricString, boolean metric) {
     this.metricString = metricString;
     this.metric = metric; }

   private String metricString = "";
   private boolean metric = false;

   public String getMetricString() { return this.metricString; }

   public boolean isMetric() {
     return this.metric;
   }
 }

