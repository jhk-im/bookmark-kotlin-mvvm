# bookmark_se_kotlin

* lateinit var   
-> 늦은 초기화 / 프로퍼티의 선언과 동시에 초기화하지 않아도 됨   
-> 제약이 있음   
    -> lateinit var 로 선언한 프로퍼티에서만 사용할 수 있다.   
    -> 생성자에서는 사용할 수 없다.   
    -> getter/setter 를 사용하지 않은 프로퍼티에만 사용할 수 있다.   
    -> null 허용 프로퍼티에는 사용할 수 없다.   
    -> 기초 타입 프로퍼티에는 사용할 수 없다.   
   
* fun   
코틀린의 기본 함수 형태   
fun 함수명(변수): Unit {  }   
fun 함수명(변수): 리턴타입 { return 값 }   
   
