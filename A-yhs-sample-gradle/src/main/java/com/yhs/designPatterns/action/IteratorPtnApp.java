package com.yhs.designPatterns.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 *  여러개의 원소를 가진 자료구조들(자바의 경우 Map,Set,List, array등)에 대해서 데이터 타입에 상관없이 각 원소에 접근할수 있는 패턴.
	
	우리가 알고 있는 일반적인 집합체들은 전부 Iterator를 제공합니다. Set, List 등은 Collection 을 상속 받는데, Collection이 Iteratable을 상속 받기 때문
	
	 걍 있는 거 안 쓰고 굳이  아래처럼 구현한 건 예제 파일을 함 보여줄라고 한 겁니다. 사실은 예제 전체가 억지로 만들어낸 겁니다. 일반적인 집합체를 구현해서 쓰는 일은 거의 없고, 
	JDK 안에 들어 있는 애들을 가져다 쓰는데, 걔들은 거의 대부분 Iterator를 제공하거든요.(Map은 한 다리 건너서 제공합니다.) 
	그래서 Iterator를 직접 구현할 일은 거의 없습니다. 가져다가 쓸 일이 있을 뿐이죠.
	
	이제 Map은 왜 Iterator를 제공하지 않는 지를 살펴보죠. Map은 Set이나 List와는 달리 key-value의 구조입니다. key에 대한 Iterator인지 value에 대한 Iterator인지 구별할 방법이 없죠.
	그래서 아예 제공을 안 합니다. 그러나 Map에는 key에 대해서는 Set<K> keySet()이라는 key를 Set으로 가져오기를 지원하고, value에 대해서는 Collection<V> values() 를 제공합니다. 
	위에서 말씀드렸다시피 Set과 Collection은 둘다 Iterator를 제공합니다.
	
	Enumeration vs Iterator ?
	둘은 굉장히 유사합니다. Enumeration의 경우는 boolean hasMoreElements() 와 E nextElement() 를 제공합니다. Iterator의 hasNext() , next() 에 대응되는 메쏘드들이죠. 
	차이는 두 가집니다. 첫째 Iterator에는 remove()가 있다. 둘째, Iterator의 함수 이름이 훨씬 쉽다.(타이핑 노가다가 쭐어든다.-_-; )
	처음에 Enumeration이 나왔고, 그걸 쫌 편하게 만들어보자한 것이 Iterator랍니다.
 */

public class IteratorPtnApp {
	 public static void main(String[] args) {
		 
		  MagicianList magicians = new MagicianList();
		  magicians.add("이은결");
		  magicians.add("Kevin parker");
		  magicians.add("David Blaine");
		  
		  Iterator<String> iterator = magicians.iterator();
		  while (iterator.hasNext()) {
		   String element = iterator.next();
		   System.out.println(element);
		  }
	    
	 }	 
}

class MagicianList implements Iterable<String> {
	 private List<String> list = new ArrayList<String>();
	 
	 public void add(String name){
	  list.add(name);
	 }
	 
	 public Iterator<String> iterator() {
		  return  new Iterator<String>(){
			   int seq = 0;
			   
			   public boolean hasNext() {
			    return  seq < list.size();
			   }
			   
			   public String next() {
			    return list.get(seq++);
			   }
			   
			   public void remove() {
			        throw new UnsupportedOperationException();
			   }
		  };
	 }
}