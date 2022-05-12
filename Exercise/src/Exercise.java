import java.util.ArrayList;
import java.util.List;

class Resource {
}

public class Exercise {
    static Resource r = new Resource();


    public static void main(String[] a) {
        try {
            synchronized (r) {
                assert false;
            }
        } catch (NullPointerException npe) {
        }
    }
}

class Product {
    String description;
    List<String> reviews;

    Product(String description, List<String> reviews) {
        this.description = description;
        this.reviews = reviews;
    }
}

class ComplexCode {
    public boolean polite(String s) {/*..*/}

    public String fixGrammar(String s) {/*..*/}

    public boolean fix1(Product productId){
        List<String> d = productId.reviews.parallelStream().filter(this::polite).map(this::fixGrammar).toList();
        if(d.size() == productId.reviews.size() && polite(productId.description)){
            productId.reviews = d;
            productId.description = fixGrammar((productId.description));
        }
        return d.size() == productId.reviews.size();
    }

    public String guessPassword() throws InterruptedException {
        while(true){
            String result=guessOneTime();
            if(result!=null){return result;}
        }
    }

    //the grammar of a polite Product is fixed, and true is returned.
    //if the product is not polite, false is returned and the product is not modified.
    public boolean fix(Product productId) {
        String description;
        ArrayList<String> reviews = new ArrayList<String>(productId.reviews);
        String tag = new String("###");//created with new, have unique identity!
        description = productId.description;
        ArrayList<Thread> kools = new ArrayList<Thread>();
        for (int i = 0; i < productId.reviews.size(); i++) {
            final int j = i;
            Thread koolParrallel = new Thread() {
                public void run() {
                    String current = reviews.get(j);
                    if (polite(current)) {
                        reviews.set(j, fixGrammar(current));
                    } else {
                        reviews.set(j, tag);
                    }
                }
            };
            koolParrallel.start();
            kools.add(koolParrallel);
        }
        if (polite(description)) {
            description = fixGrammar(description);
        } else {
            description = tag;
        }
        if (description != tag) {
            for (Thread t : kools) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            boolean hasTag = false;
            for (int i = 0; i < productId.reviews.size(); i++) {
                if (reviews.get(i) == tag) {
                    hasTag = true;
                    break;
                }
            }
            if (!hasTag) {
                productId.description = description;
                productId.reviews.clear();
                productId.reviews.addAll(reviews);
            } else {
                return false;
            }
        } else {//we did not touch the Product!
            return false;
        }
        return true;
    }
}