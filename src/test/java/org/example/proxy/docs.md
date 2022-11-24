# Docs

수업 내용에서 있었던 내용

---

## 프록시 조회했을 때, 그 프록시를 touch해도 쿼리가 안나가는 경우가 있는가?

```java
proxy.getId();
```

했을때는, 쿼리가 안나간다, 왜냐하면

```java
em.find(Member.class, 1L);
```

위와같이 ID를 지정해서 find 시도했기 때문에 id는 알고있다.

---

## 프록시의 정체는 무엇? = XXX의 XX객체

실제 `Entity`의 `상속` 객체

Member Entity가 있다면 Proxy는 다음과 같다.

```java
public class Proxy extends Member{
   //do something... 
}
```

---

## 프록시에서 Touch를 통해 쿼리를 날리는 프로세스는?

다음과 같은 proxy 객체에는 target Entity, 즉 실제 엔티티의 참조값을 가지고 있다.

이 참조값이 null이라면 loading되지 않은것이므로, database에 질의를 통해 실 엔티티를 가져온다.

```java

public class Proxy extends Member{
    
    private Member targetEntity;
    
    @Override
    public String getUserName(){
        if(this.targetEntity==null){
            // query to database
            this.targetEntity = ~~~;
        }
        
        return super.getUserName();
    }
}
```

---
## JPA를 사용하면서 == 가 안먹을수도 있음?

Entity와 Proxy가 하나는 서브클래스고 하나는 슈퍼클래스라서 ==가 안먹고

instanceof 만 되는 경우도 있다고 한다.

따라서 JPA를 쓸때는 ==보단 instanceof가... 더 낫다고 한다.

em.find랑 em.getReference로 같은 걸 찾아서 비교해보자
---

