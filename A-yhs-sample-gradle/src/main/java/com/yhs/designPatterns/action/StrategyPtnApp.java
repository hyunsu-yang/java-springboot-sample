package com.yhs.designPatterns.action;

/*
 * 전략 패턴
 	Template Method 패턴이 상속을 이용해서 어떤 구현을 했다면, Strategy 패턴은 구성을 이용합니다. 
 	Template Method와 마찬가지로 바뀌는 부분과 바뀌지 않는 부분을 나눠서 생각할 수 있습니다. 
 	Template Method가 하위 클래스에서 바뀌는 부분을 처리한다면 Starategy는 바뀌는 부분을 인터페이스로 분리하여 처리합니다. 
 	그 인터페이스의 구현체를 바꿈으로서 로직을 변경하는 것입니다. 또 Template Method와 크게 다른 점은 Template Method에서는 
 	외부로 공개되는 것이 Template Method를 가지고 있는 상위 클래스였지만, Strategy에서는 인터페이스를 사용하는 클래스(그 클래스를 Context라고 합니다.)입니다.
 	
 */
public class StrategyPtnApp {

	public static void main(String[] args) {
		  Seller cupSeller = new CupSeller();
        Seller phoneSeller = new PhoneSeller();
        
        Mart mart1 = new Mart(cupSeller);
        mart1.order();
        
        Mart mart2 = new Mart(phoneSeller);
        mart2.order();

	}
   
}
/*
 위에서 보시다 시피 테스트 클래스에서는 Seller의 sell()을 호출하지 않습니다. Mart의 order()를 호출합니다. Seller의 메쏘드는 외부로 공개되지 않습니다. 
Mart 클래스가 여기서는 외부로 공개되는 Context가 됩니다. Mart는 멤버 변수로 Seller를 가집니다.
 Mart에서 가지는 Seller를 바꿔치기함으로써 Mart의 order()에서 실제 실행되는 로직이 달라질 수 있습니다.
 */

//------------------------ 상위 인터페이스 --------------------
 interface Seller {
    public void sell();
}

//------------------------- 인터페이스 구현체1 -----------------
class CupSeller implements Seller {
    public void sell() {
        System.out.println("컵을 팔아요.");
    }
}
//------------------------- 인터페이스 구현체2 -----------------
class PhoneSeller implements Seller {
    public void sell() {
        System.out.println("전화기를 팔아요.");
    }
}
//------------------------- 인터페이스 사용하는 클래스 -----------------
class Mart {
    private Seller seller;
    public Mart(Seller seller) {
        this.seller = seller;
    }
    public void order(){
        seller.sell();
    }
}
