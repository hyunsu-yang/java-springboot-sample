package com.yhs.designPatterns.action;

/*
 * Template Method 패턴
 전체적인 로직에는 큰 차이가 없지만 일부분만 바뀌는 비스무레한 몇 가지 클래스가 있다고 칩시다. 
 일부분을 위해서 전체를 새로 작성할 필요는 없지요. Template Method에서는 전반적인 구현은
  상위클래스(주로 Abstract로 만듭니다.)에서 담당하고 부분적인 곳의 구체적인 구현은 하위클래스가 담당합니다
 
 Worker 클래스의 work()는 내부적으로 abstract 메쏘드인 doit()을 호출하고 있습니다. 
 work() 안에서 전반적인 로직이 수행되고, 로직 중 각각의 특성을 탈 수 있는 부분을 doit() 안에서 해결합니다. 
 doit()은 실제 구현체에서 알아서 구현하면 됩니다.
work() 를 final로 구현한 것은 하위 클래스에서 전체적인 로직 변경을 하지 못하도록 하는 것

Template Method 사용시 고려사항

Template Method는 위험성을 어느 정도 내포하고 있습니다. 바로 전체적인 프로세스가 바뀌는 것입니다. 
상위 클래스에서 변동이 일어날 경우 하위 클래스가 안전하리라는 보장은 할 수 없습니다. 
상위 클래스에 abstract method가 하나만 추가되어도 모든 하위 클래스는 변경이 불가피합니다. 
나중에 발생하는 작은 변경이 큰 재난을 일으킬 수 있습니다. 이것은 상속이 가지는 위험성입니다.
그래서 Template Method 패턴을 사용할 때는 상위클래스에 대한 심사숙고가 반드시 필요합니다. 

일반적으로는 전체적인 프로세스를 담당하는 로직을 final 메쏘드로 정의하기도 하지만, 
프로세스 자체의 변경을 고려해 상속의 여지를 남겨두기 위해 final 메쏘드로 정의하지 않기도 합니다.

또 한가지는 하위 클래스의 메쏘드들은 외부에서 직접 호출되지 않고 상위 클래스의 Template Method에서 호출됩니다.
 그래서 주로 protected 로 선언됩니다. 그런 이유로 외부의 호출과 구체적인 구현체의 메쏘드가 실행되기까지의 과정을
  쉽게 파악하기가 어렵습니다. 문제가 생겼을 때 추적이 어려울 수도 있다는 것이죠.
  
  템플릿 패턴의 장단점
	장점	
	- 코드중복을 크게 줄일수 있다.
	- 자식객체의 롤을 최대한 줄임으로서 핵심로직에 집중한다.
	- 쉽게 자식 객체를 추가, 확장해 나갈수 있다.
	
	단점
	- 구현 클래스가 구현해야 하는 abstact method가 너무 많으면 관리가 곤란하다.
	- 반드시 추상 클래스의 템플릿 메서드에서 구현클래스의 메서드를 부르는 식으로 로직을 구성해야 한다.(상위->하위) 자칫하면 혼선이 생기기 쉽다.
  
 */
public class TemplatePtnApp {

	public static void main(String[] args) {
		 Worker designer = new Designer();
        designer.work(); 
        Worker gamer = new Gamer();
        gamer.work();

	}

}


//------------- 템플릿 메쏘드가 있는 Abstract Class ---------------
abstract class Worker {
    protected abstract void doit();
    
    //final로 구현한 것은 하위 클래스에서 전체적인 로직 변경을 하지 못하도록 하는 것
    public final void work(){
        System.out.println("출근");
        doit();
        System.out.println("퇴근");
    }
}

//------------- Abstract Class 구현체 1 ---------------------
class Designer extends Worker {
    @Override
    protected void doit() {
        System.out.println("열심히 디자인");
    }
}
//------------- Abstract Class 구현체 2 ---------------------
class Gamer extends Worker {
    @Override
    protected void doit(){
        System.out.println("열심히 껨질");
    }
}