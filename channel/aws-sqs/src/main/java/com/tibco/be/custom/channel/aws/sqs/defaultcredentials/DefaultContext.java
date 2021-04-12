package com.tibco.be.custom.channel.aws.sqs.defaultcredentials;

public class DefaultContext {

  private String regionName = "eu-west-1";
  private String queueUrl = "";

  public String getQueueUrl() { return queueUrl; }

  public void setQueueUrl(String queueUrl) { this.queueUrl = queueUrl; }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }


  private DefaultContext(DefaultContextBuilder builder) {
    this.queueUrl = builder.queueUrl;
    this.regionName = builder.regionName;

  }

  public static class DefaultContextBuilder {

    private String queueUrl;
    private String regionName;


    public DefaultContextBuilder() {
      super();
    }

    public DefaultContextBuilder(String regionName,String queueUrl) {
      this.queueUrl = queueUrl;
      this.regionName = regionName;
    }

    public DefaultContext.DefaultContextBuilder setQueueUrl(String queueUrl) {
      this.queueUrl = queueUrl;
      return this;
    }
    public DefaultContext.DefaultContextBuilder setRegionName(String regionName) {
      this.regionName = regionName;
      return this;
    }


    public DefaultContext build() {
      return new DefaultContext(this);
    }
  }
}
