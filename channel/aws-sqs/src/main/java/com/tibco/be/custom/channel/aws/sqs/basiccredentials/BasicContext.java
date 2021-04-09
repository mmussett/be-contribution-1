/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.basiccredentials;

import java.util.HashMap;

public class BasicContext {

    private String regionName = "eu-west-1";
    private String queueUrl = "";
    private String accessKey;
    private String secretKey;
    private String roleArn;
    private String sessionName = "BE";
    private int tokenExpirationDuration = 900;

    public String getQueueUrl() { return queueUrl; }

    public void setQueueUrl(String queueUrl) { this.queueUrl = queueUrl; }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getTokenExpirationDuration() {
        return tokenExpirationDuration;
    }

    public void setTokenExpirationDuration(int duration) {
        this.tokenExpirationDuration = duration;
    }

    private BasicContext(BasicContextBuilder builder) {
        this.queueUrl = builder.queueUrl;
        this.regionName = builder.regionName;
        this.accessKey = builder.accessKey;
        this.secretKey = builder.secretKey;
        this.roleArn = builder.roleArn;
        this.sessionName = builder.sessionName;
        this.tokenExpirationDuration = builder.tokenExpirationDuration;
    }

    public static class BasicContextBuilder {

        private String queueUrl;
        private String regionName;
        private String accessKey;
        private String secretKey;
        private String roleArn;
        private String sessionName = "BE";
        private int tokenExpirationDuration = 900;

        public BasicContextBuilder() {
            super();
        }

        public BasicContextBuilder(String regionName,String queueUrl, String accessKey, String secretKey, String roleArn, String sessionName, int tokenExpirationDuration) {
            this.queueUrl = queueUrl;
            this.regionName = regionName;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.roleArn = roleArn;
            this.sessionName = sessionName;
            this.tokenExpirationDuration = tokenExpirationDuration;
        }

        public BasicContextBuilder setQueueUrl(String queueUrl) {
            this.queueUrl = queueUrl;
            return this;
        }
        public BasicContextBuilder setRegionName(String regionName) {
            this.regionName = regionName;
            return this;
        }

        public BasicContextBuilder setAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        public BasicContextBuilder setSecretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public BasicContextBuilder setRoleArn(String roleArn) {
            this.roleArn = roleArn;
            return this;
        }

        public BasicContextBuilder setSessionName(String sessionName) {
            this.sessionName = sessionName;
            return this;
        }

        public BasicContextBuilder setTokenExpirationDuration(int tokenExpirationDuration) {
            this.tokenExpirationDuration = tokenExpirationDuration;
            return this;
        }

        public BasicContext build() {
            return new BasicContext(this);
        }
    }

}
