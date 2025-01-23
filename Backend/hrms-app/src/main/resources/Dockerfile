FROM alpine:latest

RUN apk --update add  procps
RUN apk fetch openjdk21
RUN apk add openjdk21
RUN apk add --no-cache bash
RUN apk add --no-cache expat-dev
RUN apk add --no-cache libxml2==2.13.4-r3
RUN apk add ttf-dejavu
RUN apk update && apk upgrade
Run fc-cache --force
RUN apk add --no-cache curl

MAINTAINER Visionwaves
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN addgroup -S visionwaves && adduser -S -G visionwaves visionwaves

ENV SERVICE_ARCHIVE hrms

ENV SERVICE_PATH /opt/visionwaves/$SERVICE_ARCHIVE

ENV BASE_PATH /opt/visionwaves/$SERVICE_ARCHIVE

ENV USER_NAME visionwaves

RUN mkdir -p "$BASE_PATH"

ADD ./$SERVICE_ARCHIVE.tar $BASE_PATH
ADD ./melodyposthook.sh /opt/
RUN chmod 777 /opt/melodyposthook.sh
RUN chown -R $USER_NAME:$USER_NAME $BASE_PATH

#UserAdd
USER $USER_NAME

WORKDIR $SERVICE_PATH

USER visionwaves

#Persisting volume from local storage to reduce loss of data
CMD bash run.sh start ; sleep 5s ; tail -f  logs/hrms-app.log
