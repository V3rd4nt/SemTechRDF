package rdf;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import java.util.UUID;

/**
 * Created by Peter & Natalia.
 */
public class Person {

    private Resource res;
    private Model model;

    public Person(Model model, String nsPersons) {
        String id = UUID.randomUUID().toString();
        this.model = model;
        res = model.createResource(nsPersons + id);
        LogHelper.logInfo("Created person resource with UUID: " + id);
    }

    protected void setName(String name) {
        res.addProperty(FOAF.name, name);
        LogHelper.logInfo("Set name property to " + name);
    }

    protected void setGender(String gender) {
        res.addProperty(FOAF.gender, model.createLiteral(gender));
        LogHelper.logInfo("Set gender property to " + gender);
    }

    protected void setCompany(String company, String nsPersonProps) {
        Property property = model.createProperty(nsPersonProps + "worksFor");
        res.addProperty(property, model.createLiteral(company));
        LogHelper.logInfo("Set company property to " + company);
    }

    protected void setBirthday(String birthday) {
        res.addProperty(FOAF.birthday, model.createLiteral(birthday));
        LogHelper.logInfo("Set birthday property to " + birthday);
    }

    protected void setAddress(String address, String nsPersonProps) {
        Property prop = model.createProperty(nsPersonProps + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
        LogHelper.logInfo("Set address property to " + address);
    }
}
