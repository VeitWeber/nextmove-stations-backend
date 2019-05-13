FROM docker-registry.eclever.net/eclever/payara-micro:5.191-ec0.0.3

COPY target/*.war ${DEPLOY_DIR}/
