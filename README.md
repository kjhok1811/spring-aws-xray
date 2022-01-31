## 설명
- 해당 코드는 Spring 웹 애플리케이션을 AWS X-RAY 라는 서비스를 이용하여 APM을 구성한 것 입니다. 이 예제에서는 다음과 같은 기능을 다룹니다.
  - 애플리케이션 요청 및 응답에 대한 데이터 기록 및 성능측정, 오류예측 등 모니터링 시스템 구축

<br>

## 프로젝트 환경
- 프로젝트 환경구성은 다음과 같습니다.
  - JAVA 11
  - Spring Boot 2.6.2
  - Spring AOP
  - Spring Data JPA
  - AWS EC2 (Region : ap-northeast-2)
  - AWS X-RAY
  - AWS CloudWatch Logs
  - AWS CloudWatch Service Lens
  - Docker
  - MySQL 8.0
  
<br>

## 테스트 환경 구축
- CloudWatchLogsFullAccess Role을 가진 사용자의 accessKey, secretAccessKey 발급받기
  - <a href='https://kim-jong-hyun.tistory.com/85'>여기</a>를 참고하여 CloudWatchLogsFullAccess Role을 가진 사용자를 생성합니다.

- Spring 애플리케이션에서 발생된 로그를 AWS Cloud Watch로 전송하기
  - <a href='https://kim-jong-hyun.tistory.com/111'>여기</a>를 참고하여 진행합니다.
  
- AWSXRayDaemonWriteAccess Role을 가진 역할 생성
  - <a href='https://kim-jong-hyun.tistory.com/115'>여기</a>를 참고하여 역할을 생성합니다.

- EC2 인스턴스 생성
  - 보안그룹에 8080번 포트번호를 추가해줍니다.
  - '3. 인스턴스 구성' > 고급 세부 정보 > 사용자 데이터 (텍스트로) 에서 아래 스크립트를 입력하고 인스턴스를 생성합니다.
  ```bash
  #!/bin/bash
  amazon-linux-extras install -y java-openjdk11
  yum install -y docker
  systemctl start docker
  docker pull mysql
  docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 $(docker images | grep -v awk | awk '/mysql/ {print $3}')
  curl https://s3.ap-northeast-2.amazonaws.com/aws-xray-assets.ap-northeast-2/xray-daemon/aws-xray-daemon-3.x.rpm -o /home/ec2-user/xray.rpm
  yum install -y /home/ec2-user/xray.rpm
  ```
  - sudo vi ~/.bashrc 파일을 열어 아래내용을 파일 맨 하단에 추가합니다.
  ```bash
  export AWS_XRAY_COLLECT_SQL_QUERIES=true
  export ACCESS_KEY=위에서 발급받은 accessKey
  export SECRET_ACCESS_KEY=위에서 발급받은 secretAccessKey
  ```
  - source ~/.bashrc 명령어로 추가한 환경변수의 내용을 반영합니다.

- JAVA 애플리케이션 배포
  - 해당 프로젝트를 jar로 빌드하여 EC2에 배포합니다.
  
- HTTP 요청전송
  - HTTP 요청을 보내면 CloudWatch Log에 AWS-XRAY: TraceId를 복사하여 https://ap-northeast-2.console.aws.amazon.com/xray/home?region=ap-northeast-2#/service-map로 들어가서 검색하면 됩니다.

<br>

## 참고링크
- https://kim-jong-hyun.tistory.com/115
