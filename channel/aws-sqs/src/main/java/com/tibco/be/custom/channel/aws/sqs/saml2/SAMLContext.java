/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

import com.amazonaws.services.securitytoken.model.Credentials;

public class SAMLContext {

    private String idProviderType;
    private String idpEntryUrl;
    private String idpUsername;
    private String idpPassword;
    private String awsRole;
    private int tokenExpirationDuration;
    private Credentials credentials;
    private boolean idpUseProxy;
    private String proxyUserName;
    private String proxyPassword;
    private String proxyHost;
    private int proxyPort;
    private String regionName;
    private String queueUrl;

    public void setQueueUrl(String queueUrl) { this.queueUrl = queueUrl; }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setIdProviderType(String idProviderType) {
        this.idProviderType = idProviderType;
    }

    public void setIdpEntryUrl(String idpEntryUrl) {
        this.idpEntryUrl = idpEntryUrl;
    }

    public void setIdpUsername(String idpUsername) {
        this.idpUsername = idpUsername;
    }

    public void setIdpPassword(String idpPassword) {
        this.idpPassword = idpPassword;
    }

    public void setAwsRole(String awsRole) {
        this.awsRole = awsRole;
    }

    public void setTokenExpirationDuration(int tokenExpirationDuration) {
        this.tokenExpirationDuration = tokenExpirationDuration;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getQueueUrl() { return queueUrl; }

    public String getRegionName() {
        return regionName;
    }

    public String getIdProviderType() {
        return idProviderType;
    }

    public String getIdpEntryUrl() {
        return idpEntryUrl;
    }

    public String getIdpUsername() {
        return idpUsername;
    }

    public String getIdpPassword() {
        return idpPassword;
    }

    public String getAwsRole() {
        return awsRole;
    }

    public int getTokenExpirationDuration() {
        return tokenExpirationDuration;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isIdpUseProxy() {
        return idpUseProxy;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setIdpUseProxy(boolean idpUseProxy) {
        this.idpUseProxy = idpUseProxy;
    }

    public boolean getIdpUseProxy() {
        return idpUseProxy;
    }

    private SAMLContext(SAMLContextBuilder builder) {
        this.queueUrl = builder.queueUrl;
        this.regionName = builder.regionName;
        this.idProviderType = builder.idProviderType;
        this.idpEntryUrl = builder.idpEntryUrl;
        this.idpUsername = builder.idpUsername;
        this.idpPassword = builder.idpPassword;
        this.idpUseProxy = builder.idpUseProxy;
        this.proxyUserName = builder.proxyUserName;
        this.proxyPassword = builder.proxyPassword;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.awsRole = builder.awsRole;
        this.tokenExpirationDuration = builder.tokenExpirationDuration;
    }

    public static class SAMLContextBuilder {

        private String idProviderType;
        private String idpEntryUrl;
        private String idpUsername;
        private String idpPassword;
        private String awsRole;
        private int tokenExpirationDuration;
        private boolean idpUseProxy;
        private String proxyUserName;
        private String proxyPassword;
        private String proxyHost;
        private int proxyPort;
        private String regionName;
        private String queueUrl;

        public SAMLContextBuilder() {
            super();
        }

        public SAMLContextBuilder(String regionName,String queueUrl, String idProviderType, String idpEntryUrl, String idpUsername, String idpPassword, boolean idpUseProxy, String proxyUserName, String proxyPassword, String proxyHost, int proxyPort , int tokenExpirationDuration) {

            this.regionName = regionName;
            this.queueUrl = queueUrl;
            this.idProviderType = idProviderType;
            this.idpEntryUrl = idpEntryUrl;
            this.idpUsername = idpUsername;
            this.idpPassword = idpPassword;
            this.idpUseProxy = idpUseProxy;
            this.proxyUserName = proxyUserName;
            this.proxyPassword = proxyPassword;
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
            this.tokenExpirationDuration = tokenExpirationDuration;
        }

        public SAMLContextBuilder setRegionName(String regionName) {
            this.regionName = regionName;
            return this;
        }

        public SAMLContextBuilder setQueueUrl(String queueUrl) {
            this.queueUrl = queueUrl;
            return this;
        }


        public SAMLContextBuilder setIdProviderType(String idProviderType) {
            this.idProviderType = idProviderType;
            return this;
        }

        public SAMLContextBuilder setIdpEntryUrl(String idpEntryUrl) {
            this.idpEntryUrl = idpEntryUrl;
            return this;
        }

        public SAMLContextBuilder setIdpUsername(String idpUsername) {
            this.idpUsername = idpUsername;
            return this;
        }

        public SAMLContextBuilder setIdpPassword(String idpPassword) {
            this.idpPassword = idpPassword;
            return this;
        }

        public SAMLContextBuilder setIdpUseProxy(boolean idpUseProxy) {
            this.idpUseProxy = idpUseProxy;
            return this;
        }

        public SAMLContextBuilder setProxyUserName(String proxyUserName) {
            this.proxyUserName = proxyUserName;
            return this;
        }

        public SAMLContextBuilder setProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }
        public SAMLContextBuilder setProxyPort(int proxyPort) {
            this.proxyPort=proxyPort;
            return this;
        }

        public SAMLContextBuilder setAwsRole(String awsRole) {
            this.awsRole = awsRole;
            return this;
        }

        public SAMLContextBuilder setTokenExpirationDuration(int tokenExpirationDuration) {
            this.tokenExpirationDuration = tokenExpirationDuration;
            return this;
        }

        public SAMLContext build() {
            return new SAMLContext(this);
        }
    }





}
