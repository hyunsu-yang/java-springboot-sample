package com.yhs.designPatterns.action;

import java.util.ArrayList;
import java.util.List;

/*
 1. 방문자 패턴(Visitor Pattern)이란?
일반적인경우 OOP에서는 객체가 스스로 행위에 대한 내용을 포함하도록 하지만, 객체에 대한 행위의 내용을 외부 클래스로 빼서 객체의 행위를 위임하기도 한다. 
이런 타입의 패턴으로 전략패턴, 커맨드 패턴, 비지터 패턴등이 있다. 셋 모두 객체의 행위를 바깥으로 위임하는 것이지만, 
전략패턴이 하나의 객체에 대해 여러 동작을 하게 하거나(1:N), 커맨드 패턴이 하나의 객체에 대하 하나의 동작(+보조동작)에 대한 설계방식(1:1)인 반면에, 
방문자 패턴은 여러 객체들에 대해 객체의 동작들을 지정하는 방식(N:N) 이다. N대 N이다 보니 구현에 조금 복잡해진다. 
두개의 N에 대해 처리해야 하다보니 두개의 인터페이스(비지터 인터페이스와 엘리먼트 인터페이스)가 필요하다. 

 3. 방문자 패턴 장단점
 
3.1 장점
- 객체 집단 혹은 객체 구조에 대한 업무구현을 객체 외부로 위임할수 있다. (전략 패턴이나 커맨드 패턴보다 더 윗단계)
- command pattern을 전체 집단에 대한 처리 개념으로 확대함으로서,  사용자 입장에서 매우 단순하게 전체 객체구조를 다룰수 있게 한다.
- 업무의 추가나, 업무 대상객체의 추가시에 instanceOf를 사용하지 않아도 되도록 하기 때문에, 안정적이고 확장에 용이한 구조로 만든다.
- visitor의 구별을 위해 instanceOf를 사용하지 않기 때문에, proxy 방식으로 객체의 제어가 가능하다. 
(Proxy방식에서는 객체생성 의존관계를 느슨하게 해줄수 있다. 그런데 instanceOf 방식으로 객체를 판별해버리면 Proxy는 모두 같은 proxy객체로 판별해 버리기 떄문에 문제가 생긴다. )

3.2 단점
- 최초 구조 잡는 것이 쉽지 않다.
- 대상 객체가 추가 될때마다 비지터에도 추가를 해줘야 한다.
- 객체간에 결합도가 높은 편이고, 비지터가 객체의 속성값을 직접 제어하므로 캡슐화가 약해진다. ( 반복자 패턴과는 정 반대)


 복잡한 구조체 안을 돌아다니면서 어떤 일을 해야 할 경우가 있습니다. Visitor는 어떤 구조체에 대해 그 안을 돌아다니면서 어떤 일을 하는 것입니다. 
 이 때, 구조체 1개에 하는 일이 딱 1개라는 보장은 없습니다. 하나의 구조체에 대해 다양한 일들을 할 수 있습니다. 
 하고 싶은 일이 추가된다고 해서 구조체를 변경하는 것은 무리입니다. 이런 때는 Visitor를 추가하면 됩니다. 
 예제에서는 PC의 디렉토리-파일 구조에 대해 야동을 찾는 일을 하는 Visitor를 구현해보았습니다.
 
 */
public class VisitorPtnApp {

	public static void main(String[] args) {
		Composite main = createComposite();
        YadongFinder visitor = new YadongFinder();
        visitor.visit(main);
        for (String string : visitor.getYadongList()) {
            System.out.println(string);
        }
	}
	
	private static Composite createComposite() {
        Composite main = new Composite("C:");
        Composite sub1 = new Composite("Program Files");
        Composite sub2 = new Composite("WINDOWS");
        Composite sub11 = new Composite("Pruna");
        Composite sub21 = new Composite("system32");
        Composite sub111= new Composite("Incoming");

        Leaf leaf1111 = new Leaf("강호동 닮은여자-짱이쁨.avi");
        Leaf leaf1112 = new Leaf("EBS야동특강.avi");
        Leaf leaf211 = new Leaf("야메떼-다이조부.avi");
        Leaf leaf212 = new Leaf("이건 야동아님.jpg");
        
        main.add(sub1);
        main.add(sub2);
        sub1.add(sub11);
        sub2.add(sub21);
        sub11.add(sub111);

        sub111.add(leaf1111);
        sub111.add(leaf1112);
        sub21.add(leaf211);
        sub21.add(leaf212);
        return main;
    }

}

/*
 위의 예제에서 중요한 것은 Visitor의 visit(Acceptor) 와  Acceptor의 accept(Visitor)  입니다.

Visitor는 visit(Acceptor) 를 가지고 있고, Acceptor는 accept(Visitor) 를 가지고 있습니다. 둘의 차이가 헤깔립니다. 
게다가 accept(Visitor) 를 구현해 놓은 것을 보면 아래와 같이 그냥 Visitor한테 자기 자신을 던져버리는 게 하는 일의 전붑니다.

public void accept(Visitor visitor) {
    visitor.visit(this);
}

이렇게 해 놓은 이유는 구조체를 돌아다닐 수 있게 하기 위한 것입니다. 실제로 구조체를 돌아다니는 일은 Visitor에서 담당하게 됩니다. 
만약 이렇게 해놓지 않았다면, Visitor 안에서 구조체를 돌기 위해 재귀적인 호출을 해야만 복잡한 구조를 다 돌 수 있습니다. 
YadongFinder의 visit(Acceptor) 를 보면, 재귀적인 호출은 없습니다. 일반적으로 accept(Visitor)의 구현은 위와 같으며 달라질 일이 거의 없습니다.

Visitor의 visit(Acceptor)나 Acceptor의 accept(Visitor) 중 하나는 구조체를 도는 역할을 해야합니다. 
구조체를 도는 역할은 Visitor의 visit(Accept)에 맡기는 것이 좋습니다. 구조체를 돌면서 하는 일 뿐만 아니라 "구조체를 도는 방법"도 다른 게 할 수도 있기 때문입니다.  
 */

//------------------ Visitor를 받아들일 수 있는 구조체 인터페이스---------------- 
interface Acceptor {
  void accept(Visitor visitor);
}

//------------------ Acceptor를 방문하는 Visitor 인터페이스---------------- 
interface Visitor {
  void visit(Acceptor acceptor);
}

//------------------ Visitor를 받아들일 수 있는 구조체 ---------------- 
abstract class Component implements Acceptor{
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    private String componentName;
    protected List<Component> children = new ArrayList<Component>();
    public Component(String componentName) {
        this.componentName = componentName;
    }
    public String getComponentName() {
        return componentName;
    }
    public abstract void add(Component c);
    public List<Component> getChildren(){
        return children;
    }
}

class Composite extends Component {
    public Composite(String componentName) {
        super(componentName);
    }
    @Override
    public void add(Component c) {
        children.add(c);
    }
}

class Leaf extends Component{
    public Leaf(String componentName) {
        super(componentName);
    }
    @Override
    public void add(Component c) {
        throw new UnsupportedOperationException();
    }
}


//------------------ 야동 찾는 YadongFinder ---------------- 
class YadongFinder implements Visitor {
    private List<String> yadongList = new ArrayList<String>();
    private List<String> currentList = new ArrayList<String>();
    
    public void visit(Acceptor acceptor) {
        if (acceptor instanceof Composite) {
            Composite composite = (Composite) acceptor;
            currentList.add(composite.getComponentName());
            List<Component> children = composite.getChildren();
            for (Component component : children) {
                component.accept(this);
            }
            currentList.remove(currentList.size()-1);
        }else  if (acceptor instanceof Leaf) {
            Leaf leaf = (Leaf) acceptor;
            doSomething(leaf);
        }
    }
    
    protected void doSomething(Leaf leaf){
        if (isYadong(leaf)) {
                String fullPath = getFullPath(leaf);
                yadongList.add(fullPath);
            }
    }
    
    protected String getFullPath(Leaf leaf) {
        StringBuilder fullPath = new StringBuilder();
        for (String element : currentList) {
            fullPath.append(element).append("\\");
        }
        return fullPath.append(leaf.getComponentName()).toString();
    }
    
    private boolean isYadong(Leaf leaf) {
        return leaf.getComponentName().endsWith(".avi");
    }

    public List<String> getYadongList() {
        return yadongList;
    }
}