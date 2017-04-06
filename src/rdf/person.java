package rdf;

import org.apache.jena.rdf.model.*;

public class person {

    String name, address, birthday, employer;
    char gender;

    public person(Model m, String name, String address, String birthday, char gernder, String employer) {
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.employer = employer;
    }


}
