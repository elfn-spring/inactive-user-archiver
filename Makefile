APP_NAME=inactive-user-archiver
VERSION=$(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
DOCKER_REGISTRY ?= docker.io
DOCKER_USER ?= elfn
TAG ?= $(VERSION)
IMAGE_NAME=$(DOCKER_REGISTRY)/$(DOCKER_USER)/$(APP_NAME):$(TAG)

.PHONY: build docker-build docker-run docker-push docker-push-hub prepare-jar k8s-cronjob k8s-configmap all

all: build docker-build docker-run

MVNW = ./mvnw

ifeq ($(OS),Windows_NT)
	MVNW = mvnw.cmd
endif

build:
	$(MVNW) clean package -DskipTests

prepare-jar:
	cp target/$(APP_NAME)-$(VERSION).jar docker/app.jar

docker-build: prepare-jar
	docker build -t $(IMAGE_NAME) -f docker/Dockerfile .

docker-push:
	docker push $(IMAGE_NAME)

docker-push-hub:
#	@echo "📤 Push manuel vers Docker Hub avec tag : 2.0.0"
	docker tag $(IMAGE_NAME) $(DOCKER_USER)/$(APP_NAME):2.0.0
	docker push $(DOCKER_USER)/$(APP_NAME):2.0.0

#docker-run:
#	docker run -p 8085:8085 --rm $(IMAGE_NAME)

k8s-cronjob:
	kubectl apply -f k8s/cronjob.yml

k8s-configmap:
	kubectl apply -f k8s/configmap.yml

k8s-h2-data-pvc:
	kubectl apply -f k8s/pvc.yml

k8s-test-persistence:
	kubectl apply -f k8s/job-test-persistence.yml

k8s-service:
	kubectl apply -f k8s/service.yml
