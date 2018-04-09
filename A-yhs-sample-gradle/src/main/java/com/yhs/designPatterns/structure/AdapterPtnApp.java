package com.yhs.designPatterns.structure;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/* Adapter 패턴
 * 미 구현되어 있는 코드가 있는데, 둘이 연결 좀 시켜주고 싶을 때가 있죠. 어떤 좋은 메쏘드가 있는데, 인자로 A라는 형식을 받습니다. 
 * 근데, 이미 구현되어 있는 코드에는 B라는 형식으로 구현되어 있습니다. 
 * 이럴 때, B를 A의 형식으로 바꿔주면 좋은 메쏘드를 써먹을 수 있습니다. 
 * Adapter 패턴은 어떤 오브젝트를 캐스팅이 불가능한 다른 클래스의 형태로 변환시켜주는 것입니다.
 * 
 * 우리가 최종적으로 쓰고자하는 것은 goodMethod() 입니다. 그 녀석은 인자로 Enumeration을 받고 있지요. 
 * 그러나 우리가 가지고 있는 것은 Iterator입니다. IteratorToEnumeration 클래스는 Iterator를 받아서 Enumeration 으로 변경시켜줍니다. 
	AtoB의 형태를 가지는 Adapter는 A를 멤버변수로 가지고 B를 구현합니다. 
	
	Adapter를 구현하는 방법
	위에서 소개된 방법은 "구성을 통한 방법" 또는 "위임을 통한 방법"입니다. Adapter 자체는 하는 일이 별로 없습니다. 
	내부적으로  멤버한테다가 일을 다 떠넘깁니다. 외관상 다른 형태로 변환가능하기 위한 것이지 어떤 일을 직접할려는 것은 아닙니다.

	두번째 방법은 상속을 이용하는 방법입니다. A to B로 할 경우 A와 B를 둘다 구현하는 방법입니다. A와 B가 둘 다 인터페이스거나, 하나만 인터페이스일 때는 가능하지만, 둘 다 클래스일 경우에는 불가능하죠. 
	상속을 쓰는 것은 바람직하지 않습니다. "상속보다는 구성을 이용하라"는 원칙에 어긋납니다. 이 원칙에 대해서는 다음 설명드리죠.
	
	세번째 방법은 Adapter 클래스를 만들지 않고 method로 만드는 방법입니다.  다음과 같은 코드는 위에서 구현한 것과 같은 효과를 보여주죠.
	
	    public static Enumeration<String> iteratorToEnumeration(final Iterator<String> iter) {
	        return new Enumeration<String>() {
	            public boolean hasMoreElements() {
	                return iter.hasNext();
	            }
	
	            public String nextElement() {
	                return iter.next();
	            }
	        };
	    }
	세번째 방법은 Adapter 패턴이라고 불리지는 않습니다. 그러나 하는 일이 똑같죠.
	 세번째 방법을 통한 케이스는 무수히 많습니다. Integer, Float, Long 등과 같은 Wrapper 클래스들이 잔뜩 있지요.
	Interger.valueOf(String) 은 String을 Integer로 바꿔주지요
	
 */
public class AdapterPtnApp {

	public static void main(String[] args) {
		  List<String> list = new ArrayList<String>();
	        list.add("이은결");
	        list.add("Kevin parker");
	        list.add("David Blaine");
	        
	        Enumeration<String> ite = new IteratorToEnumeration(list.iterator());
	        Test.goodMethod(ite);
	}

}

//---------------Adapter Class --------------------
class IteratorToEnumeration implements Enumeration<String>{
	// adapter pattern 은 아래와 같이 변경 대상인 객체를 private 으로 선언하는게 포인트
    private Iterator<String> iter;
    
    public IteratorToEnumeration(Iterator<String> iter) {
        this.iter = iter;
    }
    
    public boolean hasMoreElements() {
        return iter.hasNext();
    }
    
    public String nextElement() {
        return iter.next();
    }
}

//---------------뭔가 훌륭한 method를 가지고 있는 클래스 ------------
class Test {
    public static void goodMethod(Enumeration<String> enu){
        while (enu.hasMoreElements()) {
            System.out.println(enu.nextElement());
        }
    }
}