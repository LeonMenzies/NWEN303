package dataCakes;

@SuppressWarnings("serial")
public class Wheat extends Counter{
  volatile static int cTot=0;
  @Override
void incr() {cTot+=1;c=cTot;}
}