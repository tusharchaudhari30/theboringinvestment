import { WebTracerProvider } from '@opentelemetry/sdk-trace-web';
import { BatchSpanProcessor } from '@opentelemetry/sdk-trace-base';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { ZoneContextManager } from '@opentelemetry/context-zone';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';
import { Resource } from '@opentelemetry/resources';

// Sends traces through Nginx Ingress → /otlp → otelcollector:4318/v1/traces
const exporter = new OTLPTraceExporter({
  url: '/otlp/v1/traces',
});

const provider = new WebTracerProvider({
  resource: new Resource({
    'service.name': 'personalfin-frontend',
    'service.version': '1.0.0',
  }),
  spanProcessors: [new BatchSpanProcessor(exporter)],
});

provider.register({
  // ZoneContextManager keeps trace context alive across async/await and Promises
  contextManager: new ZoneContextManager(),
});

registerInstrumentations({
  instrumentations: [
    new FetchInstrumentation({
      // Inject W3C traceparent header into all backend API calls through Ingress
      propagateTraceHeaderCorsUrls: [
        /\/authentication\/.*/,
        /\/portfolio\/.*/,
        /\/transaction\/.*/,
      ],
      clearTimingResources: true,
    }),
  ],
});

export default provider;
