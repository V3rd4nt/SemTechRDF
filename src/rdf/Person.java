package rdf;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;

/**
 * Created by Peter & Natalia.
 */
public class Person {

    private Resource res;
    private Model model;

    public Person(Model model, String nsPersons, String nsFoaf, String fullID) {
        this.model = model;
        res = ResourceFactory.createResource(nsPersons + fullID);
        model.add(res, RDF.type, FOAF.Person);
        setID(fullID, nsFoaf);
        LogHelper.logInfo("Created person resource with ID: " + fullID);
    }

    private void setID(String fullID, String nsFoaf) {
        Property property = model.createProperty(nsFoaf + "hasID");
        model.add(res, property, model.createLiteral(fullID));
        LogHelper.logInfo("Set ID property to " + fullID);
    }

    protected void setName(String name) {
        model.add(res, FOAF.name, model.createLiteral(name));
        LogHelper.logInfo("Set name property to " + name);
    }

    protected void setGender(String gender) {
        model.add(res, FOAF.gender, model.createLiteral(gender));
        LogHelper.logInfo("Set gender property to " + gender);
    }

    protected void setCompany(String company, String nsFoaf) {
        Property property = model.createProperty(nsFoaf + "worksFor");
        model.add(res, property, model.createLiteral(company));
        LogHelper.logInfo("Set company property to " + company);
    }

    protected void setBirthday(String birthday) {
        model.add(res, FOAF.birthday, model.createLiteral(birthday));
        LogHelper.logInfo("Set birthday property to " + birthday);
    }

    protected void setAddress(String address, String nsFoaf) {
        Property property = model.createProperty(nsFoaf + "hasAddress");
        model.add(res, property, model.createTypedLiteral(address));
        LogHelper.logInfo("Set address property to " + address);
    }
}
