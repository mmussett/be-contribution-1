package com.tibco.be.custom.aws.services.sqs;

import static com.tibco.be.custom.aws.services.sqs.Queue.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueueUnitTest {

    private static DockerImageName localStackImage =
        DockerImageName.parse("localstack/localstack:latest");

    @Container
    private static LocalStackContainer localStackContainer = new
        LocalStackContainer(localStackImage)
        .withServices(Service.SQS);

    @BeforeEach
    void prerequisitesExist() {
        assertNotNull(localStackContainer);
        assertTrue(localStackContainer.isRunning());
    }


    @BeforeAll
    public static void setup() {
    }


    @BeforeEach
    public void before() throws Exception {
    }

    @AfterEach
    public void after() throws Exception {
    }


    @Test
    @Order(1)
    public void testGetQueueAttributes() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        Map<String, String> attributes =
            (Map<String,String>) getQueueAttributes(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                localStackContainer.getRegion());

        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

        int count = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }

    @Test
    @Order(2)
    public void testGetQueueAttribute() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        String attribute =
            getQueueAttribute(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                "ApproximateNumberOfMessages",
                localStackContainer.getRegion());

        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

        int count = Integer.parseInt(attribute);

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }


    @Test
    @Order(3)
    public void testGetQueueAttributesWithAccessKeySecret() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        Map<String, String> attributes =
            (Map<String,String>) getQueueAttributesWithAccessKeySecret(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                localStackContainer.getRegion(),
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey());

        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

        int count = Integer.parseInt(attributes.get("ApproximateNumberOfMessages"));

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }

    @Test
    @Order(4)
    public void testGetQueueAttributeWithAccessKeySecret() throws Exception {


        String queueName = generateAlphaNumericRandomString(32);

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

        AmazonSQS client = AmazonSQSClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
            .build();


        CreateQueueResult queue = client
            .createQueue(queueName);


        String queueURL = queue.getQueueUrl();

        client.sendMessage(queueURL, "test message");

        String attribute =
            getQueueAttributeWithAccessKeySecret(
                localStackContainer.getEndpointOverride(Service.SQS).toASCIIString(),
                queueURL,
                "ApproximateNumberOfMessages",
                localStackContainer.getRegion(),
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey());

        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

        int count = Integer.parseInt(attribute);

        Assert.assertEquals("Expecting ApproximateNumberOfMessages == 1", count, 1);

        client.shutdown();

    }


    // function to generate a random string of length n
    private static String generateAlphaNumericRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "0123456789abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

} 
