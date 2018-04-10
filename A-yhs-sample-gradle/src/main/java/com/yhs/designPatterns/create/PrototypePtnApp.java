package com.yhs.designPatterns.create;

import java.util.Date;

/*
 Prototype 패턴은..

기존에 만들어진 복잡다난한 인스턴스의 내용이 일부만 살짝 변경된 비스무레한 객체가 필요한 경우에 쓰입니다. 
일반적으로 객체를 새로 생성할 때는 new Object()와 같은 방법으로 생성을 합니다. 그러나 그렇게 생성할 경우 기존에 만들어진 것과 유사하다고 해도 결국 모든 정보를 다시 세팅해주어야 합니다. 
그러나, clone()을 이용할 경우에는 기존에 만들어진 것을 복사해서 바뀐 부분만 대체해 주면 인스턴스를 생성하기가 쉽습니다. 
아주 일반적인 "원형"을 만들어서 그것을 복사한 후 적당히 커스터마이징을 하면 new로 객체를 생성하는 것보다 쉽게 됩니다. 

또 위의 예제에서는 그렇게 구현하지 않았지만 일반적으로 Prototype은 외부로 드러내지 않습니다. 팩토리 패턴과 조합해서 쓰는 게 일반적입니다. 
Factory 클래스에서 원형을 관리하고, 그 Factory의 create 메쏘드가 호출되면, 원형으로부터 복사해서 외부로 던져주는 겁니다. 
Prototype은 Factory에서만 관리되고 그 외부로 드러나지 않습니다.
clone() 메쏘드가 호출되어 새로운 객체가 생성되는 시점에 원형이 어찌 생겼는지 크게 신경쓰지 않습니다. 그냥 다짜고짜 복사할 뿐입니다.

 */
public class PrototypePtnApp {

	public static void main(String[] args) {
		Complex com = new Complex("매우 복잡한 정보");
        try {
            Complex cloned1 = (Complex)com.clone();
            cloned1.setDate(new Date(2008,0,1));

            Complex cloned2 = (Complex)com.clone();
            cloned2.setDate(new Date(2008,2,1));
            
            System.out.println(cloned1.getDate());
            System.out.println(cloned2.getDate());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

	}

}

//---------------- 복잡한 정보를 가지고 있는 Complex -------------
class Complex implements Cloneable{
    private String complexInfo;

    private Date date;

    public Complex(String complexInfo) {
        this.complexInfo = complexInfo;
    }
    public String getComplexInfo() {
        return complexInfo;
    }
    public void setDate(Date date){
        this.date = new Date(date.getTime());
    }
    public Date getDate() {
        return date;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Complex tmp = (Complex) super.clone();
        return tmp;
    }
}