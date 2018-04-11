/**
 * 
 */
package com.yhs.java8;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
	 선언형으로 연속적인 함수형 작업을 연결하는 방법에는 map과 flatMap이라는 방법이 존재한다.
	map은 한겹으로 둘러쌓여진 값에 대해 연산후 한겹으로 둘러쌓인 객체를 반환하고, 
	flatMap은 두겹으로 둘러쌓여진 값에 대해 적용하여 한겹으로 만드는 방법이다. 
	
	Stream, Optional, CompletableFuture는 이러한 map과 flatMap에 대해 모두 지원하고 있다.
	map과 flapMap은 함수형 프로그램의 핵심개념(모나드)으로 함수형 프로그래밍을 주언어로 생각하고 있다면 꼭 숙지
 */
public class FlatMapTest {
	
	/*
	 Java 8에 포함된 Stream에는 map이 있다.
		각각의 element들에 대해서 입력을 받아 다양한 형태로 출력을 하는것이 map이다. 
		Stream의 map은 정확히는 Stream으로 둘러 쌓인 값들에 대해서 연산을 하여 Steam형태로 반환해주는 메소드이다. 

	Optional에도 map이 있다.
		Optional은 보통 NullPointerException처리를 하지 않기 위한 용도 정도로만 알고 있다. 
		Optional도 map이라는 메소드가 정의되어 있어서 Optional안에 들어있는 값에 다양한 연산을 적용할수 있다. 
		또한 Stream에서 다양한 함수형 처리를 하다보면  Optional이 필수적으로 사용되게 된다. 
	
	CompletableFuture에도 map 이 있다는 것도 알고 있는가?
		비동기형 프로그래밍을 하기 위해 도입된 CompletableFuture에도 map메소드가 존재한다. 이름은 map이 아니라 thenApply로 바뀌었다.
		방식은 map과 완전히 동일하다. 단지 선후관계를 명확하게 표시하기 위해 map이 아닌 then이라는 접두어를 차용했다.
	 */
	@Test
	public void whatisMap() {
		//Stream
		Stream<String> nameStream = Stream.of("Gold","Silver");
		Stream<String> sentenceStream = nameStream.map(name->name+" goes to school.");
		            
		//Optional
		Optional<String> nameOptional = Optional.of("Gold");
		Optional<String> sentenceOptional = nameOptional.map(name->name+" goes to school.");
		    
		//CompletableFuture
		CompletableFuture<String> namePromise =  CompletableFuture.completedFuture("Gold");
		CompletableFuture<String> sentencePromise = namePromise.thenApply(name->name+" goes to school");
		                  
		System.out.println("Stream :"+sentenceStream.reduce((t,u)->t+":"+u));
		System.out.println("Optional :"+sentenceOptional.get());
		System.out.println("CompletableFuture :"+sentencePromise.join());
	}
	
	/*
	 여기서 조금 더 나아가 보자. flatMap이라는 것을 들어본적이 있는가?
		flatMap은 map인데 평평하게 해주는 map이라는 의미이다. 해주는 일은 두겹으로 쌓여있는 객체를 한겹으로 만들어주는 역할이다.
		왜 이런 flatMap이 필요할까? 이유는 함수형 프로그래밍에서는 값을 직접가지고 오는 것이 아니라 값을 가져오는 과정을 함수로 정의하고,
		실제값은 나중에 연산되기 때문에, flatMap이 없이는 두겹으로 쌓인 값에 접근하는 것이 불가능하기 때문이다. 
		map은 오로지 한겹으로 쌓인 값에 접근하는 경우에만 해당된다.

	자바8에서는 Stream과 Optional 모두 flatMap을 지원한다.	
		위 정의에 따르면 Stream이 두겹으로 쌓여있거나 Optional이 두겹으로 쌓여있는 형태를 한겹으로 만들어준다.
		예를 들어 Stream<Stream<String>>은  Stream<String>로,
		 Optional<Optional<Integer>>은 Optional<Integer>로 만들어준다.
	
	또한 CompletableFuture도 flatMap을 지원한다. 
		thenApply의 경우와 비슷하게, thenCompose라는 메소드를 통해 flatMap의 기능을 지원한다 
	 */
	@Test
	public void whatisFlatMap() {
		//Stream
		Stream<Stream<String>> nameSteamOfStream2 = Stream.of(Stream.of("Gold"),Stream.of("Silver"));
		Stream<String> sentenceFlatMapStream = nameSteamOfStream2.flatMap(
		               nameStreamEach->nameStreamEach.map(nameEach->nameEach+" goes to school"));
		//Optional
		Optional<Optional<String>> nameOptionalOfOptional2 = Optional.of(Optional.of("Gold"));
		Optional<String> sentenceFlatMapOptional = nameOptionalOfOptional2.flatMap(
		               nameOptionalEach->nameOptionalEach.map(nameEach->nameEach+" goes to school"));
		//CompletableFuture
		CompletableFuture<CompletableFuture<String>> namePromiseOfPromise2 = 
		               CompletableFuture.completedFuture(CompletableFuture.completedFuture("Gold"));
		CompletableFuture<String> sentenceFlatMapPromise = namePromiseOfPromise2.thenCompose(
		               namePromiseEach->namePromiseEach.thenApply(nameEach->nameEach+" goes to school"));

		System.out.println("Stream: "+sentenceFlatMapStream.reduce((t,u)->t+":"+u));
		System.out.println("Optional: "+sentenceFlatMapOptional.get());
		System.out.println("CompletableFuture: "+sentenceFlatMapPromise.join());
	}
	
	/*
	 flatMap이 필요한 이유		
		위의 예에서 보았듯이,"Gold"나 "Silver"등의 값에 함수적으로 접근하기 위해서는 flatMap이라는 메소드가 필요하다.
		만약 flatMap 없이 두겹으로 감싸진 값들에 접근하려면, 실제 연산으로 얻어진 객체를 통해서만 값에 접근이 가능해진다. 
		
		예를 들면 flatMap이 없이 Stream<Stream<String>>형태에서 String값에 접근하려면 다음과 같이 해야 한다 		
	 */
	@Test
	public void why_we_need_FlatMap() {
		
		Stream<Stream<String>> nameStreamOfStream3 = Stream.of(Stream.of("Gold"),Stream.of("Silver"));
		Stream<Stream<String>> sentenceFlatMapStream3 = nameStreamOfStream3.map(nameStreamEach->nameStreamEach.map(nameEach->nameEach+" goes to school"));
		
		//flatMap 이 아닌 경우는 List 형태로 결과물을 미리 뽑은 후 for 문으로 탐색 출력
		List<Stream<String>> streamList=sentenceFlatMapStream3.collect(Collectors.toList());
		streamList.forEach(stream->stream.forEach(sentence->System.out.println(sentence)));


	}

}
