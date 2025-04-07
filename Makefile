APP_NAME = inactive-user-archiver

# Récupération de la version Maven (ex: 1.0.0)
VERSION = $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

# Séparation des versions : MAJOR.MINOR.PATCH
MAJOR = $(shell echo $(VERSION) | cut -d. -f1)
MINOR = $(shell echo $(VERSION) | cut -d. -f2)
PATCH = $(shell echo $(VERSION) | cut -d. -f3)

# 🟡 Choisis ici quoi incrémenter : MAJOR, MINOR, PATCH
INCREMENT = MAJOR

ifeq ($(INCREMENT),MAJOR)
	NEXT_VERSION = $(shell echo $$(( $(MAJOR) + 1 )).0.0)
endif
ifeq ($(INCREMENT),MINOR)
	NEXT_VERSION = $(shell echo $(MAJOR).$$(( $(MINOR) + 1 )).0)
endif
ifeq ($(INCREMENT),PATCH)
	NEXT_VERSION = $(shell echo $(MAJOR).$(MINOR).$$(( $(PATCH) + 1 )))
endif

# Docker
DOCKER_REGISTRY ?= docker.io
DOCKER_USER ?= elfn
TAG ?= $(NEXT_VERSION)
IMAGE_NAME = $(DOCKER_REGISTRY)/$(DOCKER_USER)/$(APP_NAME):$(TAG)

.PHONY: build docker-build docker-run docker-push prepare-jar all

MVNW = ./mvnw
ifeq ($(OS),Windows_NT)
	MVNW = mvnw.cmd
endif

all: build docker-build docker-run

build:
	$(MVNW) clean package -DskipTests

prepare-jar:
	cp target/$(APP_NAME)-$(VERSION).jar docker/app.jar

docker-build: prepare-jar
	docker build -t $(IMAGE_NAME) -f docker/Dockerfile .

docker-push:
	docker push $(IMAGE_NAME)

docker-run:
	docker run -p 8085:8085 $(IMAGE_NAME)


print-version:
	@echo "Version actuelle   : $(VERSION)"
	@echo "Version incrémentée : $(NEXT_VERSION)"

version-major:
	@echo "📈 Incrémentation MAJEURE de la version..."
	@old=$$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout); \
	major=$$(echo $$old | cut -d. -f1); \
	new_ver="$$(($$major + 1)).0.0"; \
	mvn versions:set -DnewVersion=$$new_ver -DgenerateBackupPoms=false; \
	echo "✅ Nouvelle version appliquée : $$new_ver"
