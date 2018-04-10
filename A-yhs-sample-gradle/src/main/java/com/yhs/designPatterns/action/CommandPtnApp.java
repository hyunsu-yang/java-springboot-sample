package com.yhs.designPatterns.action;

/*
 1. 커맨드 패턴(Command Pattern)이란?
일반적으로 객체는 그 객체가 행동할 것에 대해서 메소드를 가지고 있다. 만약 각각의 메소드를 하나의 클래스화 한다면 어떻게 될까?
이런 방식으로 메소드 하나를 클래스로 분리 하는 방식을 커맨드 패턴이라고 한다.  메소드를 클래스와 하면 객체의 행위에 대해 undo,redo, 로깅등의 다양한 조작을 할수 있다.
 메소드를 동적으로 바인딩시키는 것은 전략패턴과도 비슷해 보이지만, 전략패턴은 객체의 다양한 전략에 대해 설정하는 반면에, 커맨드패턴은 단일행위 자체에 집중하고 그 단일 행위에 대한 사전/사후 동작들을 정의하는 방법이다.  

2. 커맨드 패턴과 람다(Lambda) 식과의 관계
 자바8에서 람다식이란 간단히 말해 메소드가 하나만 정의된 인터페이스이다. 인터페이스가 하나라는 것은 커맨드패턴과 유사하다. 
 그래서 사실상 많은 커맨드 패턴들은 자바8에서 Lambda식으로 처리된다. Runnable, Callable 등이 대표적인 예이다. 
 람다식이 되면 코드가 굉장히 간결해져서 가독성이 향상되게 된다. JDK8의 신규모듈들(Nashorn등..)은 이미 람다식을 기반으로 해서 구현이 되어있고,
  이를 통해 많은 소스 코드량을 줄였다고 한다. 앞으로도 JDK의 많은 커맨드 패턴들은 람다식으로 구현될 것이다. 
  
  Examples in JDK
	java.lang.Runnable & java.lang.Thread
	: Runnable은 run이라는 메소드하나를 정의하고 있는 interface객체이다. 사실 strategy라고 볼수도 있고 command 라고도 볼수 있다.
	그러나 메소드가 단일이라는 점에서 command에 더 가깝다고 볼수 있다.
	
	java. util.concurrent.Callable & java.util.concurrent.Future
	: java8에 포함된 Callable은 call()이라는 단일 메소드를 가진 커맨드 패턴 인터페이스이다. (좀더 정확히는 커맨드 패턴 + 중재자 패턴)
	용도는 단일 쓰레드상에서 시간이 걸리는 여러 작업들을 순차적으로 처리하고 싶을때, callback의 복잡성을 피하는 쓰레드 프로그래밍 방식이며,
	쓰레드 위에서 진행되고 있는 타스크의 상황을 확인하거나 해당 타스크를 중간에 취소할수도 있다.
 */
public class CommandPtnApp {

	public static void main(String[] args) {
		StartCar  startCarCommand = new StartCar();

		Car myCar = new Car(startCarCommand); 

		myCar.startCar();
		myCar.doParking();

	}

}


class StartCar {
  public void execute() {
	  System.out.println("start Car");
  }

  public void cancel() {
	  System.out.println("Cancel start Car");

  }
}


class Car {
  private StartCar startCarCommand = null;

  public Car(StartCar startCarCommand) {
    this. startCarCommand = startCarCommand;

  }

  public void startCar() {
    if (startCarCommand!=null) {
      startCarCommand.execute();
    }

  }

  public void doParking() {
     if (startCarCommand!=null) {
       startCarCommand.cancel();

     }
  }

}