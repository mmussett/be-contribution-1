package com.tibco.be.custom.aws.services.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithSAMLRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithSAMLResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.tibco.be.custom.aws.services.saml2.IdpAuthHandler;
import com.tibco.be.custom.aws.services.saml2.IdpEnum;
import com.tibco.be.custom.aws.services.saml2.SAMLService;
import com.tibco.be.custom.aws.services.saml2.idpimpl.GenericIdpAuthHandler;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;

public class SAMLUtils {

    public static Credentials createCredentialsWithSAML(String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String regionName, String awsRole, int duration, boolean useProxy, String proxyUsername , String proxyPassword, String proxyHost, int proxyPort) throws Exception{

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        ClientConfiguration clientConfiguration = new ClientConfiguration();

        //proxy settings
        boolean isProxy = false;
        Proxy proxy = null;

        if(useProxy){
            isProxy = true;
            SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
            proxy = new Proxy(Proxy.Type.HTTP, addr);
        }


        //get saml assertion from idp
        IdpAuthHandler idpAuth = new GenericIdpAuthHandler();
        String assertion = idpAuth.generateSAMLAssertion(IdpEnum.getIdpByName(idpName), idpEntryUrl, idpUsername, idpPassword, false, isProxy, proxy, proxyUsername, proxyPassword, sslSocketFactory);

        byte[] decodedAssertionBytes = DatatypeConverter.parseBase64Binary(assertion);
        String decodedAssertion =  new String(decodedAssertionBytes, "UTF-8");

        //parse saml assertion and extract role arn
        SAMLService samlSvc = SAMLService.getInstance();
        //Assertion assertionObj = samlSvc.parseSAMLResponse(decodedAssertion);
        List<String> attr = samlSvc.getRoleAttributeValues(samlSvc.parseSAMLResponse(decodedAssertion), "https://aws.amazon.com/SAML/Attributes/Role");

        //find role ARN in SAML assertion
        String arn = getARN(attr, awsRole);

        String roleARN = null;
        String principalARN = null;
        for(String arnInfo : arn.split(",")){
            if(arnInfo.contains(":role/"+awsRole))
                roleARN = arnInfo;

            if(arnInfo.contains(":saml-provider/"))
                principalARN = arnInfo;
        }

        //generate AWS credentials
        Credentials cred = getCreds(roleARN, principalARN, assertion, duration, clientConfiguration, regionName);

        return cred;
    }



    private static String getARN(List<String> attrLst, String roleName) throws Exception{
        for(String attr : attrLst){
            if(findRole(attr,roleName))
            {
                return attr;
            }
        }
        throw new Exception("Invalid AWS role. Role not found in SAML assertion.");
    }

    private static AWSCredentialsProvider createCredentialsProvider(Credentials credentials) {

        BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                credentials.getAccessKeyId(),
                credentials.getSecretAccessKey(),
                credentials.getSessionToken());

        return (AWSCredentialsProvider) new AWSStaticCredentialsProvider(sessionCredentials);
    }

    /*
     * Generates AWS temporary credentials
     */

    public static Credentials getCreds(String roleARN, String principalARN, String samlAssertion, int tokenExpirationDuration, ClientConfiguration clientConfiguration, String regionName){

        AssumeRoleWithSAMLRequest assumeRoleSAMLReq = new AssumeRoleWithSAMLRequest();

        assumeRoleSAMLReq.setRoleArn(roleARN);
        assumeRoleSAMLReq.setPrincipalArn(principalARN);
        assumeRoleSAMLReq.setSAMLAssertion(samlAssertion);

        //if duration is greater than 0 use it else aws defaults it to 60 mins
        if(tokenExpirationDuration > 0)
            assumeRoleSAMLReq.setDurationSeconds(tokenExpirationDuration * 60);

        BasicAWSCredentials basicCreds=new BasicAWSCredentials("", "");
        AWSSecurityTokenService awsSTS =new AWSSecurityTokenServiceClient(basicCreds, clientConfiguration);
        if(System.getProperty("com.tibco.aws.useregionalendpoint") != null && Boolean.valueOf(System.getProperty("com.tibco.aws.useregionalendpoint")) == true)
        {
            awsSTS.setRegion(RegionUtils.getRegion(regionName));
            awsSTS.setEndpoint("sts."+ regionName +".amazonaws.com");
            //logger.log(Level.DEBUG,"Using region specific sts endpoint - "+ "sts."+ regionName +".amazonaws.com");
        }
        else {
            //logger.log(Level.DEBUG,"Using global sts endpoint");
        }

        AssumeRoleWithSAMLResult assumeRoleSAMLResult = awsSTS.assumeRoleWithSAML(assumeRoleSAMLReq);


        return assumeRoleSAMLResult.getCredentials();
    }


    /*
     * Search AWS role name in ARN
     */

    private static boolean findRole(String arn, String role){
        for(String roleARN : arn.split(",")){
            if(roleARN.contains(":role/")){
                if(roleARN.split("/")[1].equalsIgnoreCase(role))
                    return true;
            }
        }
        return false;

    }
}
