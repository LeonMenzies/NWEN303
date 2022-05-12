package akkaCakes;

import java.io.Serializable;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import akkaUtils.AkkaConfig;
import dataCakes.Cake;
import dataCakes.Gift;
import dataCakes.Sugar;
import dataCakes.Wheat;

abstract class Producer<T> extends AbstractActor {
	
	Class<T> type;
	Queue<T> items = new LinkedList<>();
	int maxSize = 10;
	boolean running;
	
	public Producer(Class<T> type){
		this.type = type;
	}

	public abstract CompletableFuture<T> make();
	
	public Receive createReceive() {
		return receiveBuilder().match(type, r -> {// startup message

			items.offer(r);

		}).match(MakeOne.class, r -> {// startup message
			if (items.size() >= maxSize) {
				running = false;
			} else {
				CompletableFuture<T> createType = make();
				CompletableFuture<MakeOne> wait = createType.thenApply((w) -> new MakeOne());
				Patterns.pipe(createType, getContext().dispatcher()).to(self());
				Patterns.pipe(wait, getContext().dispatcher()).to(self());
			}

		}).match(GiveOne.class, r -> {// startup message

			if (items.isEmpty()) {
				ActorRef s = sender();
				Patterns.pipe(make(), getContext().dispatcher()).to(s);
			} else {
				sender().tell(items.poll(), self());
			}

			if (!running && items.size() < maxSize) {
				running = true;
				self().tell(new MakeOne(), sender());
			}
		}).build();
	}
}

@SuppressWarnings("serial")
class GiftRequest implements Serializable {
}

@SuppressWarnings("serial")
class MakeOne implements Serializable {
}

@SuppressWarnings("serial")
class GiveOne implements Serializable {
}

//--------
class Alice extends Producer<Wheat> {

	public Alice(){
		super(Wheat.class);
	}

	@Override
	public CompletableFuture<Wheat> make() {
		return CompletableFuture.supplyAsync(() -> new Wheat());
	}
}

class Bob extends Producer<Sugar> {

	public Bob(){
		super(Sugar.class);
	}
	
	@Override
	public CompletableFuture<Sugar> make() {
		return CompletableFuture.supplyAsync(() -> new Sugar());
	}
}

class Charles extends Producer<Cake> {
	ActorRef alice;
	ActorRef bob;
	int index = 0;


	public Charles(ActorRef alice, ActorRef bob) {
		super(Cake.class);
		this.alice = alice;
		this.bob = bob;
	}
	
	@Override
	public CompletableFuture<Cake> make() {
		CompletableFuture<Object> wheat = Patterns.ask(alice, new GiveOne(), Duration.ofMillis(10_000_000))
				.toCompletableFuture();

		CompletableFuture<Object> sugar = Patterns.ask(bob, new GiveOne(), Duration.ofMillis(10_000_000))
				.toCompletableFuture();
		return wheat.thenCombine(sugar, (w, s) -> new Cake((Sugar) s, (Wheat) w));
	}
}

class Tim extends AbstractActor {
	int hunger;
	boolean running = true;
	ActorRef charles;
	ActorRef originalSender = null;

	public Tim(int hunger, ActorRef charles) {
		this.hunger = hunger;
		this.charles = charles;
	}


	@Override
	public Receive createReceive() {
		return receiveBuilder().match(GiftRequest.class, () -> originalSender == null, gr -> {
			originalSender = sender();
			charles.tell(new GiveOne(), self());
		}).match(Cake.class, () -> running, c -> {
			hunger -= 1;
			System.out.println("JUMMY but I'm still hungry " + hunger);
			if (hunger > 0) {
				charles.tell(new GiveOne(), self());
				return;
			}
			running = false;
			originalSender.tell(new Gift(), self());
		}).build();
	}
}

public class Cakes {
	public static void main(String[] args) {
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Gift g = computeGift(1000);
		assert g != null;
		System.out.println("\n\n-----------------------------\n\n" + g + "\n\n-----------------------------\n\n");
	}

	public static Gift computeGift(int hunger) {
		ActorSystem s = AkkaConfig.newSystem("Cakes", 2501,
				Map.of("Tim", "192.168.56.1", "Bob", "192.168.56.1", "Charles", "192.168.56.1"
				// Alice stays local
				));
		ActorRef alice = // makes wheat
				s.actorOf(Props.create(Alice.class, () -> new Alice()), "Alice");
		ActorRef bob = // makes sugar
				s.actorOf(Props.create(Bob.class, () -> new Bob()), "Bob");
		ActorRef charles = // makes cakes with wheat and sugar
				s.actorOf(Props.create(Charles.class, () -> new Charles(alice, bob)), "Charles");
		ActorRef tim = // tim wants to eat cakes
				s.actorOf(Props.create(Tim.class, () -> new Tim(hunger, charles)), "Tim");
	  
		
		CompletableFuture<Object> gift = Patterns.ask(tim, new GiftRequest(), Duration.ofMillis(10_000_000))
				.toCompletableFuture();
		try {
			return (Gift) gift.join();
		} finally {
			alice.tell(PoisonPill.getInstance(), ActorRef.noSender());
			bob.tell(PoisonPill.getInstance(), ActorRef.noSender());
			charles.tell(PoisonPill.getInstance(), ActorRef.noSender());
			tim.tell(PoisonPill.getInstance(), ActorRef.noSender());
			s.terminate();
		}
	}
}