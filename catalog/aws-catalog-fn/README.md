# AWS Catalog Functions

A set of catalog functions supporting:

* [S3](#s3)
* [SNS](#sns)
* [SQS](#sqs)

## Authentication with AWS

The catalog functions support the following authentication modes:

* Default Credential Chain
* API and Secret Key
* Assume Role using API and Secret Key
* SAML Authentication using ADFS as the IdP

## S3

The S3 catalog functions allow you to manipulate S3 objects and buckets through the following operations:

* Delete a Bucket
  * [S3.deleteBucket](#s3deletebucket)
  * [S3.deleteBucketWithRoleARN](#s3deletebucketwithrolearn)
  * [S3.deleteBucketWithSAML](#s3deletebucketwithsaml)
* Create a Bucket
  * [S3.createBucket](#s3createbucket)
  * [S3.createBucketWithRoleARN](#s3createbucketwithrolearn)
  * [S3.createBucketWithSAML](#s3createbucketwithsaml)
* Does a Bucket Exist?
  * [S3.doesBucketExist](#s3doesbucketexist)
  * [S3.doesBucketExistWithRoleARN](#s3doesbucketexistwithrolearn)
  * [S3.doesBucketExistWithSAML](#s3doesbucketexistwithsaml)
* Does an Object exist in a Bucket?
  * [S3.doesObjectExist](#s3doesobjectexist)
  * [S3.doesObjectExistWithRoleARN](#s3doesobjectexistwithrolearn)
  * [S3.doesObjectExistWithSAML](#s3doesobjectexistwithsaml)
* Delete an Object from a Bucket
  * [S3.deleteS3Object](#s3deletes3object)
  * [S3.deleteS3ObjectWithRoleARN](#s3deletes3objectwithrolearn)
  * [S3.deleteS3ObjectWithSAML](#s3deletes3objectwithsaml)
* Put an Object in to a Bucket
  * [S3.putS3Object](#s3puts3object)
  * [S3.putS3ObjectWithRoleARN](#s3puts3objectwithrolearn)
  * [S3.putS3ObjectWithSAML](#s3puts3objectwithsaml)
* Put an Object in to a Bucket using SSE-S3 encryption
  * [S3.putS3ObjectUsingSSES3](#s3puts3objectusingsses3)
  * [S3.putS3ObjectUsingSSES3WithRoleARN](#s3puts3objectusingsses3withrolearn)
  * [S3.putS3ObjectUsingSSES3WithSAML](#s3puts3objectusingsses3withsaml)
* Put an Object in to a Bucket using SSE-KMS encryption
  * [S3.putS3ObjectUsingSSEKMS](#s3puts3objectusingssekms)
  * [S3.putS3ObjectUsingSSEKMSWithRoleARN](#s3puts3objectusingssekmswithrolearn)
  * [S3.putS3ObjectUsingSSEKMSWithSAML](#s3puts3objectusingssekmswithsaml)
* Put an Object in to a Bucket using CSE-KMS encryption
  * [S3.putS3ObjectUsingCSEKMS](#s3puts3objectusingcsekms)
  * [S3.putS3ObjectUsingCSEKMSWithRoleARN](#s3puts3objectusingcsekmswithrolearn)
  * [S3.putS3ObjectUsingCSEKMSWithSAML](#s3puts3objectusingcsekmswithsaml)
* Get an Object from a Bucket
  * [S3.getS3Object](#s3gets3object)
  * [S3.getS3ObjectWithRoleARN](#s3gets3objectwithrolearn)
  * [S3.getS3ObjectWithSAML](#s3gets3objectwithsaml)
* Generate a Pre-Signed URL for an Object in a Bucket
  * [S3.generatePreSignedUrl](#s3generatepresignedurl)
  * [S3.generatePreSignedUrlWithRoleARN](#s3generatepresignedurlwithrolearn)
  * [S3.generatePreSignedUrlWithSAML](#s3generatepresignedurlwithsaml)

# S3.deleteBucket

Purpose: Delete an S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
void deleteBucket(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| endpoint     | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName   | S3 bucket name to delete                                                 |
| regionName   | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey | AWS Access Key                                                           |
| awsSecretKey | AWS Secret Key                                                           |

Returns:

N/A

### S3.deleteBucketWithRoleARN

Purpose: Delete an S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
void deleteBucketWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```
Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name to delete                                                 |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

N/A

### S3.deleteBucketWithSAML

Purpose: Delete an S3 bucket using SAML authentication mode.

```java
void deleteBucketWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name to delete                                                                            |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |
|             |                                                                                                     |

Returns:

N/A

### S3.createBucket

Purpose: Create an S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String createBucket(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| endpoint     | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName   | S3 bucket name to create                                                 |
| regionName   | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey | AWS Access Key                                                           |
| awsSecretKey | AWS Secret Key                                                           |

Returns:

| Type   | Description            |
|:-------|:-----------------------|
| String | Returns S3 Bucket name |


### S3.createBucketWithRoleARN

Purpose: Create an S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String createBucketWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name to delete                                                 |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description            |
|:-------|:-----------------------|
| String | Returns S3 Bucket name |

### S3.createBucketWithSAML

Purpose: Create an S3 bucket using SAML authentication mode.

```java
void createBucketWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name to create                                                                            |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description            |
|:-------|:-----------------------|
| String | Returns S3 Bucket name |

### S3.doesBucketExist

Purpose: Does a S3 bucket exist using API Key and Secret authentication mode.

Function Signature:

```java
boolean doesBucketExist(String endpoint, String bucketName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| endpoint     | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName   | S3 bucket name to check                                                  |
| regionName   | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey | AWS Access Key                                                           |
| awsSecretKey | AWS Secret Key                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the bucket exists, else false |


### S3.doesBucketExistWithRoleARN

Purpose: Does a S3 bucket exist using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
boolean doesBucketExistWithRoleARN(String endpoint, String bucketName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name to check                                                  |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the bucket exists, else false |


### S3.doesBucketExistWithSAML

Purpose: Does a S3 bucket exist using SAML authentication mode.

```java
void doesBucketExistWithSAML(String endpoint, String bucketName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name to check                                                                             |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the bucket exists, else false |

### S3.doesObjectExist

Purpose: Does a S3 object exist in a bucket using API Key and Secret authentication mode.

Function Signature:

```java
boolean doesObjectExist(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| endpoint     | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName   | S3 bucket name                                                           |
| objectName   | S3 Object name to check                                                  |
| regionName   | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey | AWS Access Key                                                           |
| awsSecretKey | AWS Secret Key                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the object exists, else false |


### S3.doesObjectExistWithRoleARN

Purpose: Does a S3 object exist in a bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
boolean doesObjectExistWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name to check                                                  |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the object exists, else false |


### S3.doesObjectExistWithSAML

Purpose: Does a S3 object exist in a bucket using SAML authentication mode.

```java
void doesObjectExistWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name                                                                                      |
| objectName  | S3 object name to check                                                                             |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type    | Description                                   |
|:--------|:----------------------------------------------|
| Boolean | Returns true if the object exists, else false |


### S3.deleteS3Object

Purpose: Delete an S3 object in a bucket using API Key and Secret authentication mode.

Function Signature:

```java
void deleteS3Object(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| endpoint     | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName   | S3 bucket name                                                           |
| objectName   | S3 Object name to delete                                                 |
| regionName   | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey | AWS Access Key                                                           |
| awsSecretKey | AWS Secret Key                                                           |

Returns:

N/A

### S3.deleteS3ObjectWithRoleARN

Purpose: Delete an S3 object in a bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
void deleteS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name to delete                                                 |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

N/A


### S3.deleteS3ObjectWithSAML

Purpose: Delete an S3 object in a bucket using SAML authentication mode.

```java
void deleteS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name                                                                                      |
| objectName  | S3 object name to delete                                                                            |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

N/A



### S3.putS3Object

Purpose: Put an object in to S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String putS3Object(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments      | Purpose                                                                  |
|:---------------|:-------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName     | S3 bucket name                                                           |
| objectName     | S3 Object name                                                 |
| objectContents | Object contents to put                                                   |
| regionName     | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey   | AWS Access Key                                                           |
| awsSecretKey   | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectWithRoleARN

Purpose: Put an object in to S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name                                                  |
| objectContents  | Object contents to put                                                   |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectWithSAML


Purpose: Put an object in to S3 bucket using SAML authentication mode.

```java
String putS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name                                                                                      |
| objectName  | S3 object name                                                                            |
| objectContents | Object contents to put                                                   |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSES3

Purpose: Put an SSE-S3 encrypted object in to S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingSSES3(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String awsAccessKey, String awsSecretKey) 
```

Args:

| Arguments      | Purpose                                                                  |
|:---------------|:-------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName     | S3 bucket name                                                           |
| objectName     | S3 Object name                                                 |
| objectContents | Object contents to put                                                   |
| regionName     | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey   | AWS Access Key                                                           |
| awsSecretKey   | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSES3WithRoleARN

Purpose: Put an SSE-S3 encrypted object in to S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingSSES3WithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name                                                  |
| objectContents  | Object contents to put                                                   |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSES3WithSAML

Purpose: Put an SSE-S3 encrypted object in to S3 bucket using SAML authentication mode.

```java
String putS3ObjectUsingSSES3ithSAML(String endpoint, String bucketName, String objectName, String objectContent, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments      | Purpose                                                                                             |
|:---------------|:----------------------------------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName     | S3 bucket name                                                                                      |
| objectName     | S3 object name                                                                                      |
| objectContents | Object contents to put                                                                              |
| regionName     | AWS region e.g. "eu-west-1"                                                                         |
| idpName        | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl    | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername    | IdP Username to authenticate                                                                        |
| idpPassword    | IdP Password to authenticate                                                                        |
| awsRole        | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration       | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSEKMS

Purpose: Put an SSE-KMS encrypted object in to S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingSSEKMS(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments      | Purpose                                                                  |
|:---------------|:-------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName     | S3 bucket name                                                           |
| objectName     | S3 Object name                                                           |
| objectContents | Object contents to put                                                   |
| kmsKeyID       | The KMS Key ID to encrypt with                                           |
| regionName     | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey   | AWS Access Key                                                           |
| awsSecretKey   | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSEKMSWithRoleARN

Purpose: Put an SSE-KMS encrypted object in to S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingSSEKMSWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments              | Purpose                                                                  |
|:-----------------------|:-------------------------------------------------------------------------|
| endpoint               | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName             | S3 bucket name                                                           |
| objectName             | S3 object name                                                           |
| objectContents         | Object contents to put                                                   |
| kmsKeyID               | The KMS Key ID to encrypt with                                                                         |
| regionName             | AWS region e.g. "eu-west-1"                                              |
| roleARN                | The role ARN to assume                                                   |
| roleSessionName        | The name of the role session                                             |
| duration               | The duration of the short-lived STS token                                |
| awsAccessKey           | AWS Access Key                                                           |
| awsSecretKey           | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingSSEKMSWithSAML

Purpose: Put an SSE-KMS encrypted object in to S3 bucket using SAML authentication mode.

```java
String putS3ObjectUsingSSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```
Args:

| Arguments      | Purpose                                                                                             |
|:---------------|:----------------------------------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName     | S3 bucket name                                                                                      |
| objectName     | S3 object name                                                                                      |
| objectContents | Object contents to put                                                                              |
| kmsKeyID       | The KMS Key ID to encrypt with                                                                      |
| regionName     | AWS region e.g. "eu-west-1"                                                                         |
| idpName        | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl    | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername    | IdP Username to authenticate                                                                        |
| idpPassword    | IdP Password to authenticate                                                                        |
| awsRole        | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration       | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingCSEKMS


Purpose: Put an CSE-KMS encrypted object in to S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingCSEKMS(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments      | Purpose                                                                  |
|:---------------|:-------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName     | S3 bucket name                                                           |
| objectName     | S3 Object name                                                           |
| objectContents | Object contents to put                                                   |
| kmsKeyID       | The KMS Key ID to encrypt with                                           |
| regionName     | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey   | AWS Access Key                                                           |
| awsSecretKey   | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingCSEKMSWithRoleARN

Purpose: Put an CSE-KMS encrypted object in to S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String putS3ObjectUsingCSEKMSWithRoleARN(String endpoint, String bucketName, String objectName, String objectContent, String kmsKeyID, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments              | Purpose                                                                  |
|:-----------------------|:-------------------------------------------------------------------------|
| endpoint               | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName             | S3 bucket name                                                           |
| objectName             | S3 object name                                                           |
| objectContents         | Object contents to put                                                   |
| kmsKeyID               | The KMS Key ID to encrypt with                                                                         |
| regionName             | AWS region e.g. "eu-west-1"                                              |
| roleARN                | The role ARN to assume                                                   |
| roleSessionName        | The name of the role session                                             |
| duration               | The duration of the short-lived STS token                                |
| awsAccessKey           | AWS Access Key                                                           |
| awsSecretKey           | AWS Secret Key                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |


### S3.putS3ObjectUsingCSEKMSWithSAML

Purpose: Put an CSE-KMS encrypted object in to S3 bucket using SAML authentication mode.

```java
String putS3ObjectUsingCSEKMSWithSAML(String endpoint, String bucketName, String objectName, String objectContent,  String kmsKeyID, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments      | Purpose                                                                                             |
|:---------------|:----------------------------------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName     | S3 bucket name                                                                                      |
| objectName     | S3 object name                                                                                      |
| objectContents | Object contents to put                                                                              |
| kmsKeyID       | The KMS Key ID to encrypt with                                                                      |
| regionName     | AWS region e.g. "eu-west-1"                                                                         |
| idpName        | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl    | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername    | IdP Username to authenticate                                                                        |
| idpPassword    | IdP Password to authenticate                                                                        |
| awsRole        | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration       | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                |
|:-------|:---------------------------|
| String | Returns the S3 object ETag |



### S3.getS3Object

Purpose: Get an object from S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String getS3Object(String endpoint, String bucketName, String objectName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments      | Purpose                                                                  |
|:---------------|:-------------------------------------------------------------------------|
| endpoint       | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName     | S3 bucket name                                                           |
| objectName     | S3 Object name                                                 |
| regionName     | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey   | AWS Access Key                                                           |
| awsSecretKey   | AWS Secret Key                                                           |

Returns:

| Type   | Description                   |
|:-------|:------------------------------|
| String | Returns the S3 object content |


### S3.getS3ObjectWithRoleARN

Purpose: Get an object from S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String getS3ObjectWithRoleARN(String endpoint, String bucketName, String objectName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name                                                  |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description                   |
|:-------|:------------------------------|
| String | Returns the S3 object content |


### S3.getS3ObjectWithSAML


Purpose: Get an object from S3 bucket using  SAML authentication mode.

```java
String getS3ObjectWithSAML(String endpoint, String bucketName, String objectName, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration) 
```

Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName  | S3 bucket name                                                                                      |
| objectName  | S3 object name                                                                            |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                   |
|:-------|:------------------------------|
| String | Returns the S3 object content |


### S3.generatePreSignedUrl

Purpose: Generate pre-signed URL for an object in a S3 bucket using API Key and Secret authentication mode.

Function Signature:

```java
String generatePreSignedUrl(String endpoint, String bucketName, String objectName, long expTimeDuration, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 Object name                                                           |
| expTimeDuration | Pre-signed URL duration                                                  |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description                     |
|:-------|:--------------------------------|
| String | Returns a pre-signed URL string |


### S3.generatePreSignedUrlWithRoleARN

Purpose: Generate pre-signed URL for an object in a S3 bucket using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String generatePreSignedUrlWithRoleARN(String endpoint, String bucketName, String objectName, long expTimeDuration, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                  |
|:----------------|:-------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint |
| bucketName      | S3 bucket name                                                           |
| objectName      | S3 object name                                                           |
| expTimeDuration | Pre-signed URL duration                                                  |
| regionName      | AWS region e.g. "eu-west-1"                                              |
| roleARN         | The role ARN to assume                                                   |
| roleSessionName | The name of the role session                                             |
| duration        | The duration of the short-lived STS token                                |
| awsAccessKey    | AWS Access Key                                                           |
| awsSecretKey    | AWS Secret Key                                                           |

Returns:

| Type   | Description                     |
|:-------|:--------------------------------|
| String | Returns a pre-signed URL string |


### S3.generatePreSignedUrlWithSAML

Purpose: Generate pre-signed URL for an object in a S3 bucket using SAML authentication mode.

```java
String generatePreSignedUrlWithSAML(String endpoint, String bucketName, String objectName, long expTimeDuration, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments       | Purpose                                                                                             |
|:----------------|:----------------------------------------------------------------------------------------------------|
| endpoint        | Override S3 Service endpoint, set to null to use the default S3 endpoint                            |
| bucketName      | S3 bucket name                                                                                      |
| objectName      | S3 object name                                                                                      |
| expTimeDuration | Pre-signed URL duration                                                                             |
| regionName      | AWS region e.g. "eu-west-1"                                                                         |
| idpName         | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl     | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername     | IdP Username to authenticate                                                                        |
| idpPassword     | IdP Password to authenticate                                                                        |
| awsRole         | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration        | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                     |
|:-------|:--------------------------------|
| String | Returns a pre-signed URL string |


---

## SQS

The SQS catalog functions allow you to interact with SQS Queues:


* Get Queue Attributes
  * [SQS.getQueueAttributes](#sqsgetqueueattributes)
  * [SQS.getQueueAttributesWithAccessKeySecret](#sqsgetqueueattributeswithaccesskeysecret)
  * [SQS.getQueueAttributesWithRoleARN](#sqsgetqueueattributeswithrolearn)
  * [SQS.getQueueAttributesWithSAML](#sqsgetqueueattributeswithsaml)
  * [SQS.getQueueAttribute](#sqsgetqueueattribute)
  * [SQS.getQueueAttributeWithAccessKeySecret](#sqsgetqueueattributewithaccesskeyscret)
  * [SQS.getQueueAttributeWithRoleARN](#sqsgetqueueattributewithrolearn)
  * [SQS.getQueueAttributeWithSAML](#sqsgetqueueattributewithsaml)

### SQS.getQueueAttributes

Purpose: Get SQS Queue attributes using default credentials.

Function Signature:

```java
Object getQueueAttributes(String endpoint, String queueURL, String regionName)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL     | SQS Queue URL                                                              |
| regionName   | AWS region e.g. "eu-west-1"                                                |

Returns:

| Type   | Description                                                            |
|:-------|:-----------------------------------------------------------------------|
| Object | Returns a Map<String,String> containing all known SQS Queue attributes |



### SQS.getQueueAttributesWithAccessKeySecret

Purpose: Get SQS Queue attributes using API Key and Secret authentication mode.

Function Signature:

```java
Object getQueueAttributesWithAccessKeySecret(String endpoint, String queueURL, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL     | SQS Queue URL                                                              |
| regionName   | AWS region e.g. "eu-west-1"                                                |
| awsAccessKey | AWS Access Key                                                             |
| awsSecretKey | AWS Secret Key                                                             |

Returns:

| Type   | Description                                                            |
|:-------|:-----------------------------------------------------------------------|
| Object | Returns a Map<String,String> containing all known SQS Queue attributes |


### SQS.getQueueAttributesWithRoleARN

Purpose: Get SQS Queue attributes using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
Object getQueueAttributesWithRoleARN(String endpoint, String queueURL, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                    |
|:----------------|:---------------------------------------------------------------------------|
| endpoint        | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL        | SQS Queue URL                                                             |
| regionName      | AWS region e.g. "eu-west-1"                                                |
| roleARN         | The role ARN to assume                                                     |
| roleSessionName | The name of the role session                                               |
| duration        | The duration of the short-lived STS token                                  |
| awsAccessKey    | AWS Access Key                                                             |
| awsSecretKey    | AWS Secret Key                                                             |

Returns:

| Type   | Description                                                            |
|:-------|:-----------------------------------------------------------------------|
| Object | Returns a Map<String,String> containing all known SQS Queue attributes |


### SQS.getQueueAttributesWithSAML


Purpose: Get SQS Queue attributes using SAML authentication mode.

```java
Object getQueueAttributesWithSAML(String endpoint, String queueURL, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override SQS Service endpoint, set to null to use the default SQS endpoint                          |
| queueURL    | SQS Queue URL                                                                                      |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                                                            |
|:-------|:-----------------------------------------------------------------------|
| Object | Returns a Map<String,String> containing all known SQS Queue attributes |


### SQS.getQueueAttribute

Purpose: Get a SQS Queue attribute using default credentials.

Function Signature:

```java
String getQueueAttribute(String endpoint, String queueURL, String attributeName, String regionName)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL     | SQS Queue URL                                                              |
| attributeName | SQS Queue attribute to get |
| regionName   | AWS region e.g. "eu-west-1"                                                |

Returns:

| Type   | Description                                                                     |
|:-------|:--------------------------------------------------------------------------------|
| String | Returns value of SQS Queue attributes as named by String attributeName argument |



### SQS.getQueueAttributeWithAccessKeySecret

Purpose: Get a SQS Queue attribute using API Key and Secret authentication mode.

Function Signature:

```java
String getQueueAttributeWithAccessKeySecret(String endpoint, String queueURL, String attributeName, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL     | SQS Queue URL                                                              |
| attributeName | SQS Queue attribute to get |
| regionName   | AWS region e.g. "eu-west-1"                                                |
| awsAccessKey | AWS Access Key                                                             |
| awsSecretKey | AWS Secret Key                                                             |

Returns:

| Type   | Description                                                                     |
|:-------|:--------------------------------------------------------------------------------|
| String | Returns value of SQS Queue attributes as named by String attributeName argument |


### SQS.getQueueAttributeWithRoleARN

Purpose: Get a SQS Queue attribute using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String getQueueAttributeWithRoleARN(String endpoint, String queueURL, String attributeName, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                    |
|:----------------|:---------------------------------------------------------------------------|
| endpoint        | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| queueURL        | SQS Queue URL                                                             |
| attributeName | SQS Queue attribute to get |
| regionName      | AWS region e.g. "eu-west-1"                                                |
| roleARN         | The role ARN to assume                                                     |
| roleSessionName | The name of the role session                                               |
| duration        | The duration of the short-lived STS token                                  |
| awsAccessKey    | AWS Access Key                                                             |
| awsSecretKey    | AWS Secret Key                                                             |

Returns:

| Type   | Description                                                                     |
|:-------|:--------------------------------------------------------------------------------|
| String | Returns value of SQS Queue attributes as named by String attributeName argument |


### SQS.getQueueAttributeWithSAML


Purpose: Get a SQS Queue attribute using SAML authentication mode.

```java
String getQueueAttributeWithSAML(String endpoint, String queueURL, String attributeName,  String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override SQS Service endpoint, set to null to use the default SQS endpoint                          |
| queueURL    | SQS Queue URL                                                                                      |
| attributeName | SQS Queue attribute to get |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                                                                     |
|:-------|:--------------------------------------------------------------------------------|
| String | Returns value of SQS Queue attributes as named by String attributeName argument |


---

## SNS

The SNS catalog functions allow you to send text or SMS messages to a pre-defined SNS topic:


* Publish Notification to SNS Topic
  * [SNS.publishNotification](#snspublishnotification)
  * [SNS.publishNotificationWithAccessKeySecret](#snspublishnotificationwithaccesskeysecret)
  * [SNS.publishNotificationWithRoleARN](#snspublishnotificationwithrolearn)
  * [SNS.publishNotificationWithSAML](#snspublishnotificationwithsaml)
* Publish SMS Message to SNS Topic
  * [SNS.publishNotificationSMS](#snspublishnotificationsms)
  * [SNS.publishNotificationSMS](#snspublishnotificationsmswithaccesskeysecret)
  * [SNS.publishNotificationSMSWithRoleARN](#snspublishnotificationsmswithrolearn)
  * [SNS.publishNotificationSMSWithSAML](#snspublishnotificationsmswithsaml)

### SNS.publishNotification

Purpose: Publish a message to SNS Topic using Default Credential Chain mode.

Function Signature:

```java
String publishNotification(String endpoint, String topicARN, String message, String subject, String regionName)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SNS Service endpoint, set to null to use the default SNS endpoint |
| topicARN     | SNS Topic ARN                                                              |
| message      | Notification message to publish                                            |
| subject      | Notification subject to publish                                            |
| regionName   | AWS region e.g. "eu-west-1"                                                |

Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |


### SNS.publishNotificationWithAccessKeySecret

Purpose: Publish a message to SNS Topic using API Key and Secret authentication mode.

Function Signature:

```java
String publishNotificationWithAccessKeySecret(String endpoint, String topicARN, String message, String subject, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments    | Purpose                                                                    |
|:-------------|:---------------------------------------------------------------------------|
| endpoint     | Override SNS Service endpoint, set to null to use the default SNS endpoint |
| topicARN     | SNS Topic ARN                                                              |
| message      | Notification message to publish                                            |
| subject      | Notification subject to publish                                            |
| regionName   | AWS region e.g. "eu-west-1"                                                |
| awsAccessKey | AWS Access Key                                                             |
| awsSecretKey | AWS Secret Key                                                             |

Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |


### SNS.publishNotificationWithRoleARN


Purpose: Publish a message to SNS Topic using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String publishNotificationWithRoleARN(String endpoint, String topicARN, String message, String subject, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments       | Purpose                                                                    |
|:----------------|:---------------------------------------------------------------------------|
| endpoint        | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| topicARN        | SNS Topic ARN                                                              |
| message         | Notification message to publish                                                                           |
| subject         | Notification subject to publish                                                                           |
| regionName      | AWS region e.g. "eu-west-1"                                                |
| roleARN         | The role ARN to assume                                                     |
| roleSessionName | The name of the role session                                               |
| duration        | The duration of the short-lived STS token                                  |
| awsAccessKey    | AWS Access Key                                                             |
| awsSecretKey    | AWS Secret Key                                                             |

Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |



### SNS.publishNotificationWithSAML

Purpose: Publish a message to SNS Topic using SAML authentication mode.

```java
String publishNotificationWithSAML(String endpoint, String topicARN, String message, String subject, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| endpoint    | Override SQS Service endpoint, set to null to use the default SQS endpoint                          |
| topicARN    | SNS Topic ARN                                                                                       |
| message     | Notification message to publish                                                                     |
| subject     | Notification subject to publish                                                                                                    |
| regionName  | AWS region e.g. "eu-west-1"                                                                         |
| idpName     | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername | IdP Username to authenticate                                                                        |
| idpPassword | IdP Password to authenticate                                                                        |
| awsRole     | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration    | The duration of the short-lived STS token                                                           |

Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |



### SNS.publishNotificationSMS


Purpose: Publish a SMS message to SNS Topic using Default Credential mode.

Function Signature:

```java
String publishNotificationSMS(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName)
```

Args:

| Arguments            | Purpose                                                                    |
|:---------------------|:---------------------------------------------------------------------------|
| endpoint             | Override SNS Service endpoint, set to null to use the default SNS endpoint |
| topicARN             | SNS Topic ARN                                                              |
| message              | SMS Message to send                                             |
| smsSenderID          | SMS Sender ID (see AWS docs on sending SMS messages using SNS)                                            |
| smsOriginationNumber | SMS Origination Number (see AWS docs on sending SMS messages using SNS)                                                                             |
| smsMaxPrice          | Maximum Price for SMS message (see AWS docs on sending SMS messages using SNS)                                                                           |
| smsType              | SMS Type (see AWS docs on sending SMS messages using SNS)                                                                           |
| regionName           | AWS region e.g. "eu-west-1"                                                |


Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |



### SNS.publishNotificationSMSWithAccessKeySecret


Purpose: Publish a SMS message to SNS Topic using API Key and Secret authentication mode.

Function Signature:

```java
String publishNotificationSMSWithAccessKeySecret(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments            | Purpose                                                                    |
|:---------------------|:---------------------------------------------------------------------------|
| endpoint             | Override SNS Service endpoint, set to null to use the default SNS endpoint |
| topicARN             | SNS Topic ARN                                                              |
| message              | SMS Message to send                                             |
| smsSenderID          | SMS Sender ID (see AWS docs on sending SMS messages using SNS)                                            |
| smsOriginationNumber | SMS Origination Number (see AWS docs on sending SMS messages using SNS)                                                                             |
| smsMaxPrice          | Maximum Price for SMS message (see AWS docs on sending SMS messages using SNS)                                                                           |
| smsType              | SMS Type (see AWS docs on sending SMS messages using SNS)                                                                           |
| regionName           | AWS region e.g. "eu-west-1"                                                |
| awsAccessKey         | AWS Access Key                                                             |
| awsSecretKey         | AWS Secret Key                                                             |


Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |



### SNS.publishNotificationSMSWithRoleARN


Purpose: Publish a SMS message to SNS Topic using AssumeRole, API Key and Secret authentication mode.

Function Signature:

```java
String publishNotificationSMSWithRoleARN(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String roleARN, String roleSessionName, int duration, String awsAccessKey, String awsSecretKey)
```

Args:

| Arguments            | Purpose                                                                    |
|:---------------------|:---------------------------------------------------------------------------|
| endpoint             | Override SQS Service endpoint, set to null to use the default SQS endpoint |
| topicARN             | SNS Topic ARN                                                              |
| message              | SMS Message to send                                             |
| smsSenderID          | SMS Sender ID (see AWS docs on sending SMS messages using SNS)                                                              |
| smsOriginationNumber | SMS Origination Number (see AWS docs on sending SMS messages using SNS)    |
| smsMaxPrice          | Maximum Price for SMS message (see AWS docs on sending SMS messages using SNS)                                                                           |
| smsType              | SMS Type (see AWS docs on sending SMS messages using SNS)                                                                           |
| regionName           | AWS region e.g. "eu-west-1"                                                |
| roleARN              | The role ARN to assume                                                     |
| roleSessionName      | The name of the role session                                               |
| duration             | The duration of the short-lived STS token                                  |
| awsAccessKey         | AWS Access Key                                                             |
| awsSecretKey         | AWS Secret Key                                                             |

Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |



### SNS.publishNotificationSMSWithSAML

Purpose: Publish a SMS message to SNS Topic using SAML authentication mode.

```java
String publishNotificationSMSWithSAML(String endpoint, String topicARN, String message, String smsSenderID, String smsOriginationNumber, String smsMaxPrice, String smsType, String regionName, String idpName, String idpEntryUrl, String idpUsername, String idpPassword, String awsRole, int duration)
```

Args:

| Arguments            | Purpose                                                                                             |
|:---------------------|:----------------------------------------------------------------------------------------------------|
| endpoint             | Override SQS Service endpoint, set to null to use the default SQS endpoint                          |
| topicARN             | SNS Topic ARN                                                                                       |
| message              | SMS Message to send                                                                                 |
| smsSenderID          | SMS Sender ID (see AWS docs on sending SMS messages using SNS)                                                                                       |
| smsOriginationNumber | SMS Origination Number (see AWS docs on sending SMS messages using SNS)                             |
| smsMaxPrice          | Maximum Price for SMS message (see AWS docs on sending SMS messages using SNS)                      |
| smsType              | SMS Type (see AWS docs on sending SMS messages using SNS)                                           |
| regionName           | AWS region e.g. "eu-west-1"                                                                         |
| idpName              | IdP name - MUST BE 'ADFS'                                                                           |
| idpEntryUrl          | IdP URL e.g. https://mydomain.com/adfs/ls/IdpInitiatedSignOn.aspxs?loginToRp=urn:amazon:webservices |
| idpUsername          | IdP Username to authenticate                                                                        |
| idpPassword          | IdP Password to authenticate                                                                        |
| awsRole              | The AWS Role setup for SAML authentication e.g. ADFS-Dev                                            |
| duration             | The duration of the short-lived STS token                                                           |


Returns:

| Type   | Description                                              |
|:-------|:---------------------------------------------------------|
| String | Returns value of SNS Message ID of the published message |

