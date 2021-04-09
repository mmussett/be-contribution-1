package com.tibco.be.custom.aws.services.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.DeleteTopicResult;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;
import com.tibco.be.custom.aws.services.util.SAMLUtils;
import com.tibco.be.custom.aws.services.util.StsUtils;
import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.BUI;

@BEPackage(
        catalog = "AWS",//Add a catalog name here
        category = "SNS", //Add a category name here
        synopsis = "AWS SNS Functions") //Add a synopsis here
public class Notification {


    @BEFunction(
            name = "publishNotification",
            signature = "String publishNotification (String endpoint, String topicARN, String message, String regionName, String awsAccessKey, String awsSecretKey)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Override default AWS SNS Service endpoint*/),
                @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
                @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
                @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
            },
            freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
            version = "1.0", /*Add Version here*/
            see = "",
            mapper = @BEMapper(),
            description = "Publish message to SNS Topic using Access and Secret Key" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "String result = SNS.publishNotification(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotification(String endpoint, String topicARN, String message, String subject, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSNS client = createAmazonSNSClient(endpoint, regionName, awsAccessKey, awsSecretKey);
            String result = publish(client, topicARN, message, subject);
            client.shutdown();
            return result;

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
            name = "publishNotificationWithRoleARN",
            signature = "String publishNotificationWithRoleARN(String endpoint, String topicARN, String message, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Add Description here*/),
                @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
                @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
                @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
                @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
                @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
                @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
            },
            freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
            version = "1.0", //Add Version here
            see = "",
            mapper = @BEMapper(),
            description = "Publish SNS Notification using Access Key, Secret Key, and Role ARN" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "String result = SNS.publishNotificationWithRoleARN(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotificationWithRoleARN(String endpoint, String topicARN, String message, String subject, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSNS client = createAmazonSNSClient(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
            String result =  publish(client, topicARN, message, subject);
            client.shutdown();
            return result;

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    @BEFunction(
            name = "publishNotificationWithSAML",
            signature = "String publishNotificationWithSAML(String endpoint, String topicARN, String message, String subject, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Add Description here*/),
                @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
                @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
                @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
                @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
                @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
                @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
                @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
                @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
            },
            freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
            version = "1.0", /*Add Version here*/
            see = "",
            mapper = @BEMapper(),
            description = "Publish SNS Notification using SAML authentication" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "String result = SNS.publishNotificationWithSAML(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotificationWithSAML(String endpoint, String topicARN, String message, String subject, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        try {
            AmazonSNS client = createAmazonSNSClient(endpoint,regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
            String result =  publish(client, topicARN, message, subject);
            client.shutdown();
            return result;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @BEFunction(
        name = "publishNotificationSMS",
        signature = "String result = publishNotificationSMS (String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsSenderID", type = "String", desc = "SMS Sender ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsOriginationNumber", type = "String", desc = "SMS Origination Number" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsMaxPrice", type = "String", desc = "SMS Maximum Price" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsType", type = "String", desc = "SMS Type (Promotional or Transactional)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Publish SMS via SNS Notification using Access and Secret Key" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = SNS.publishNotificationSMS(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotificationSMS(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSNS client = createAmazonSNSClient(endpoint, regionName, awsAccessKey, awsSecretKey);
            Map<String, MessageAttributeValue> smsAttributes = createSMSAttributes(smsSenderID, smsOriginationNumber, smsMaxPrice, smsType);
            String result =  publishWithAttributes(client, topicARN, message, smsAttributes);
            client.shutdown();
            return result;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
        name = "publishNotificationSMSWithRoleARN",
        signature = "String publishNotificationSMSWithRoleARN(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsSenderID", type = "String", desc = "SMS Sender ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsOriginationNumber", type = "String", desc = "SMS Origination Number" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsMaxPrice", type = "String", desc = "SMS Maximum Price" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsType", type = "String", desc = "SMS Type (Promotional or Transactional)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleARN", type = "String", desc = "AWS Role ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "roleSessionName", type = "String", desc = "AWS Role Session Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Session Token duration" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", //Add Version here
        see = "",
        mapper = @BEMapper(),
        description = "Publish SMS via SNS Notification using Access Key, Secret Key, and Role ARN" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = SNS.publishNotificationSMSWithRoleARN(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotificationSMSWithRoleARN(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSNS client = createAmazonSNSClient(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
            Map<String, MessageAttributeValue> smsAttributes = createSMSAttributes(smsSenderID, smsOriginationNumber, smsMaxPrice, smsType);
            String result =  publishWithAttributes(client, topicARN, message, smsAttributes);
            client.shutdown();
            return result;

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
        name = "publishNotificationSMSWithSAML",
        signature = "String publishNotificationSMSWithSAML(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration, boolean useProxy, String proxyUsername, String proxyPassword, String proxyHost, int proxyPort)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SNS Service endpoint, set to null to use default SNS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "topicARN", type = "String", desc = "SNS Topic ARN" /*Add Description here*/),
            @FunctionParamDescriptor(name = "message", type = "String", desc = "Message" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsSenderID", type = "String", desc = "SMS Sender ID" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsOriginationNumber", type = "String", desc = "SMS Origination Number" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsMaxPrice", type = "String", desc = "SMS Maximum Price" /*Add Description here*/),
            @FunctionParamDescriptor(name = "smsType", type = "String", desc = "SMS Type (Promotional or Transactional)" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpName", type = "String", desc = "IDP Name" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpEntryUrl", type = "String", desc = "IDP URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpUsername", type = "String", desc = "IDP Username" /*Add Description here*/),
            @FunctionParamDescriptor(name = "idpPassword", type = "String", desc = "IDP Password" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsRole", type = "String", desc = "AWS Role" /*Add Description here*/),
            @FunctionParamDescriptor(name = "duration", type = "int", desc = "Token duration" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Publish SMS via SNS Notification using SAML authentication" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String result = SNS.publishNotificationSMSWithSAML(null, \"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String publishNotificationSMSWithSAML(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {
        try {
            AmazonSNS client = createAmazonSNSClient(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
            Map<String, MessageAttributeValue> smsAttributes = createSMSAttributes(smsSenderID, smsOriginationNumber, smsMaxPrice, smsType);
            String result =  publishWithAttributes(client, topicARN, message, smsAttributes);
            client.shutdown();
            return result;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String publish(AmazonSNS client, String topicARN, String message, String subject) throws Exception {

        try {
            PublishRequest publishReq = new PublishRequest()
                    .withTopicArn(topicARN)
                    .withMessage(message)
                    .withSubject(subject);

            PublishResult result = client.publish(publishReq);
            String messageId = result.getMessageId();
            return messageId;


        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String publishWithAttributes(AmazonSNS client, String topicARN, String message, Map<String, MessageAttributeValue> attributes) throws Exception {

        try {
            PublishRequest publishReq = new PublishRequest()
                .withTopicArn(topicARN)
                .withMessage(message)
                .withMessageAttributes(attributes);

            PublishResult result = client.publish(publishReq);
            String messageId = result.getMessageId();
            return messageId;


        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static AmazonSNS createAmazonSNSClient(String endpoint, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws Exception {

        Credentials creds = StsUtils.createCredentialsWithAssumeRole(roleARN, roleSessionName, regionName, duration, awsAccessKey, awsSecretKey);

        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                creds.getAccessKeyId(),
                creds.getSecretAccessKey(),
                creds.getSessionToken());

        if (endpoint != null && endpoint.length() != 0) {
            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSNS client =
                AmazonSNSClientBuilder.standard()
                    .withEndpointConfiguration(config)
                    .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                    .build();

            return client;

        } else {
      AmazonSNS client =
          AmazonSNSClientBuilder.standard()
              .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
              .withRegion(regionName)
              .build();

      return client;
        }
    }

    public static AmazonSNS createAmazonSNSClient(String endpoint, String regionName, String awsAccessKey, String awsSecretKey) throws Exception {

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        if (endpoint != null && endpoint.length() != 0) {
            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSNS client = AmazonSNSClientBuilder
                .standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();

            return client;

        } else {
        AmazonSNS client = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withRegion(regionName)
                .build();

        return client;
        }
    }

    public static AmazonSNS createAmazonSNSClient(String endpoint, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws Exception {

        Credentials credentials = SAMLUtils.createCredentialsWithSAML(idpName,  idpEntryUrl,  idpUsername,  idpPassword, regionName, awsRole, duration, false, null ,  null,  null,  0);


        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                credentials.getAccessKeyId(),
                credentials.getSecretAccessKey(),
                credentials.getSessionToken());

        if (endpoint != null && endpoint.length() != 0) {
            AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSNS client = AmazonSNSClientBuilder
                .standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .build();

            return client;
        } else {
        AmazonSNS client = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
            .withRegion(regionName)
                .build();

        return client;
        }
    }

    public static Map<String, MessageAttributeValue> createSMSAttributes(String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType) {

        Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();

        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
            .withStringValue(smsSenderID) //The sender ID shown on the device.
            .withDataType("String"));
        if (smsOriginationNumber != null) {
            smsAttributes.put("AWS.MM.SMS.OriginationNumber", new MessageAttributeValue()
                .withStringValue(smsOriginationNumber) //Sets the origination number.
                .withDataType("String"));
        }
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
            .withStringValue(smsMaxPrice) //Sets the max price to 0.50 USD.
            .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
            .withStringValue(smsType) //Sets the type to promotional.
            .withDataType("String"));

        return smsAttributes;

    }

}
