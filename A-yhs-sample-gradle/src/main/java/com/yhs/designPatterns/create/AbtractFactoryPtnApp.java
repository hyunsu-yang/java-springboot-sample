package com.yhs.designPatterns.create;

/*
 * 추상 팩토리 패턴 :
 * 한마디로  팩토리 패턴에 상속구조를 둠으로서 세밀한 팰토리 관리가 가능하게 함.
 
 1. 추상 팩토리 패턴 (Abstract Factory Pattern)
	: 공통기능을 가지고 있지만 약간씩 다른 성격의 다양한 팩토리 인스턴스를 만들어낸후, 그 팩토리 인스턴스를 사용해 객체를 생성하는 방식.
	팩토리 메소드 패턴과의 가장 기본적인 차이점은, 팩토리 메소드 패턴이 클래스가 객체를 생성하는 패턴이라고 하면, 추상 팩토리 패턴에서는 객체가 객체를 만드는 방법이라는 점이다.  객체 => 객체

4. 추상 팩토리 장점
	첫째) 관리용이성 - 클래스 이름대신 팩토리 메소드를 사용해 객체를 생성하기 때문에, 추후 실제 생성되는 객체가 바뀌거나 추가되어도 문제가 없다.
	두번째) 보안성 - 클래스의 대부분의 내용은 숨기고 싶을때, 인터페이스나 abstract를 통해서만 객체에 접근하게 할수 있다.
	셋째) 리소스재활용성 - 팩토리 메소드가 반드시 객체를 새로 생성할 필요는 없고, 상황에 따라 새로 생성될수도, 기존의 것을 리턴할수도 있다.
	넷째) 위의 세가지 모두 팩토리 메서드 패턴과 같으나, 추상 팩토리의 경우 여기 더해 팩토리에 상속 구조를 둠으로서 세밀한 팩토리 관리가 가능하다. 
 
 */
public class AbtractFactoryPtnApp {

	public static void main(String[] args) {
		boolean hasCarLicense = true, hasBikeLicense = false;
		VehicleFactory vf;
		
		if (hasCarLicense == true) 
			  vf = new CarFactory();
		else if (hasBikeLicense == true) 
		  vf = new BikeFactory();
		else
		  vf = null;


		if (vf !=null) 
		  System.out.println("Vehicle name=" + vf.createOne().getName());
	}

}


abstract class Vehicle{
	public abstract String getName();
}

class Car extends Vehicle{	
	public void goBackward() {
		System.out.println("goBackward");
	}

	@Override
	public String getName() {
		return "Car";
	}
}

class Bike extends Vehicle{
	public void doStrunt() {
		System.out.println("doStrunt");	}

	@Override
	public String getName() {
		return "Bike";
	}

}

abstract class VehicleFactory {
	abstract public Vehicle createOne();
}

class CarFactory extends VehicleFactory {
	public Vehicle createOne(){  return new Car(); };

}

class BikeFactory extends VehicleFactory{
	public Vehicle createOne(){ return new Bike(); };
}