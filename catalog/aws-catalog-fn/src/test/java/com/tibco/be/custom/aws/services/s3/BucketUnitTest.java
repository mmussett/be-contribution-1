package com.tibco.be.custom.aws.services.s3;

import static com.tibco.be.custom.aws.services.s3.Bucket.createBucket;
import static com.tibco.be.custom.aws.services.s3.Bucket.deleteBucket;
import static com.tibco.be.custom.aws.services.s3.Bucket.deleteObject;
import static com.tibco.be.custom.aws.services.s3.Bucket.doesBucketExist;
import static com.tibco.be.custom.aws.services.s3.Bucket.doesObjectExist;
import static com.tibco.be.custom.aws.services.s3.Bucket.generatePreSignedUrl;
import static com.tibco.be.custom.aws.services.s3.Bucket.getObject;
import static com.tibco.be.custom.aws.services.s3.Bucket.putObject;
import static com.tibco.be.custom.aws.services.s3.Bucket.putObjectWithSSE_S3;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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
public class BucketUnitTest {

  private static final DockerImageName localStackImage =
      DockerImageName.parse("localstack/localstack:latest");

  @Container
  private static final LocalStackContainer localStackContainer = new
      LocalStackContainer(localStackImage)
      .withServices(Service.S3);

  @BeforeEach
  void prerequisitesExist() {
    assertNotNull(localStackContainer);
    assertTrue(localStackContainer.isRunning());
  }


  private static AmazonS3 amazonS3;


  // Test for KMS & CSE
  static String kmsKeyID;



  @BeforeAll
  public static void setup() {

    try {

      kmsKeyID = UUID.randomUUID().toString();

      BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(localStackContainer.getAccessKey(), localStackContainer.getSecretKey());

      EndpointConfiguration endpointConfiguration = localStackContainer
          .getEndpointConfiguration(Service.SQS);

      amazonS3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .withEndpointConfiguration(endpointConfiguration)
        .build();


    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }

  }

  @BeforeEach
  public void before() {
  }

  @AfterEach
  public void after() {}

  @Test
  void testBucketExists() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    boolean exists =
        doesBucketExist(amazonS3, bucketName);
    assertTrue(exists);

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  void testCreateBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(amazonS3, bucketName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  void testDeleteBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(amazonS3, bucketName);

      assertFalse(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3Object() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              amazonS3,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String content =
          getObject(
              amazonS3, bucketName, objectName);

      assertEquals(content, objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }


  @Test
  public void testDeleteS3Object() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          putObject(
              amazonS3,
              bucketName,
              objectName,
              objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          getObject(
              amazonS3, bucketName, objectName);

      fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExist()  {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(amazonS3, bucketName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExist() {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              amazonS3,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExist(
              amazonS3, bucketName, objectName);

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName );
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3Object() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
          putObject(
              amazonS3,
              bucketName,
              objectName,
              objectContent);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getObject(
              amazonS3, bucketName, objectName);

      assertEquals(content, objectContent);

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

//  @Test
//  public void testPutS3ObjectUsingCSEKMS() throws Exception {
//
//    String bucketName = generateAlphaNumericRandomString(60);
//    String objectName = generateAlphaNumericRandomString(60);
//    String objectContent = generateAlphaNumericRandomString(1024);
//
//    try {
//      createBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      String result =
//          putObjectWithCSE_KMS(
//              amazonS3EncryptionV2,
//              bucketName,
//              objectName,
//              objectContent);
//
//      Assert.assertNotNull(result);
//      Assert.assertFalse(result.isEmpty());
//
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    Thread.sleep(3000);
//
//    try {
//      boolean exists =
//          doesObjectExist(
//              amazonS3, bucketName, objectName);
//
//      Assert.assertTrue(exists);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteObject(amazonS3, bucketName, objectName );
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//  }

//  @Test
//  public void testPutS3ObjectUsingSSEKMS() throws Exception {
//
//    String bucketName = generateAlphaNumericRandomString(60);
//    String objectName = generateAlphaNumericRandomString(60);
//    String objectContent = generateAlphaNumericRandomString(1024);
//
//    try {
//      createBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      String result =
//          putObjectWithSSE_KMS(
//              amazonS3,
//              bucketName,
//              objectName,
//              kmsKeyID,
//              objectContent);
//
//      Assert.assertNotNull(result);
//      Assert.assertFalse(result.isEmpty());
//
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    Thread.sleep(3000);
//
//    try {
//      boolean exists =
//          doesObjectExist(
//              amazonS3, bucketName, objectName );
//
//      Assert.assertTrue(exists);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteObject(
//          amazonS3, bucketName, objectName );
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//
//    try {
//      deleteBucket(amazonS3, bucketName);
//    } catch (RuntimeException e) {
//      Assert.fail(e.getMessage());
//    }
//  }

  @Test
  public void testPutS3ObjectUsingSSES3() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObjectWithSSE_S3(
              amazonS3,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExist(
              amazonS3, bucketName, objectName );

      assertTrue(exists);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName );
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrl()  {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          putObject(
              amazonS3,
              bucketName,
              objectName,
              objectContent);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrl(
              amazonS3,
              bucketName,
              objectName, null, 6000);

      assertNotNull(result);
      assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteObject(
          amazonS3, bucketName, objectName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }

    try {
      deleteBucket(amazonS3, bucketName);
    } catch (RuntimeException e) {
      fail(e.getMessage());
    }
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
