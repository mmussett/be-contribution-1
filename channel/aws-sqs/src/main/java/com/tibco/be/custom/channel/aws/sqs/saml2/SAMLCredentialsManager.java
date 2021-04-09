/*
 * Copyright © 2020. TIBCO Software Inc.
 * This file is subject to the license terms contained
 * in the license file that is distributed with this file.
 */
package com.tibco.be.custom.channel.aws.sqs.saml2;

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
import com.tibco.be.custom.channel.aws.sqs.basiccredentials.BasicCredentialsManager;
import com.tibco.be.custom.channel.aws.sqs.saml2.idpimpl.GenericIdpAuthHandler;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;

import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SAMLCredentialsManager {

	private static final Logger logger = LogManagerFactory
			.getLogManager().getLogger(SAMLCredentialsManager.class);

	private static Map<SAMLContext, SAMLCredential> credentialsMap = new HashMap<SAMLContext, SAMLCredential>();

	
	public static Credentials getCredentials(SAMLContext context, SSLSocketFactory sslSocketFactory, ClientConfiguration clientConfiguration) throws Exception{


		if(!credentialsMap.containsKey(context) || isExpired(context))
		{
			logger.log(Level.DEBUG,"Creating new SAML Context object");

			SAMLCredential samlCred = new SAMLCredential();
			samlCred.setCredentials(generateAwsCredentialsViaSAML(context, sslSocketFactory, clientConfiguration ));
			samlCred.setLastLoginTime(System.currentTimeMillis());
			credentialsMap.put(context, samlCred);
			return credentialsMap.get(context).getCredentials();
		}

		logger.log(Level.DEBUG,"Using existing SAML Context object");

		return credentialsMap.get(context).getCredentials();
	}
	
	
	public static boolean isExpired(SAMLContext context){

		if (!credentialsMap.containsKey(context)) {
			logger.log(Level.DEBUG,"Context not found in map");
			return true;
		}

		int tokenExpirationDuration = context.getTokenExpirationDuration();


		//Renew token mins before expiry
		int minBeforeExpiry = 2;
		if(System.getProperty("com.tibco.be.awssqs.minbeforeexpiry") != null && System.getProperty("com.tibco.be.awssqs.minbeforeexpiry").length()>0)
			minBeforeExpiry = Integer.parseInt(System.getProperty("com.tibco.bw.awssqs.minbeforeexpiry"));
		//Use default value 60 if 0
		tokenExpirationDuration = (tokenExpirationDuration > 0 ? tokenExpirationDuration : 60);
		
		if((tokenExpirationDuration - minBeforeExpiry) > 0)
			tokenExpirationDuration = tokenExpirationDuration - minBeforeExpiry;

			long intervalSinceLastRefresh = System.currentTimeMillis() - credentialsMap.get(context).getLastLoginTime();

			logger.log(Level.DEBUG,"Token refresh interval remaining : " + ((tokenExpirationDuration * 60 * 1000) - intervalSinceLastRefresh)+" ms");

			if (intervalSinceLastRefresh >= tokenExpirationDuration * 60 * 1000) {
				logger.log(Level.DEBUG,"SAML context token has expired");
				return true;
    	} else {
				logger.log(Level.DEBUG,"SAML context token has not expired");
      	return false;
			}
	}
	
	private static Credentials generateAwsCredentialsViaSAML(SAMLContext context, SSLSocketFactory sslSocketFactory, ClientConfiguration clientConfiguration) throws Exception{
		
		String idpName = context.getIdProviderType();
		String idpEntryUrl = context.getIdpEntryUrl();
		String idpUsername = context.getIdpUsername();
		String idpPassword = context.getIdpPassword();
		String regionName = context.getRegionName();

		String awsRole = context.getAwsRole();
		int tokenExpirationDuration = context.getTokenExpirationDuration();
		
		//proxy settings
		boolean isProxy = false;
		String proxyUsername = null;
		String proxyPassword = null;
		Proxy proxy = null;

		boolean isIdpUseProxy = context.getIdpUseProxy();
		if(isIdpUseProxy){
			isProxy = true;
			proxyUsername = context.getProxyUserName();
			proxyPassword = context.getProxyPassword();

			SocketAddress addr = new InetSocketAddress((String) context.getProxyHost(), context.getProxyPort());
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
		Credentials cred = getCreds(roleARN, principalARN, assertion, tokenExpirationDuration, clientConfiguration, regionName, context);

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

	/*
	 * Generates AWS temporary credentials 
	 */

	public static Credentials getCreds(String roleARN, String principalARN, String samlAssertion, int tokenExpirationDuration, ClientConfiguration clientConfiguration, String regionName, SAMLContext context){
		
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
			logger.log(Level.DEBUG,"Using region specific sts endpoint - "+ "sts."+ regionName +".amazonaws.com");
		}
		else {
			logger.log(Level.DEBUG,"Using global sts endpoint");
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
