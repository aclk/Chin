package net.miladinov.generics;

import java.lang.reflect.*;

import static net.miladinov.util.Print.*;

// Does not implement Performs:
class Mime {
    public void walkAgainstTheWind() {
    }

    public void sit() {
        print("Pretending to sit");
    }

    public void pushInvisibleWalls() {
    }

    public String toString() {
        return "Mime";
    }
}

// Does not implement Performs:
class SmartDog {
    public void speak() {
        print("Woof!");
    }

    public void sit() {
        print("Sitting");
    }

    public void reproduce() {

    }
}

class CommunicateReflectively {
    public static void perform(Object speaker) {
        Class<?> classOfSpeaker = speaker.getClass();
        try {
            try {
                Method speak = classOfSpeaker.getMethod("speak");
                speak.invoke(speaker);
            } catch (NoSuchMethodException e) {
                print(speaker + " cannot speak");
            }
            try {
                Method sit = classOfSpeaker.getMethod("sit");
                sit.invoke(speaker);
            } catch (NoSuchMethodException e) {
                print(speaker + " cannot sit");
            }
        } catch (Exception e) {
            throw new RuntimeException(speaker.toString(), e);
        }
    }
}

public class LatentReflection {
    public static void main(String[] args) {
        CommunicateReflectively.perform(new SmartDog());
        CommunicateReflectively.perform(new Robot());
        CommunicateReflectively.perform(new Mime());
    }
}
