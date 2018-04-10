package com.yhs.designPatterns.structure;

import java.util.HashMap;
import java.util.Map;

import com.yhs.designPatterns.structure.PersonFactory.Person;

/* Flyweight 패턴은
 Flyweight 는 동일한 것을 공유해서 객체 생성을 줄여 가볍게 만드는 것입니다. 클래스 별로 factory를 씁니다. 
 그리고 그 factory에서는 자신이 찍어내는 객체들을 관리합니다. 이미 가지고 있는 객체에 대한 요청이 들어왔을 때는 관리하고 있던 객체를 던져주고, 
 가지고 있지 않은 것을 요청하면 새로 객체를 만들어 관리 리스트에 추가시키고 던져줍니다.
 
 관리하려는 클래스는 Person입니다. Person은 PersonFactory에 의해 관리됩니다. 
 다시 말해 Person이 Flyweight 를 담당하고, PersonFactory가 Flyweight Factory를 담당합니다.
 
 PersonFactory에 getPerson() 메쏘드는 sysnchronized로 처리가 되어있습니다. 
 HashMap이 아니라 Hashtable을 쓰면, 알아서 synchonized가 걸리지만, 그건 Hashtable의 메쏘드 안에서만 걸린다는 뜻입니다. 
 예제에서 녹색처리된 if 블럭이 하나의 synchronized 블럭에 들어가 있어야 하기 때문에 메쏘드에 synchronized가 걸려 있어야 합니다. 
 Hashtable로 해결해서는 안 됩니다. 다시 정리를 하면, if 블럭 안에서 하는 일은 두 단계로 되어있습니다. 
 이미 있는 놈인지 체크를 하고(1단계) 없으면 새로 만듭니다.(2단계) 1,2 단계가 합쳐져서 하나의 synchronized 안에 들어가야 한다는 것입니다. 
 Hashtable을 쓰면, 각각의 단계별로 synchronized가 걸립니다.
 
  Flyweight  추가 사항
	Flyweight 클래스(여기서는 Person)은 가능하면 생성자를 외부로 공개하지 않는 것이 좋습니다. 몇 가지 방법이 있습니다.
	첫째는 위에서 사용한 것처럼 Flyweight Factory에서 내부 클래스로 가지고 있으면서 생성자를 외부로 공개하지 않는 방법입니다.
	두번째는 Flyweight의 클래스 선언은 public으로 생성자는 default(package-private 또는 friendly 라고도 합니다. 접근자를 선언하지 않는 거죠) 로 선언하고,
	 Flyweight Factory 클래스를 같은 패키지 안에 넣는 방법입니다.
 */
public class FlyweightPtnApp {

	public static void main(String[] args) {
		 Person p1 = PersonFactory.getPerson("홍길동");
        Person p2 = PersonFactory.getPerson("김말자");
        Person p3 = PersonFactory.getPerson("홍길동");
        
        System.out.println(p1 == p2);
        System.out.println(p1 == p3);

	}

}

//--------------- Person class 및 Person을 Flyweight로 관리하는 Factory -------------
class PersonFactory {
    private static Map<String, Person> map = new HashMap<String, Person>();
    
    public synchronized static Person getPerson(String name){
        if (!map.containsKey(name)) {
            Person tmp = new Person(name);
            map.put(name, tmp);
        }
        return map.get(name);
    }
    
    public static class Person {
        private final String name;
        private Person(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
}
