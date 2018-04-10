package com.yhs.designPatterns.structure;

/*
  1. 브리지(Bridge) 패턴이란
	브리지 패턴은 "객체의 구현부를 비워놓는 클래스를 선언하고, 구현부는 객체의 생성자로 입력 받는 방식" 이라고 할 수 있다.
	이렇게 하면 객체의 구현부를 다양하게 입력으로 받을수 있기 때문에, 객체와 객체의 구현 로직을 분리(decoupling)할 수 있다.
	브리지 패턴은 두가지 패턴과 비교해 볼필요가 있다.  어댑터 패턴과 전략패턴(Strategy) 패턴이다.

어댑터(Adapter) 패턴과 비교해보자.
	공통점:
	객체를 입력으로 받아, 입력 받은 객체와 매우 관련성이 높은 새로운 클래스 혹은 인터페이스로 참조할 수 있다. 
	
	차이점:
	어댑터 패턴이 기존에 존재하는 대상 객체를 참조할수 있는 새로운 인터페이스를 리턴해 주는 개념이라고 하면, 브리지 패턴은 기존에 존재하는 객체의 참조와는 관련이 없고,
	 객체의 범용성을 증가시키기 위해 구현부를 비워놓고, 객체의 생성시에 구현부를 입력으로 받는 방식이다.

전략(Strategy) 패턴과 비교해보자.
	공통점:
	객체의 행동방식은 입력받은 객체에 의해 정해진다.
	
	차이점:
	전략패턴은 객체의 생성시점이 아닌 동작시점의 행동방식 변경에 초점이 맞춰져 있다. 객체는 언제든지, 행동 객체의 입력에 따라 그 동작방식이 변경될수 있다.
	브리지 패턴은 생성시점에 초점이 맞춰져 있다. 전략 패턴처럼 런타임에 행동이 변경되는 것이 아니라 객체의 생성시점에 입력으로 받는 행동객체에 따라 브리지 객체의 행동방식이 결정된다.
 */
public class BridgePtnApp {

	public static void main(String[] args) {
		 Shape1[] shapes = new Shape1[2];
       shapes[0] = new CircleShape(1, 2, 3, new DrawingAPI1());
       shapes[1] = new CircleShape(5, 7, 11, new DrawingAPI2());
       
       for (Shape1 shape : shapes)
       {
           shape.resizeByPercentage(2.5);
           shape.draw();
       }

	}

}


/** "Implementor" */
interface DrawingAPI
{
    public void drawCircle(double x, double y, double radius);
}

/** "ConcreteImplementor" 1/2 */
class DrawingAPI1 implements DrawingAPI
{
   public void drawCircle(double x, double y, double radius)
   {
        System.out.printf("API1.circle at %f:%f radius %f\n", x, y, radius);
   }
}

/** "ConcreteImplementor" 2/2 */
class DrawingAPI2 implements DrawingAPI
{
   public void drawCircle(double x, double y, double radius)
   {
        System.out.printf("API2.circle at %f:%f radius %f\n", x, y, radius);
   }
}

/** "Abstraction" */
interface Shape1 {
   public void draw();       // low-level
   public void resizeByPercentage(double pct);     // high-level
}

/** "Refined Abstraction" */
class CircleShape implements Shape1
{
   private double x, y, radius;
   private DrawingAPI drawingAPI;
   
   public CircleShape(double x, double y, double radius, DrawingAPI drawingAPI)
   {
       this.x = x;  this.y = y;  this.radius = radius;
       this.drawingAPI = drawingAPI;
   }
   // low-level i.e. Implementation specific
   public void draw()
   {
        drawingAPI.drawCircle(x, y, radius);
   }
   // high-level i.e. Abstraction specific
   public void resizeByPercentage(double pct)
   {
        radius *= pct;
   }
}











