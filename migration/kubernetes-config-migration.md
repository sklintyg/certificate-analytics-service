# Kubernetes configuration migration guide

## Overview

This document maps the old property names used in Kubernetes ConfigMaps and SealedSecrets to the new
canonical `app.*` names introduced by the Spring Boot config refactor.

The datasource bridge properties (`database.*`) are **unchanged** — they are kept to allow K8s to
supply individual components of the JDBC URL rather than a full connection string.

---

## Property rename table

| Old property name | New property name | Notes |
|---|---|---|
| `certificate.analytics.message.queue.name` | `app.jms.queue-name` | Renamed to canonical `app.*` key |
| `pseudonymization.key` | `app.pseudonymization.key` | Renamed; inject via SealedSecret env var |
| `pseudonymization.context` | `app.pseudonymization.context` | Renamed; inject via ConfigMap |
| `database.server` | `database.server` | **Unchanged** — bridge property kept |
| `database.port` | `database.port` | **Unchanged** — bridge property kept |
| `database.name` | `database.name` | **Unchanged** — bridge property kept |
| `database.username` | `database.username` | **Unchanged** — bridge property kept |
| `database.password` | `database.password` | **Unchanged** — inject via SealedSecret env var |
| `spring.activemq.broker-url` | `spring.activemq.broker-url` | **Unchanged** — standard Spring key |
| `spring.activemq.user` | `spring.activemq.user` | **Unchanged** — standard Spring key |
| `spring.activemq.password` | `spring.activemq.password` | **Unchanged** — inject via SealedSecret env var |

---

## Properties with defaults (no K8s injection required)

These properties have safe defaults in `application.yml` and only need to be overridden if the
deployed environment requires a different value:

| Property | Default | Description |
|---|---|---|
| `database.port` | `3306` | MySQL port |
| `server.port` | `8081` | HTTP server port |
| `spring.jpa.open-in-view` | `false` | JPA session scope |
| `logging.structured.format.console` | `ecs` | ECS structured logging for production |

---

## ConfigMap — before and after

### Before

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: certificate-analytics-service-config
data:
  certificate.analytics.message.queue.name: "prod.certificate.analytics.message.queue"
  pseudonymization.context: "analytics-prod"
  database.server: "mysql.internal"
  database.name: "certificate_analytics_service"
  spring.activemq.broker-url: "tcp://activemq.internal:61616"
  spring.activemq.user: "activemqUser"
```

### After

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: certificate-analytics-service-config
data:
  app.jms.queue-name: "prod.certificate.analytics.message.queue"
  app.pseudonymization.context: "analytics-prod"
  database.server: "mysql.internal"
  database.name: "certificate_analytics_service"
  spring.activemq.broker-url: "tcp://activemq.internal:61616"
  spring.activemq.user: "activemqUser"
```

---

## SealedSecret — before and after

### Before

```yaml
apiVersion: bitnami.com/v1alpha1
kind: SealedSecret
metadata:
  name: certificate-analytics-service-secrets
spec:
  encryptedData:
    pseudonymization.key: "<encrypted>"
    database.username: "<encrypted>"
    database.password: "<encrypted>"
    spring.activemq.password: "<encrypted>"
```

### After

The preferred approach is to inject secrets as environment variables and reference them via Spring
Boot's relaxed binding or explicit `${ENV_VAR:}` placeholders in `application.yml`.

`application.yml` already defines:

```yaml
app:
  pseudonymization:
    key: "${PSEUDONYMIZATION_KEY:}"
    context: "${PSEUDONYMIZATION_CONTEXT:}"
  jms:
    queue-name: "${JMS_QUEUE_NAME:}"
```

The SealedSecret should therefore expose the secret values as environment variables:

```yaml
apiVersion: bitnami.com/v1alpha1
kind: SealedSecret
metadata:
  name: certificate-analytics-service-secrets
spec:
  encryptedData:
    PSEUDONYMIZATION_KEY: "<encrypted>"
    database.username: "<encrypted>"
    database.password: "<encrypted>"
    spring.activemq.password: "<encrypted>"
```

Spring Boot's relaxed binding will map `database.username` and `database.password` from the
SealedSecret directly. `PSEUDONYMIZATION_KEY` is picked up via the `${PSEUDONYMIZATION_KEY:}`
placeholder in `application.yml`.

---

## Step-by-step migration checklist for platform engineers

1. **Update the ConfigMap**
   - Rename `certificate.analytics.message.queue.name` → `app.jms.queue-name`
   - Rename `pseudonymization.context` → `app.pseudonymization.context`
   - Remove `pseudonymization.key` from ConfigMap (move to SealedSecret env var, see step 2)
   - Keep all `database.*`, `spring.activemq.*` keys unchanged

2. **Update the SealedSecret**
   - Replace the `pseudonymization.key` secret entry with `PSEUDONYMIZATION_KEY`
   - Re-seal with the cluster's public key

3. **Deploy the new application version** (containing this config refactor)

4. **Verify the application starts** and the health endpoint responds:
   ```
   GET /actuator/health
   ```

5. **Verify JMS message consumption** is working by checking application logs for successful
   message processing from the queue.

6. **Verify pseudonymization** is working by checking that analytics events are being stored with
   pseudonymized identifiers.

---

## Rollback procedure

If issues are encountered after deploying the new version:

1. **Roll back the application** to the previous container image via your deployment tool
   (Helm, Argo CD, kubectl).

2. **Restore the old ConfigMap** from version control or backup:
   - Rename `app.jms.queue-name` back to `certificate.analytics.message.queue.name`
   - Rename `app.pseudonymization.context` back to `pseudonymization.context`
   - Add `pseudonymization.key` back if it was moved to env var

3. **Restore the old SealedSecret** if `PSEUDONYMIZATION_KEY` was re-sealed as a new entry.

The old application version reads `pseudonymization.*` and `certificate.analytics.message.queue.name`
directly, so reverting both config and image is sufficient.

---

## Known exceptions

### Bridge properties (`database.*`)

The JDBC URL is constructed from individual bridge properties:

```yaml
spring:
  datasource:
    url: "jdbc:mysql://${database.server}:${database.port}/${database.name}?..."
```

These bridge properties are intentionally **not** renamed to `app.*` because they allow K8s to
supply individual connection components. Renaming them would require K8s to provide the entire
JDBC URL as a single string, removing the ability to override host, port, and database name
independently.

A future cleanup could move to `spring.datasource.url` directly in the ConfigMap if the full URL
is stable and individually overridable components are no longer needed.
