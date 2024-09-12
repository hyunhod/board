<div align="center">
<h2>게시판 시스템</h2>
사용자가 게시글을 작성, 수정, 삭제할 수 있으며, 댓글 및 대댓글 기능, 강화된 로그인 시스템을 제공합니다. Spring Boot와 JPA를 사용해 백엔드를 구현하였고, Oracle 데이터베이스를 활용해 데이터를 관리합니다.
<br>
</br>


</div>

## 목차
  - [개요](#개요) 
  - [프로젝트 소개](#프로젝트-소개)
  - [](#)


## 개요
- 프로젝트 이름: 게시판 
- 프로젝트 지속기간: 2024.04-2024.08
- 개발 언어 <br> </br>
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-0078D7?style=for-the-badge&logo=hibernate&logoColor=white)
- 멤버: 나현호

## 프로젝트 소개
**게시판 시스템**은 사용자가 다양한 게시글을 작성, 수정, 삭제할 수 있을 뿐만 아니라, 댓글 및 대댓글 기능, 강화된 로그인 시스템 등 여러 고급 기능을 제공합니다. 이 프로젝트는 **Spring Boot**, **Oracle 데이터베이스**, **JPA**를 사용하여 효율적이고 확장 가능한 백엔드를 구축하였으며, **Thymeleaf**를 사용한 유연한 프론트엔드로 다양한 사용자 경험을 제공합니다.
<br>
</br>
- **회원가입 및 로그인 시스템**: 비밀번호는 대문자 및 숫자를 포함한 강력한 패턴을 만족해야 하며, 보안에 초점을 맞춘 인증 기능.
- **게시글 CRUD**: 사용자는 게시글을 작성(Create), 조회(Read), 수정(Update), 삭제(Delete)할 수 있습니다.
- **댓글 및 대댓글 시스템**: 사용자는 게시글에 댓글을 작성할 수 있으며, 댓글에 대한 대댓글도 추가할 수 있습니다.
- **페이지네이션**: 많은 게시글을 페이지로 나누어 보여줌으로써 효율적인 탐색 제공.
- **게시글 검색**: 제목이나 내용으로 게시글을 검색할 수 있는 강력한 검색 기능.
- **파일 업로드**: 게시글 작성 시 파일을 첨부할 수 있으며, 서버에 안전하게 저장.
- **사용자 권한 관리**: 관리자와 일반 사용자 간 권한 차별화, 관리자 페이지에서 게시글 관리 가능.

## 기술 스택
- **백엔드**: Java, Spring Boot
- **프론트엔드**: HTML, CSS, Thymeleaf, Bootstrap
- **데이터베이스**: Oracle
- **버전 관리**: Git, GitHub

## 프로젝트 구조
