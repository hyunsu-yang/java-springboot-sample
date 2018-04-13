package com.yhs.lambda.type;

import java.util.function.Function;

import org.junit.Test;

/*
 람다식 ?
 	람다식은 간단히 말해서 메서드를 하나의 식으로 표현한 것이다.
	메서드를 람다식으로 표현하게 되면 메서드의 이름과 반환값이 없어지므로 람다식을 익명 함수(anonymous function)이라고도 한다
 	메서드에서 이름과 반환타입을 제거하고 매개변수 선언부와 몸통 { } 사이에 ->를 추가
 	람다식의 도입으로 인해 자바는 객체지향언어인 동시에 함수형 언어가 되었다.
  
  내부클래스란(inner class)?
	말 그대로 클래스 내부에 선언된 클래스를 말한다.
	두 클래스가 서로 긴밀한 관계에 있기 때문에 내부에 선언한 것이다.
	내부 클래스를 사용하면 외부 클래스의 멤버들을 쉽게 접근할 수 있다.
	이로 인해 코드의 복잡성이 감소하며 캡슐화를 할 수 있게 된다.
	단, 내부 클래스는 외부 클래스를 제외하고 다른 클래스에서 잘 사용되지 않는 것이어야 한다.

익명 클래스(anonymous class)
	내부 클래스에는 익명 클래스라는 것이 존재한다. 익명 클래스도 마찬가지로 이름 그대로의 역할을 수행한다. 
	클래스의 선언과 객체의 생성을 동시에 하는 이름없는 클래스로, 일회용 클래스인 것이다. 
	이름이 없기 때문에 생성자도 가질 수 없으며, 조상클래스의 이름이나 구현하고자 하는 인터페이스의 이름을 사용해서 정의하기 때문에, 
	하나의 클래스로 상속받는 동시에 인터페이스를 구현하거나 둘 이상의 인터페이스를 구현할 수 없다.
 */
public class LambdaExpTest {

	@Test
	public void anonymousTest() {
		
		// 익며클래스 
		Runnable r = new Runnable() {
			 public void run() {
				 System.out.println("test");
			 }
		};
		
		// 익며클래스
		Function f = new Function<String, Integer>() {
		    @Override
		    public Integer apply(String s) {
		      return Integer.parseInt(s) * 100;
		    }
		  };
	}
	
	@Test
	public void LambdaTest() {
		// 람다식
		// 람다식은 statement 이 아니고 expression 이기 때문에 ; 을 붙이지 앟는다.
		Runnable r2 = () ->  System.out.println("test");
		Runnable r3 = () ->  {System.out.println("test");};
				
		Function<String, Integer> f = str -> Integer.parseInt(str) * 100;
	}

}
