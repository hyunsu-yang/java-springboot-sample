package com.yhs.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

/*
 ForkJoinPool 
  	
정확히는 Fork Join Framework 이라고 불러야 하고, ForkJoinPool은  그것의 대표 클래스이다. 
이 글에서는 Fork Join Framework을 편의상 ForkJoinPool이라고 칭한다. ForkJoinPool은 기본적으로 쓰레드 풀 서비스의 일종이다.

 다른점은 ForkJoinPool은 기본 개념은 큰 업무를 작은 업무 단위로 쪼개고, 그것을 각기 다른 CPU에서 병렬로 실행한후 결과를 취합하는 방식이다. 
 마치 분할정복 알고리즘과 흡사하다. 여러 CPU들을 최대한 활용하면서 동기화와 GC를 피할수 있는 여러 기법이 사용되었기 때문에, J
 ava 뿐 아니라 Scala에서도 널리 쓰이고 있는 병렬처리 기법이다.  또한 C,C++을 위한 Thread Building Block(TBB) 나 C#의 Task Parallel Library또한 Fork Join Framework의 개념을 가지고 있다.

ForkJoinPool의 절차를 조금 더 자세히 이야기 하면,
1) 큰 업무를 작은 단위의 업무로 쪼갠다.
2) 부모 쓰레드로 부터 처리로직을 복사하여 새로운 쓰레드에서  쪼개진 업무를 수행(Fork) 시킨다.
3) 2을 반복하다가, 특정 쓰레드에서 더이상 Fork가 일어나지 않고 업무가 완료되면 그 결과를 부모 쓰레드에서 Join하여 값을 취합한다.
4) 3을 반복하다가 최초에 ForkJoinPool을 생성한 쓰레드로 값을 리턴하여 작업을 완료한다.

ForkJoinPool의 성능의 핵심 : Work-Stealing
기본적으로는 newCachedThreadPool이나 newFixedThreadPool처럼 ExecutorService의 구현체이다 (링크).
그러나 일반 ExecutorService 구현 클래스와는 기본적으로 다른점이 하나 존재하는데 work-stealing algorithm이 구현되어있다는 점이다.
일반적으로 멀티쓰레드 프로그래밍을 할때 어려운 점중의 하나는 CPU자원을 골고루 사용하여 최대한의 성능을 뽑아내는 일이다. 
정확히 어느정도의 CPU자원이 소모되는 일인지 가늠하기 힘든경우 Task를 어떻게 나누는지에 따라서 노는(Idle) CPU 시간이 많아질수도 있다.
ForkJoinPool의 work-stealling은 이런 경우를 효과적으로 해결한다

ForkJoinPool대표 클래스들
ForkJoinPool을 사용하려면 기본적으로 알아야 할 클래스들이 있다.
1. ForkJoinPool: fork-join방식으로 타스크를 생성하고 동작시키는 ForkJoin Framework의 모체
2. RecursiveTask<V>: 실제 작업의 단위가 되는 클래스는 이 클래스를 상속해야 한다. 또한 compute메소드에서 결과값을 리턴해야 한다.
3. RecursiveAction: RecursiveTask과 같은 용도로서, 작업의 단위가 되는 클래스가 상속해야 하는 클래스이다. 결과는 리턴하지 않는다.
4. ForkJoinTask<V>: RecursiveTask의 부모클래스스로서 fork와 join메소드가 정의되어있다. 직접적으로 사용하지는 않는다
 */
public class ForkJoinPoolApp {

	public static void main(String[] args) {		
		int[] array = {1,2,5,6,7,4,2,6,7,2,3,8,1,100,500};		
		int[] arrayInt = Stream.iterate(0, n -> n+1).limit(10000).filter(n -> n%2 == 0).mapToInt(x -> x).toArray();
		//Integer[] arrayInt2 = Stream.iterate(0, n -> n+1).limit(10000).filter(n -> n%2 == 0).toArray(Integer[]::new);
		
        long result = Sum.sumArray(arrayInt);        
        System.out.println("Fork Join result ="+result);
	}

}


class Sum extends RecursiveTask<Long> { // RecursiveTask를 상속해서 구현해야 한다.뒤에는 compute가 리턴한 타입을 명시한다.
    static final int SEQUENTIAL_THRESHOLD = 5; // 충분히 작은 단위는 여기서 5로 정했다. 정하기 나름.
    int low;   // 배열의 시작 index
    int high;  // 배열의 끝 index
    int[] array; // 전체 배열의 reference

    Sum(int[] arr, int lo, int hi) { // 현객체로 처리할 배열과 시작 인덱스, 끝인덱스 세팅)
        array = arr;
        low   = lo;
        high  = hi;
    }

    protected Long compute() { // compute는 추상 메소드이기에 반드시 구현해줘야 한다. 리턴값은 RecursiveTask<Long> 의 Long과 일치시킨다.
        if(high - low <= SEQUENTIAL_THRESHOLD) { // 충분히 작은 배열 구간이면 값을 계산해 리턴한다.
            long sum = 0;
            for(int i=low; i < high; ++i) 
                sum += array[i];
            return sum;
         } else { 
             // 배열이 기준보다 크다면 divede and conquer방식으로 적당히 둘이상의 객체로 나누고, 
            // 현재 쓰레드에서 처리할 객체는 compute를 호출해 값을 계산하고, fork할 객체는 join하여 값을 기다린후 얻는다.   
            int mid = low + (high - low) / 2;
            Sum left  = new Sum(array, low, mid);
            Sum right = new Sum(array, mid, high);
            left.fork();

            long rightAns = right.compute();
            long leftAns  = left.join();
            
            // 값을 모두 얻은후에는 다 합해서 리턴한다.
            return leftAns + rightAns;
         }
     }

     static long sumArray(int[] array) {
        // ForkJoinPool의 시작전과 후의 thread pool size를 비교한다.
        int beforeSize=ForkJoinPool.commonPool().getPoolSize();        
        System.out.println("ForkJoin commonPool beforeSize="+beforeSize); 
        
        long result = ForkJoinPool.commonPool().invoke(new Sum(array,0,array.length));     

        int afterSize=ForkJoinPool.commonPool().getPoolSize();
        System.out.println("ForkJoin commonPool afterSize="+afterSize);

        return result;
     }
}