package com.yhs.designPatterns.action;


/*
 Mediator 패턴은..

비행기가 이착륙하다가 충돌하는 일은 좀체로 일어나지 않습니다. 비행기들끼리 서로 통신하지 않는데도 말이죠. 
각각의 비행기는 관제탑하고만 통신을 하고, 관제탑이 각각의 비행기에게 착륙해도 된다 또는 안 된다 식으로 메시지를 보내줍니다. 
비행기들끼리 서로서로 직접 통신을 한다면 통신할 경우의 수가 무진장 많아져서 혼란스럽게 됩니다. 
Mediator 패턴은 관제탑과 같이 통신을 집중시킴으로써 통신의 경로를 줄이고 단순화시키는 역할을 합니다. 

 1.중재자 패턴은
    "서로 전혀 다른 객체간에  상호 통신이 매우 복잡할때 중재자를 통해 객체간의 통신을 가능하도록 캡슐화하는 방법"이다.
	좀 복잡하게 들릴수 있지만, 간단하게 말하면 두 객체를 직접 연결을 하지 않고, 제 3의 객체를 통해 느슨하게 연결하는 것이다. 
	가장 대표적인 자바의  중재자 구현 구현은 JMS(Java Message Server) 이다. JMS는 객체가 서로에 대해 레퍼런스를 가지고 있지 않더라도, 
	중간에 JMS서버를 두고 message를 받는 쪽은 subscribt, 보내는 쪽은 publcation 방식으로 메시지를 주고 받을 수 있다. 
	즉 Sender - JMS - receiver 를 통한 통신이 가능한 것

2. 중재자 패턴을 위한 요소
	- 중재자 (Mediator)
	: 실제 중재가 구현 로직에 대한 인터페이스로, 중재자의 서비스를 받기 바라는 클라이언트를 등록,실행하는 API가 정의되어있다.
	
	- 중재자에 대한 구현체(Concrete Mediator)
	: 중재자 인터페이스를 실제로 구현한 클래스
	
	- 중재자에 참여하는 동료 클래스들(Colleague Classes)
	: 중재자에 의해 서비스를 받기를 원하는 대상 클래스들

4. 중재자 패턴의 장단점

	장점: 	
	효율적인 자원 관리(리소스 풀등)를 가능하게 한다.	
	객체간의 통신을 위해 서로간에 직접 참조할 필요가 없게 한다.	
	중재자 구현 클래스는 추후에 더 효율적인 클래스로 변경될수 있다.	
	
	단점:	
	객체간의 통신 로직이 복잡해지거나 객체의 형태가 자주 변경되는 경우 유지보수,관리가 어렵다.
 */
public class MediatorPtnApp {

	public static void main(String[] args) {
		 ControlTower tower = new ControlTower();
        Airplane[] airplanes = new Airplane[10];
        
        for (int i = 0; i < airplanes.length; i++) {
            airplanes[i] = new Airplane(tower, i);
        }
        
        for (Airplane airplane : airplanes) {
            airplane.start();
        }

	}

}

/*
 위의 예제를 좀 더 확장해서 생각해 봅시다.

첫번째로 Airplane 은 ControlTower가 한 번 세팅이 되면 바꿀 방법이 없습니다. 위의 예제만으로는 비행기는 일회용이겠죠. 
다른 공항에 착륙하고자할 때는 비행기가 통신할 관제탑이 바뀔 수도 있습니다. 그래서 보통은 Colleague 클래스는 setMediator 와 같은 메쏘드를 가지고 있습니다.

두번째로는 callback입니다. 예제에서는 비행기들이 관제탑에 "나 착륙해도 되?" 라고 계속 물어봅니다. "된다"라는 대답을 들을 때까지! 관제탑에서는 정신없습니다. (녹색부분의 주석을 풀어보면 얼마나 정신없는 지 바로 보입니다.)  
질문 방식을 바꾸면 어떨까요? 비행기가 "나 착륙할라고 하는데 활주로 비면 연락줘. 기둥기께" 라고 관제탑에 메시지를 보내고 관제탑은 그 비행기를 착륙 대기 리스트에 추가시켜 놓고, 
리스트 앞에서 부터 각각의 비행기에게 "너 인제 착륙해도 된다"는 메시지를 보내면 됩니다. 이렇게 하면 통신 횟수를 확 줄일 수 있습니다.
callback은 명령을 내리고 임무를 완료하면 다시 연락하라는 겁니다. 짜장면 주문을 예로 들어보겠습니다. 짜장면집에 전화를 걸고, 주문을 합니다. 
우리는 짜장면이 올 때까지 딴짓을 합니다. 짜장면이 배달오면 배달원이 초인종을 누릅니다. "주문" 이 명령이고, "배달"이 임무 완료입니다. "내"가 "짜장면집"에 명령을 하고 "짜장면집"은 그 명령을 수행합니다. 
"짜장면집"이 임무를 완수하면 "나"한테 "다시 연락"을 줍니다. 임무가 수행되는 동안 "나"는 "짜장면집"에서 짜장면을 잘 만들고 있는 지 신경을 쓰지 않습니다.

 세번째는 활주로가 1개 뿐이라는 겁니다. 활주로가 여러개라면 Runway 라는 활주로 클래스를 만들어야 합니다. 이 활주로는 당연히 ControlTower와 통신을 해야 합니다.  
 ControlTower가 어떤 비행기가 착륙 요청했다는 정보를 활주로에 알려주고, 비행기한테는 어떤 활주로에 착륙해라 라고 비행기와 활주로 사이에 관계를 맺어줍니다. 
 실제 착륙 작업에서는 더 이상 관제탑이 관여할 게 없습니다. 다만, 착륙이 완료 되면 활주로한테 이제 활주로가 다시 사용가능하다는 callback은 받아야겠죠. 
이 경우 Runwary도 Colleague가 되어야 합니다. Colleague는 한 가지 특정타입(예제의 경우 Airplane)으로 국한될 필요는 없습니다.
Airplane의 getPermission은 getAvailableRunway로 바뀌고 리턴 타입역시 Runway로 바뀌어야겠습니다. 사용가능한 활주로가 없을 때는 null을 리턴한다거나 하면 되겠습니다.
그리고 land 메쏘드도 ControlTower가 아닌 Runway로 옮겨가야겠지요. 그리고 land 안에는 ControlTower에게 착륙이 끝났다는 정보를 전달할 수 있는 로직이 필요합니다.

3. 수 많은 Adapter가 필요한 경우
A,B,C 3개 회사가 합병을 했다 칩시다. 각각의 회원 정보를 다음과 같은 인터페이스로 정의해서 사용합니다.

interface ACompanyUser{     String getName();  }
interface BCompanyUser{     String getName();  }
interface CCompanyUser{     String getName();  }

이제 하나가 된 만큼 회원 정보를 서로 공유해야 하는데, 각 회사의 시스템은 예전 자기 회사의 인터페이스만을 받아들이도록 되어있습니다. 서로 캐스팅이 불가능한 객체를 캐스팅하고 싶을 때 Adapter 패턴을 씁니다. 
다음과 같은 6개의 Adapter가 필요합니다.

AToBUser
AToCuser
BToAUser
BToCUser
CToAUser
CToBUser

아... 복잡해지기 시작합니다. 중계 객체를 하나 두면 어떨까요? 중계 객체를 M이라 합시다. 즉, A에서 B로 바꾸려면 위에서는 AToBUser 를 통해서 바꾸면 되었지만, 이제 AToM , MToB와 같이 두 단계를 거쳐서 만들면 됩니다. 
단계가 늘어났지만 뭔가 좋은 게 있을 겁니다.

AToM
BToM
CToM
MToA
MToB
MToC

얼레? 똑같이 6갭니다. 그러나 갯수가 많아지면 얘기가 달라집니다. N개의 회사가 있다고 하면, 직접 변환을 할 경우 N*(N-1) 개의 인터페이스가 필요합니다. 그러나, 중계 객체를 만들면 2*N개만 있으면 됩니다. 
4개의 회사의 경우 12개 - 8개 , 5개 회사는 20개-10개 와 같이 갯수가 많아 질수록 중계 객체를 두는 게 유리해집니다.
 */

//------------------ 관제탑 역할을 하는 ControlTower (활주로 역할도 함) ---------------- 
class ControlTower {
    private volatile  boolean inUse;
    
    public synchronized boolean getPermission(){
        if (inUse) {
            return false;
        }else{
            inUse = true;
            return true;
        }
    }
        
    public void land(Airplane airplane) throws InterruptedException{
        int seq = airplane.getSeq();
        System.out.println(seq +"번 비행기 착륙 시작");
        Thread.sleep(50L);
        System.out.println(seq + "번 비행기 착륙 끝");
        inUse = false;
    }
}

//------------------ 착륙허가를 받아야하는 Airplane ---------------- 
class Airplane extends Thread {
    private final ControlTower tower;
    private final int seq;

    public Airplane(ControlTower tower, int seq) {
        this.tower = tower;
        this.seq = seq;
    }

    public int getSeq() {
        return seq;
    }

    @Override
    public void run() {
        try {
            while (!tower.getPermission()) {
                // System.out.println(seq +"번 째 비행기 대기 중.");
                Thread.sleep(10L);
            }
            tower.land(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}