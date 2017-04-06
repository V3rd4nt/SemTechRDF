package rdf;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

public class person {

    Resource res;

    public person(Model model, String nsPrefix, String name) {
        Resource res = model.createResource(nsPrefix + name);
    }

    protected void setGender (Model model, String gender) {
        model.add(res, FOAF.gender, model.createLiteral(gender));
    }
    protected void setName (Model model, String name) {
        model.add(res, FOAF.name, model.createLiteral(name));
    }
    protected void setCompany (Model model, String companyName) {
        Property prop = model.createProperty(FOAF.getURI() + "worksFor");
        res.addProperty(prop, model.createTypedLiteral(companyName));
    }
    protected void setBirthday (Model model, String birthday) {
        model.add(res, FOAF.birthday, model.createLiteral(birthday));
    }
    protected void setAddress (Model model, String address) {
        Property prop = model.createProperty(FOAF.getURI() + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
    }
}
