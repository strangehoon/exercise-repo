## Whatever Market 과제

### 서비스 설명
**Whatever Market**은 상품 거래와 알바 모집을 위한 플랫폼입니다. 사용자들은 상품을 구매하거나 판매할 수 있으며, 알바 모집 공고를 올리고 지원할 수 있습니다.

### 현재 구현된 기능 목록
- **상품 기능**
    - 상품 목록 조회
    - 상품 상세 조회
    - 상품 등록
    - 상품 수정
    - 상품 삭제
- **알바 모집 기능**
    - 알바 모집 글 목록 조회
    - 알바 모집 글 상세 조회
    - 알바 모집 글 등록
    - 알바 모집 글 수정
    - 알바 모집 글 삭제

### 전제 조건
- **사용자**와 **판매자**는 다른 마이크로 서비스에서 관리되고 있으며, 본 과제에서는 해당 서비스를 활용합니다.
- **사용자 식별**: POST 요청의 바디에서 `"user_id"`로 식별합니다. 
- **판매자 식별**: POST 요청의 바디에서 `"seller_id"`로 식별합니다.
- GET 요청의 경우에는 쿼리 파라미터로 `"user_id"` 또는 `"seller_id"`를 전달합니다.

## 과제 목표
기획자는 아래와 같은 새로운 기능을 추가하기를 원합니다:

1. **찜 기능**
    - 사용자는 상품을 찜할 수 있어야 합니다.
    - 사용자는 찜한 상품 목록을 조회할 수 있어야 합니다.

2. **조회수 증가**
    - 사용자가 상품의 상세 페이지를 조회할 때마다 조회수가 증가해야 합니다.

3. **알바 모집 글 신고**
    - 사용자는 알바 모집 글을 신고할 수 있어야 합니다.
    - 알바 모집 글이 신고를 5번 이상 받을 경우, 자동으로 삭제되어야 합니다.

### 과제 조건
- 위에 명시된 요청 사항들을 구현해 주세요.
- 빌드나 실행이 완벽하지 않아도 무방합니다.
- 테스트 코드는 필수가 아닙니다.
- 새로운 저장소를 사용하여 처음부터 구현해도 괜찮습니다.
- 단, 기획자에게 기능 시연 또는 설명을 할 수 있어야 합니다.

**제출 시 유의사항**
- Git Commit 을 기능, 목적 단위로 나누어 커밋 내역을 잘 기록해 주세요.
- 코드는 가독성 있게 작성해 주세요.
- 기능별로 명확한 주석을 달아 주시기 바랍니다.
- 구현한 기능이 작동하는 방법을 설명할 수 있도록 준비해 주세요.