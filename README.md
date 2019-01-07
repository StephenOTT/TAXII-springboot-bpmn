# TAXII-springboot
SpringBoot implementation of TAXII server


# BPMN Usage

...

# Sample Json

```json
{
  "type": "tenant",
  "tenant": {
    "tenant_id": "1234567890",
    "tenant_slug": "tenant123",
    "title": "Some Title",
    "description": "some description",
    "versions": [
      "taxii-2.0"
    ],
    "max_content_length": 10485760
  },
  "_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "created_at": ISODate("2019-01-07T03:56:04.794Z"),
  "modified_at": ISODate("2019-01-07T03:56:04.794Z")
}
```

```json
{
  "type": "tenant",
  "tenant": {
    "tenant_id": "1234567890",
    "tenant_slug": "tenant123",
    "title": "Some Title",
    "description": "some description",
    "versions": [
      "taxii-2.0"
    ],
    "max_content_length": 10485760
  },
  "_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "created_at": "2019-01-07T03:56:04.794Z",
  "modified_at": "2019-01-07T03:56:04.794Z"
}
```

```json
{
  "type": "user",
  "tenant_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "username": "steve",
  "_id": "f128f3b5-64b5-44fc-89b2-3a103c912bf8",
  "created_at": "2019-01-07T03:56:14.466Z",
  "modified_at": "2019-01-07T03:56:14.433Z"
}
```

```json
{
  "type": "user",
  "tenant_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "username": "steve",
  "_id": "f128f3b5-64b5-44fc-89b2-3a103c912bf8",
  "created_at": ISODate("2019-01-07T03:56:14.466Z"),
  "modified_at": ISODate("2019-01-07T03:56:14.433Z")
}
```