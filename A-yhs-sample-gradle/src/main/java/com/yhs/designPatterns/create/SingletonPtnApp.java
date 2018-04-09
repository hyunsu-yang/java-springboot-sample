package com.yhs.designPatterns.create;

/*
 * 각종 설정 등이 저장된 클래스가 하나 있다고 칩시다. 프로그램 내에서 여기저기서 마구 접근해서 설정을 바꾸기도 하고 값을 가져오기도 합니다. 
 * 이런 클래스는 인스턴스를 하나만 가져야 합니다. 하나 만들어서 쓰는 곳마다 인자로 전달해주면 되긴 합니다만, 접근하는 곳이 많다면, 계속 인자로 전달하는 것은 그다지 바람직하지 않습니다. 
 * 전역변수처럼 아무곳에서나 이 인스턴스에 접근을 하면 편하겠죠. 
 * Singleton 패턴을 이용하면, 하나의 객체를 만들어서 아무데서나 접근할 수 있습니다.
 */

/*
 4. Singleton의 특징

Singleton은 당연히 인스턴스가 1개만 생깁니다. 그러자고 만든 거니까요. 또 하나의 규약은 private 생성자 때문에 상속이 안 된다는 점입니다. 
(상속받은 하위체는 상위체의 생성자를 호출합니다.) 예를 들어 Singleton에서 설정관련된 xml 파일을 수정한다고 칩시다. 
상속을 받아 다른 객체를 만들어서 파일을 수정하는 시도를 하면 안되지요. 상속을 받게 되면 "인스턴스 1개"라는 원칙을 깨게 됩니다.

private 생성자는 외부에서의 직접호출을 통한 생성을 막는 것과 상속을 막는 두 가지 기능을 수행합니다. 둘 다 "인스턴스 1개"라는 원칙을 지키는 것이죠.
Factory 패턴과 사용법이 매우 유사합니다. Singleton은 Factory의 특이 케이스로 볼 수도 있습니다. Factory는 매번 객체를 만들어서 리턴하는 방법이고 
Singleton은 한 개만 만들어서 요청이 들어올 때마다 만들어진 객체를 리턴한다는 게 차이점입니다. 
또 일반적으로 Factory는 create...과 같은 메쏘드 이름을 사용하고 Singleton은 getInstance라는 메쏘드 이름을 사용합니다.

위에서 말한 세가지 방법 중 첫번째 방법의 경우는 public으로 멤버 변수를 선언하고 외부에서 직접 변수에 접근해서 사용하게 해도 됩니다. 
(반드시 private이어야할 필요는 없다는 거죠. ) 두번째와 세번째는 초기화가 보장이 안 되어 있지만, 첫번째의 경우는 보장되어있기 때문입니다. 
주의할 점은 외부에서 악의적으로 public 멤버 변수는 바꿔치기를 할 수도 있기 때문에 이런 식으로 접근할 때는 final 을 붙여주는 게 좋습니다.
(어차피 private 생성자를 가지고 있으니, 외부에서 새로운 객체를 만들어 낼 수는 없지만 null을 대입할 수는 있기 때문에 final이 필요합니다.) 
그럼 public static final이 되는군요! 상수란 말이죠. 하지만 일반적인 상수와는 다릅니다. 일반적인 상수는 Immutable 로 구현이 되어있기 때문입니다. 
상수로 많이 쓰는 String, Integer, Boolean 등은 전부 Immutalbe입니다. 
물론 이런 접근이 권장사항은 아닙니다. 그냥 가능하긴 하다는 얘깁니다.

5. JAVA API에 있는 Singleton

Boolean에 있는 valueOf 들은 전부 Singleton 비스무레하게 구현되어 있습니다. 다만 인자를 받기 때문에 멤버 변수로 예제처럼 1개만 가지고 있는 것이 아니라 여러개를 가질 수 있습니다. 
true라는 값을 가지는 Boolean과 false라는 값을 가지는 Boolean 객체 2개가 존재하는 것이죠.
Collections에 있는 empty.. 하는 메쏘드들도 전부 Singleton입니다.

jdk 안에 있는 Singleton은 대부분 위에서 말한 방법 중 첫번째 방법을 쓰고 있습니다. 
래스 로드시 멤버 변수들을 초기화하는 방법입니다. 그래서 대부분 그 멤버 변수들은 public static final 로 선언되어있습니다.

 */
public class SingletonPtnApp {
	 public static void main(String[] args) {
		 SingletonPtnApp test = new SingletonPtnApp();
		 
		 test.Amethod();
		 test.Bmethod();
	    }
	 
	 public void Amethod(){
	        SingletonCounter sc = SingletonCounter.getInstance();
	        System.out.println("Amethod에서 카운터 호출 " + sc.getNextInt() );
    }
    public void Bmethod(){
        SingletonCounter sc = SingletonCounter.getInstance();
        System.out.println("Bmethod에서 카운터 호출 " + sc.getNextInt() );
    }
}


/*
 * singleton에서 중요한 것은 다음 세 가지입니다.
첫째, private 멤버 변수로 자기 자신의 클래스의 인스턴스를 가집니다. 황토색 부분입니다.
둘째, private 생성자를 지정하여, 외부에서 절대로 인스턴스를 생성하지 못하게 합니다. 보라색 부분입니다.
셋째,getInstance() 메쏘드를 통해 객체를 static하게 가져올 수 있습니다. 파란색 부분입니다.

이는 유일무이한 인스턴스를 만들기 위해 생긴 규약들입니다. 무슨 수를 써도 Singleton 클래스를 수정하지 않는 한 새로운 인스턴스를 만들 수 없습니다. 
 */
class SingletonCounter {
    private static SingletonCounter singleton = new SingletonCounter();
    private int cnt = 0;
    private SingletonCounter(){
    }
    public static SingletonCounter getInstance(){
        return singleton;
    }
    public int getNextInt(){
        return ++cnt;
    }
}

// ----------- Singleton을 구현하는 몇 가지 방법 ------------------------------------ //


/* * 
 * 1. 클래스 로드시 new가 실행이 됩니다. 항상 1개의 인스턴스를 가지게 되겠죠. 코드가 가장 짧고 쉽습니다. 성능도 다른 방법에 비해 좋습니다. 
 */
class Singleton1 {
    private static Singleton1 single = new Singleton1();
    public static Singleton1 getInstance(){
        return single;
    }
    private Singleton1(){
    }
}

/*
 * 2. 클래스 로드시에는 인스턴스가 생성되지 않습니다. getInstance()가 처음 호출될 때 생성이 되지요. 
 * 그러나 synchornized가 걸려 있어서 성능이 안 좋습니다. 인스턴스를 사용할 필요가 없을 때는 인스턴스가 생성되지 않는다는 점이 첫번째 방벙에 비해 장점입니다.
 */
class Singleton2 {
    private static Singleton2 single;
    public static synchronized Singleton2 getInstance(){
        if (single == null) {
            single = new Singleton2();
        }
        return single;
    }
    private Singleton2(){
    }
}

/*
 * 3. 첫번째의 장점인 성능이 좋다(synchronized 가 안 걸려서)와 두번째의 장점인 안 쓸 때는 인스턴스를 아예 만들지 않는다의 장점만 뽑아온 방법입니다. 코드는 제일 깁니다^^.
여기서 중요한 점은 if(single == null) 을 두 번이나 체크합니다. A, B 2개의 thread가 접근을 한다고 가정합니다.
A와 B가 거의 동시에 들어와서 바깥쪽 single== null 인 부분을 통과했다고 칩시다. 그리고 A가 조금 먼저 synchronized 블럭에 진입했습니다. 
B는 그 앞에서 대기 중이지요. A가 다시 single== null을 체크합니다. 여전히 null이지요. 그러면 인스턴스를 만들고 synchronized 블럭을 탈출합니다. 
그러면 B가 synchronized 안으로 진입합니다. single은 더 이상 null이 아닙니다. A가 만들었으니까요. B는 그냥 synchronized 블럭을 빠져나옵니다.
바깥쪽 if(single == null) 가 없다면, 성능 저하가 발생합니다. 매번 synchronized 블럭 안으로 들어가니까요. 두번째 방법과 같다고 보시면 됩니다. 
안쪽의 if(single == null) 가 없다면, singleton이 보장되지 않습니다. 
volatile 키워드도 꼭 써줘야 합니다. volatile 키워드는 변수의 원자성을 보장합니다. 
single = new Singleton3(); 이란 구문의 실행은 원자성이 아닙니다.(원자성이란 JVM이 실행하는 최소단위의 일을 말합니다. 즉 객체 생성은 JVM이 실행하는 최소단위가 몇 번 실행되어야 완료되는 작업이란 뜻입니다.)  
JVM에 따라서 single이라는 변수의 공간만을 먼저 생성하고 초기화가 나중에 실행되는 경우도 있습니다. 
변수의 공간만 차지해도 null은 아니기 때문에 singleton이 보장된기 어렵습니다. JVM 버전이 1.4(어쩌면 1.5 잘 기억이..--;; ) 
이전에서는 volatile 키워드가 정상적으로 작동하지 않을 수도 있다고 합니다.
 */
class Singleton3 {
    private volatile static Singleton3 single;
    public static Singleton3 getInstance(){
        if (single == null) {
            synchronized(Singleton3.class) {
                if (single == null) {
                    single = new Singleton3();
                }
            }
        }
        return single;
    }
    private Singleton3(){
    }
}

/*
 * 내부 클래스를 사용하는 방법입니다. 기존의 3가지 방법에서는 Singleton 클래스가 자기 자신의 타입을 가지는 멤버 변수를 가지고 있는데, 
 * 네번째의 경우는 내부 클래스가 가지고 있습니다. 내부 클래스가 호출되는 시점에 최초 생성이 되기 때문에, 속도도 빠르고 필요치 않다면 생성하지도 않습니다.
 */
class Singleton4 {
    private Singleton4(){
    }
    
    public static Singleton4 getInstatnce(){
        return SingletonHolder.single;
    }
    
    // private static inner class
    private static class SingletonHolder{
        static final Singleton4 single = new Singleton4();
    }   
}