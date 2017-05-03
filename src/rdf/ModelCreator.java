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
    private String nsPersons = "http://www.example.com/persons/";
    private String nsPersonProps = "http://www.example.com/personproperties.rdf#";

    public ModelCreator() {
        dataset = TDBFactory.createDataset("d:/tools/semtech/dataset");
        model = dataset.getDefaultModel().removeAll();
        queries = new Queries(dataset, nsPersonProps);
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
        queries.displayModel();
    }

    public void personDoesNotExist (String name) {
        System.out.print("ERROR:\tThe person " + name + " does not exist!");
    }

    public void createPerson(String value) {
        person = new Person(model, nsPersons, value);
        person.setName(value);
    }

    public void createPerson() throws IOException{
        System.out.print("Name: ");
        String value = createString();
        createPerson(value);
        setGender();
        setBirthday();
        setAddress();
        setCompany();
        writeModelDB();
    }

    public void createDummyPerson() throws IOException {
        String value = "Hanna";
        createPerson(value);
        setGender("male");
        setBirthday("01.01.1992");
        setAddress("Hauptstraße 1, 4020 Linz");
        setCompany("Johannes Kepler University");
        writeModelDB();
        value = "Thomas";
        createPerson(value);
        setGender("male");
        setBirthday("16.05.1985");
        setAddress("Seitenstraße 1, 4030 Linz");
        setCompany("FH Hagenberg");
        writeModelDB();
        value = "Sarah";
        createPerson(value);
        setGender("female");
        setBirthday("27.11.1999");
        setAddress("Gasse 14, 4010 Linz");
        setCompany("Uni Wien");
        writeModelDB();
    }

    /*public void changeName(String name, String value) {
        queries.changeName(name, value);
    }*/

    protected void setGender(String value) {
        person.setGender(value);
    }

    protected void setGender() throws IOException {
        System.out.print("Gender (m/f): ");
        String value = createGenderString();
        setGender(value);
    }

    protected void changeGender(String name, String value) throws IOException {
        queries.changeGender(name, value);
    }

    protected void changeGender(String name) throws IOException {
        String value = createGenderString();
        changeGender(name, value);
    }

    protected void setBirthday(String value) {
        person.setBirthday(value);
    }

    protected void setBirthday() {
        System.out.print("New date of birth in the format 'dd.MM.yyyy': ");
        String value = createBirthdayString();
        setBirthday(value);
    }

    protected void changeBirthday(String name, String value) throws IOException {
        queries.changeBirthday(name, value);
    }

    protected void changeBirthday(String name) throws IOException {
        String value = createBirthdayString();
        changeBirthday(name, value);
    }

    protected void setAddress(String value) {
        person.setAddress(value, nsPersonProps);
    }

    protected void setAddress() throws IOException {
        System.out.print("Address: ");
        String value = createString();
        setAddress(value);
    }

    protected void changeAddress(String name, String value) throws IOException {
        queries.changeAddress(name, value);
    }

    protected void changeAddress(String name) throws IOException {
        String value = createString();
        changeAddress(name, value);
    }

    protected void setCompany(String value) {
        person.setCompany(value, nsPersonProps);
    }

    protected void setCompany() throws IOException {
        System.out.print("Workplace: ");
        String value = createString();
        setCompany(value);
    }

    protected void changeCompany(String name, String value) throws IOException {
        queries.changeCompany(name, value);
    }

    protected void changeCompany (String name) throws IOException {
        String value = createString();
        changeCompany(name, value);
    }

    private String createBirthdayString() {
        Scanner scanner = new Scanner(System.in);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        while (date == null) {
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.print("Sorry, that's not valid. Please try again: ");
            }
        }
        return String.valueOf(date);
    }

    private String createGenderString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String gender = "";
        char input;
        do {
            input = br.readLine().charAt(0);
            switch (input) {
                case 'm':
                    gender = "male";
                    break;
                case 'f':
                    gender = "female";
                    break;
                default:
                    System.out.print("Sorry, that's not valid.\n" +
                            "A gender must be (m)ale or (f)emale. Please try again: ");
                    break;
            }
        } while (input != 'm' && input != 'f');
        return gender;
    }

    private String createString() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    /*private void readModelFile() {
        file = new File(modelUri);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model = RDFDataMgr.loadModel(modelUri);
    }*/


    /*protected void writeModelFile() throws IOException {
        Writer writer = new FileWriter(file);
        model.write(writer, "TURTLE");
        writer.close();
        model.write(System.out, "TURTLE");
    }*/

}
