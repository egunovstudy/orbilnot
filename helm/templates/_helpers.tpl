{{/*
Expand the name of the chart.
*/}}
{{- define "orbilnot.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "orbilnot.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "orbilnot.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "orbilnot.labels" -}}
helm.sh/chart: {{ include "orbilnot.chart" . }}
{{ include "orbilnot.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "orbilnot.selectorLabels" -}}
app.kubernetes.io/name: {{ include "orbilnot.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "orbilnot.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "orbilnot.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}


{{- define "orbilnot.order.name" -}}
{{- printf "order-%s" .Release.Name -}}
{{- end }}

{{- define "orbilnot.billing.name" -}}
{{- printf "billing-%s" .Release.Name -}}
{{- end }}

{{- define "orbilnot.notification.name" -}}
{{- printf "notification-%s" .Release.Name -}}
{{- end }}

{{- define "orbilnot.gateway.name" -}}
{{- printf "gateway-%s" .Release.Name -}}
{{- end }}