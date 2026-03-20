import { init as initApm } from '@elastic/apm-rum';

// Elastic APM Real User Monitoring
// Sends page load, route changes, HTTP requests, and JS errors to APM Server.
// Works alongside the OTel tracing in tracing.js — OTel propagates W3C
// traceparent headers on fetch(); APM RUM captures browser-side performance
// data (page load, LCP, FID, CLS, JS errors) and correlates with backend spans.
const apm = initApm({
  serviceName: 'personalfin-frontend',
  serverUrl: '/apm',           // routes through Nginx Ingress → apm-server:8200
  serviceVersion: '1.0.0',
  environment: 'production',

  // Capture 100% of transactions in development
  transactionSampleRate: 1.0,

  // Propagate trace context to backend requests so RUM + backend traces link up
  distributedTracingOrigins: [window.location.origin],

  // Capture unhandled Promise rejections and JS errors
  captureErrors: true,

  // Include extra context in transactions
  logLevel: 'warn',
});

export default apm;
