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


/* Location:              C:\Users\PRH7261\Downloads\com.dynatrace.diagnostics.plugin.jmx_1.0.9.jar!\com\dynatrace\diagnostics\plugin\jmx\helper\AttrEntityHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */