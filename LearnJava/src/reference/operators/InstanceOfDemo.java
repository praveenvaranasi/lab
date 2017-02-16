package reference.operators;

/**
 * Created by root on 2/16/17.
 */
public class InstanceOfDemo
{
    public void demo()
    {
        Parent parent = new Parent();
        Child child = new Child();
        System.out.println("Child Obj instance of Parent class: " +(child instanceof Parent));
        System.out.println("Parent obj instance of Child class: "+(parent instanceof Child));
    }
}
