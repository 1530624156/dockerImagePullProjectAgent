#!/usr/bin/env bash
# scripts/lint-arch.sh - 简单的架构依赖检查脚本

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

CONTROLLER_DIR="src/main/java/com/mavis/digg_agent/controller"

if [ ! -d "$CONTROLLER_DIR" ]; then
  echo "❌ 错误: 找不到 Controller 目录: $CONTROLLER_DIR"
  exit 1
fi

echo "🔍 正在检查架构依赖 ($CONTROLLER_DIR)..."

errors=0

# 1) Controller 互相依赖: 字段声明形如 `private XxxController name;`
cross_deps=$(grep -rnE \
  '^[[:space:]]*(private|protected|public)[[:space:]]+[A-Za-z0-9_]+Controller[[:space:]]+[A-Za-z_][A-Za-z0-9_]*[[:space:]]*[;=]' \
  --include="*.java" "$CONTROLLER_DIR" || true)

if [ -n "$cross_deps" ]; then
  echo "❌ 错误: Controller 层禁止互相依赖!"
  echo "$cross_deps" | sed 's/^/   /'
  errors=$((errors + 1))
fi

# 2) Controller 层禁止使用 @Transactional
transactional=$(grep -rn "@Transactional" --include="*.java" "$CONTROLLER_DIR" || true)
if [ -n "$transactional" ]; then
  echo "❌ 错误: 禁止在 Controller 层使用 @Transactional!"
  echo "$transactional" | sed 's/^/   /'
  errors=$((errors + 1))
fi

if [ "$errors" -gt 0 ]; then
  echo ""
  echo "💥 共发现 $errors 项架构违规"
  exit 1
fi

echo "✅ 架构检查通过!"
