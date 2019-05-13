.DEFAULT_GOAL := help

TAG := $(shell git rev-parse --short HEAD)
REGISTRY_EXT := docker-registry.eclever.net
REGISTRY_INT := 100.125.7.25:20202
ORG := eclever
REPO := stations-api
K8S_RESOURCE := statefulset.apps/stations
K8S_CONTAINER_NAME := stations

HELP_FUNC = \
    %help; \
    while(<>) { \
        if(/^([a-z0-9_-]+):.*\#\#(?:@(\w+))?\s(.*)$$/) { \
            push(@{$$help{$$2}}, [$$1, $$3]); \
        } \
    }; \
    print "usage: make [target]\n\n"; \
    for ( sort keys %help ) { \
        print "$$_:\n"; \
        printf("  %-20s %s\n", $$_->[0], $$_->[1]) for @{$$help{$$_}}; \
        print "\n"; \
    }

.PHONY: help
help: ##@targets Show this help
	@perl -e '$(HELP_FUNC)' $(MAKEFILE_LIST)

.PHONY: verify
verify: ##@targets Run Maven build phase `verify`
	mvn -B -s settings.xml verify

.PHONY: build
build: ##@targets Build the Docker image
	docker build --tag "$(REGISTRY_EXT)/$(ORG)/$(REPO):$(TAG)" .

.PHONY: login
login: ##@targets Login to the Docker registry
	docker login $(REGISTRY_EXT)

.PHONY: push
push: ##@targets Push Docker image to the registry
	docker push $(REGISTRY_EXT)/$(ORG)/$(REPO):$(TAG)

.PHONY: deploy-dev
deploy-dev: ##@targets Set image of to the newly created image
	kubectl config use-context k8s-dev
	kubectl set image $(K8S_RESOURCE) $(K8S_CONTAINER_NAME)=$(REGISTRY_INT)/$(ORG)/$(REPO):$(TAG)

.PHONY: dev
dev: verify build login push deploy-dev ##@targets Run verify, build, login, push and deploy-dev

.PHONY: clean
clean: ##@targets Cleanup Maven build (mvn clean)
	mvn clean
