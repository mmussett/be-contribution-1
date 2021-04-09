package com.tibco.be.custom.aws.services.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;

public class StsUtils {

    public static String getSessionToken(String regionName, String awsAccessKey, String awsSecretKey) {

        AWSCredentialsProvider credentialsProvider = createCredentialProvider(awsAccessKey,awsSecretKey);

        // Creating the STS client is part of your trusted code. It has
        // the security credentials you use to obtain temporary security credentials.
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(regionName)
                .build();

        return stsClient.getSessionToken().getCredentials().getSessionToken();
    }

    public static String getSessionTokenWithAssumeRole(String roleARN, String roleSessionName, String regionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        Credentials credentials = createCredentialsWithAssumeRole(roleARN,  roleSessionName,  regionName,  duration,  awsAccessKey, awsSecretKey);
        return credentials.getSessionToken();

    }


    public static Credentials createCredentialsWithAssumeRole(String roleARN, String roleSessionName, String regionName, int duration, String awsAccessKey, String awsSecretKey) {

        // Creating the STS client is part of your trusted code. It has
        // the security credentials you use to obtain temporary security credentials.
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(createCredentialProvider(awsAccessKey,awsSecretKey))
                .withRegion(regionName)
                .build();

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        AssumeRoleRequest roleRequest = new AssumeRoleRequest()
                .withRoleArn(roleARN)
                .withExternalId("externalId")
                .withDurationSeconds(duration)
                .withRoleSessionName(roleSessionName);

        AssumeRoleResult roleResponse = stsClient.assumeRole(roleRequest);

        return roleResponse.getCredentials();

    }



    public static AWSCredentialsProvider createCredentialProvider(String accessKey, String secretKey) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        return credentialsProvider;
    }

}
