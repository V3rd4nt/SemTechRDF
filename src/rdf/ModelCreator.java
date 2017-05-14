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

    private Dataset dataset;
    private Queries queries;

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

    public ModelCreator() {
        String path = System.getProperty("user.home") + "/Desktop/SemtechRDF-TDB";
        File directory = new File(path);
        if (!directory.exists()){
            directory.mkdirs();
        }
        dataset = TDBFactory.createDataset(path);
        queries = new Queries(dataset, nsExPersonsG, nsDelPersonsG, nsProperties, nsPersons);
        queries.createNamedGraphs();
    }

    public void listAllPersons() {
        dataset.begin(ReadWrite.READ);
        try {
            dataset.getDefaultModel().write(System.out, "TURTLE");
        } finally {
            dataset.end();
        }
    }

    public void listGraphExisting() {
        System.out.println("<-GRAPH---EXISTING-PERSONS->");
        queries.listGraph(nsExPersonsG);
    }

    public void listGraphDeleted() {
        System.out.println("<-GRAPH---DELETED-PERSONS->");
        queries.listGraph(nsDelPersonsG);
    }

    public void filerPersonGender() throws IOException {
        queries.filter(1);
    }

    public void filerPersonLocation() throws IOException {
        queries.filter(2);
    }

    public boolean createPerson(String fullID) {
        if (!personExists(fullID)) {
            queries.createPerson(fullID);
            return true;
        } else {
            LogHelper.logError("The person with ID: " + fullID + " already exists!");
            return false;
        }
    }

    protected boolean personExists(String fullID) {
        return queries.personExists(fullID);
    }

    private boolean personExistsInGraph(String fullID) {
        return queries.personExistsG(fullID);
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

        if (createPerson(fullID)) {
            setName(fullID, name);
            setBirthday(fullID, birthday);
            setGender(fullID);
            setAddress(fullID);
            setCompany(fullID);
            addPersonToGraph(fullID);
        }
    }

    private void addPersonToGraph(String fullID) {
        if (!personExistsInGraph(fullID)) {
            queries.addNewPersonG(fullID);
        }
        LogHelper.logInfo("Added person resource with ID: " + fullID + " to named graph");
    }

    private void modifyFriend(String fullID, int mode) throws IOException {
        System.out.print(fullIDText);
        String fullID_friend = createString();
        if (personExists(fullID_friend)) {
            switch (mode) {
                case 1:
                    // add a friend
                    queries.setFriend(fullID, fullID_friend);
                    break;
                case 2:
                    // delete a friend
                    queries.deleteFriend(fullID, fullID_friend);
                    break;
                default:
                    LogHelper.logError("Value " + mode + " is not recognized as mode parameter");
            }
        } else LogHelper.logError("The person with ID: " + fullID_friend + " does not exist.");
    }

    public void addFriend(String fullID) throws IOException {
        modifyFriend(fullID, 1);
    }

    public void deleteFriend(String fullID) throws IOException {
        modifyFriend(fullID, 2);
    }

    private void deletePerson(String fullID) throws IOException {
        queries.deletePersonG(fullID);
        listGraphExisting();
        listGraphDeleted();
    }

    public void deletePerson() throws IOException {
        System.out.print(fullIDText);
        String fullID = createString();
        if (personExists(fullID)) {
            deletePerson(fullID);
        } else LogHelper.logError("The person with ID: " + fullID + " does not exist.");
    }

    public void deleteAllPersons() {
        queries.deleteAllPersonsG();
        listGraphExisting();
        listGraphDeleted();
    }

    public void setName(String fullID, String value) {
        queries.setName(fullID, value);
    }

    private void changeName(String fullID, String value) {
        queries.changeName(fullID, value);
    }

    public void changeName(String fullID) throws IOException {
        System.out.print(nameText);
        changeName(fullID, createString());
    }

    private void setGender(String fullID, String value) {
        queries.setGender(fullID, value);
    }

    public void setGender(String fullID) throws IOException {
        System.out.print(genderText);
        setGender(fullID, createGenderString());
    }

    private void changeGender(String fullID, String value) {
        queries.changeGender(fullID, value);
    }

    public void changeGender(String fullID) throws IOException {
        System.out.print(genderText);
        changeGender(fullID, createGenderString());
    }

    private void setBirthday(String fullID, String value) {
        queries.setBirthday(fullID, value);
    }

    private void changeBirthday(String fullID, String value) {
        queries.changeBirthday(fullID, value);
    }

    public void changeBirthday(String fullID) throws IOException {
        System.out.print(birthdayText);
        changeBirthday(fullID, createBirthdayString());
    }

    private void setAddress(String fullID, String value) {
        queries.setAddress(fullID, value);
    }

    public void setAddress(String fullID) throws IOException {
        System.out.print(addressText);
        setAddress(fullID, createString());
    }

    private void changeAddress(String fullID, String value) {
        queries.changeAddress(fullID, value);
    }

    public void changeAddress(String fullID) throws IOException {
        System.out.print(addressText);
        changeAddress(fullID, createString());
    }

    private void setCompany(String fullID, String value) {
        queries.setCompany(fullID, value);
    }

    public void setCompany(String fullID) throws IOException {
        System.out.print(companyText);
        setCompany(fullID, createString());
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

        /*
    public void createDummyPersons() throws IOException {

        Person p1 = createPerson("1111010192");
        setName(p1, "Johannes");
        setGender(p1, "male");
        setBirthday(p1, "01.01.1992");
        setAddress(p1, "Hauptstraße 1, 4020 Linz");
        setCompany(p1, "Johannes Kepler University");
        writeModelDB();
        Person p2 = createPerson("2222160585");
        setName(p2, "Markus");
        setGender(p2, "male");
        setBirthday(p2, "16.05.1985");
        setAddress(p2, "Seitenstraße 1, 4030 Linz");
        setCompany(p2, "FH Hagenberg");
        writeModelDB();
        Person p3 = createPerson("3333271199");
        setName(p3, "Sandra");
        setGender(p3, "female");
        setBirthday(p3, "27.11.1999");
        setAddress(p3, "Gasse 14, 4010 Linz");
        setCompany(p3, "Uni Wien");
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
    }*/
}
