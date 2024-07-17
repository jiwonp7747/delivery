# delivery

2024/07/08
1. 자바 프로젝트로 멀티 모듈 구성 gradle 설정
2. JpaConfig를 통한 다른 모듈간의 Entity, Repository 빈으로 등록
3. Mysql에 delivery 스키마 생성 및 테스트 용 account 테이블 생성

2024/07/09
1. 응답 객체 AccountMeResponse 생성 및 Custom ObjectMapper 생성하여 Json 변환 시 snake case로 반환
2. Swagger ui 사용 위한 의존성 추가
3. Custom object mapper 사용위한 SwaggerConfig 생성
4. Filter를 통해 응답 데이터가 넘어가기 전 로그 확인 기능 구현
5. 공통 스펙 api 설정하여 응답 정보를 커스텀 

2024/07/10
1. Api 에러 코드 추가
2. ExceptionHandler 스택 트레이스 로그 수집, 오류 응답 처리 
3. Api Controller에 대한 Api Exception 작성 (추상화) 및 해당 예외 다루는 Api ExceptionHandler 작성 (우선순위 핸들러)
4. 추후 인증을 위한 인터셉터 기반, WebConfig를 통한 인증 Api 분류

2024/07/12
1. UserEntity 추가, UserRepository
2. 상태 enums 추가

2024/07/13
1. UserOpenApiController, UserBusiness, UserService, UserConverter 로직 구현
2. 사용자 등록 요청 UserRegisterRequest, 그에 대한 응답 UserResponse 구현
로직 흐름 Controller -> Business -> Service -> Repository

2024/07/14
1. 토큰 생성, 검증 로직 추가

2024/07/15
1. 인터셉터를 통한 JWT 인증 로직 추가
2. 가맹점 등록, 가맹점 검색 기능 추가
3. 메뉴 등록, 메뉴 검색 기능 추가

2024/07/17
1. 사용자의 주문 내역 테이블 user_order, 주문 메뉴 테이블 추가 user_order_menu
2. Entity, Repository 추가
3. user_order_menu Repository query 추가 -> 주문 내역에 해당하는 메뉴 리스트 가져오기
4. user_order Repository query 추가 ->
   1) user_id에 해당하는 모든 주문내역 가져오기
   2) user_id에 해당하는 특정 주문내역 가져오기
   3) user_id에 해당하는 주문 중에 상태에 따라 가져오기
5. user_order service 로직 추가
   1) 주문 목록 가져오기
   2) 특정 주문 가져오기
   3) 상태에 따라 주문 목록 가져오기
   4) 주문하기 (등록)
   5) 주문확인/ 조리 시작/ 배달 시작/ 배달 완료
   6) 현재 진행 중인 내역/ 과거 주문 내역 가져오기
