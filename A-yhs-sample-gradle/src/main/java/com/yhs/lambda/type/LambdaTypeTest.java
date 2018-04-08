package com.yhs.lambda.type;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.junit.Test;

/*
 * 메서드의 타입은 타입 파라미터, 인자 타입, 반환 타입, 예외 타입으로 구성된다.
	함수 타입은 함수형 인터페이스 I의 함수 타입은 I의 추상 메서드를 override 하는데 사용되는 메서드 타입을 의미하며, 함수 타입의 구성 요소는 메서드 타입의 구성 요소와 같다.	
	따라서 타입 파라미터, 인자 타입, 반환 타입, 예외 타입의 호환성 검사를 통해, 어떤 람다나 메서드 레퍼런스가 어떤 함수형 인터페이스 대신 사용될 수 있는지 판단할 수 있다.
	
	타입 파라미터의 호환성		
		함수 타입에 타입 파라미터가 있으면 람다는 호환되지 않고, 메서드 레퍼런스만 호환 가능하다.
		타입 파라미터의 호환성은 제네릭을 참고한다.
		
	인자 타입의 호환성		
		람다식의 인자 타입이 명시적이면, 함수 타입의 인자 타입과 동일해야 한다.		
		명시적이지 않은 인자 타입은 함수 타입의 인자 타입으로 추론될 수 있어야 한다.
	
	반환 타입의 호환성	
		함수 타입의 반환 타입이 void가 아니면, 람다식이나 메서드 레퍼런스의 반환 타입이 함수 타입의 반환 타입에 할당 가능해야 한다.		
		함수 타입의 반환 타입이 void이면		
		람다식의 body가 할당, 전위덧셈, 전위뺄셈, 전위덧셈, 전위뺄셈, 메서드호출, 클래스인스턴스생성, 이렇게 7개 중의 하나에 해당한다면 void 호환성이 적용된다.		
		메서드 레퍼런스는 반환 타입에 무관하게 호환된다.
	
	예외 타입의 호환성	
		람다식이나 메서드 레퍼런스에서 던져지는 예외 타입은 함수 타입의 예외 타입에 할당 가능해야 한다.		
		람다식이나 메서드 레퍼런스에서 사용되는 Unchecked 예외는 예외 타입 호환성과 무관하다.
 */
public class LambdaTypeTest {

	@Test
	public void 람다식타입_함수타입_합동_test() {
		// Integer가 Object를 상속하고 있으므로
		// Object obj = new Integer(3); 은 가능하지만,
		// 아래와 같이 람다식의 인자 타입이 명시된 경우
		// 함수 타입의 인자 타입과 람다식의 인자 타입은 할당 가능이 아니라 일치 해야만 한다.
		//Consumer<Object> consumer1 = (Integer i) -> System.out.println(i);  // 컴파일 에러(Incompatible parameter type)
		
		// 람다식의 인자 타입이 명시되지 않으면 추론에 의해 아래와 같은 람다 사용이 가능하다.
		Consumer<Object> consumer2 = (i) -> System.out.println(i);  // 이건 가능(i가 Object로 추론됨)

		// 람다식의 반환 타입 Integer은 Object에 할당가능하므로 아래와 같은 람다 사용이 가능하다.
		Callable<Object> callable1 = () -> new Integer(1);
		

		// 함수 타입의 반환 타입이 void인 Runnable에 statement expression이 아닌 단순한 값 3은 사용 불가
		//Runnable runnable1 = () -> 3;  // 컴파일 에러(Bad return type)

		// Runnable의 함수 타입의 반환 타입은 void지만, statement expression에 해당하는 인스턴스 생성식은 사용 가능
		Runnable runnable2 = () -> new Integer(3);

		// 함수 타입에 타입 파라미터가 있는 경우 람다를 쓸 수 없다.
	/*	interface Lister {
		    <T> List<T> makeList();
		}
		Lister lister = () -> ...어쩌라고... // 람다식은 언어 차원에서 타입 파라미터가 지원되지 않는다.
*/
		// 함수 타입에 throws가 있는 경우
		
		// 함수 타입에 throws IOException 이 있으므로
		// 아래와 같이 body에서 IOException을 던지는 람다 사용 가능
		WithThrows withThrows1 = () -> {
		    if (1 == 1)
		        throw new IOException();
		    return new Integer(3);
		};
		// 함수 타입의 throws로 지정된 IOException을 상속한 EOFException을 던지는 람다도 사용 가능
		WithThrows withThrows2 = () -> {
		    if (1 == 1)
		        throw new EOFException();
		    return new Integer(1);
		};
		// 함수 타입의 throws로 지정된 IOException을 상속하지 않은 예외를 던지는 람다는 사용 불가
		/*WithThrows withThrows3 = () -> {
		    if (1 == 1)
		        throw new InterruptedException();  // 컴파일 에러(Unhandled exception)
		    return new Integer(1);
		};*/
		// body에서 Unchecked Exception을 던지는 람다는 함수 타입의 예외 타입과 관계 없이 사용 가능 
		WithThrows withThrows4 = () -> {
		    if (1 == 1)
		        throw new RuntimeException();
		    return new Integer(1);
		};
		// body에서 Unchecked Exception을 던지는 람다는 함수 타입에 throws 가 없더라도 사용 가능
		Runnable runnable3 = () -> {
		    if (1 != 1)
		        throw new RuntimeException();
		    System.out.println("Unchecked Exception in a lambda body is OK");
		};
	}
	
	@Test
	public void 메서드레퍼런스타입_함수타입_합동_test() {
		// 함수 타입의 반환 타입이 void이고, 메서드 레퍼런스의 반환 타입도 void
		Runnable runnable4 = System.out::println;

		// 함수 타입의 반환 타입이 void이면, 반환 타입이 void가 아닌 메서드 레퍼런스도 사용 가능
		Integer integer2 = new Integer(2);
		Runnable runnable6 = integer2::doubleValue;

		// 함수 타입에 타입 파라미터가 있는 경우에도 메서드 레퍼런스 사용 가능		
		Lister lister1 = ArrayList::new;

		// 함수 타입의 반환 타입이 void가 아니고, 반환 타입이 함수 타입의 반환 타입에 할당 불가능한 레퍼런스는 사용 불가
		Integer integer1 = new Integer(1);
		//Callable<Integer> callable2 = integer1::doubleValue;  // 컴파일 에러(Bad return type)
		// 함수 타입의 반환 타입이 void가 아니고, 반환 타입이 함수 타입의 반환 타입에 할당 가능한 메서드 레퍼런스는 가능
		Callable<Object> callable3 = integer1::doubleValue;
	}
	
	@Test
	public void 메서드레퍼런스와람다의차이_test() {
		// 람다는 이게 안된다
		//ListFactory lf1 = () -> new ArrayList();  // 컴파일 에러(Target method is generic)
		        
		// 메서드 레퍼런스는 이게 가능하다.
		ListFactory lf2  = ArrayList::new;


		List<String> ls = lf2.make();
		List<Number> ln = lf2.make();
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void 람다의void호환성_test() {
		// 람다의 body가 statement expression이면, 그 람다의 반환 타입이 (무엇이든 상관없이) void인 함수 타입과 호환 된다.
		
		// list.add()는 boolean을 반환하지만 메서드 호출이므로,
		// 반환 타입이 void인 accept(T t) 메서드를 가진 Consumer<T>에 할당 가능
		List list = new ArrayList();
		list.add(1);
		
		Consumer<String> b = s -> list.add(s);
		
		list.add(2);
		b.accept("test");
		System.out.println(b.toString());
	}

}

interface WithThrows {
    Integer makeTrouble() throws IOException;
}

interface Lister {
    <T> List<T> makeList();
}

interface ListFactory {
    <T> List<T> make();
}
