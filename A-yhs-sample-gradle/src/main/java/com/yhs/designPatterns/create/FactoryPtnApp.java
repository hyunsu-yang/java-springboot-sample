package com.yhs.designPatterns.create;

/*
 * actory는 공장이죠. 객체를 막 찍어내는 놈입니다. 객체 선언은 보통 new 객체() 이런식으로 하죠. 
 * factory는 내부에서 그런 일을 해줍니다. 즉 factory를 가져다가 쓰는 부분에서는 new 객체()와 같은 식으로 변수를 선언할 필요가 없습니다. 
 * Abstract class나 인터페이스에 대해서 다양한 하위 구현체가 있을 경우에 사용하면 좋습니다. 사용법은 Factory.create(인자는 맘대로) 와 같이 됩니다. 
 */

/*
Animal을 구현하고 있는 클래스가 늘어나면, if로 인한 분기문도 많이 늘어나게 될텐데요
이런 부분을 해소 할 수 있는 방법은 없을까요?

	제일 간단한 방법은 Class.forName()을 이용하는 방법입니다.
	
	예제는 아래와 같습니다. (exception 처리는 일단 생략합니다.)
	
	public static Animal create(String animalName){
	Class<?> clazz = Class.forName(animalName);
	Animal ret = (Animal)clazz.newInstance();
	return ret;
	}
	
	호출하는 코드는 아래와 같습니다.	
	Animal a4= AnimalFactory.create("ch03_StaticFactory.Cow");
	a4.printDescription();
	
	여기서 문제점은 인자로 클래스의 이름(패키지 포함)을 넘겨줘야 한다는 것입니다. 따라서 나중에 리팩토링을 해서 이름이 바뀌면(클래스 이름이 바뀌는 경우는 흔치는 않지만, 패키지는 바뀌는 경우가 종종있습니다.) 
	호출하는 클라이언트 코드에 가서 전부 수정해주어야 한다는 것입니다. 구찮은 작업이죠.
	
	그래서 일반적으로는 DI 를 사용하는데, 주로 xml에 기술하는 방식입니다. Spring의 예를 보면, xml을 아래와 같이 만듭니다.
	
	<bean id="소" class = "ch03_StaticFactory.Cow">
	</bean>
	
	그리고 factory에서는 처음에 이 xml을 읽어와서 map으로 관리를 합니다. 대략 코드로 설명을 드리자면,
	
	public class AnimalFactory {
	private static Map<String, Class> animalMap = new HashMap<String, Class>();
	static{
	//xml에서 읽어와서 animalMap에 세팅.
	}
	public static Animal create(String animalName){
	return (Animal)animalMap.get(animalName).newInstance();
	}
	}
 */
public class FactoryPtnApp {
	 public static void main(String[] args) {
	        Animal a1= AnimalFactory.create("소");
	        a1.printDescription();
	        Animal a2= AnimalFactory.create("고양이");
	        a2.printDescription();
	        Animal a3= AnimalFactory.create("개");
	        a3.printDescription();
	        
	        Animal b1= AnimalFactory.create("소");
	        System.out.println(a1==b1);
	    }
}

// 정확히 얘기하면 아래는 static Factory method 라고 한다. 그냥 Factory method 는 factory 인스턴스를 생성하여 생산하는 방식.
// 이러한 방식은  factory의 인스턴스에 귀속되는 객체를 생성해야 할 때는 이런 방식을 사용.
class AnimalFactory {
    public static Animal create(String animalName){
        if (animalName == null) {
            throw new IllegalArgumentException("null은 안 되지롱~");
        }
        if (animalName.equals("소")) {
            return new Cow();
        }else if (animalName.equals("고양이")) {
            return new Cat();
        }else if (animalName.equals("개")) {
            return new Dog();
        }else{
            return null;
        }
    }
}

interface Animal {
    public void printDescription();
}


class Cat implements Animal {
    public void printDescription() {
        System.out.println("쥐잡기 선수");
    }
}


class Cow implements Animal {
    public void printDescription() {
        System.out.println("우유 및 고기 제공");
    }
}


class Dog implements Animal {
    public void printDescription() {
        System.out.println("주로 집 지킴");
    }
}

