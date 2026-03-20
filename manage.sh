#!/bin/bash
# manage.sh — bring the Boring Project stack up or down
# Usage:
#   ./manage.sh up          — start core app stack
#   ./manage.sh up --elk    — start core app stack + ELK/APM
#   ./manage.sh down        — delete core app stack
#   ./manage.sh down --elk  — delete core app stack + ELK/APM
#   ./manage.sh status      — show pod / pvc status

set -euo pipefail

NAMESPACE="boring-project"
CORE_MANIFEST="boring-project-deployment.yaml"
ELK_MANIFEST="elk-apm-deployment.yaml"
DASHBOARDS_NDJSON="kibana-dashboards.ndjson"

ELK=false
for arg in "$@"; do
  [[ "$arg" == "--elk" ]] && ELK=true
done

# ─── helpers ─────────────────────────────────────────────────────────────────
info()    { echo "[INFO]  $*"; }
success() { echo "[OK]    $*"; }
warn()    { echo "[WARN]  $*"; }
err()     { echo "[ERROR] $*" >&2; exit 1; }

require_kubectl() {
  command -v kubectl &>/dev/null || err "kubectl not found. Install kubectl first."
}

require_files() {
  [[ -f "$CORE_MANIFEST" ]] || err "Missing $CORE_MANIFEST (run from project root)"
  if $ELK; then
    [[ -f "$ELK_MANIFEST" ]]       || err "Missing $ELK_MANIFEST"
    [[ -f "$DASHBOARDS_NDJSON" ]]  || err "Missing $DASHBOARDS_NDJSON"
  fi
}

wait_for_pods() {
  local label="$1"
  local timeout="${2:-120s}"
  info "Waiting for pods ($label) to be ready (timeout: $timeout)..."
  kubectl wait pod \
    --for=condition=ready \
    --selector="$label" \
    --namespace="$NAMESPACE" \
    --timeout="$timeout" 2>/dev/null || warn "Some pods not ready yet — check with: kubectl get pods -n $NAMESPACE"
}

# ─── UP ──────────────────────────────────────────────────────────────────────
cmd_up() {
  require_kubectl
  require_files

  info "Creating namespace '$NAMESPACE' (if not exists)..."
  kubectl get namespace "$NAMESPACE" &>/dev/null \
    || kubectl create namespace "$NAMESPACE"

  info "Applying core stack ($CORE_MANIFEST)..."
  kubectl apply -f "$CORE_MANIFEST"

  if $ELK; then
    info "Applying ELK/APM stack ($ELK_MANIFEST)..."
    kubectl apply -f "$ELK_MANIFEST"

    info "Loading Kibana dashboards from $DASHBOARDS_NDJSON into ConfigMap..."
    kubectl create configmap kibana-dashboards-config \
      --from-file=dashboards.ndjson="$DASHBOARDS_NDJSON" \
      --namespace="$NAMESPACE" \
      --dry-run=client -o yaml | kubectl apply -f -
  fi

  echo ""
  info "Waiting for infrastructure (postgres, redis, kafka)..."
  wait_for_pods "app=postgres"  "90s"
  wait_for_pods "app=redis"     "60s"
  wait_for_pods "app=kafka"     "120s"

  info "Waiting for backend services..."
  wait_for_pods "app=authenticationservice" "180s"
  wait_for_pods "app=portfolioservice"      "180s"
  wait_for_pods "app=transactionservice"    "180s"
  wait_for_pods "app=stockdatafeed"         "180s"

  if $ELK; then
    info "Waiting for Elasticsearch (may take ~2 min)..."
    wait_for_pods "app=elasticsearch" "300s"
    wait_for_pods "app=kibana"        "240s"
    wait_for_pods "app=apm-server"    "180s"
  fi

  echo ""
  success "Stack is up!"
  echo ""
  kubectl get pods -n "$NAMESPACE"
  echo ""
  echo "  Access points (after: kubectl port-forward svc/frontendpersonalfin 3000:80 -n $NAMESPACE)"
  echo "    Frontend  → http://localhost:3000"
  echo "    Jaeger    → http://localhost:3000/jaeger"
  if $ELK; then
    echo "    Kibana    → http://localhost:3000/kibana"
    echo "    APM UI    → http://localhost:3000/kibana/app/apm"
  fi
}

# ─── DOWN ────────────────────────────────────────────────────────────────────
cmd_down() {
  require_kubectl

  if $ELK; then
    if [[ -f "$ELK_MANIFEST" ]]; then
      info "Deleting ELK/APM stack..."
      kubectl delete -f "$ELK_MANIFEST" --ignore-not-found
      kubectl delete configmap kibana-dashboards-config \
        --namespace="$NAMESPACE" --ignore-not-found
    else
      warn "$ELK_MANIFEST not found, skipping ELK teardown"
    fi
  fi

  if [[ -f "$CORE_MANIFEST" ]]; then
    info "Deleting core stack..."
    kubectl delete -f "$CORE_MANIFEST" --ignore-not-found
  else
    warn "$CORE_MANIFEST not found, skipping core teardown"
  fi

  echo ""
  read -r -p "[WARN]  Delete namespace '$NAMESPACE' and ALL persistent volumes? [y/N] " confirm
  if [[ "$confirm" =~ ^[Yy]$ ]]; then
    kubectl delete namespace "$NAMESPACE" --ignore-not-found
    success "Namespace deleted."
  else
    info "Namespace kept. PVCs and data preserved."
  fi

  success "Stack is down."
}

# ─── STATUS ──────────────────────────────────────────────────────────────────
cmd_status() {
  require_kubectl
  echo "=== Pods ==="
  kubectl get pods -n "$NAMESPACE" -o wide 2>/dev/null || echo "  (namespace not found)"
  echo ""
  echo "=== Services ==="
  kubectl get svc -n "$NAMESPACE" 2>/dev/null || true
  echo ""
  echo "=== PersistentVolumeClaims ==="
  kubectl get pvc -n "$NAMESPACE" 2>/dev/null || true
  echo ""
  echo "=== Jobs ==="
  kubectl get jobs -n "$NAMESPACE" 2>/dev/null || true
}

# ─── ENTRYPOINT ──────────────────────────────────────────────────────────────
case "${1:-}" in
  up)     cmd_up ;;
  down)   cmd_down ;;
  status) cmd_status ;;
  *)
    echo "Usage: $0 {up|down|status} [--elk]"
    echo ""
    echo "  up            Start core app stack"
    echo "  up --elk      Start core app stack + ELK/APM observability"
    echo "  down          Tear down core app stack"
    echo "  down --elk    Tear down core app stack + ELK/APM"
    echo "  status        Show pod / service / PVC status"
    exit 1
    ;;
esac
