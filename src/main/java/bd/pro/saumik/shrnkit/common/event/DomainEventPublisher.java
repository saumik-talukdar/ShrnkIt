package bd.pro.saumik.shrnkit.common.event;

public interface DomainEventPublisher {

    void publish(Object event);

}