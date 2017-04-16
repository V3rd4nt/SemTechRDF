package rdf;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Peter on 15.04.2017.
 */
public class ModelCreator {

    Model model;
    String nsPersons = "http://www.example.com/persons";
    String nsPersonsDeleted = "http://www.example.com/personsDeleted";
    String nsPersonProps = "http://www.example.com/personproperties.rdf#";
    private SortedSet<Person> set;

    public ModelCreator() {
        model = ModelFactory.createDefaultModel();
        model.setNsPrefix("Person", nsPersons);
        model.setNsPrefix("PersonProps", nsPersonProps);
        set = new TreeSet<>();
    }

    public void createPerson(String name) {
        Person person = new Person(model, nsPersons, name);
        set.add(person);
    }

    public boolean setPersonGender(String name, String gender) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setGender(gender);
            return true;
        }
        return false;
    }

    public boolean changeGender(String name, String newGender) {
        for (Person p : set){
            if(p.getName().equals(name)) p.changeGender(newGender);
            write();
            return true;
        }
        return false;
    }

    public boolean setBirthday(String name, String birthday) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setBirthday(birthday);
            return true;
        }
        return false;
    }

    public boolean changeBithday(String name, String newBirthday) {
        for (Person p : set){
            if(p.getName().equals(name)) p.changeBirthday(newBirthday);
            write();
            return true;
        }
        return false;
    }

    public boolean setAddress(String name, String address) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setAddress(address, nsPersonProps);
            return true;
        }
        return false;
    }

    public boolean changeAddress(String name, String newAddress) {
        for (Person p : set){
            if(p.getName().equals(name)) p.changeAddress(newAddress);
            write();
            return true;
        }
        return false;
    }

    public boolean setCompany(String name, String companyName) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setCompany(companyName, nsPersonProps);
            return true;
        }
        return false;
    }

    public boolean changeCompany(String name, String newConpanyName) {
        for (Person p : set){
            if(p.getName().equals(name)) p.changeCompany(newConpanyName);
            write();
            return true;
        }
        return false;
    }

    public boolean changeName(String name, String newName) {
        for (Person p : set){
            if(p.getName().equals(name)) p.changeName(newName);
            write();
            return true;
        }
        return false;
    }

    public void write() {
        model.write(System.out, "TURTLE");
    }

    public Model get() {
        return model;
    }
}
