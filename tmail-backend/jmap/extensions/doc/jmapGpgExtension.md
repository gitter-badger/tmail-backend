# JMAP GPG

This extension allow access to encrypted email subpart for efficient email display, typically within a webmail. The clients
need to access part of the underlying data and do not need to perform any MIME parsing / manipulation.

We thus introduce the following objects:

 - `EncryptedEmailFastView` holds information required to list emails.
 - `EncryptedEmailDetailedView` holds information required to view emails.
 - `Keystore` allows clients to upload, list and remove GPG public keys used to encrypt their mailbox content.

Furthermore, the `Email/send` additional method allows sending clear text emails when encryption is activated.

## Additions to the Capabilities Object

Servers supporting the JMAP GPG extension advertise it via the `com:linagora:params:jmap:pgp` capability.

The value of this property in the JMAP session capabilities property is an empty object.

The value of this property in an account’s accountCapabilities property is an empty object.

## Keystore

The `Keystore` object represents an underlying GPG public key

A `Keystore` has the following fields:

 - `id`: String, immutable. The computed GPG identifier of the public key.
 - `key`: String, immutable. The public key in its armored format.
 
Example:

```
{
  "id": "",
  "key": "-----BEGIN PGP PUBLIC KEY BLOCK-----\n  
    \n  
    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\n  
    x6e6cS5SeRbzaDlNKIjjEw==\n  
    =Q4mr\n  
    -----END PGP PUBLIC KEY BLOCK-----\n
          "
}
```

### Keystore/get

This is a standard `“/get”` method as described in [RFC-8620, Section 5.1](https://jmap.io/spec-core.html#get).
The `ids` argument may be `null` to fetch all at once.

### Keystore/set

This is a standard “/set” method as described in [RFC8620, Section 5.3](https://jmap.io/spec-core.html#set).

All fields being immutable, updates are rejected.

### Examples

Uploading a new public key:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "Keystore/set",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "create": {
                    "K87": {
                        "key": "-----BEGIN PGP PUBLIC KEY BLOCK-----\n
      \n
      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\n
      x6e6cS5SeRbzaDlNKIjjEw==\n
      =Q4mr\n
      -----END PGP PUBLIC KEY BLOCK-----\n"
                    }
                }
            },
            "c1"
        ]
    ]
}
```

Will return:

```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "Keystore/set",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "created": {
                    "K87": {
                        "id": "3BA423385C8C80D453D7E6F95BF4E866E9CC6FA2"
                    }
                }
            },
            "c1"
        ]
    ]
}
```

Listing existing GPG keys:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "Keystore/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba"
            },
            "c1"
        ]
    ]
}
```

Will return:

```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "Keystore/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "state": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
                "list": [
                    "3BA423385C8C80D453D7E6F95BF4E866E9CC6FA2": {
                        "id": "3BA423385C8C80D453D7E6F95BF4E866E9CC6FA2",
                        "payload": "..."
                    }
                ]
            },
            "c1"
        ]
    ]
}
```

Removing a key:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "Keystore/set",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "destroy": ["3BA423385C8C80D453D7E6F95BF4E866E9CC6FA2"]
            },
            "c1"
        ]
    ]
}
```

Will return:

```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "Keystore/set",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "destroyed": [
                    "3BA423385C8C80D453D7E6F95BF4E866E9CC6FA2"
                ]
            },
            "c1"
        ]
    ]
}
```

## EncryptedEmailFastView

`EncryptedEmailFastView` allows secure encrypted access to the sensible fields available within the listing of emails.

It is composed of the following fields:

 - `id`: String, server assigned, matches the id of the underlying email.
 - `encryptedPreview`: String, server assigned. The preview of the message, encrypted and GPG armored.
 - `hasAttachment`: Boolean, server assigned. Decrypted, does this message have attachments? This information is not 
 encrypted.
 
### EncryptedEmailFastView/get

This is a standard `“/get”` method as described in [RFC-8620, Section 5.1](https://jmap.io/spec-core.html#get).

### Examples

Retrieving a specific EncryptedEmailFastView:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "EncryptedEmailFastView/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "ids": [ "1" ]
            },
            "c2"
        ]
    ]
}
```

Will return:


```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "EncryptedEmailFastView/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "state": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
                "list": [
                        {
                            "id": "1",
                            "encryptedPreview": "-----BEGIN PGP MESSAGE-----\n
                                 Version: BCPG v1.68\n
                                 \n
                                 hQGMA1v06GbpzG+iAQwAym4NG323nZbBKJI7eQ6PzmWGF/z9Px0czq4AouovL4He\n
                                 N5u1mtWKmoLKToEEH/tJbMCsKMGq0tBpvg9zIYDdnmik2c4mALwsdLzp7FIbFqfz\n
                                 84CMyRUJpR5b57TmFgi3ClRiBeAnpOdPyIOsssMkwieKouuTBGuBoVJ5hEad+Iv/\n
                                 SPxB5omv8wg/PUpcquAVtJ6qgW7rKQca2DfIf8WmRreTME/tWi/ExZFxgqzVL/Xk\n
                                 eBOOed4OPan0r2jTVogZCpXcAXxWR9IYlPBukjYPWmhqjy0XyhUYBU0ArR8EfuZn\n
                                 wydcq9p7eSD1iWGgnFoF+sBFbdayLAuGN6R7kY0i0dxlcjex6IBWSVqNFYuwGHQ9\n
                                 OdzpmEvcsCvb6uK0tPHILweoloJzC8oWlNV2xYWo/g+vt250sOSkkKp5PV1mWA1y\n
                                 tjyVollbjWQSiMYckLyIOyHOcQYvso7z2JspEl0siHQjN1LpEoAvzVMQf50G+7hz\n
                                 ijs/cQlN3kVCncRmtx130sA3ATJbBjgFrrOwnhGFZHBrNIxvXeEAQUY0AYq+8raD\n
                                 UqIBcyBM69yODWM3JUiLRj7jBhWiofxzJ6kxM+RTgnsmfrD8SQGrSS0tiRX6RUXy\n
                                 EkW3rAOfLoKEBo/htU7rj+oXmCtr8/WFzO/k9iSeI2/lgboydYgixiZNp8Uio4An\n
                                 +uga60fXiwVcgSf1NYsH2xwsuDdzSeJDCb1qB/1SwKCyVast9Bxd69R6wjHI4PCZ\n
                                 3nAtER6aZI9FF7Wwp5CXlMz0gK+KlOipdqQxglO73F/Ci4/iwhVYCJB3gRrpLc3z\n
                                 Ovyw5kXi4au6CKeJsOOXMWqaLz6TCn21mg==\n
                                 =2VG+\n
                                 -----END PGP MESSAGE-----\n
                                 ",
                            "hasAttachment": true
                        }
                ]
            },
            "c2"
        ]
    ]
}
```

## EncryptedEmailDetailedView

`EncryptedEmailDetailedView` allows secure encrypted access to the sensible fields available within the detailed view
of an email.

It is composed of the following fields:

 - `id`: String, server assigned, matches the id of the underlying email.
 - `encryptedPreview`: String, server assigned. The preview of the message, encrypted and GPG armored.
 - `hasAttachment`: Boolean, server assigned. Decrypted, does this message have attachments? This information is not 
 encrypted.
 - `encryptedHtml`: String, server assigned. The html to be displayed of the message, encrypted and GPG armored.
 - `encryptedAttachmentMetadata`: String, server assigned. Details for attachment displays. This 
 correspond to the encrypted, GPG armored list of `EncryptedAttachmentMetadata` of this email as JSON.

The `EncryptedAttachmentMetadata` object is composed of the following field:

 - `blobId`: String, allow download of the attachment, encrypted.
 - `position`: Integer, positive, position of this attachment within the email, indexed from 0.
 - `name`: String|null, the file name of this attachment.
 - `cid`: String|null, the file name of this attachment.
 - `isInline`: Boolean, if this attachment is inlined.
 - `size`: Integer, positive, the size of this attachment in bytes, decrypted.
 - `contentType`: String. The content type of this attachment.
 
### EncryptedEmailDetailedView/get

This is a standard `“/get”` method as described in [RFC-8620, Section 5.1](https://jmap.io/spec-core.html#get).

### Examples

Here is the decrypted `encryptedAttachmentMetadata`:

```
[
  {
    "position": 0,
    "blobId": "encryptedAttachment_1_0",
    "name": "untitled_1.odp",
    "contentType": "application/vnd.oasis.opendocument.presentation; name=\"untitled_1.odp\"",
    "isLine": false,
    "size": 11512
  }
]

```


Retrieving a specific EncryptedEmailDetailedView:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "EncryptedEmailDetailedView/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "ids": [ "1" ]
            },
            "c2"
        ]
    ]
}
```

Will return:

```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "EncryptedEmailDetailedView/get",
            {
                "accountId": "2bdee8dcc7f7a6817e2699d21c0462f3ab0b66d25de7e71ae2762d4ae8c591ba",
                "state": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
                "list": [
                    {
                        "id": "1",
                        "encryptedPreview": "-----BEGIN PGP MESSAGE-----\n
                             Version: BCPG v1.68\n
                             \n
                             hQGMA1v06GbpzG+iAQwAym4NG323nZbBKJI7eQ6PzmWGF/z9Px0czq4AouovL4He\n
                             N5u1mtWKmoLKToEEH/tJbMCsKMGq0tBpvg9zIYDdnmik2c4mALwsdLzp7FIbFqfz\n
                             84CMyRUJpR5b57TmFgi3ClRiBeAnpOdPyIOsssMkwieKouuTBGuBoVJ5hEad+Iv/\n
                             SPxB5omv8wg/PUpcquAVtJ6qgW7rKQca2DfIf8WmRreTME/tWi/ExZFxgqzVL/Xk\n
                             eBOOed4OPan0r2jTVogZCpXcAXxWR9IYlPBukjYPWmhqjy0XyhUYBU0ArR8EfuZn\n
                             wydcq9p7eSD1iWGgnFoF+sBFbdayLAuGN6R7kY0i0dxlcjex6IBWSVqNFYuwGHQ9\n
                             OdzpmEvcsCvb6uK0tPHILweoloJzC8oWlNV2xYWo/g+vt250sOSkkKp5PV1mWA1y\n
                             tjyVollbjWQSiMYckLyIOyHOcQYvso7z2JspEl0siHQjN1LpEoAvzVMQf50G+7hz\n
                             ijs/cQlN3kVCncRmtx130sA3ATJbBjgFrrOwnhGFZHBrNIxvXeEAQUY0AYq+8raD\n
                             UqIBcyBM69yODWM3JUiLRj7jBhWiofxzJ6kxM+RTgnsmfrD8SQGrSS0tiRX6RUXy\n
                             EkW3rAOfLoKEBo/htU7rj+oXmCtr8/WFzO/k9iSeI2/lgboydYgixiZNp8Uio4An\n
                             +uga60fXiwVcgSf1NYsH2xwsuDdzSeJDCb1qB/1SwKCyVast9Bxd69R6wjHI4PCZ\n
                             3nAtER6aZI9FF7Wwp5CXlMz0gK+KlOipdqQxglO73F/Ci4/iwhVYCJB3gRrpLc3z\n
                             Ovyw5kXi4au6CKeJsOOXMWqaLz6TCn21mg==\n
                             =2VG+\n
                             -----END PGP MESSAGE-----\n
                             ",
                        "encryptedHtml": "-----BEGIN PGP MESSAGE-----\n
                             Version: BCPG v1.68\n
                             \n
                             hQGMA1v06GbpzG+iAQv+JQr4nsvBlwOvIPs+dtv/F6OUBg2qurQk8ZdUDQ/eVJkP\n
                             K8Xcra636yb6Op8II34hTKb410ZqDVKceRATGyIvihlEAeRKrsjK3Yw4kV7QDJhr\n
                             1RTxqFinVi7vBfjbuwXpWL4VPr6qK1eFvV2YteJBwPKa/hrWpbOHwvUiFQFA0nFl\n
                             4J03CbPFeB4qKdl0rPDXZnxa1KyH62SnRqqkjoOiQu9Sq24hq5IigOgtU/dJpf3T\n
                             CDOTz+s2NmP4TOZ9q3MefIVlw0E8mqR/haDv8bNuPqBqTqUxpB0KGbBtancp4Ezh\n
                             NfD04UzuhddBM3y+gT+EgaQZmmYnTTLASKjy5D7EI5G7UKTfKIycn/ehgJyY1rT3\n
                             bDtkDKtWWnW55c/jMbn3hC5/noZyGcAzN7h+Z+OXG6v4N+TVfTxwoyg6Jv7S8UWq\n
                             y9wrYYJtF2XBpLoakg3w/HYM9Fz/iIMkYzk6T8ZIMvIjXlrLfoqgNrWhtgo0DvI/\n
                             5cWR3QkooqN0Tni/Uy800sDoAbTCfegN3hl/y/InBaPHbR3zq4JnoX9onkWkY9vJ\n
                             pkx5V6FT2Y4tM4tpUU7NCY4w6w100XhIuCe+AF3KBP0/fzH+NxLn+gr/jE3V32Yn\n
                             80kr7yRRt+w+dry5DLO9PmX95ny6cnwxSNzXpIXfLFs2XjdFgE8TdNpxm/6x52GS\n
                             TDMj2WrTPp6OzxoG8hjCvcRpcK0htClhTludSCUbO2rXB5JRknCEEhpbBOMF/heG\n
                             aXvv16gOyOaHkchCKd0ey4ItpmLld876g5S8Rb7ucZ/EdBgKTheX9qz8emB3kZAX\n
                             74NsVB1j7XP9yePvlqWXFSr+soL4MSI76QrX2GqO1IGM/s32doJsjYAEmxuybeJc\n
                             4CKjNgglSkJr4mC3Qvl6nY3g6BtcmF6GDSvV0gtZ//stugX5m1TdyTqtkTPvot/O\n
                             3lRiLfE8GwsoWBTX5AQCpdCtqQ4HYvi/Qqiq4kSG5uhmv85428Hp1mwF/c5aFMsf\n
                             nA/hvVppG0YjiS8aFLKyGfdhs92ZPMZk+BzjQtbPIL91W5DS7c0+eQciCmr+I91f\n
                             vtYiEs2dN8LotQ==\n
                             =WTQo\n
                             -----END PGP MESSAGE-----\n
                             ",
                        "hasAttachment": true,
                        "encryptedAttachmentMetadata": "-----BEGIN PGP MESSAGE-----\n
                             Version: BCPG v1.68\n
                             \n
                             hQGMA1v06GbpzG+iAQwAp23JGSJVVG09cqWr15kHL1IEXrIUq4qW0WcUIkD0G3OP\n
                             XUAveXRxySXFXPiQKn0K5WJ9X/UZ3suYWbIQzQ6twRNIq+Pg1hD1ocZL//6FcjnN\n
                             Gdsl83wuHrZvUE9NWjg6CAUw5NlpU82BIRH3Y1mYCbaTa6POz5OBayOPRpOxqTII\n
                             zV0bNLkxBv/eMTxpyINbqbzrrv7it4M6IzBeESggas9Ofw9p2nY22E184hCuStH/\n
                             nPuSUY3HuflgsYrL1rtSsurlp8PYsHXakV/LNC/IA5vpKemqncqDAEZU6CVX30YA\n
                             EwAFR5tGOExoX2yfzgJyUuMFwVNrDHYBA9WyYpJT0mYkApgrBIJRBVHpSZlAFT5n\n
                             8zdIfxtUx3QfVB5dqiNFbmLpJ6lk7xiJUM4BIqm6bmqX5ybta27rqlHADDd+PZfJ\n
                             A65s6SH8iEvugk7Nj1EEqYfJugkRLC1Y04la772DzujuCU1fviId7lRTo+CYjFLA\n
                             iHhJKlCniHu0rl/zxGdl0sAXAbLzdTCHp3BDW6p7FsthGN2kfhncX45MmdKVlgj1\n
                             AFcxez6bU3nJV9QPrZzOEgnHU/MhIs79HK9xlPDGtxyyYQXRAOLgGfj9yLS+cRV8\n
                             GBGRtiiSzHtEjM4jhOQIHWs40NMVZsx2YlNPxJ9KlAtak77yQQmXpycoeAy49P2e\n
                             PcjaP12EvF5qaPIjKVl1xtpyeLq4FZcf1MMBGcYjjbAFyMkxXKnkqbpc+OE+h9vj\n
                             wJlASIv4QNDEETVUrpfDFt0iXQP5SQTrByX0IVGi4LKe/puYc83qEO0=\n
                             =zC8W\n
                             -----END PGP MESSAGE-----\n
                             "
                    }
                ]
            },
            "c1"
        ]
    ]
}
```

## Email/send

This method behaves like a `/set` method with only `create` implemented. It combines the behaviours of `Email/set create`
and of `EmailSubmission/set create` in a single method call.

The creation object have the following properties:

 - `email/create` takes the same argument that `Email/set` creation entries.
 - `emailSubmission/set` takes the same argument that `EmailSubmission/set` creation entries.

Additionally, `Email/send` takes two extra properties, `onSuccessUpdateEmail` and `onSuccessDestroyEmail` as described
for `EmailSubmission/set`.

### Example

Sending:

```
{
    "using": [
        "urn:ietf:params:jmap:core",
        "urn:ietf:params:jmap:mail",
        "urn:ietf:params:jmap:submission",
        "com:linagora:params:jmap:pgp"
    ],
    "methodCalls": [
        [
            "Email/send",
            {
                "accountId": "$ACCOUNT_ID",
                "create": {
                    "K87": {
                        "email/create": {
                            "mailboxIds": {
                                "${getBobInboxId(server).serialize}": true
                            },
                            "subject": "World domination",
                            "htmlBody": [
                                {
                                    "partId": "a49d",
                                    "type": "text/html"
                                }
                            ],
                            "bodyValues": {
                                "a49d": {
                                    "value": "$HTML_BODY",
                                    "isTruncated": false,
                                    "isEncodingProblem": false
                                }
                            }
                        },
                        "emailSubmission/set": {
                            "envelope": {
                                "mailFrom": {
                                    "email": "bob@domain.tld"
                                },
                                "rcptTo": [
                                    {
                                        "email": "andre@domain.tld"
                                    }
                                ]
                            }
                        }
                    },
                    "K88": {
                        "email/create": {
                            "mailboxIds": {
                                "${getBobInboxId(server).serialize}": true
                            },
                            "subject": "World domination 88"
                        },
                        "emailSubmission/set": {
                            "envelope": {
                                "mailFrom": {
                                    "email": "bob@domain.tld"
                                },
                                "rcptTo": [
                                    {
                                        "email": "andre@domain.tld"
                                    }
                                ]
                            }
                        }
                    },
                    "K89": {
                        "email/create": {
                            "mailboxIds": {
                                "${getBobInboxId(server).serialize}": true
                            },
                            "subject": "World domination 89"
                        },
                        "emailSubmission/set": {
                            "envelope": {
                                "mailFrom": {
                                    "email": "bob@domain.tld"
                                },
                                "rcptTo": [
                                    {
                                        "email": "andre@domain.tld"
                                    }
                                ]
                            }
                        }
                    }
                },
                "onSuccessUpdateEmail": {
                    "#K87": {
                        "keywords": {
                            "$$sent": true
                        }
                    }
                },
                "onSuccessDestroyEmail": ["#K88", "#K89"]
            },
            "c1"
        ]
    ]
}
```

Will return a `Email/send` method response and the implicit `Email/set` response call performed by `onSuccess*` properties:

```
{
    "sessionState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
    "methodResponses": [
        [
            "Email/send",
            {
                "accountId": "29883977c13473ae7cb7678ef767cbfbaffc8a44a6e463d971d23a65c1dc4af6",
                "newState": "2c9f1b12-b35a-43e6-9af2-0106fb53a943",
                "created": {
                    "K87": {
                        "emailSubmissionId": "6aa031e8-1481-4847-8137-d23a5834a707",
                        "emailId": "1",
                        "blobId": "1",
                        "threadId": "1",
                        "size": 1655
                    },
                    "K88": {
                        "emailSubmissionId": "887460cc-41e2-4cf0-a6a1-d551ecce2792",
                        "emailId": "2",
                        "blobId": "2",
                        "threadId": "2",
                        "size": 1472
                    },
                    "K89": {
                        "emailSubmissionId": "c37e214a-d516-4a43-b0f0-b502e8e43daf",
                        "emailId": "3",
                        "blobId": "3",
                        "threadId": "3",
                        "size": 1476
                    }
                }
            },
            "c1"
        ],
        [
            "Email/set",
            {
                "accountId": "29883977c13473ae7cb7678ef767cbfbaffc8a44a6e463d971d23a65c1dc4af6",
                "oldState": "66ac6fb7-a2e4-4dfd-971e-09aa46b8be7e",
                "newState": "b6c263f5-cb66-4cab-ae1f-dfc7a2000eb4",
                "updated": {
                    "1": null
                },
                "destroyed": [
                    "2",
                    "3"
                ]
            },
            "c1"
        ]
    ]
}
```