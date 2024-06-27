# 목차
1. [사이트 기본 구성](https://github.com/yonggyo1125/project501_13_jsp?tab=readme-ov-file#%EC%B4%88%EA%B8%B0-%EC%84%A4%EC%A0%95)
2. [게시판 만들기](https://github.com/yonggyo1125/project501_13_jsp/tree/board)
3. [포켓몬 도감 가이드](https://github.com/yonggyo1125/project501_13_jsp/tree/pokemon)

--- 

# 게시판 만들기 

## 사이트 기본 UI 구성 

## 회원 인증, 인가 기능 구성 

## 관리자 구성하기 

### 게시판 설정 테이블 

```sql
CREATE TABLE BOARD (
	BID VARCHAR2(30) PRIMARY KEY,
	BNAME VARCHAR2(60) NOT NULL,
	ROWS_PER_PAGE NUMBER(4) DEFAULT 20,
	ACTIVE NUMBER(1) DEFAULT 0,
	ACTIVE_CATEGORY NUMBER(1) DEFAULT 0,
	CATEGORY CLOB,
	AUTHORITY VARCHAR2(10) DEFAULT 'ALL' CHECK(AUTHORITY IN ('ALL', 'USER', 'ADMIN'))
);
```

## 파일 업로드 구현

## 게시글 쓰기 및 수정 구현

## 게시글 보기, 목록 구현

## 댓글 구현 