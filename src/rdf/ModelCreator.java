package rdf;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Peter, Natalia on 15.04.2017.
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
        model.setNsPrefix("PersonDeleted", nsPersonsDeleted);
        model.setNsPrefix("PersonProps", nsPersonProps);
        set = new TreeSet<>();
    }

    public void personDoesNotExist (String name) {
        System.out.println("ERROR:\tThe person " + name + " does not exist!");
    }

    public void createPerson(String name) {
        Person person = new Person(model, nsPersons, nsPersonsDeleted, name);
        set.add(person);
        System.out.println("INFO:\tCreated person resource " + name);
        System.out.println("INFO:\tSet name property of person " + name + " to " + name);
    }

    public boolean changeName(String name, String newName) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.changeName(newName);
                write();
                System.out.println("INFO:\tSet name property of person " + name + " to " + newName);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean deletePerson(String name) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.delete();
                write();
                set.remove(p);
                System.out.println("INFO:\tPerson resource " + name + " has been deleted from\n" +
                        "\t\tNamespace : " + nsPersons +
                        "\n\t\tand added to\n" +
                        "\t\tNamespace : " + nsPersonsDeleted);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }


    public boolean setPersonGender(String name, String gender) {
        for (Person p : set){
            if(p.getName().equals(name))  {
                p.setGender(gender);
                System.out.println("INFO:\tSet gender property of person " + name + " to " + gender);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean changeGender(String name, String newGender) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.changeGender(newGender);
                write();
                System.out.println("INFO:\tChanged gender property of person " + name + " to " + newGender);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean setBirthday(String name, String birthday) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.setBirthday(birthday);
                System.out.println("INFO:\tSet birthday property of person " + name + " to " + birthday);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean changeBirthday(String name, String newBirthday) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.changeBirthday(newBirthday);
                write();
                System.out.println("INFO:\tChanged birthday property of person " + name + " to " + newBirthday);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean setAddress(String name, String address) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.setAddress(address, nsPersonProps);
                System.out.println("INFO:\tSet address property of person " + name + " to " + address);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean changeAddress(String name, String newAddress) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.changeAddress(newAddress);
                write();
                System.out.println("INFO:\tChanged address property of person " + name + " to " + newAddress);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean setCompany(String name, String companyName) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.setCompany(companyName, nsPersonProps);
                System.out.println("INFO:\tSet company property of person " + name + " to " + companyName);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public boolean changeCompany(String name, String newCompanyName) {
        for (Person p : set){
            if(p.getName().equals(name)) {
                p.changeCompany(newCompanyName);
                write();
                System.out.println("INFO:\tChanged company property of person " + name + " to " + newCompanyName);
                return true;
            }
        }
        personDoesNotExist(name);
        return false;
    }

    public void write() {
        model.write(System.out, "TURTLE");
    }
}
