# 런타임 스테이지
FROM openjdk:17-jdk-slim

# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /app

# EC2 인스턴스에 존재하는 JAR 파일을 이미지에 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/rchive-back.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "/app/rchive-back.jar"]
