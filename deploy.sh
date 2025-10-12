#!/bin/bash
# donation-backend應用程式打包和Docker部署腳本

# 設定變數
APP_NAME="donation-backend"
IMAGE_NAME="donation-backend-image"
CONTAINER_NAME="donation-backend-container"
PORT=8080
ENV_FILE="dockerenv"

# 檢查是否提供了環境變數檔案參數
if [ ! -z "$1" ]; then
  ENV_FILE="$1"
fi

echo "===== 開始構建和部署 $APP_NAME ====="
echo "使用環境變數檔案: $ENV_FILE"

# 使用Maven打包應用程式
echo "1. 使用Maven打包應用程式..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
  echo "Maven打包失敗，退出腳本"
  exit 1
fi
echo "Maven打包成功"

# 構建Docker映像檔
echo "2. 構建Docker映像檔..."
docker build -t $IMAGE_NAME .
if [ $? -ne 0 ]; then
  echo "Docker映像檔構建失敗，退出腳本"
  exit 1
fi
echo "Docker映像檔構建成功: $IMAGE_NAME"

# 檢查並停止現有容器
echo "3. 檢查是否存在同名容器..."
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "發現同名容器，正在停止並移除..."
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

# 執行新容器，使用環境變數檔案
echo "4. 啟動新容器..."
docker run -d --name $CONTAINER_NAME -p $PORT:8080 --env-file $ENV_FILE $IMAGE_NAME
if [ $? -ne 0 ]; then
  echo "容器啟動失敗"
  exit 1
fi

echo "===== 部署完成 ====="
echo "應用程式已成功部署在: http://localhost:$PORT"
echo "查看容器日誌: docker logs -f $CONTAINER_NAME"