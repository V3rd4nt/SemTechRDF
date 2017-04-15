package rdf;

/**
 * Created by Peter on 06.04.2017.
 */
public class Graph {

    public static void main (String[] args) {
        ModelCreator mod = new ModelCreator();

        String person1 = "Tom";
        mod.createPerson(person1);
        mod.setPersonGender(person1, "male");
        mod.setBirthday(person1, "08.02.1991");
        mod.setAddress(person1, "Straße 1, 4020 Linz");
        mod.setWorkPlace(person1, "Voest-Alpine");
        mod.write();
    }
}
