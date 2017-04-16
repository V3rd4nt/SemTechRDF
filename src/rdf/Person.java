package rdf;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.vocabulary.RDF;

public class Person implements Comparable<Person> {

    private Resource res;
    private Model model;
    private String name, gender, address, birthday, companyName;

    public Person(Model model, String nsPersons, String name) {
        this.model = model;
        res = model.createResource(nsPersons + name);
        setName(name);
    }

    protected void setName(String name) {
        this.name = name;
        res.addProperty(FOAF.name, name);
    }

    protected String getName() {
        return name;
    }

    protected void changeName(String newName) {
        update("PREFIX rdf: <"+RDF.getURI()+">\n" +
                        "PREFIX foaf: <"+FOAF.getURI()+">\n" +
                        "DELETE { ?person foaf:name \"" + name + "\" }\n" +
                        "INSERT { ?person foaf:name \"" + newName + "\"\n} " +
                        "WHERE { ?person foaf:name \"" + name + "\" }\n");
    }

    protected void setGender(String gender) {
        this.gender = gender;
        res.addProperty(FOAF.gender, model.createLiteral(gender));
    }

    protected void changeGender(String newGender) {
        update("PREFIX rdf: <"+RDF.getURI()+">\n" +
                        "PREFIX foaf: <"+FOAF.getURI()+">\n" +
                        "DELETE { ?person foaf:gender \"" + gender + "\" }\n" +
                        "INSERT { ?person foaf:gender \"" + newGender + "\"\n} " +
                        "WHERE { ?person foaf:gender \"" + gender + "\" }\n");
    }

    protected void setCompany(String companyName, String nsPersonProps) {
        this.companyName = companyName;
        Property property = model.createProperty(nsPersonProps + "worksFor");
        res.addProperty(property, model.createLiteral(companyName));
    }

    protected void changeCompany(String newCompanyName) {
        update("PREFIX rdf: <"+RDF.getURI()+">\n" +
                "PREFIX foaf: <"+FOAF.getURI()+">\n" +
                "PREFIX PersonProps: <http://www.example.com/personproperties.rdf#>\n" +
                "DELETE { ?person PersonProps:worksFor \"" + companyName + "\" }\n" +
                "INSERT { ?person PersonProps:worksFor \"" + newCompanyName + "\"\n} " +
                "WHERE { ?person PersonProps:worksFor \"" + companyName + "\" }\n");
    }

    protected void setBirthday(String birthday) {
        this.birthday = birthday;
        res.addProperty(FOAF.birthday, model.createLiteral(birthday));
    }

    protected void changeBirthday(String newBirthday) {
        update("PREFIX rdf: <"+RDF.getURI()+">\n" +
                "PREFIX foaf: <"+FOAF.getURI()+">\n" +
                "DELETE { ?person foaf:birthday \"" + birthday + "\" }\n" +
                "INSERT { ?person foaf:birthday \"" + newBirthday + "\"\n} " +
                "WHERE { ?person foaf:birthday \"" + birthday + "\" }\n");
    }

    protected void setAddress(String address, String nsPersonProps) {
        this.address = address;
        Property prop = model.createProperty(nsPersonProps + "hasAddress");
        res.addProperty(prop, model.createTypedLiteral(address));
    }

    protected void changeAddress(String newAddress) {
        update("PREFIX rdf: <"+RDF.getURI()+">\n" +
                "PREFIX foaf: <"+FOAF.getURI()+">\n" +
                "PREFIX PersonProps: <http://www.example.com/personproperties.rdf#>\n" +
                "DELETE { ?person PersonProps:hasAddress \"" + address + "\" }\n" +
                "INSERT { ?person PersonProps:hasAddress \"" + newAddress + "\"\n} " +
                "WHERE { ?person PersonProps:hasAddress \"" + address + "\" }\n");
    }

    @Override
    public int compareTo(Person p) {
        return this.getName().compareTo(p.getName());
    }

    private void update(String query) {
        UpdateAction.parseExecute(query, model);
    }
}
