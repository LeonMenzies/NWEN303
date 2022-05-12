package dataCakes;

@SuppressWarnings("serial")
public class Sugar extends Counter{
  volatile static int cTot=0;
  public Sugar() {
      try {
          Thread.sleep(200);
      } catch (InterruptedException ex) {
      }
  }
  @Override
void incr() {cTot+=1;c=cTot;}
}