package rdf;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
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

    private final static String nameText = "Enter a name: ";
    private final static String genderText = "Enter a gender - (m)ale or (f)emale: ";
    private final static String birthdayText = "Enter the date of birth in the format 'dd.MM.yyyy': ";
    private final static String addressText = "Enter an Address: ";
    private final static String companyText = "Enter a Workplace: ";

    private String nsPersons = "http://www.example.com/persons/";
    private String nsPersonProps = "http://www.example.com/personproperties.rdf#";

    public ModelCreator() {
        dataset = TDBFactory.createDataset("d:/tools/semtech/dataset");
        model = dataset.getDefaultModel();
        queries = new Queries(dataset, nsPersonProps, nsPersons);
        model.setNsPrefix("Person", nsPersons);
        model.setNsPrefix("PersonProps", nsPersonProps);
    }

    protected void writeModelDB() {
        dataset.begin(ReadWrite.WRITE);
        try {
            dataset.commit();
        } finally { dataset.end(); }
        dataset.begin(ReadWrite.READ);
        try {
            dataset.getDefaultModel().write(System.out, "TURTLE");
        } finally { dataset.end(); }
        listAllPersons();
    }

    public void listAllPersons() {
        queries.listAllPersons();
    }

    public void deleteAllPersons() {
        queries.deleteAllPersons();
    }

    public void createPerson(String value) {
        person = new Person(model, nsPersons, value);
        person.setName(value);
    }

    public void createPerson() throws IOException{
        System.out.print(nameText);
        String name = createString();
        if(!queries.personExists(name)) {
            createPerson(name);
            setGender();
            setBirthday();
            setAddress();
            setCompany();
            writeModelDB();
        }
    }

    public void createDummyPersons() throws IOException {
        createPerson("Hanna");
        setGender("male");
        setBirthday("01.01.1992");
        setAddress("Hauptstraße 1, 4020 Linz");
        setCompany("Johannes Kepler University");
        writeModelDB();
        createPerson("Thomas");
        setGender("male");
        setBirthday("16.05.1985");
        setAddress("Seitenstraße 1, 4030 Linz");
        setCompany("FH Hagenberg");
        writeModelDB();
        createPerson("Sarah");
        setGender("female");
        setBirthday("27.11.1999");
        setAddress("Gasse 14, 4010 Linz");
        setCompany("Uni Wien");
        writeModelDB();
    }

    public void changeDummyPersons() throws IOException {
        System.out.println("<-CHANGE-GENDER->");
        changeGender("Hanna", "female");
        System.out.println("<-CHANGE-COMPANY->");
        changeCompany("Thomas", "SIEMENS");
        System.out.println("<-CHANGE-BIRTHDAY->");
        changeBirthday("Sarah", "02.02.1991");
        System.out.println("<-CHANGE-ADDRESS->");
        changeAddress("Sarah", "Umfahrung 67, 1010 Wien");
    }

    public void deleteDummyPersons() {
        deletePerson("Sarah");
        deletePerson("Thomas");
    }

    private void deletePerson(String value) {
        queries.deletePerson(value);
    }

    public void deletePerson() throws IOException{
        System.out.print(nameText);
        deletePerson(createString());
    }

    private void setGender(String value) {
        person.setGender(value);
    }

    public void setGender() throws IOException {
        System.out.print(genderText);
        setGender(createGenderString());
    }

    private void changeGender(String name, String value) {
        queries.changeGender(name, value);
    }

    public void changeGender(String name) throws IOException {
        System.out.print(genderText);
        changeGender(name, createGenderString());
    }

    private void setBirthday(String value) {
        person.setBirthday(value);
    }

    public void setBirthday() {
        System.out.print(birthdayText);
        setBirthday(createBirthdayString());
    }

    private void changeBirthday(String name, String value) {
        queries.changeBirthday(name, value);
    }

    public void changeBirthday(String name) throws IOException {
        System.out.print(birthdayText);
        changeBirthday(name, createBirthdayString());
    }

    private void setAddress(String value) {
        person.setAddress(value, nsPersonProps);
    }

    public void setAddress() throws IOException {
        System.out.print(addressText);
        setAddress(createString());
    }

    private void changeAddress(String name, String value) {
        queries.changeAddress(name, value);
    }

    public void changeAddress(String name) throws IOException {
        System.out.print(addressText);
        changeAddress(name, createString());
    }

    private void setCompany(String value) {
        person.setCompany(value, nsPersonProps);
    }

    public void setCompany() throws IOException {
        System.out.print(companyText);
        setCompany(createString());
    }

    private void changeCompany(String name, String value) {
        queries.changeCompany(name, value);
    }

    public void changeCompany (String name) throws IOException {
        System.out.print(companyText);
        changeCompany(name, createString());
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
                System.err.print("\tERROR:\tSorry, that's not valid. Please try again: ");
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
                    System.err.print("\tERROR:\tSorry, that's not valid.\n" +
                            "\t\tA gender must be (m)ale or (f)emale. Please try again: ");
                    break;
            }
        } while (input != 'm' && input != 'f');
        return "";
    }

    public static char createChar() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine().charAt(0);
    }

    public static String createString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    /*
    private void readModelFile() {
        file = new File(modelUri);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model = RDFDataMgr.loadModel(modelUri);
    }
    */

    /*
    private void writeModelFile() throws IOException {
        Writer writer = new FileWriter(file);
        model.write(writer, "TURTLE");
        writer.close();
        model.write(System.out, "TURTLE");
    }
    */
}
