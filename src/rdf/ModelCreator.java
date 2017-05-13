package rdf;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.tdb.TDBFactory;
import sun.rmi.runtime.Log;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Peter & Natalia.
 */
public class ModelCreator {

    private Model model;
    private Dataset dataset;
    private Queries queries;
    private Person person;

    protected final static String fullIDText = "Enter an ID: ";
    protected final static String subIdText = "Enter the first 4 digits of your social security number: ";
    protected final static String nameText = "Enter a name: ";
    protected final static String genderText = "Enter a gender - (m)ale or (f)emale: ";
    protected final static String birthdayText = "Enter the date of birth in the format 'dd.MM.yyyy': ";
    protected final static String addressText = "Enter an Address: ";
    protected final static String companyText = "Enter a Workplace: ";
    protected final static String errorMsg = "Sorry, that's not a valid input. Please try again: ";
    protected final static String errorMsgGender = "Sorry, that's not valid a valid input.\n" +
            "A gender must be (m)ale or (f)emale. Please try again: ";

    private String nsPersons = "http://semTechRDF.org/persons/";
    private String nsProperties = "http://semTechRDF.org/properties.rdf#";
    private String nsExPersonsG = "http://semTechRDF.org/ex-persons.rdf#";
    private String nsDelPersonsG = "http://semTechRDF.org/del-persons.rdf#";
    private String nsFoaf = FOAF.getURI();

    public ModelCreator() {
        String path = System.getProperty("user.home") + "/Desktop/SemtechRDF-TDB";
        File directory = new File(path);
        if (!directory.exists()){
            directory.mkdirs();
        }
        dataset = TDBFactory.createDataset(path);
        queries = new Queries(dataset, nsExPersonsG, nsDelPersonsG, nsProperties);
        dataset.getDefaultModel().setNsPrefix("foaf", nsFoaf);
        dataset.getDefaultModel().setNsPrefix("Person", nsPersons);
        dataset.getDefaultModel().setNsPrefix("properties", nsProperties);
        queries.createNamedGraphs();
    }

    protected void writeModelDB() {
        dataset.begin(ReadWrite.WRITE);
        try {
            dataset.commit();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            dataset.abort();
        } finally {
            dataset.end();
        }
        listAllPersons(1);
    }

    public void listAllPersons(int mode) {
        switch (mode) {
            case 1:
                queries.listAllPersons();
                break;
            case 2:
                dataset.begin(ReadWrite.READ);
                try {
                    dataset.getDefaultModel().write(System.out, "TURTLE");
                } finally {
                    dataset.end();
                }
                break;
            default:
                LogHelper.logError("Value " + mode + " is not recognized as mode parameter");
        }
    }

    public void listGraphExisting() {
        queries.listGraph(nsExPersonsG);
    }

    public void listGraphDeleted() {
        queries.listGraph(nsDelPersonsG);
    }

    public void filerPersonGender() throws IOException {
        queries.filter(1);
    }

    public void filerPersonLocation() throws IOException {
        queries.filter(2);
    }

    public void createPerson(String fullID) {
        person = new Person(dataset.getDefaultModel(), nsPersons, nsFoaf, fullID);
    }

    protected boolean personExists(String fullID) {
        return queries.personExists(fullID);
    }

    private boolean personInGraph(String fullID) {
        return queries.personInGraph(fullID);
    }

    public void createPerson() throws IOException {
        System.out.print(nameText);
        String name = createString();
        System.out.print(subIdText);
        String idPart1 = createIDSubstring();
        System.out.print(birthdayText);
        String birthday = createBirthdayString();
        String idPart2 = birthday.replaceAll("\\." , "");
        String fullID = idPart1 +
                idPart2.substring(0, idPart2.length()-4) +
                idPart2.substring(idPart2.length()-2, idPart2.length());
        if (!personExists(fullID)) {
            createPerson(fullID);
            setName(name);
            setBirthday(birthday);
            setGender();
            setAddress();
            setCompany();
            writeModelDB();
            addPersonToGraph(fullID);
        } else LogHelper.logError("The person with ID: " + fullID + " already exists!");
    }

    private void addPersonToGraph(String fullID) {
        if (!personInGraph(fullID)) {
            queries.addNewPerson(fullID);
        }
        listGraphExisting();
        LogHelper.logInfo("Added person resource with ID: " + fullID + " to named graph");
    }

    public void addFriend(String fullID) throws IOException {
        System.out.print(fullIDText);
        String fullID_friend = createString();
        if (personExists(fullID_friend)) {
            queries.addFriend(fullID, fullID_friend);
        } else LogHelper.logError("The person with ID: " + fullID_friend + " does not exist.");
    }

    public void createDummyPersons() throws IOException {

        createPerson("1111010192");
        setName("Johannes");
        setGender("male");
        setBirthday("01.01.1992");
        setAddress("Hauptstraße 1, 4020 Linz");
        setCompany("Johannes Kepler University");
        writeModelDB();
        createPerson("2222160585");
        setName("Markus");
        setGender("male");
        setBirthday("16.05.1985");
        setAddress("Seitenstraße 1, 4030 Linz");
        setCompany("FH Hagenberg");
        writeModelDB();
        createPerson("3333271199");
        setName("Sandra");
        setGender("female");
        setBirthday("27.11.1999");
        setAddress("Gasse 14, 4010 Linz");
        setCompany("Uni Wien");
        writeModelDB();
    }

    public void changeDummyPersons() throws IOException {
        System.out.println("<-CHANGE-GENDER->");
        changeGender("2222160585", "female");
        System.out.println("<-CHANGE-COMPANY->");
        changeCompany("3333271199", "SIEMENS");
        System.out.println("<-CHANGE-BIRTHDAY->");
        changeBirthday("1111010192", "02.02.1991");
        System.out.println("<-CHANGE-ADDRESS->");
        changeAddress("1111010192", "Umfahrung 67, 1010 Wien");
    }

    public void deleteDummyPersons () throws IOException {
        deletePerson("3333271199");
    }

    private void deletePerson(String fullID) throws IOException {
        System.out.println("<-LISTING-GRAPH---Deleted-Persons->");
        queries.deletePerson(fullID);
        listGraphDeleted();
        System.out.println("<-LISTING-GRAPH---Remaining-Existing-Persons->");
        listGraphExisting();
    }

    public void deletePerson() throws IOException {
        System.out.print(fullIDText);
        String fullID = createString();
        if (personExists(fullID)) {
            deletePerson(fullID);
        } else LogHelper.logError("The person with ID: " + fullID + " does not exist.");
    }

    public void deleteAllPersons() {
        System.out.println("<-LISTING-GRAPH---All-Deleted-Persons->");
        queries.deleteAllPersons();
        listGraphDeleted();
        System.out.println("<-LISTING-GRAPH---No-Existing-Persons->");
        listGraphExisting();
    }

    public void setName(String value) {
        person.setName(value);
    }

    private void changeName(String fullID, String value) {
        queries.changeName(fullID, value);
    }

    public void changeName(String fullID) throws IOException {
        System.out.print(nameText);
        changeName(fullID, createString());
    }

    private void setGender(String value) {
        person.setGender(value);
    }

    public void setGender() throws IOException {
        System.out.print(genderText);
        setGender(createGenderString());
    }

    private void changeGender(String fullID, String value) {
        queries.changeGender(fullID, value);
    }

    public void changeGender(String fullID) throws IOException {
        System.out.print(genderText);
        changeGender(fullID, createGenderString());
    }

    private void setBirthday(String value) {
        person.setBirthday(value);
    }

    private void changeBirthday(String fullID, String value) {
        queries.changeBirthday(fullID, value);
    }

    public void changeBirthday(String fullID) throws IOException {
        System.out.print(birthdayText);
        changeBirthday(fullID, createBirthdayString());
    }

    private void setAddress(String value) {
        person.setAddress(value, nsFoaf);
    }

    public void setAddress() throws IOException {
        System.out.print(addressText);
        setAddress(createString());
    }

    private void changeAddress(String fullID, String value) {
        queries.changeAddress(fullID, value);
    }

    public void changeAddress(String fullID) throws IOException {
        System.out.print(addressText);
        changeAddress(fullID, createString());
    }

    private void setCompany(String value) {
        person.setCompany(value, nsFoaf);
    }

    public void setCompany() throws IOException {
        System.out.print(companyText);
        setCompany(createString());
    }

    private void changeCompany(String fullID, String value) { queries.changeCompany(fullID, value);
    }

    public void changeCompany(String fullID) throws IOException {
        System.out.print(companyText);
        changeCompany(fullID, createString());
    }

    private static String createBirthdayString() {
        Scanner scanner = new Scanner(System.in);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        while (date == null) {
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                LogHelper.logError(errorMsg);
            }
        }
        return format.format(date);
    }

    private static String createGenderString() throws IOException {
        char input;
        do {
            switch (input = createChar()) {
                case 'm':
                    return "male";
                case 'f':
                    return "female";
                default:
                    LogHelper.logError(errorMsgGender);
                    break;
            }
        } while (input != 'm' && input != 'f');
        return "";
    }

    public static char createChar() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line.length() == 0) return '\u0000';
        else return line.charAt(0);
    }

    public static String createString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    private static String createIDSubstring() throws IOException {
        Boolean error;
        BufferedReader br;
        String line;
        do {
            error = false;
            br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            if (line.length() > 4) error = true;
            for (char c : line.toCharArray()) {
                if (!Character.isDigit(c)) {
                    error = true;
                    break;
                }
            }
            if (error == true) LogHelper.logError(errorMsg);
        } while (error == true);
        return line;
    }
}
