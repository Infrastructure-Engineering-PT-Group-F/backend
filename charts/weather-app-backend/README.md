# weather-app-backend Helm chart

This chart deploys the weather application backend as a Spring Boot service.
It is designed for platform consumption by GitOps and Crossplane compositions
while keeping local development opt-ins available.

## Image tag

Set `image.tag` to the backend image tag that should run for the tenant. When
`image.tag` is empty, the chart falls back to `Chart.appVersion`.

## Tenant label

Set `tenant.label` to add the tenant marker to chart-managed resources:

```yaml
tenant:
  label: tenant-a
```

The chart renders this as `platform.fh-burgenland.at/tenant` in common labels
only. Selector labels are unchanged.

## AVWX API key

By default, the chart expects the AVWX API key to be provided by an existing
Kubernetes Secret, typically reconciled by External Secrets Operator:

```yaml
apiKeys:
  useExistingSecret: true
  existingSecretName: api-keys
  existingSecretKey: avwx-api-key
```

The Secret value must contain the complete Authorization header value, for
example `Token <avwx-api-key>`.

For local development only, the chart can create an Opaque Secret:

```yaml
apiKeys:
  useExistingSecret: false
  avwx: "Token <avwx-api-key>"
```

When this opt-in path is used, `apiKeys.avwx` is required. The chart does not
render a chart-managed Secret from an empty default value.

## Gateway API HTTPRoute

HTTPRoute rendering is disabled by default. Platform consumers can enable it
by providing the Gateway attachment contract and tenant hostname:

```yaml
gateway:
  httpRoute:
    enabled: true
    parentRef:
      name: shared-gateway
      namespace: platform-gateway
      sectionName: https
    hostname: tenant.gcp.ajdininfrastructure.lol
```

The chart does not hard-code an Envoy Gateway name, namespace, listener name,
or tenant hostname.

## ServiceMonitor

Prometheus Operator ServiceMonitor rendering is disabled by default:

```yaml
serviceMonitor:
  enabled: true
  labels: {}
  interval: ""
  scrapeTimeout: ""
  path: /actuator/prometheus
```

`serviceMonitor.labels` can be used for platform-specific Prometheus discovery
labels. Empty `interval` and `scrapeTimeout` values are omitted from the
rendered manifest.

## Service account and database contracts

The chart does not set a fixed `iam.gke.io/gcp-service-account` annotation.
Add service account annotations only when the platform has confirmed a direct
backend GCP API requirement.

Database Secret configuration remains unchanged. The chart continues
to read the datasource URL, username, and password from the configured existing
database Secret keys.
