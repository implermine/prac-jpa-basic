# @Embedded를 권장하는 Hibernate 공식문서의 그림

![img.png](../../../../../../docs/file/embed.png)

내가 만든 코드에선 PhoneNumber를 Contact(Embeddable)와 PhoneEntity를 Phone(Entity)로

구현하였다.

# @Embedded를 사용할 때 공유참조를 주의

![img.png](../../../../../../docs/file/embed2.png)

그에따라 @Embeddable 클래스는 Setter를 주어지게 하지 않는다.