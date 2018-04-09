package com.yhs.designPatterns.structure;

/*
 *  어답터 패턴
	
	객체를 참조하는 인터페이스를 변경하는 방법. 객채 어답터 패턴과 클래스 어답터 패턴이 있다. 	
	객체 어댑터는 어댑터객체가 생성될때, 어댑터 객체의 생성로직안에서 대상객체를 새로 생성해서 참조하는 방식이고, 	
	클래스 어댑터는 클래스의 생성시점에 대상객체를 생성하는 것이아니라, 클래스의 선언시에 다중상속을 이용해서 대상 클래스와 외부로 노출할 클래스를 모두 상속하는 방식으로 클래스를 작성하는 방식이다.
	
 	 객체 어답터(Object Adapter) 패턴
	
	객체 A의 형태를 다른 형태의 객체 혹은 인터페이스로 변경해준다.	
	예)  아래에서 보면 기존의 단일 객체들인 LegacyLine과 LegacyRectangle을 확장성있게 이용하기 위해	
	Shape라는 공통인터페이스를 구현하는 Line객체와 Rectangle객체를 선언하고 그 객체들은 각각 내부적으로	
	LegacyLine과 LegacyRectangle 객체를 필드 값으로 가지게 되었다.
	
	클래스 어답터(ClassAdapter) 패턴
	원래 Original 객체를 client에게 접근가능하도록 할때, 하나의 Adapter클래스를 만들어 이 클래스는 client가 접근 가능한 인터페이스는 공개로 하고, 
	Client에게서 감출 인터페이스는 private으로 하여 숨기는 형태가 일반적이다. 이렇게 하기 위해서는 다중상속이 가능해야 한다.	
	c++에서는 다중 상속을 지원하나, java나 c#에서는 지원하지 않는다.
 */
public class ObjectAdapterPtnApp {

	public static void main(String[] args) {
		Shape[] shapes = 
	        {
	            new Line(), new Rectangle()
	        };
	        // A begin and end point from a graphical editor
	        int x1 = 10, y1 = 20;
	        int x2 = 30, y2 = 60;
	        for (int i = 0; i < shapes.length; ++i)
	          shapes[i].draw(x1, y1, x2, y2);

	}

}

class LegacyLine
{
    public void draw(int x1, int y1, int x2, int y2)
    {
        System.out.println("line from (" + x1 + ',' + y1 + ") to (" + x2 + ',' 
          + y2 + ')');
    }
}

class LegacyRectangle
{
    public void draw(int x, int y, int w, int h)
    {
        System.out.println("rectangle at (" + x + ',' + y + ") with width " + w
          + " and height " + h);
    }
}

interface Shape
{
  void draw(int x1, int y1, int x2, int y2);
}

class Line implements Shape
{
	// adapter pattern 은 아래와 같이 변경 대상인 객체를 private 으로 선언하는게 포인트
    private LegacyLine adaptee = new LegacyLine();
    public void draw(int x1, int y1, int x2, int y2)
    {
        adaptee.draw(x1, y1, x2, y2);
    }
}

class Rectangle implements Shape
{
    private LegacyRectangle adaptee = new LegacyRectangle();
    public void draw(int x1, int y1, int x2, int y2)
    {
        adaptee.draw(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1),
          Math.abs(y2 - y1));
    }
}