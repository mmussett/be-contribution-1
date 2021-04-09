package com.tibco.be.custom.aws.services.sns;

import static com.tibco.be.custom.aws.services.sns.Notification.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class NotificationIntegrationTest {

  static String endpoint;
  static String regionName;

    final static String message = "Hello, World";
    final static String subject = "my-subject";

  // Test data for Basic
    final static String topicARNBasic = "arn:aws:sns:eu-west-1:747829735052:MyTopic";
    static String awsAccessKeyBasic;
    static String awsSecretKeyBasic;

  // Test data for Assume Role
    final static String topicARNAssumeRole = "arn:aws:sns:eu-west-1:696093067220:MyTopic";
    final static String roleSessionNameAssumeRole = "BE";
    final static int durationAssumeRole = 3600;
    static String roleARNAssumeRole;
    static String awsAccessKeyAssumeRole;
    static String awsSecretKeyAssumeRole;

    // Test data for SAML
    final static String topicSAML = "arn:aws:sns:eu-west-1:747829735052:MyTopic";
    final static int durationSAML = 60;
    static String idpUsernameSAML;
    static String idpPasswordSAML;
    static String idpNameSAML;
    static String idpEntryUrlSAML;
    static String awsRoleSAML;


    // Test data for SMS
    final static String smsSenderID = "Test";
    final static String smsOriginationNumber = null;
    final static String smsType = "Promotional";
    final static String smsMaxPrice = "0.50";


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

      endpoint = props.getProperty("endpoint","https://sns.eu-west-1.amazonaws.com");
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

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

    @Test
    void testPublishNotification() {

       String result =  publishNotification(endpoint,topicARNBasic, message, subject, regionName,
           awsAccessKeyBasic, awsSecretKeyBasic);
       Assert.assertNotNull(result);
       Assert.assertFalse(result.length()==0);
    }

    @Test
    void testPublishNotificationWithRoleARN() {

      String result = publishNotificationWithRoleARN(endpoint,topicARNAssumeRole,  message,  subject,  regionName,
          roleARNAssumeRole, roleSessionNameAssumeRole,
          durationAssumeRole, awsAccessKeyAssumeRole, awsSecretKeyAssumeRole);
      Assert.assertNotNull(result);
      Assert.assertFalse(result.length()==0);
    }

    @Test
    void testPublishNotificationWithSAML() {

      String result = publishNotificationWithSAML(endpoint,topicSAML, message, subject, regionName,
          idpNameSAML, idpEntryUrlSAML,
          idpUsernameSAML, idpPasswordSAML, awsRoleSAML, durationSAML);
      Assert.assertNotNull(result);
      Assert.assertFalse(result.length()==0);
    }

  @Test
  void testPublishNotificationSMS() {

    String result =  publishNotificationSMS(endpoint,topicARNBasic, message, smsSenderID,smsOriginationNumber,smsMaxPrice,smsType,  regionName,
        awsAccessKeyBasic, awsSecretKeyBasic);
    Assert.assertNotNull(result);
    Assert.assertFalse(result.length()==0);
  }

  @Test
  void testPublishNotificationSMSWithRoleARN() {

    String result = publishNotificationSMSWithRoleARN(endpoint,topicARNAssumeRole,  message,  smsSenderID, smsOriginationNumber,   smsMaxPrice,smsType,regionName,
        roleARNAssumeRole, roleSessionNameAssumeRole,
        durationAssumeRole, awsAccessKeyAssumeRole, awsSecretKeyAssumeRole);

    Assert.assertNotNull(result);
    Assert.assertFalse(result.length()==0);
  }

  @Test
  void testPublishNotificationSMSWithSAML() {

    String result = publishNotificationSMSWithSAML(endpoint,topicSAML, message, smsSenderID, smsOriginationNumber, smsMaxPrice,smsType, regionName,
        idpNameSAML, idpEntryUrlSAML,
        idpUsernameSAML, idpPasswordSAML, awsRoleSAML, durationSAML);
    Assert.assertNotNull(result);
    Assert.assertFalse(result.length()==0);
  }
}