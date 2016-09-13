# End user API

This section covers the end user API. It's divided into the following subsections:
<!-- toc -->

## Authentication

Usage of any API using the `/api/*` uri pattern does require authentication of the API user. The authentication is enforced via HTTP Basic Authentication. The username and password for the API can be configured via the following properties:

| Property                                               | Description                                |
|--------------------------------------------------------|--------------------------------------------|
| /token-server/engine/api/basic-authentication/user     | Username used in the authentication header |
| /token-server/engine/api/basic-authentication/password | Password used in the authentication header |

Encrypting the authentication parameters is highly recommended.

## Access Token API

The access token API enables the end user to list the access tokens are used for its resources. The API will also offer the end user the possibility to revoke access for a client to the resources by deleting a specific access token.

### Overview of access tokens

Endpoint: `GET /oauth/api/v1/users/{userId}/tokens`

| Parameter | Description            |
|-----------|------------------------|
| userId    | Identifier of the user |

When no tokens are found because the user does not exist or the user has no valid access tokens a 404 NOT FOUND is returned. When the user does have valid tokens an array with one or more tokens is returned. Each token object has the following attributes:

| Token attribute | Description                                                                                                                                                 |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| id              | UUID identifying the access token.                                                                                                                          |
| client_name     | Name specified for the client that has access to the user's resources via this token.                                                                       |
| device_name     | Name of the device to which this token is granted. (May be not present as this attribute only contains a value in case of a dynamically registered client.) |
| created_at      | Timestamp of the moment the access token was created.                                                                                                       |
| scopes          | String array with scopes that were granted for this access token.                                                                                           |
| type            | The authentication method to be used with this access token.                                                                                                |

Example response
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "tokens": [
    {
      "id": "7d507b7e-6221-4f06-a75e-ef6e6f06d32b",
      "client_name": "Client X",
      "device_name": "my iPad",
      "created_at": 1381322054000,
      "scopes": [
        "email",
        "profile"
      ],
      "type":"DEFAULT"
    },
    {
      "id": "1c05119e-21b2-4905-bc93-8f67790a16d6",
      "client_name": "Client Y",
      "created_at": 1381321302000,
      "scopes": [
        "email"
      ],
      "type":"FINGER_PRINT"
    }
  ]
 }
```

Example error response
```json
{
  "error": "No tokens found"
}
```

### Delete / revoke access token

Endpoint: `DELETE /oauth/api/v1/users/{userId}/tokens/{tokenId}`

| Parameter | Description                    |
|-----------|--------------------------------|
| userId    | Identifier of the user         |
| tokenId   | Identifier of the access token |

## Consent API

The consent API enables the end user to list the consents that are given. The API will also offer the end user the possibility to revoke consent for a client by deleting a specific consent entry.

### Overview of consents

Endpoint: `GET /oauth/api/v1/users/{userId}/consents`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

When no consents are found because the user does not exist or the user does not have consents, a 404 NOT FOUND will be returned. When the user does have consents an array with one or more consents is returned. Each consent object has the following attributes:
 
| Token attribute | Description                                                                                                                                                 |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| id              | UUID identifying the consent.                                                                                                                               |
| client_name     | Name specified for the client that was given consent to request an access grant.                                                                            |
| device_name     | Name of the device to which this token is granted. (May be not present as this attribute only contains a value in case of a dynamically registered client.) |
| scopes          | String array with scopes were the user has given consent for.                                                                                               |

Example result
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "consents": [
    {
      "id": "fc4ef972-7167-4421-aa89-f109be79d7c2",
      "client_name": "Client X",
      "device_name": "My iPad",
      "scopes": [
        "email",
        "profile"
      ]
    },
    {
      "id": "451f5c34-3d03-4ce0-80bd-4676fc0eddf5",
      "client_name": "Client Y",
      "scopes": [
        "email"
      ]
    }
  ]
}
```

Example error response
```json
{
  "error": "No consents found"
}
```

### Delete / revoke consent

Endpoint: `DELETE /oauth/api/v1/users/{userId}/consents/{consentId}`

| Parameter | Description               |
|-----------|---------------------------|
| userId    | Identifier of the user    |
| consentId | Identifier of the consent |

Deleting a consent for a specific user & client will also remove the corresponding Access Grant and Access Token when available.

The API will always return a "HTTP 204 no content" response, independent of the execution result.

Example result
```http
HTTP/1.1 204 No Content
Cache-Control: no-store
Pragma: no-cache
```

## Device API

The device API enables the end user to list the devices that he has (dynamically) registered. The API will also offer the end user the possibility to delete a device.

### Overview of devices

Endpoints: 
- `GET /oauth/api/v1/users/{userId}/devices`
- `GET /oauth/api/v2/users/{userId}/devices`

| Parameter | Description            |
|-----------|------------------------|
| userId    | Identifier of the user |

When no devices are found because the user does not exist or the user does not have any devices, a 404 NOT FOUND will be returned. When the user does have devices an array with one or more devices is returned. Each device object has the following attributes:
(in API version 2, new attributes have been added, they are marked as `v2` in the `Version` column)

| Token attribute | Description                                                                                  | Version  |
|-----------------|----------------------------------------------------------------------------------------------|----------
| id              | UUID identifying the device                                                                  | v1       |
| name            | The device name                                                                              | v1       |
| application     | A web client / group of dynamic clients that use the same configuration                      | v1       |
| platform        | Platform / os of the device                                                                  | v1       |
| created_at      | Timestamp when device was connected; if no device is connected, this parameter stays empty   | v1       |
| token_types     | List of access token types issued for a device (possible values `DEFAULT` and `FINGER_PRINT`)| v2       |
| last_login                    | Date of the last login using a device                                          | v2       |
| mobile_authentication_enabled | `true` if a device is enrolled for mobile authentication                       | v2       |

Example result
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "devices": [
    {
      "id": "fc4ef972-7167-4421-aa89-f109be79d7c2",
      "name": "Device X",
      "application": "application 1",
      "platform": "ANDROID",
      "created at": 1381322054000        
    },
    {
      "id": "451f5c34-3d03-4ce0-80bd-4676fc0eddf5",
      "name": "Device Y",
      "application": "application 2",
      "platform": "IOS"
    }
  ]
}
```

Example error response
```json
{
  "error": "No devices found"
}
```

### Delete / revoke device

Endpoint: 
- `DELETE /oauth/api/v1/users/{userId}/devices/{deviceId}`
- `DELETE /oauth/api/v2/users/{userId}/devices/{deviceId}`

| Parameter | Description              |
|-----------|--------------------------|
| userId    | Identifier of the user   |
| deviceId  | Identifier of the device |

The API will always return a "HTTP 204 no content" response, independent of the execution result.

In API version 2, when there are multiple users (profiles) registered on a device, only data related to the provided user_id will be deleted. 

Example result
```http
HTTP/1.1 204 No Content
Cache-Control: no-store
Pragma: no-cache
```

### Disable finger print

Endpoint: 
- `POST /oauth/api/v2/users/{userId}/devices/{deviceId}/disableFingerprint`

| Parameter | Description              |
|-----------|--------------------------|
| userId    | Identifier of the user   |
| deviceId  | Identifier of the device |

The API will always return a "HTTP 204 no content" response, independent of the execution result.


### Disable mobile authentication

Endpoint: 
- `POST /oauth/api/v2/users/{userId}/devices/{deviceId}/disableMobileAuthentication`

| Parameter | Description              |
|-----------|--------------------------|
| userId    | Identifier of the user   |
| deviceId  | Identifier of the device |

The API will always return a "HTTP 204 no content" response, independent of the execution result.


## Applications API

Via this API the user can manage access to his applications. An application can be a web client or a group of dynamic clients that use the same configuration (e.g. the instances that are installed on a tablet and on a mobile phone are separate dynamic clients but refer to the same application).

### Overview of Applications

Endpoint: `GET /oauth/api/v1/users/{userId}/applications`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

When no applications are found because the user does not exist or the user does not have any applications, a 404 NOT FOUND will be returned. When the user does have applications an array with one or more applications is returned. Each applications object has the following attributes:

| Token attribute | Description                      |
|-----------------|----------------------------------|
| id              | UUID identifying the application |
| name            | The application name             |

Example result
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "apps": [
    {
      "id": "69cd25e7-ec86-43e2-8714-8fbb1d7583ec",
      "app_name": "Application X"
    },
    {
      "id": "408a9d9a-2b25-4c7a-8e2c-038150ab040c",
      "app_name": "Application Y"
    }
  ]
}
```

Example error response
```json
{
  "error": "No applications found"
}
```

### Delete / revoke access to application

By deleting access for a user to an application, all access tokens, access grants & consents a user has for any instance of the application will be removed.

Endpoint: `DELETE /oauth/api/v1/users/{userId}/applications/{applicationId}`

| Parameter     | Description                      |
|---------------|----------------------------------|
| userId        | Identifier of the user           |
| applicationId | UUID identifying the application |

The API will always return a "HTTP 204 no content" response, independent of the execution result.

## Notification Setting API

Via this API the notification setting for a user can be looked up, set and deleted. Currently Onegini supports the following notification types: EMAIL, SMS, NONE. In case of EMAIL an email notification is send to the end-user. In case of SMS an sms message is send to the end-user. In case of NONE the user is not notified.

### Lookup value

Endpoint: `GET /oauth/api/v1/users/{userId}/settings/notification/`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

When no notification settings is set for a user, but the user does have one or more tokens or consents the default value, EMAIL, will be returned as a result. The notification setting is returned as a Json response containing the notification type. For users which are unknown because there is no notification setting plus tokens and consents are missing a "404 NOT FOUND" response will be returned.

| Attribute         | Description                                                    |
|-------------------|----------------------------------------------------------------|
| notification_type | Notification type used to notify the user (SMS / EMAIL / NONE) |

Example result
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "notification_type": "EMAIL"
}
```

Example error response
```json
{
  "error": "No notification setting found."
}
```

### Set notification setting

Endpoint: `POST /oauth/api/v1/users/{userId}/settings/notification/{notificationType}`

| Parameter        | Description                                                                     |
|------------------|---------------------------------------------------------------------------------|
| userId           | Identifier of the user                                                          |
| notificationType | Notification type used to notify the user. Possible values: SMS, EMAIL or NONE. |

If the user already has a notification setting set, the old value will be overridden. The API will return a "HTTP 204 no content" response, when the setting was successfully set.

Example result
```http
HTTP/1.1 204 No Content
Cache-Control: no-store
Pragma: no-cache
```

#### Invalid Notification Type

If the notification type was unknown an error response will be returned. The response type for the error response is "HTTP 400 bad request".

Example error result
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
 
{
  "error" : "INVALID_NOTIFICATION_TYPE"
}
```

### Delete notification setting

Endpoint: `DELETE /oauth/api/v1/users/{userId}/settings/notification/`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

The API will always return a "HTTP 204 no content" response, independent of the execution result.

Example result
```http
HTTP/1.1 204 No Content
Cache-Control: no-store
Pragma: no-cache
```

## Authentication applications API

Via this API the user can manage application instances used for authentication. An application can be a web client or a group of dynamic clients that use the same configuration (e.g. the instances that are installed on a tablet and on a mobile phone are separate dynamic clients but refer to the same application).

### Overview of Authentication applications

Endpoint: `GET /oauth/api/v1/users/{userId}/authentication/apps`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

When no applications are found because the user does not exist or the user does not have any authentication applications, a 404 NOT FOUND will be returned. When the user does have authentication applications an array with one or more applications is returned. Each object has the following attributes:

| Token attribute | Description                                                       |
|-----------------|-------------------------------------------------------------------|
| id              | UUID identifying the authentication application instance          |
| device_id       | UUID identifying the device, currently device_id equals client_id |
| device_name     | The name of the device the application instance is installed on   |
| platform        | The platform the application instance is installed on             |

Example result
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "authentication_app_instances": [
    {
      "id": "77cea55b-c82f-448f-b0a1-6cd4c07bdb54",
      "device_id": "fc4ef972-7167-4421-aa89-f109be79d7c2"
      "device_name": "My device",
      "platform": "ANDROID"
    }
  ]
}
```

Example error response
```json
{
  "error": "No authentication apps found"
}
```

### Delete authentication application instances

By deleting an authentication application instance the application instance can not be used for mobile authentication anymore until enrolled again.

Endpoint: `DELETE /oauth/api/v1/users/{userId}/authentication/apps/{applicationInstanceId}`

| Parameter             | Description                               |
|-----------------------|-------------------------------------------|
| userId                | Identifier of the user                    |
| applicationInstanceId | UUID identifying the application instance |

## One time password API

Via this API Token Server can provide an OTP code which can be used in enrolment process. At generation time the OTP is not being linked to any specific client.

### Overview of providing one time password.

Endpoint: `GET /oauth/api/v1/otp/{userId}`

| Parameter | Description            |
|-----------|------------------------|
| userId    | identifier of the user |

Example OTP request:
```http
GET /oauth/api/v1/otp/some_dummy_user_id HTTP/1.1
Host: onegini.example.com
Content-Type: application/json
```

Example responses:

| Attribute | Description                                    |
|-----------|------------------------------------------------|
| code      | A twenty-five character one time password code |

OTP successful response
```http
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "code": "2FG34-33G9C-G5MME-4G753-32872"
}
```

Example error response
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache
 
{
  "error": "One time password generation failed. User id is missing or invalid."
}
```