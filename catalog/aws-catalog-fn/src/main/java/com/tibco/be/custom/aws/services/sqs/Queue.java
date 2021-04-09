package com.tibco.be.custom.aws.services.sqs;

import static com.tibco.be.custom.aws.services.util.StsUtils.createCredentialsWithAssumeRole;
import static com.tibco.be.model.functions.FunctionDomain.ACTION;
import static com.tibco.be.model.functions.FunctionDomain.BUI;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.tibco.be.custom.aws.services.util.SAMLUtils;
import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import java.util.Map;


@BEPackage(
        catalog = "AWS",//Add a catalog name here
        category = "SQS", //Add a category name here
        synopsis = "AWS SQS Functions") //Add a synopsis here
public class Queue {


    @BEFunction(
            name = "getQueueAttributes",
            signature = "Object getQueueAttributes(String endpoint, String queueName, String regionName, String awsAccessKey, String awsSecretKey)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
                @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
                @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
                @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
            },
            freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
            version = "1.0", /*Add Version here*/
            see = "",
            mapper = @BEMapper(),
            description = "Get all Queue attributes" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "Object map = SQS.getQueueAttributes(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static Object getQueueAttributes(String endpoint, String queueURL, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, awsAccessKey, awsSecretKey);
            return getQueueAttributes(client, queueURL);

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    @BEFunction(
            name = "getQueueAttributesWithRoleARN",
            signature = "Object getQueueAttributesWithRoleARN(String endpoint, String queueURL, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
                @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
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
            description = "Get all Queue attributes" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "Object map = SQS.getQueueAttributesWithRoleARN(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static Object getQueueAttributesWithRoleARN(String endpoint, String queueURL, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
            return getQueueAttributes(client, queueURL);

        } catch(Exception e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
            name = "getQueueAttributesWithSAML",
            signature = "Object getQueueAttributesWithSAML(String endpoint, String queueURL, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
            params = {
                @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
                @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
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
            description = "Get all Queue attributes" /*Add Description here*/,
            cautions = "none",
            fndomain = {ACTION, BUI},
            example = "Object map = SQS.getQueueAttributesWithSAML(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static Object getQueueAttributesWithSAML(String endpoint, String queueURL, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
            return getQueueAttributes(client, queueURL);

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
        name = "getQueueAttribute",
        signature = "String getQueueAttribute (String endpoint, String queueName, String attributeName, String regionName, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "attributeName", type = "String", desc = "SQS Queue Attribute" /*Add Description here*/),
            @FunctionParamDescriptor(name = "regionName", type = "String", desc = "AWS Region" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsAccessKey", type = "String", desc = "AWS Access Key" /*Add Description here*/),
            @FunctionParamDescriptor(name = "awsSecretKey", type = "String", desc = "AWS Secret Key" /*Add Description here*/),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "" /*Add Description here*/),
        version = "1.0", /*Add Version here*/
        see = "",
        mapper = @BEMapper(),
        description = "Get a Queue attribute" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String attribute = SQS.getQueueAttribute(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String getQueueAttribute(String endpoint, String queueURL, String attributeName, String regionName, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, awsAccessKey, awsSecretKey);
            return getQueueAttribute(client, queueURL, attributeName);

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    @BEFunction(
        name = "getQueueAttributeWithRoleARN",
        signature = "String getQueueAttributeWithRoleARN(String endpoint, String queueURL, String attributeName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "attributeName", type = "String", desc = "SQS Queue Attribute" /*Add Description here*/),
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
        description = "Get a Queue attribute" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String attribute = SQS.getQueueAttributesWithRoleARN(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"...\", \"...\");\r\n"
    )
    public static String getQueueAttributeWithRoleARN(String endpoint, String queueURL, String attributeName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, roleARN, roleSessionName, duration, awsAccessKey, awsSecretKey);
            return getQueueAttribute(client, queueURL, attributeName);

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @BEFunction(
        name = "getQueueAttributeWithSAML",
        signature = "String getQueueAttributeWithSAML(String endpoint, String queueURL, String attributeName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)",
        params = {
            @FunctionParamDescriptor(name = "endpoint", type = "String", desc = "AWS SQS Service endpoint, set to null to use default SQS Service address" /*Add Description here*/),
            @FunctionParamDescriptor(name = "queueURL", type = "String", desc = "SQS Queue URL" /*Add Description here*/),
            @FunctionParamDescriptor(name = "attributeName", type = "String", desc = "SQS Queue Attribute" /*Add Description here*/),
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
        description = "Get a Queue attribute" /*Add Description here*/,
        cautions = "none",
        fndomain = {ACTION, BUI},
        example = "String attribute = SQS.getQueueAttributeWithSAML(null,\"https://sqs.eu-west-1.amazonaws.com/01234567890123/test-queue\", \"ApproximateNumberOfMessages\", \"...\", \"...\");\r\n"
    )
    public static String getQueueAttributeWithSAML(String endpoint, String queueURL, String attributeName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws RuntimeException {

        try {
            AmazonSQS client = createAmazonSQSClient(endpoint, regionName, idpName, idpEntryUrl, idpUsername, idpPassword, awsRole, duration);
            return getQueueAttribute(client, queueURL, attributeName);

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    public static Map<String,String> getQueueAttributes(AmazonSQS client, String queueURL) throws Exception {

        try {
            GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest()
                .withAttributeNames(QueueAttributeName.All.toString())
                .withQueueUrl(queueURL);
            GetQueueAttributesResult result = client.getQueueAttributes(getQueueAttributesRequest);
            Map<String, String> attributes = result.getAttributes();
            return attributes;

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String getQueueAttribute(AmazonSQS client, String queueURL, String attributeName) throws Exception {

        try {
            GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest()
                .withAttributeNames(attributeName)
                .withQueueUrl(queueURL);
            GetQueueAttributesResult result = client.getQueueAttributes(getQueueAttributesRequest);
            String attribute = result.getAttributes().get(attributeName);
            return attribute;

        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static AmazonSQS createAmazonSQSClient(String endpoint, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey) throws Exception {

        Credentials creds = createCredentialsWithAssumeRole(roleARN, roleSessionName, regionName, duration, awsAccessKey, awsSecretKey);

        BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
                creds.getAccessKeyId(),
                creds.getSecretAccessKey(),
                creds.getSessionToken());


        if (endpoint != null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration endpointConfiguration
                = new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();

            return client;


        } else {

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
                .withRegion(regionName)
                .build();

            return client;
        }

    }

    public static AmazonSQS createAmazonSQSClient(String endpoint, String regionName, String awsAccessKey, String awsSecretKey) throws Exception {

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);


        if (endpoint != null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration endpointConfiguration
                = new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();

            return client;

        } else {

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(regionName)
                .build();

            return client;
        }

    }

    public static AmazonSQS createAmazonSQSClient(String endpoint, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) throws Exception {

        Credentials credentials = SAMLUtils.createCredentialsWithSAML(idpName,  idpEntryUrl,  idpUsername,  idpPassword, regionName, awsRole, duration, false, null ,  null,  null,  0);

        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                credentials.getAccessKeyId(),
                credentials.getSecretAccessKey(),
                credentials.getSessionToken());

        if (endpoint != null && endpoint.length() != 0) {

            AwsClientBuilder.EndpointConfiguration endpointConfiguration
                = new AwsClientBuilder.EndpointConfiguration(endpoint, regionName);

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();

            return client;

        } else {

            AmazonSQS client = AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withRegion(regionName)
                .build();

            return client;
        }


    }

}
