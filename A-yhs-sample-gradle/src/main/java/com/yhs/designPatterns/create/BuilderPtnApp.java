package com.yhs.designPatterns.create;

/* Builder 패턴은
 2. 빌더 패턴 장점
첫째) 객체를 만드는 방법을 따로 둠으로서, 객체의 생성과정과 실제 생성부분을 분리할수 있다. 이렇게 되면 관리가 용이하다.
둘째) 코드가 깔끔해진다. 복잡한 속성의 객체를 단 한라인에도 생성할수 있게 된다.
셋째) 불변객체(Immutable)를 만들수가 있다. 
 => 매우 중요하다. 일반적인 객체는 불변객체화 하기가 어렵다. 위에서 보듯 빌더패턴에서는 가능하다.

3. 빌더 패턴 단점
=> 객체의 속성을 추가하면 빌더에도 수정을 해줘야 한다. (속성 설정함수와 실제객체의 생성 부분에서)

 */

/*
 Factory 패턴과의 차이점
Factory와 Builder는 모두 객체 생성에 관련된 패턴이고 둘이 좀 비슷해보이기도 합니다. 그러나 용도가 좀 다릅니다.
Factory는 일반적으로 다음의 두 가지 경우에 해당하는 경우에 사용합니다.
	1. 리턴 타입이 일정하지 않은 경우. 예를들어, create("소") 라고 하면 Cow 클래스의 인스턴스를 리턴하고, create("개")라고 하면 Dog 클래스의 인스턴스를 리턴하는 것처럼 인자에 따라 리턴 타입이 바뀌는 경우.
	2. 대략 비슷하지만 멤버가 살짝 다른 경우. Boolean.valueOf(String arg) 와 같은 경우 리턴 타입은 모두 Boolean 이지만, 속의 내용이 조금 다름. 이 경우는 대부분 Singleton 패턴으로 처리됨.

그러나 Builder는 객체 생성과정의 복잡성을 떠넘기는 게 포인트입니다.
 */
public class BuilderPtnApp {

	public static void main(String[] args) {
		ImmutablePerson.PersonBuilder pb = new ImmutablePerson.PersonBuilder("Black");
		
		// 생성된 빌더로 객체 속성값을  변경후 객체 생성. 생성된 객체는 값을 참조만 할수 있고, 이름등을 변경하지는 못한다.
		ImmutablePerson jack = pb.firstName("Jack").gender("Man").createPerson();
		ImmutablePerson jenny = pb.firstName("Jenny").gender("Woman").createPerson();
		ImmutablePerson cindy = pb.firstName("Cindy").createPerson();
		    
		System.out.println(jack);
		System.out.println(jenny);
		System.out.println(cindy);   
	}

}


class ImmutablePerson {
   private final String familyName;
   private final String firstName;
   private final String gender;
   
   private ImmutablePerson(
      final String newFamilytName, final String newFirstName,
      final String newGender) {
      this.familyName = newFamilytName;
      this.firstName = newFirstName;
      this.gender = newGender;
   }
   
   public String getFamilyName()
   {
      return this.familyName;
   }
   public String getFirstName()
   {
      return this.firstName;
   }
   public String getGender()
   {
      return this.gender;
   }
   public String toString() {
     return "I am "+firstName+" "+familyName+". My gender is "+gender;
   }
   
   public static class PersonBuilder {
      private String nestedFamilyName;
      private String nestedFirstName;
      private String nestedGender;
      
      public PersonBuilder(final String newFamilyName) 
      {
         this.nestedFamilyName = newFamilyName;
      }
      public PersonBuilder firstName(final String newFirstName)
      {
         this.nestedFirstName = newFirstName;
         return this;
      }
      public PersonBuilder gender(final String newGender)
      {
         this.nestedGender = newGender;
         return this;
      }
      public ImmutablePerson createPerson()
      {
         return new ImmutablePerson(
            nestedFamilyName,nestedFirstName,  nestedGender
            );
      }
   }
}



