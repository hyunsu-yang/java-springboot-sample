package com.yhs.stream;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class StreamBasicTest {
	Stream<String> stream;
	
	@Before
	public void init() {
		stream = Stream.of("Using", "Stream", "API", "From", "Java8");
	}
	
	@Test
	public void 스트림생성() {
		//컬렉션을 메서드로 변환
		String contents = "test";
		Stream<String> stream = Stream.of(contents.split("[\\P{L}]+"));
		
		//of 메서드는 가변 인자 파라미터를 받아 스트림을 생성
		Stream<String> stream1 = Stream.of("Using", "Stream", "API", "From", "Java8");
		
		//배열의 일부에서 스트림을 생성
		String[] wordArray = {"Using", "Stream", "API", "From", "Java8"};
		// Arrays.stream(array, from, to);
		Stream<String> stream2 = Arrays.stream(wordArray, 0, 4);
		
		//무한 스트림을 만드는 generate 정적 메서드(Supplier<T>)
		Stream<String> stream4 = Stream.generate(() -> "Stream");
		Stream<Double> stream5 = Stream.generate(Math::random);
		
		//무한 스트림을 만드는 iterate 정적 메서드(UnaryOperator<T>)
		// Seed 값과 함수를 받고, 해당 함수를 이전 결과에 반복적으로 적용
		Stream<BigInteger> stream6 = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
	}
	
	@Test
	public void filter_map_flatmap() {		
		// 특정 조건과 일치하는 모든 요소를 담은 새로운 스트림을 반환
		// 필터의 인자는 Predicate<T>, 즉 T를 받고 boolean
		Stream<String> longStream = stream.filter(w -> w.length() > 5);
		
		// 스트림에 있는 값들을 특정 방식으로 변환하여 새로운 스트림을 반환
		Stream<Character> mapStream = stream.map(s -> s.charAt(0));
		
		// 스트림들을 하나의 스트림으로 합쳐서 하나의 새로운 스트림을 반환
		Stream<Character> flatMapStream = stream.flatMap(w -> characterStream(w));
	}
	
	private Stream<Character> characterStream(String s) {
		List<Character> result = new ArrayList<>();
		for (char c : s.toCharArray()) {
			result.add(c);
		}
		return result.stream();
	}
	
	@Test
	public void 서브스트림추출과스트림결합() {
		// n개 요소 이후 끝나는 새로운 스트림을 반환
		Stream<Double> limitStream = Stream.generate(Math::random).limit(10);
		
		// n개 요소를 버린 후 이어지는 스트림을 반환
		Stream<String> skipStream = stream.skip(3);
		
		// 두 스트림을 연결하여 새로운 스트림을 반환
		Stream<String> concatStream = Stream.concat(stream, stream);
	}
	
	//앞에서 살펴본 스트림 변환은 무상태 변환이다. 다시 말해 필터링 또는 매핑된 스트림에서 요소를 추출할 때 결과가 이전 요소에 의존하지 않는다. 몇 가지 상태 유지 변환도 존재한다.
	@Test
	public void 상태유지변환() {
		// 중복 값을 제거한 새로운 스트림을 반환
		Stream<String> distinctStream = stream.distinct();
		
		// 정렬된 새로운 스트림을 반환
		Stream<String> sortedStream = stream.sorted(Comparator.comparing((String value) -> value.length()).reversed());
	}
	
	//리덕션 메서드는 스트림을 프로그램에서 사용할 수 있는 값으로 리듀스한다. 리덕션은 최종 연산이다. 
	//최종 연산을 적용한 후에는 스트림을 사용할 수 없다. 이들 메서드는 전체 스트림을 검사하지만 여전히 병렬 실행(parallel())을 통해 이점을 얻을 수 있다.
	@Test
	public void 단순리덕션() {
		//stream.count()
		// 스트림의 요소 갯수를 리턴
		long count = stream.count();
		
		//stream.max()
		// 스트림에서 최대값을 리턴
		Optional<String> max = stream.max(String::compareToIgnoreCase);
		if (max.isPresent()) {
		    System.out.println("max: " + max.get());
		}
		
		//stream.min()
		// 스트림에서 최소값을 리턴
		Optional<String> min = stream.min(String::compareToIgnoreCase);
		if (min.isPresent()) {
		    System.out.println("min: " + min.get());
		}
		
		//stream.findFirst()
		// 스트림에서 비어있지 않은 첫번째 값을 반환
		Optional<String> startWithS = stream.filter(s -> s.startsWith("S")).findFirst();
		if (startWithS.isPresent()) {
		    System.out.println("findFirst: " + startWithS.get());
		}
		
		//stream.findAny()
		// 스트림에서 순서에 상관없이 일치하는 값 하나를 반환
		Optional<String> startWithSs = stream.filter(s -> s.startsWith("S")).findAny();
		if (startWithSs.isPresent()) {
		    System.out.println("findAny: " + startWithSs.get());
		}
		
		//stream.anyMath()
		// 스트림에서 일치하는 요소가 있는지 여부를 반환
		boolean aWordStartWithS = stream.anyMatch(s -> s.startsWith("S"));
	}
	
	/*
	 //Stream.reduce()
		//스트림의 요소들을 다른 방법으로 결합하고 싶은 경우는 reduce 메서드들 중 하나를 사용하면 된다. 가장 단순한 형태는 이항 함수를 받아서 처음 두 요소부터 시작하여 계쏙해서 해당 함수를 적용한다.
		//reduce 메서드가 리덕션 연산 op를 가지면, 해당 리덕션은 V0 op V1 op V2 op V3 op … 를 돌려준다. 연산 op는 결합 법칙을 지원해야 한다. 즉 결합하는 순서는 문제가 되지 않아야 한다. 
	 */
	@Test
	public void 리덕션연산() {
		
		//Optional<T> reduce(BinaryOperator<T> accumulator);		
		Stream<Integer> stream = Stream.of(1,2,3,4,5);
		Optional<Integer> sum = stream.reduce((x, y) -> x + y);
		
		// Stream 으로 선언하면 리덕선 후 값이 사라진다.
		stream = Stream.of(1,2,3,4,5);
		Optional<Integer> sum2 = stream.reduce(Integer::sum);
		int summ = sum2.get();
		
		//e op x = x와 같은 항등값이 존재할 때는 첫번째 인자로 항등값을 넣어줄 수 있다. 그러면 반환 값으로 Optional<T>가 아닌 T를 받을 수 있다.
		//T reduce(T identity, BinaryOperator<T> accumulator);
		stream = Stream.of(1,2,3,4,5);
		int sum3 = stream.reduce(0, Integer::sum);
		
		//스트림은 병렬화하기 쉽다는 장점이 있다. 값이 누적되는 연산인 경우는 대부분 바로 병렬화를 할 수 없다. 이 경우는 각 부분의 결과를 결합하도록 사용하는 함수를 3번재 인자로 넣어주어야 한다.
		/*<U> U reduce(U identity,
					 BiFunction<U, ? super T, U> accumulator,
					 BinaryOperator<U> combiner);*/

		List<String> wordList = Arrays.asList("test", "test2");
		
		int result = wordList.parallelStream().reduce(0,
						(Integer total, String word) -> total + word.length(),
						(Integer total1, Integer total2) -> total1 + total2);
		
		//실전에서는 reduce 메서드를 많이 사용하지 않을 것이다. 보통은 숫자 스트림에 매핑한 후에 각 값을 계산해주는 메서드를 이용하는 것이 더 쉽다.
		result = wordList.stream().mapToInt(str -> str.length()).sum();
		
		System.out.println("end");
		
	}
	
	/*
	 Stream.collect()
		병렬화를 지원하면서 한 객체의 스트림의 요소들을 모으려고 할 때 collect 메서드를 사용하게된다. collect 메서드는 세 가지 인자를 받는다.
		
		공급자: 대상 객체의 새로운 인스턴스를 만든다.
		누산자: 요소를 대상에 추가한다.
		결합자: 두 객체를 하나로 병합한다. 
	 */
	@Test
	public void collect() {
		String[] result = stream.toArray(String[]::new);
		
		//StringBuilder와 같이 카운트와 합계를 관리하는 객체라면 collect의 대상이 될 수 있다.
		HashSet<String> result1 = stream.collect(HashSet::new, HashSet::add, HashSet::addAll);
		
		//자바에는 세 함수를 제공하는 인터페이스를 가진 Collectors 클래스가 존재한다. 일일이 공급자, 누산자, 결합자를 지정할 필요 없이 간편하게 호출할 수 있다.		
		List<String> result2 = stream.collect(Collectors.toList());
		Set<String> result3 = stream.collect(Collectors.toSet());
		TreeSet<String> result4 = stream.collect(Collectors.toCollection(TreeSet::new));
		
		//결과 값을 하나의 문자열로 모으는 joining메서드도 존재한다.		
		String result5 = stream.collect(Collectors.joining());
		String result6 = stream.collect(Collectors.joining(", "));
		
		/*Stream.forEach(), Stream.forEachOrdered()
		하나 하나의 값에 연산을 하는 방법도 있다. forEach의 경우에는 병렬스트림에서 순서를 보장할 수 없다. 
		스트림 순서대로 조회하고 싶은 경우에는 forEachOrdered를 사용해야 한다. 하지만 이경우는 병렬성이 주는 대부분의 이점을 포기해야 한다.*/
		stream.forEach(System.out::println);
		stream.forEachOrdered(System.out::println);	
		
		//두 메서드 모두 최종 연산으로 스트림을 재사용할 수없다. 만약 재사용을 하고 싶다면 peek 메서드를 사용해야 한다.
		Object[] powers = Stream.iterate(1., p -> p * 2)
			.peek(e -> System.out.println("Fetching " + e))
			.limit(20).toArray();
	}
	
	/*
	 기본 타입 스트림
	스트림 라이브러리는 기본 타입 값들에 특화된 IntStream, LongStream, DoubleStream을 포함한다. short, char(인코딩의 코드단위로 이용), byte, boolen의 경우는 Intstream을 이용한다. 
	float인 경우는 DoubleStream을 이용한다. 다음은 기본적인 정적 스트림 생성 예제이다.	 
	 */
	@Test
	public void basicStream() {
		int[] array = {1,2,3};
		
		IntStream result = IntStream.of(1, 2, 3, 4, 5);
		IntStream result2 = Arrays.stream(array, 0, 5);
		
		//다음은 크기 증가 단위가 1인 정수 범위인 정적 스트림을 생성하는 예제이다.

		IntStream resul3t = IntStream.range(0, 5); // 최대값 제외
		IntStream result4 = IntStream.rangeClosed(1, 5); // 최대값 포함
		//다음은 객체 스트림을 기본 타입 스트림으로 변환하는 예제이다.

		IntStream result5 = stream.mapToInt(String::length);
		
	}
	
	@Test
	public void groupByStream() {
		/*
		 그룹핑과 파티셔닝
			Collectors는 비슷한 성질의 원소들을 분류하는 메서드를 지원한다.
			
			Collectors.partitionBy()
			Collectors.partitionBy()는 분류함수가 boolean을 반환할 경우 유요항다.
			
			Map<Boolean, List<Locale>> result = locales.collect(
					Collectors.partitioningBy((Locale l) -> l.getLanguage().equals("en")));
			Collectors.groupingBy()
			Collectors.groupingBy()는 분류함수의 반환값에 따라 그루핑한다.
			
			Map<String, List<Locale>> result = locales.collect(
					Collectors.groupingBy(Locale::getCountry));
			Collectors.groupingBy()는 기본적으로 List형태로 그루핑을 하나 다운스트림 컬렉터를 통해 특정 방식으로 처리가 가능하다. 아래 예제 말고도 Collectors.maxBy(), Collectors.mapping()등의 다운스트림 컬렉터가 존재하다.
			
			Map<String, Set<Locale>> result = locales.collect(
					Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));
			
			
			Map<String, Long> result = locales.collect(
					Collectors.groupingBy(Locale::getCountry, Collectors.counting()));
		 */
	}
	
	@Test
	public void Optional() {
		/*
		 Optional<T>.get() 
		get 메서드는 감싸고 있는 요소가 존재할 때는 요소를 반환하고 없을 경우는 NoSuchElementException을 던진다.
		
		 
		Optional<T> optionalValue = ...;
		optionalValue.get().someMethod();
		
		그러므로 위 예제는 다음 예제보다 안전할 것이 없다.		
		T value = ...;
		value.someMethod();
		
		Optional<T>.isPresent()		
		isPresent 메서드는 Optional 객체가 값을 포함하는지 알려준다.
		
		if(optionalValue.isPresent()) {
			optionalValue.get().someMethod();
		}
		
		하지만 다음 예제와 크게 달라보이진 않는다.		
		if(value != null) {
			value.someMethod();
		}
		Optional<T>.ifPresent()
		
		// 옵션 값이 존재하면 해당 함수로 전달되며, 그렇지 않으면 아무 일도 일어나지 않음
		optionalValue.ifPresent( v -> results.add(v));
		optionalValue.ifPresent(results::add);
		Optional<T>.map()
		
		// 값이 존재하면 해당 함수를 호출한 후, Optional<T>를 리턴
		// added에는 true, false, null을 가진 Optional을 가질 수 있음
		Optional<Boolean> added = optionalValue.map(results::add);
		Optional<T>.orElse()
		
		// 감싸고 있는 문자열, 또는 문자열이 없는 경우는 ""를 리턴
		String result = optionalValue.orElse("");
		Optional<T>.orElseGet()
		
		// 감싸고 있는 문자열, 또는 문자열이 없는 경우는 함수를 호출
		String result = optionalValue.orElseGet(() -> System.getProperty("user.dir"));
		Optional<T>.orElseThrow()
		
		// 감싸고 있는 문자열, 또는 문자열이 없는 경우는 예외를 발생
		String result = optionalValue.orElseThrow(NoSuchElementException::new);
		Optional<T>.of(), Optional<T>.empty()
		
		public static Optional<Double> inverse(Double x) {
			return x == 0 ? Optional.empty() : Optional.of(1 / x);
		}
		Optional<T>.ofNullable()
		
		// obj가 null이면 Optional.empty()를, null이 아니면 Optional.of(obj)를 반환
		Optional<String> optionalValue = Optional.ofNullable(obj); 
		 */
	}

}
