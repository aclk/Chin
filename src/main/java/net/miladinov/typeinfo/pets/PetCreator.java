package net.miladinov.typeinfo.pets;

import java.util.ArrayList;
import java.util.*;

public abstract class PetCreator {
    private Random rand = new Random(47);

    public abstract List<Class<? extends Pet>> types();

    public Pet randomPet() {
        int n = rand.nextInt(types().size());
        try {
            return types().get(n).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Pet[] createArray(int size) {
        Pet[] petArray = new Pet[size];
        for (int i = 0; i < size; i++) {
            petArray[i] = randomPet();
        }
        return petArray;
    }

    public ArrayList<Pet> arrayList(int size) {
        ArrayList<Pet> petArrayList = new ArrayList<Pet>();
        Collections.addAll(petArrayList, createArray(size));
        return petArrayList;
    }
}
