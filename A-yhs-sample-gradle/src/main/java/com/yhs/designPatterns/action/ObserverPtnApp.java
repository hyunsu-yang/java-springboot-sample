package com.yhs.designPatterns.action;

import java.util.Observable;
import java.util.Observer;

/*
 옵저버 패턴(Observer Pattern)이란?
주 객체의 변경이 일어날때마다 상태를 확인할수 있는 리스너 객체가 있다고 가정하고, 
리스너 객체를 주객체에 등록하여 객체에 변경이 일어날때마다 리스너 객체의 함수가 불리게 되도록 하는 방식. 
아마도 자바 프로그래머라면 팩토리 메소드 패턴과 더불어 가장 흔하게 사용하고 있는 패턴일 것이다.

어떤 클래스에 변화가 일어났을 때, 다른 클래스에 통보해 주는 패턴입니다. 통보를 하는 "어떤 클래스"가 Observable 이고, 
통보를 받는 "다른 클래스"는 Observer입니다. Observable은 여러개의 Observer를 가질 수 있습니다. 
Observable이 "담임 떴다"를 외치면, Observer은 알아서 그에 걸맞는 행동을 합니다. 

 */
/*
 테스트 코드와 테스트 결과를 보면, observer를 등록한 순서와 통보를 받는 순서가 일치하지 않는 것을 알 수 있습니다. 통보를 받는 순서는 등록 순서와 무관합니다. 
 
 다음은 Employee 클래스를 봅시다.
Observer 인터페이스를 구현하고 있습니다. Observer 인터페이스에는 update(Observable , Object) 메쏘드가 정의되어 있습니다.
첫번째 인자 Observable은 update를 호출해준 Observable 을 말합니다. 여기서는 Watcher 클래스가 되겠습니다. 하나의 Observer는 여러 개의 Observable에 등록될 수가 있습니다.
 "만화책 보는 놈"의 경우는 사장이 떠도 통보를 받아야겠지만, 신간 만화책이 나왔을 때도 통보를 받아야 합니다. 각기 다른 통보인 만큼 할 일이 달라지겠지요. 
 그러나, update 메쏘드를 각각의 Observable에 대응하도록 여러 개를 만들 수 없기 때문에 어떤 일인지를 Observable을 받아서 파악하고, 그에 걸맞는 행동을 합니다. 
 예제에서는 그 Observable이 단지 Watcher 클래스의 인스턴스인지 체크하는 방식으로 구현했습니다.
두번째 인자 Object를 통해서는 구체적인 정보를 받을 수 있습니다.  

이번에는 Watcher 클래스를 봅시다.
일단 Observable이란는 클래스를 상속받습니다. Observable 에는 몇 가지 메쏘드가 있습니다만 여기서는 두가지만 썼습니다. 
setChanged()와 notifyObservers() 입니다. setChanged() 는 변화가 일어났다는 것을 알리는 겁니다. 변화가 일어나지 않으면, 굳이 Observer 들에게 알릴 필요가 없습니다. 
사장이 떴을 때만 setChanged()를 호출하고, Observer들에게 알립니다. (사장인지 아닌 지를 판단하는 로직은 코드가 길어져서 뺐습니다.) 
setChanged()가 호출되지 않고 notifyObservers()가 호출되면, 아무일도 일어나지 않습니다.
다음에 notifyObservers() 메쏘드가 있는데, 이 메쏘는 오버로드되어있습니다. notifyObservers()와 notifyObservers(Object) 두 가지가 있습니다. 
Object에는 어떤 일이 일어났는지 상세 정보를 담을 수 있습니다. 사장이 떴는지, 부장이 떴는 지 정도의 정보를 담을 수 있겠지요. notifyObservers()는 notifyObservers(null) 과 같습니다. 
어찌되었건 notifyObservers(Object)가 호출이 되면, Observer들에게 전부 알립니다. 그러면, Observer들은 각각 자기가 가진 update() 메쏘드가 호출됩니다.

말이 Observer이지 단지 Observerable에게 통보를 받는 입장입니다. 
Observer 들은 Observable에 추가 삭제가 자유롭습니다. Observable 입장에서는 어떤 Observer인지 신경쓰지 않습니다. 
단지 어떤 일이 일어났다는 통보만을 합니다. 통보에 대한 반응은 전적으로 Observer가 합니다. update() 메쏘드가 Observer에 있고, 
notifyObservers() 메쏘드가 Observable에 있는 것이 바로 그런 얘깁니다

 */
public class ObserverPtnApp {

	public static void main(String[] args) {
		 Watcher watcher = new Watcher();
        Employee pc1 = new Employee("만화책보는 놈");
        Employee pc2 = new Employee("퍼질러 자는 놈");
        Employee pc3 = new Employee("포카치는 놈");
        //spy는 pc3을 보고 있음.
        //요놈은 꼰질르기의 대가
        Spy spy = new Spy(pc3);
        
        watcher.addObserver(pc1);
        watcher.addObserver(pc2);
        watcher.addObserver(pc3);
        watcher.addObserver(spy);
        
        watcher.action("사장 뜸.");
        
        watcher.deleteObserver(pc3);
        watcher.deleteObserver(spy);
        
        watcher.action("사장 뜸.");

	}
}

//---------------- 변화를 통보하는 Observable -------------
class Watcher extends Observable {
    public void action(String string) {
        System.out.println("======="+string+"========");
        setChanged();
        notifyObservers(string);
    }
}

//---------------- 변화를 통보받는 직원 -------------
class Employee implements Observer {
    private String desc;
    
    public Employee(String desc) {
        this.desc = desc;
    }
    public void update(Observable o, Object arg) {
        if (o instanceof Watcher) {
            System.out.println(desc + "이 일하는 척");
        }
    }
    public String getDesc() {
        return desc;
    }
}

//---------------- 변화를 통보받는 사장 끄나풀 -------------
class Spy implements Observer {
    private Employee employee;
    
    public Spy(Employee employee) {
        this.employee = employee;
    }
    public void update(Observable o, Object arg) {
        if (o instanceof Watcher) {
            System.out.println("고자질쟁이가 "+employee.getDesc() +"이 놀고 있었다고 고자질.");
        }
    }
}