package rdf;

import java.io.IOException;

/**
 * Created by Peter & Natalia.
 */
public class Main {

    private final static String mainMenuText = "\n<-MAIN-MENU->\n" +
            "What do you like to do?\n" +
            "\t(1) create a new person and add it to the existing persons graph\n" +
            "\t(2) change person information\n" +
            "\t(3) delete a person from the exisitng person graph (move to deleted person graph)\n" +
            "\t(4) delete all persons (move all persons to the deleted person graph)\n" +
            "\t(5) list all persons (graph independent, ResultSet)\n" +
            "\t(6) list all persons (graph independent, Turtle Notation)\n" +
            "\t(7) filter the persons listed in TDB by spedified criteria\n" +
            "\t(0) exit program\n" +
            "\t: ";
    private final static String changeMenuText = "\n<-CHANGE-INFORMATION->\n" +
            "What information do you want to change?\n" +
            "\t(1) name\n" +
            "\t(2) gender\n" +
            "\t(3) birthday\n" +
            "\t(4) address\n" +
            "\t(5) company\n" +
            "\t(6) add a friend\n" +
            "\t(0) back\n" +
            "\t: ";
    private final static String filterMenuText = "\n<-FILTER-PERSON->\n" +
            "What filter do you want to try?\n" +
            "\t(1) gender\n" +
            "\t(2) location\n" +
            "\t(0) cancel\n" +
            "\t:";

    public static void main (String[] args) throws IOException {
        System.out.println("<-RDF-PERSON-DATABASE->");
        ModelCreator mod = new ModelCreator();
        System.out.print(mainMenuText);
        char info;
        do {
            switch (info = ModelCreator.createChar()) {
                case '1':
                    //mod.createDummyPersons();
                    createPerson(mod);
                    break;
                case '2':
                    //mod.changeDummyPersons();
                    changePerson(mod);
                    break;
                case '3':
                    //mod.deleteDummyPersons();
                    deletePerson(mod);
                    break;
                case '4':
                    mod.deleteAllPersons();
                    break;
                case '5':
                    mod.listAllPersons(1);
                    break;
                case '6':
                    mod.listAllPersons(2);
                    break;
                case '7':
                    filterPersons(mod);
                    break;
                case '0':
                    System.exit(0);
                default:
                    LogHelper.logError(ModelCreator.errorMsg);
                    break;
            }
            System.out.print(mainMenuText);

        } while (info != '0');
    }

    public static void createPerson(ModelCreator mod) throws IOException {
        System.out.println("<-CREATE-PERSON->");
        mod.createPerson();
    }

    public static void changePerson(ModelCreator mod) throws IOException {
        char info;
        System.out.println("\n<-CHANGE-MENU->");
        System.out.print("Enter the name of the person you want to change information for: ");
        String fullID = ModelCreator.createString();
        if (mod.personExists(fullID)) {
            System.out.print(changeMenuText);
            do {
                switch (info = ModelCreator.createChar()) {
                    case '1':
                        System.out.println("<-CHANGE-NAME->");
                        mod.changeName(fullID);
                        break;
                    case '2':
                        System.out.println("<-CHANGE-GENDER->");
                        mod.changeGender(fullID);
                        break;
                    case '3':
                        System.out.println("<-CHANGE-BIRTHDAY->");
                        mod.changeBirthday(fullID);
                        break;
                    case '4':
                        System.out.println("<-CHANGE-ADDRESS->");
                        mod.changeAddress(fullID);
                        break;
                    case '5':
                        System.out.println("<-CHANGE-COMPANY->");
                        mod.changeCompany(fullID);
                        break;
                    case '6':
                        System.out.println("<-ADD-A-FRIEND->");
                        mod.addFriend(fullID);
                        break;
                    case '0':
                        break;
                    default:
                        LogHelper.logError(ModelCreator.errorMsg);
                        break;
                }
                System.out.print(changeMenuText);
            } while (info != '0');
        } else LogHelper.logError("The person with ID: " + fullID + " does not exists!");
    }

    public static void deletePerson(ModelCreator mod) throws IOException {
        System.out.println("<-DELETE-PERSON->");
        mod.deletePerson();
    }

    public static void deleteAllPerson(ModelCreator mod) throws IOException {
        System.out.println("<-DELETE-ALL PERSONS->");
        mod.deleteAllPersons();
    }

    public static void filterPersons(ModelCreator mod) throws IOException {
        System.out.println(filterMenuText);
        char input;
        do {
            switch (input = ModelCreator.createChar()) {
                case '1':
                    System.out.println("<-FILTER-PERSON-GENDER>");
                    mod.filerPersonGender();
                    break;
                case '2':
                    System.out.println("<-FILTER-PERSON-LOCATION>");
                    mod.filerPersonLocation();
                    break;
                case '0':
                    break;
                default:
                    LogHelper.logError(ModelCreator.errorMsg);
                    break;
            }
            System.out.print(filterMenuText);
        } while (input != '0');
    }
}
