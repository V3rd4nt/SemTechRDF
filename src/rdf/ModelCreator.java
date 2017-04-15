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
        model.setNsPrefix("nsPersons", nsPersons);
        model.setNsPrefix("nsPersonProps", nsPersonProps);
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

    public boolean setBirthday(String name, String birthday) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setBirthday(birthday);
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

    public boolean setWorkPlace(String name, String company) {
        for (Person p : set){
            if(p.getName().equals(name)) p.setCompany(company, nsPersonProps);
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
