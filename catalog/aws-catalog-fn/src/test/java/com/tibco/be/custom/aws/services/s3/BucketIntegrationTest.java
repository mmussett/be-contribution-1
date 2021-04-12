package com.tibco.be.custom.aws.services.s3;

import static com.tibco.be.custom.aws.services.s3.Bucket.*;
import static com.tibco.be.custom.aws.services.s3.Bucket.deleteBucketWithRoleARN;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;


public class BucketIntegrationTest {

  static String endpoint = "https://s3.eu-west-1.amazonaws.com";
  static String regionName = "eu-west-1";
  static final int durationPreSignedUrl = 60;

  // Test data for Basic
  static String awsAccessKeyBasic;
  static String awsSecretKeyBasic;

  // Test data for Assume Role
  static final String roleSessionNameAssumeRole = "BE";
  static String roleARNAssumeRole;
  static String awsAccessKeyAssumeRole;
  static String awsSecretKeyAssumeRole;
  static final int durationAssumeRole = 3600;

  // Test data for SAML
  static final int durationSAML = 60;
  static String idpUsernameSAML;
  static String idpPasswordSAML;
  static String idpNameSAML;
  static String idpEntryUrlSAML;
  static String awsRoleSAML;

  // Test for KMS & CSE
  static String kmsKeyIDBasic;
  static String kmsKeyIDAssumeRole;
  static String kmsKeyIDSAML;


  @BeforeAll
  public static void setup() {

    try {

      Properties props = new Properties();
      InputStream is = new FileInputStream(new File("src/test/resources/my-junit.properties"));

      if (is != null) {
        props.load(is);
      } else {
        throw new FileNotFoundException("property file junit.properties not found");
      }

      endpoint = props.getProperty("endpoint","https://s3.eu-west-1.amazonaws.com");
      regionName = props.getProperty("region","eu-west-1");

      awsAccessKeyBasic = props.getProperty("awsAccessKeyBasic");
      awsSecretKeyBasic = props.getProperty("awsSecretKeyBasic");

      roleARNAssumeRole = props.getProperty("roleARNAssumeRole");
      awsAccessKeyAssumeRole = props.getProperty("awsAccessKeyAssumeRole");
      awsSecretKeyAssumeRole = props.getProperty("awsSecretKeyAssumeRole");

      idpUsernameSAML = props.getProperty("idpUsernameSAML");
      idpPasswordSAML = props.getProperty("idpPasswordSAML");
      idpNameSAML = props.getProperty("idpNameSAML");
      idpEntryUrlSAML = props.getProperty("idpEntryUrlSAML");
      awsRoleSAML = props.getProperty("awsRoleSAML");

      kmsKeyIDBasic = props.getProperty("kmsKeyIDBasic");
      kmsKeyIDAssumeRole = props.getProperty("kmsKeyIDAssumeRole");
      kmsKeyIDSAML = props.getProperty("kmsKeyIDSAML");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @BeforeEach
  public void before() throws Exception {}

  @AfterEach
  public void after() throws Exception {}

  //-------------------------------- Default Credential Chain --------------------------------

  @Test
  void testBucketExists() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(
          endpoint,
          bucketName,
          regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    boolean exists =
        doesBucketExist(endpoint, bucketName, regionName);
    Assert.assertTrue(exists);

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testCreateBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(endpoint, bucketName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testDeleteBucket() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(endpoint, bucketName, regionName);

      Assert.assertFalse(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteS3Object() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3Object(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3Object(endpoint, bucketName, objectName, regionName);

      Assert.fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3Object() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3Object(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3Object(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(objectContent.equals(content));
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExist() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExist(endpoint, bucketName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExist() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3Object(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExist(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3Object() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3Object(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getS3Object(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(objectContent.equals(content));

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingCSEKMS() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingCSEKMS(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDBasic,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExist(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSES3() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSES3(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExist(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSEKMS() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSEKMS(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDBasic,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExist(
              endpoint, bucketName, objectName, regionName);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrl() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3Object(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrl(
              endpoint,
              bucketName,
              objectName,
              durationPreSignedUrl,
              null,
              regionName);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3Object(
          endpoint, bucketName, objectName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucket(endpoint, bucketName, regionName);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  //--------------------------------  Access Key & Secret  --------------------------------

  @Test
  void testBucketExistsWithAccessKeySecret() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithAccessKeySecret(
          endpoint,
          bucketName,
          regionName,
          awsAccessKeyBasic,
          awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    boolean exists =
        doesBucketExistWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    Assert.assertTrue(exists);

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testCreateBucketWithAccessKeySecret() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testDeleteBucketWithAccessKeySecret() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertFalse(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteS3ObjectWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithAccessKeySecret(endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(objectContent.equals(content));
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExistWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExistWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExistWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3ObjectWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getS3ObjectWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(objectContent.equals(content));

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingCSEKMSWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingCSEKMSWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDBasic,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSES3WithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSES3WithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSEKMSWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSEKMSWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDBasic,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithAccessKeySecret(
              endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrlWithAccessKeySecret() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrlWithAccessKeySecret(
              endpoint,
              bucketName,
              objectName,
              durationPreSignedUrl,
              null,
              regionName,
              awsAccessKeyBasic,
              awsSecretKeyBasic);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithAccessKeySecret(
          endpoint, bucketName, objectName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithAccessKeySecret(endpoint, bucketName, regionName, awsAccessKeyBasic, awsSecretKeyBasic);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  //-------------------------------- RoleARN --------------------------------

  @Test
  void testBucketExistsWithRoleARN() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithRoleARN(
              endpoint,
              bucketName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testCreateBucketWithRoleARN() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithRoleARN(
              endpoint,
              bucketName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testDeleteBucketWithRoleARN() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithRoleARN(
              endpoint,
              bucketName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertFalse(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(objectContent.equals(content));
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteS3ObjectWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExistWithRoleARN() throws Exception {
    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithRoleARN(
              endpoint,
              bucketName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExistWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExistWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3ObjectWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(objectContent.equals(content));

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingCSEKMSWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingCSEKMSWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDAssumeRole,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSEKMSWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSEKMSWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDAssumeRole,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);
      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSES3WithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSES3WithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);
      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrlWithRoleARN() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrlWithRoleARN(
              endpoint,
              bucketName,
              objectName,
              durationPreSignedUrl,
              null,
              regionName,
              roleARNAssumeRole,
              roleSessionNameAssumeRole,
              durationAssumeRole,
              awsAccessKeyAssumeRole,
              awsSecretKeyAssumeRole);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithRoleARN(
          endpoint,
          bucketName,
          objectName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithRoleARN(
          endpoint,
          bucketName,
          regionName,
          roleARNAssumeRole,
          roleSessionNameAssumeRole,
          durationAssumeRole,
          awsAccessKeyAssumeRole,
          awsSecretKeyAssumeRole);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }


  //-------------------------------- SAML --------------------------------


  @Test
  void testBucketExistsWithSAML() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithSAML(
              null,
              bucketName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testCreateBucketWithSAML() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithSAML(
              endpoint,
              bucketName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  void testDeleteBucketWithSAML() {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithSAML(
              endpoint,
              bucketName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertFalse(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(objectContent.equals(content));
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteS3ObjectWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String content =
          getS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.fail("Object should have been deleted");

    } catch (RuntimeException e) {
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesBucketExistWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesBucketExistWithSAML(
              endpoint,
              bucketName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testDoesObjectExistWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      boolean exists =
          doesObjectExistWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGetS3ObjectWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      String content =
          getS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(objectContent.equals(content));

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingCSEKMSWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingCSEKMSWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDSAML,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSEKMSWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSEKMSWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              kmsKeyIDSAML,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testPutS3ObjectUsingSSES3WithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectUsingSSES3ithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    Thread.sleep(3000);

    try {
      boolean exists =
          doesObjectExistWithSAML(
              endpoint,
              bucketName,
              objectName,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertTrue(exists);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testGeneratePreSignedUrlWithSAML() throws Exception {

    String bucketName = generateAlphaNumericRandomString(60);
    String objectName = generateAlphaNumericRandomString(60);
    String objectContent = generateAlphaNumericRandomString(1024);

    try {
      createBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          putS3ObjectWithSAML(
              endpoint,
              bucketName,
              objectName,
              objectContent,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      String result =
          generatePreSignedUrlWithSAML(
              endpoint,
              bucketName,
              objectName,
              durationPreSignedUrl,
              null,
              regionName,
              idpNameSAML,
              idpEntryUrlSAML,
              idpUsernameSAML,
              idpPasswordSAML,
              awsRoleSAML,
              durationSAML);

      Assert.assertNotNull(result);
      Assert.assertFalse(result.isEmpty());

    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteS3ObjectWithSAML(
          endpoint,
          bucketName,
          objectName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
    }

    try {
      deleteBucketWithSAML(
          endpoint,
          bucketName,
          regionName,
          idpNameSAML,
          idpEntryUrlSAML,
          idpUsernameSAML,
          idpPasswordSAML,
          awsRoleSAML,
          durationSAML);
    } catch (RuntimeException e) {
      Assert.fail(e.getMessage());
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
