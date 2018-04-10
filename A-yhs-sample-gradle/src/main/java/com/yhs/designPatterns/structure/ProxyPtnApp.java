package com.yhs.designPatterns.structure;

/*
 1. 프록시 패턴(Proxy Pattern) 이란?
어떤 객체를 사용하고자 할때, 객체를 직접적으로 참조 하는 것이 아니라, 그 객체를 대행(대리,proxy)하는 객체를 통해 대상객체에 접근하는 방식.

이 방식을 사용하면 실제 대상 객체가 메모리에 존재하지 않아도 기본적인 정보를 참조하거나 설정할수 있고, 실제 객체의 기능이 반드시 필요한 시점까지 객체의 생성을 미룰 수 있다. 
프록시 객체와 실제 객체는 같은 인터페이스를 구현하여, 프록시 객체는 실제 객체와 치환(맞바꿈)이 가능하다.

2. 프록시 패턴의 장점
- 사이즈가 큰 객체가 로딩되기 전에도 proxy를 통해 참조를 할수 있다.(이미지,동영상등의 로딩에서 반응성 혹은 성능 향상 - 가상 프록시)
- 실제 객체의 public, protected 메소드들을 숨기고 인터페이스를 통해 노출시킬수 있다. (안전성 - 보호 프록시)
- 로컬에 있지 않고 떨어져 있는 객체를 사용할수 있다. (RMI, EJB 의 분산처리등 -  원격 프록시)
- 원래 객체의 접근에 대해서 사전처리를 할수 있다 (객체의 레퍼런스 카운트 처리 - 스마트 프록시)
- 원본 객체의 참조만 필요할때는 원복 객체를 사용하다가, 최대한 늦게 복사가 필요한 시점에 원본 객체를 복사하여 사용하는 방식. (Concurrent package의 CopyInWriteArrayList - 변형된 가상 프록시)

3. 프록시 패턴의 단점
- 객체를 생성할때 한단계를 거치게 되므로, 빈번한 객체 생성이 필요한 경우 성능이 저하될수 있다.
- 프록시안에서 실제 객체 생성을 위해서 thread가 생성되고 동기화가 구현되야 하는 경우 성능이 저하되고 로직이 난해해질 수 있다. 
 */
public class ProxyPtnApp {

	public static void main(String[] args) {
		Image image = new ProxyImage("test_10mb.jpg");
	      //image will be loaded from disk
	      image.display(); 
	      System.out.println("");
	      
	      //image will not be loaded from disk
	      image.display();  

	}

}

interface Image {
	   void display();
	}

class RealImage implements Image {
   private String fileName;
   
   public RealImage(String fileName){
      this.fileName = fileName;
      loadFromDisk(fileName);
   }
   
   @Override
   public void display() {
      System.out.println("Displaying " + fileName);
   }
   
   private void loadFromDisk(String fileName){
      System.out.println("Loading " + fileName);
   }
}

class ProxyImage implements Image{
   private RealImage realImage;
   private String fileName;
   
   public ProxyImage(String fileName){
      this.fileName = fileName;
   }
   
   @Override
   public void display() {
      if(realImage == null){
         realImage = new RealImage(fileName);
      }
      realImage.display();
   }
}
	