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
        System.out.println("INFO:\tCreated person resource " + name);
    }

    /*protected void delete() {
        deleteResource(nsPersonsDeleted, name);
    }*/

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
        res.addProperty(FOAF.name, name);
        System.out.println("INFO:\tSet name property of person " + name + " to " + name);

    }

    protected void setGender(String gender) {
        res.addProperty(FOAF.gender, model.createLiteral(gender));
        System.out.println("INFO:\tSet gender property of person " + name + " to " + gender);
    }

    protected void setCompany(String companyName, String nsPersonProps) {
        Property property = model.createProperty(nsPersonProps + "worksFor");
        res.addProperty(property, model.createLiteral(companyName));
        System.out.println("INFO:\tSet company property of person " + name + " to " + companyName);
    }

    protected void setBirthday(String birthday) {
        res.addProperty(FOAF.birthday, model.createLiteral(birthday));
        System.out.println("INFO:\tSet birthday property of person " + name + " to " + birthday);
    }

    protected void setAddress(String address, String nsPersonProps) {
        Property prop = model.createProperty(nsPersonProps + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
        System.out.println("INFO:\tSet address property of person " + name + " to " + address);
    }

    /*private void deleteResource(String nameSpace, String resourceName) {
        // remove statements where resource is subject
        model.removeAll(res, null, (RDFNode) null);
        // remove statements where resource is object
        model.removeAll(null, null, res);
        res = model.createResource(nameSpace + resourceName);
        setName(resourceName);
        setGender(gender);
        setBirthday(birthday);
        setCompany(companyName, nsPersonProps);
        setAddress(address, nsPersonProps);
    }*/
}
