package com.yhs.designPatterns.action;

/*
 Chain of Responsiblity 패턴에서는
  문제 해결사들이 한줄로 쫙 서있다가 문제가 들어오면, 자기가 해결할 수 있으면 해결하고, 안 되면 다음 해결사에게 문제를 넘겨버립니다.
 
	Expert 클래스는 마치 Decorator 패턴 처럼 Expert를 멤버 변수로 가지고 있습니다. 그러나 Decoarator와는 달리 그 값이 null일 수도 있습니다. 
	다음 전문가가 없을 수도 있는 거죠. 위의 코드에서는 casanova가 마지막 전문가입니다. 즉, casanova는 next라는 변수 값이 null입니다. 
	일반적인 set머시기 하는 메쏘드들은 리턴 타입이 void인데, 여기서는 리턴 타입이 Expert입니다. 예제코드처럼 전문가 그룹을 연결시키는 코드를 한줄로 만들기 위해서입니다. 
	만약 리턴 타입이 void였다면, 황토색 부분은 아래와 같이 두 줄로 바뀔 것입니다.
		
	fighter.setNext(hacker);
	hacker.setNext(casanova);
	
	Expert 클래스의 support() 가 하는 일은 자기가 해결할 수 있으면 하고, 못하면 다음 전문가한테 넘기고, 떠넘길 다음 전문가가 없으면, 못한다고 생떼를 쓰는 겁니다. 내부적으로 sovle()메쏘드를 호출합니다.
	solve()는 각각의 개별 클래스별로 자기가 해결 가능한지 불가능한지를 판단하는 매쏘드입니다. 당연히 구체적으로 기술해야 하므로 하위 객체에 떠넘깁니다. 
	
	3. Chain of Responsibility의 특징
		위의 예제에서는 폭탄 앞에 전문가들이 좌절해야 했습니다. 기존에 있던 애들은 그대로 두고, 폭탄전문가를 한 명 영입하면 일이 해결될 것 같습니다. 
		폭탄전문가 클래스를 새로 만들어서 테스트 클래스에 넣어주면 일이 해결됩니다. 또 별로 쓸모 없는 전문가를 영입했을 때는 짜르기도 쉽습니다. 전체적인 로직이 바뀌지 않습니다.
		
		템플릿 메쏘드가 숨어있습니다. 테스트 클래스에서는 각 전문가클래스의 solve() 메쏘드를 호출한 적이 없지만, 내부적으로 호출이 됩니다.
		문제가 나타났을 때 어떤 전문가가 해결할 것인지 단번에 결정이 되지 않습니다. 일단 모든 문제는 fighter 객체를 거쳐갑니다. 맨 앞에 있으니까요. 
		 따라서 전문가 객체의 순서가 전체적인 수행 속도에 영향을 끼칠 수 있습니다. 가능한한 일반적인 문제해결사들을 앞쪽에 세워두는 게 좋습니다.
 */
/*
 1. 책임 연쇄 패턴(Chain of Responsibility)이란?
	어떤 요청에 대해서 이를 처리하는 객체를 하나 혹은 여럿을 두어 해당 요쳥이 해결될때까지 연쇄 적으로 연결된 객체 핸들러 함수를 호출하는 방식
	어플리케이션 입장에서는 전달하는 요청이 연결된 객체 체인중 어느부분에서 처리될지 알수가 없다. 또한 연결된 객체는 요청의 전체를 처리할 수도 있고 일부만 처리할 수도 있다. 

2. 책임 연쇄 패턴의 장점
	- 요청의 처리 방식을 if else문이 아닌 객체의 책임 여부로 돌림으로서 요청처리의 의도와 한계를 명확히 할수 있다.
	- 어떤 요청의 처리를 layer방식으로 나누어 처리할수 있기에 객체간의 coupling을 줄일수 있다.

3. 책임 연쇄 패턴의 단점
	- 객체 안에서 직접 객체를 생성하는 방식으로 작성하기 때문에 관리 이슈가 있을수 있다.
	- 요청을 아무도 처리 못했을때 에 대해 신중하게 처리해야 한다. Exception을 던지는 방식도 옵션이 될수 있다.
 */
public class ChainOfResponsibilityPtnApp {

	public static void main(String[] args) {
		 Problem[] problems = new Problem[5];
        problems[0] = new Problem("덩치 큰 깡패");
        problems[1] = new Problem("컴퓨터 보안장치");
        problems[2] = new Problem("까칠한 여자");
        problems[3] = new Problem("날렵한 깡패");
        problems[4] = new Problem("폭탄");
        
        Expert fighter = new Fighter();
        Expert hacker = new Hacker();
        Expert casanova = new Casanova();
        
        fighter.setNext(hacker).setNext(casanova);
        
        for (Problem problem : problems) {
            fighter.support(problem);
        }

	}

}


//------------------ 전문가: 상위 클래스 ---------------
abstract class Expert {
    private Expert next;
    protected String expertName;
    
    public final void support(Problem p){
        if (solve(p)) {
           System.out.println(expertName+ "이(가) " + p.getProblemName()  +"을(를) 해결해 버렸네.");
        }else{
            if (next != null) {
                next.support(p);
            }else{
                System.out.println(p.getProblemName() + "은(는) 해결할 넘이 없다.");
            }
        }
    }
    
    public Expert setNext(Expert next){
        this.next = next;
        return next;
    }
    protected abstract boolean solve(Problem p);
}

//----------- 전문가들이 풀어야할 문제 클래스 -----------
class Problem {
    private String problemName;
    public Problem(String name) {
        this.problemName = name;
    }
    public String getProblemName() {
        return problemName;
    }
}

//--------------- 첫번째 전문가 파이터! --------
class Fighter extends Expert {
    public Fighter(){
        this.expertName = "격투가";
    }
    @Override
    protected boolean solve(Problem p) {
        return p.getProblemName().contains("깡패");
    }
}

//--------------- 두번째 전문가 해커! --------
class Hacker extends Expert {
    public Hacker(){
        this.expertName = "해커";        
    }
    @Override
    protected boolean solve(Problem p) {
        return p.getProblemName().contains("컴퓨터");
    }
}

//--------------- 세번째 전문가 카사노바! --------
class Casanova extends Expert {
    public Casanova(){
        expertName = "카사노바";
    }
    @Override
    protected boolean solve(Problem p) {
        return p.getProblemName().contains("여자") || p.getProblemName().contains("여성");
    }
}
