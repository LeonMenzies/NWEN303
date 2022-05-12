package ass1;

import java.util.Random;

import org.junit.jupiter.api.Test;


class WaterBottle implements Comparable<WaterBottle>{
  public WaterBottle(long length, long width, long height) {
    super();
    this.length = length;
    this.width = width;
    this.height = height;
    this.volume = length * width * height;
  }
  long length;
  long width;
  long height;
  long volume;
  @Override
  public int compareTo(WaterBottle other) {
    return new Long(this.volume).compareTo(other.volume);
  }

}
public class TestWaterBottle {

  static public WaterBottle[][] dataset={
    {new WaterBottle(10,10,10),new WaterBottle(20,30,40),new WaterBottle(30,30,30),new WaterBottle(40,40,40),new WaterBottle(50,50,50),new WaterBottle(60,60,60)},
    {new WaterBottle(110,10,50),new WaterBottle(120,3,40),new WaterBottle(130,3,30),new WaterBottle(140,140,140),new WaterBottle(00,150,20),new WaterBottle(200,260, 700)},
    {new WaterBottle(0,0, 0),new WaterBottle(0,3,0),new WaterBottle(130,3,400),new WaterBottle(140,140, 140)},
    {},
    manyOrdered(10000),
    manyReverse(10000),
    manyRandom(10000)
  };
  static private WaterBottle[] manyRandom(int size) {
    Random r=new Random(0);
    WaterBottle[] result=new WaterBottle[size];
    for(int i=0;i<size;i++){result[i]=new WaterBottle(r.nextLong(),r.nextLong(),r.nextLong());}
    return result;
  }
  static private WaterBottle[] manyReverse(int size) {
    WaterBottle[] result=new WaterBottle[size];
    for(int i=0;i<size;i++){result[i]=new WaterBottle(size-i,size*3-i, size*3-i);}
    return result;
  }
  static private WaterBottle[] manyOrdered(int size) {
    WaterBottle[] result=new WaterBottle[size];
    for(int i=0;i<size;i++){result[i]=new WaterBottle(i*3,i*2,i*2);}
    return result;
  }

  @Test
  public void testISequentialSorter() {
    Sorter s=new ISequentialSorter();
    for(WaterBottle[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMSequentialSorter() {
    Sorter s=new MSequentialSorter();
    for(WaterBottle[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter1() {
    Sorter s=new MParallelSorter1();
    for(WaterBottle[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter2() {
    Sorter s=new MParallelSorter2();
    for(WaterBottle[]l:dataset){TestHelper.testData(l,s);}
  }
  @Test
  public void testMParallelSorter3() {
    Sorter s=new MParallelSorter3();
    for(WaterBottle[]l:dataset){TestHelper.testData(l,s);}
  }
}
