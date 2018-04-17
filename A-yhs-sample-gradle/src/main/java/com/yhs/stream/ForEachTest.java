package com.yhs.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

/*
 * for-loop를 Stream.forEach()로 바꾸지 말아야 할 3가지 이유
 * 1. 성능의 차이
 * 2. 가독성 - 적어도 대부분의 사람들에게는
 * 3. 디버깅
 */
public class ForEachTest {

	/*
	 * 둘 다 똑같은 결과가 나오지만, “modern” 스타일은 아주 조심해서 써야 한다고 주장하고 싶다.
	 *  다시 말해, Stream의 map()이나 flatMap() 같은 메서드들을 체이닝(chaining)을 통해 조합해서 사용해야할 때처럼 
	 *  내부적, 함수적 반복이 확실히 유리할 때만 “modern” 스타일을 쓰는 것이 좋다는
	 *  
	 *  결론
	 *  하지만 Java8로 마이그레이션하고, 코드에 함수형 스타일을 더 많이 써보고자 한다면, 
	 *  여러가지 이유로 인해 함수형 프로그래밍이 언제나 더 나은 답은 아니라는 것을 알 필요가 있다. 
	 *  사실상 결코 더 나은 답이 될 수 없다. 함수형 프로그래밍은 단지 좀 다른 방식일 뿐!
	 *  언제 함수형 프로그래밍을 쓰는 것이 좋은지, 어떨때 객체지향/절차형 방식을 고수하는 것이 좋은지 직관적으로 이해할 수 있어야 한다
	 */
	@Test
	public void 성능_test() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		 
		long start = System.currentTimeMillis();
		// Old school
		for (Integer i : list)
		    System.out.println(i);
		
		long end = System.currentTimeMillis();
		System.out.println(end -start);
	
		// "Modern"
		start = System.currentTimeMillis();
		list.forEach(System.out::println);
		end = System.currentTimeMillis();
		System.out.println(end -start);
	}
	
	@Test
	public void 가독성_test() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		 
		// Old school
		for (Integer i : list)
		    for (int j = 0; j < i; j++)
		        System.out.println(i * j);
		
		System.out.println("---------");
		
		// "Modern"
		list.forEach(i -> {
		    IntStream.range(0, i).forEach(j -> {
		        System.out.println(i * j);
		    });
		});
	}
	
	/*
	 * 내부 반복(Internal iteration)을 사용하면 겉으로는 드러나지 않지만, 내부적으로 JVM과 라이브러리가 할 일이 많아진다. 
	 * 예제와 같이 상당히 단순한 케이스에서도 modern 방식을 쓰면 복잡한 호출구조가 존재하는데
	 */
	@Test
	public void 디버깅_test() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		 
		// Old school
		for (Integer i : list)
		    for (int j = 0; j < i; j++)
		        System.out.println(i / j);
		 
		// "Modern"
		list.forEach(i -> {
		    IntStream.range(0, i).forEach(j -> {
		        System.out.println(i / j);
		    });
		});
	}

}
