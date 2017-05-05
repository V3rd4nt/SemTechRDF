package rdf;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

/**
 * Created by Peter & Natalia.
 */
public class Person {

    private Resource res;
    private Model model;
    private String name;

    public Person(Model model, String nsPersons, String name) {
        this.model = model;
        res = model.createResource(nsPersons + name);
        System.out.println("\tINFO:\tCreated person resource " + name);
    }

    protected void setName(String name) {
        this.name = name;
        res.addProperty(FOAF.name, name);
        System.out.println("\tINFO:\tSet name property of person " + name + " to " + name);
    }

    protected void setGender(String gender) {
        res.addProperty(FOAF.gender, model.createLiteral(gender));
        System.out.println("\tINFO:\tSet gender property of person " + name + " to " + gender);
    }

    protected void setCompany(String company, String nsPersonProps) {
        Property property = model.createProperty(nsPersonProps + "worksFor");
        res.addProperty(property, model.createLiteral(company));
        System.out.println("\tINFO:\tSet company property of person " + name + " to " + company);
    }

    protected void setBirthday(String birthday) {
        res.addProperty(FOAF.birthday, model.createLiteral(birthday));
        System.out.println("\tINFO:\tSet birthday property of person " + name + " to " + birthday);
    }

    protected void setAddress(String address, String nsPersonProps) {
        Property prop = model.createProperty(nsPersonProps + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
        System.out.println("\tINFO:\tSet address property of person " + name + " to " + address);
    }
}
