package com.yhs.designPatterns.structure;

/*
 * 데코레이터 패턴
 	기존에 구현되어 있는 클래스에 기능을 추가하기 위한 패턴입니다. 기존에 있던 클래스를 상속하여 만들기 때문에 기존 클래스와 사용법이 크게 다르지는 않습니다.
 
	 첫째, 하위 클래스는 상위클래스의 형식을 멤버변수로 가집니다. ChildDecorator 는 Decorator를 멤버변수로 받습니다. 
	 일반적으로 생성자의 인자로 받아서 멤버변수로 쎄팅을 합니다. 별도의 setter를 가지는 경우는 거의 없습니다.
	둘째, 하위 클래스는 상위클래스를 상속 받아 상위클래스의 메쏘드를 이용합니다. 하위 클래스의 getMerong() 이라는 메쏘드는
	 상위 클래스의 getMerong()을 오버라이드하지만, 내부적으로 상위클래스의 getMerong()을 사용하고 있습니다.
	 
 Decorator가 일반적인 상속과 다른 점
	Decorator는 메쏘드의 확장 개념입니다. 멤버 변수로 받은 객체의 메쏘드를 이용하여 그 메쏘드를 확장하는 것
	(ava.io에 있는 InputStream, Reader, OutputStream, Writer 등은 모두 Decorator 패턴으로 구성되어 있음)
 */
public class DecoratorPtnApp {

	public static void main(String[] args) {
		 Decorator decorator = new Decorator();
        System.out.println(decorator.getMerong());
        
        Decorator child = new ChildDecorator(decorator);
        System.out.println(child.getMerong());
        
        Decorator child2 = new ChildDecorator(child);
        System.out.println(child2.getMerong());

	}

}

//-------------- 데코레이터 -------------
class Decorator {
    public String getMerong(){
        return "merong";
    }
}

//-------------- 데코레이터를 상속 받은 넘 ----
class ChildDecorator extends Decorator{
    private Decorator decorator;
    public ChildDecorator(Decorator decorator){
        this.decorator = decorator;
    }
    @Override
    public String getMerong(){
        return "@" + decorator.getMerong() + "@";
    }
}