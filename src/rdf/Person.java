package rdf;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

public class Person implements Comparable<Person> {

    private Resource res;
    private Model model;
    private String name;

    public Person(Model model, String nsPersons, String name) {
        this.name = name;
        this.model = model;
        res = model.createResource(nsPersons + name);
        setName(name);
    }

    protected void setName (String name) {
        res.addProperty(FOAF.givenname, name);
    }

    protected String getName() {
        return name;
    }

    protected void setGender (String gender) {
        res.addProperty(FOAF.gender, model.createLiteral(gender));
    }

    protected void setCompany (String companyName, String nsPersonProps) {
        Property property = model.createProperty(nsPersonProps + "worksFor");
        res.addProperty(property, model.createLiteral(companyName));
    }
    protected void setBirthday (String birthday) {
        res.addProperty(FOAF.birthday, model.createLiteral(birthday));
    }
    protected void setAddress (String address, String nsPersonProps) {
        Property prop = model.createProperty(nsPersonProps + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
    }

    @Override
    public int compareTo(Person p) {
        return this.getName().compareTo(p.getName());
    }
}
