package com.tibco.be.custom.aws.services.sqs;

import static com.tibco.be.custom.aws.services.sqs.Queue.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * QueueAttributes Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Mar 17, 2021</pre>
 */
public class QueueIntegrationTest {

    final static String endpoint = "https://s3.eu-west-1.amazonaws.com";
    final static String regionName = "eu-west-1";

    // Test data for Basic
    final static String queueURLBasic = "https://sqs.eu-west-1.amazonaws.com/747829735052/test-queue";
    static String awsAccessKeyBasic;
    static String awsSecretKeyBasic;

    // Test data for Assume Role
    final static String queueURLAssumeRole = "https://sqs.eu-west-1.amazonaws.com/696093067220/test-queue";
    final static String roleSessionName = "BE";
    final static int duration1 = 3600;

    static String roleARNAssumeRole;
    static String awsAccessKeyAssumeRole;
    static String awsSecretKeyAssumeRole;


    // Test data for SAML
    final static String queueURLSAML = "https://sqs.eu-west-1.amazonaws.com/747829735052/test-queue";
    final static int durationSAML = 60;
    static String idpUsernameSAML;
    static String idpPasswordSAML;
    static String idpNameSAML;
    static String idpEntryUrlSAML;
    static String awsRoleSAML;

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


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testGetQueueAttributes() throws Exception {

        Map attributes = (Map<String,String>) getQueueAttributes(endpoint, queueURLBasic, regionName, awsAccessKeyBasic,
            awsSecretKeyBasic);

        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());
    }


    @Test
    public void testGetQueueAttributesWithRoleARN() throws Exception {

        Map attributes = (Map<String,String>)  getQueueAttributesWithRoleARN(endpoint, queueURLAssumeRole, regionName,
            roleARNAssumeRole, roleSessionName, duration1, awsAccessKeyAssumeRole,
            awsSecretKeyAssumeRole);
        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

    }

    @Test
    public void testGetQueueAttributesWithSAML() throws Exception {

        Map attributes =  (Map<String,String>)  getQueueAttributesWithSAML(endpoint, queueURLSAML, regionName,
            idpNameSAML, idpEntryUrlSAML,
            idpUsernameSAML, idpPasswordSAML, awsRoleSAML,
            durationSAML);
        Assert.assertNotNull(attributes);
        Assert.assertFalse(attributes.isEmpty());

    }

    @Test
    public void testGetQueueAttribute() throws Exception {

        String attribute = getQueueAttribute(endpoint, queueURLBasic, "ApproximateNumberOfMessages", regionName, awsAccessKeyBasic,
            awsSecretKeyBasic);

        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());
    }


    @Test
    public void testGetQueueAttributeWithRoleARN() throws Exception {

        String attribute =  getQueueAttributeWithRoleARN(endpoint, queueURLAssumeRole, "ApproximateNumberOfMessages", regionName,
            roleARNAssumeRole, roleSessionName, duration1, awsAccessKeyAssumeRole,
            awsSecretKeyAssumeRole);
        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

    }

    @Test
    public void testGetQueueAttributeWithSAML() throws Exception {

        String attribute =  getQueueAttributeWithSAML(endpoint, queueURLSAML, "ApproximateNumberOfMessages", regionName,
            idpNameSAML, idpEntryUrlSAML,
            idpUsernameSAML, idpPasswordSAML, awsRoleSAML,
            durationSAML);
        Assert.assertNotNull(attribute);
        Assert.assertFalse(attribute.isEmpty());

    }

} 
