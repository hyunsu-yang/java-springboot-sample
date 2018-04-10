package com.yhs.designPatterns.structure;

/*
 1. 파사드 패턴(Facade Pattern)이란?
	여러개의 메소드를 사용해 하나의 단순한 기능을 하는 인터페이스를 만드는 패턴. 
	사용자로 하여금 좀더 객체의 여러 기능을 쉽게 이용할수 있도록 한다. 
	application레벨에서 부르는 함수의 갯수를 줄임으로서 coupling을 좀더 줄이고, 또한 이로인해 추상성이 높아지고 의도가 명확한 코드를 작성하게 한다. 
	
	
 */
public class FacadePtnApp {

	public static void main(String[] args) {
		TV tv = new TV();
        Audio audio = new Audio();
        Light light = new Light();
        
        Home home = new Home(audio, light, tv);
        
        home.enjoyTv();
        home.enjoyMusic();
        home.goOut();

	}

}

//----------------- 내부구성품 1. TV ----------
class TV {
    private boolean turnedOn = false;
    public void turnOn(){
        turnedOn = true;
        System.out.println("TV를 켬.");
    }
    public void turnOff(){
        turnedOn = false;
        System.out.println("TV를 끔.");
    }
    public boolean isTurnedOn(){
        return turnedOn;
    }
}

//----------------- 내부구성품 2. 오디오 ----------
class Audio {
    private boolean playing = false;
    public void play(){
        playing = true;
        System.out.println("음악을 연주.");
    }
    public void stop(){
        playing = false;
        System.out.println("음악을 멈춤");
    }
    public boolean isPlaying() {
        return playing;
    }
}

//----------------- 내부구성품 3. 전등 ----------
class Light {
    private int lightness = 0;
    public int getLightness() {
        return lightness;
    }
    public void setLightness(int lightness) {
        System.out.println("밝기를 "+ lightness + "로 변경.");
        this.lightness = lightness;
    }
}

//----------------- Facade ----------
class Home {
    private Audio audio;
    private Light light;
    private TV tv;
    public Home(Audio audio, Light light, TV tv) {
        this.audio = audio;
        this.light = light;
        this.tv = tv;
    }
    public void enjoyTv(){
        System.out.println("==불을 밝게하고 TV보기.");
        light.setLightness(2);
        tv.turnOn();
    }
    public void enjoyMusic(){
        System.out.println("==불을 약간 어둡게하고 음악듣기.");
        light.setLightness(1);
        audio.play();
    }
    public void goOut(){
        System.out.println("==TV끄고, 음악도 끄고, 불도 끄고 외출하기.");
        if (tv.isTurnedOn()) {
            tv.turnOff();
        }
        if (audio.isPlaying()) {
            audio.stop();
        }
        light.setLightness(0);
    }
}