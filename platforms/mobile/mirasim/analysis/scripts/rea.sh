#!/bin/sh
set -eu
TASK_ROOT=$(CDPATH= cd -- "$(dirname -- "$0")/../.." && pwd)
TASK_JDK=$(find "$TASK_ROOT/analysis/tools" -maxdepth 1 -type d -name 'jdk-*' | head -1)
export JAVA_HOME="$TASK_JDK/Contents/Home"
export REA_JADX_MCP_JAR="$TASK_ROOT/analysis/tools/jadx-headless-mcp.jar"
exec "$TASK_ROOT/analysis/tools/node_modules/.bin/rea" "$@"
