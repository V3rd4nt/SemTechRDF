package rdf;

import java.io.IOException;

/**
 * Created by Peter & Natalia.
 */
public class Main {

    private ModelCreator mod = new ModelCreator();
    private final static String mainMenuText = "\n<-MAIN-MENU->\n" +
            "What do you like to do?\n" +
            "\t(1) add a new person\n" +
            "\t(2) change person information\n" +
            "\t(3) delete a person\n" +
            "\t(4) delete all persons\n" +
            "\t(5) list all available persons\n" +
            "\t(6) filter the person list\n" +
            "\t(0) exit program\n" +
            "\t: ";
    private final static String changeMenuText = "\n<-CHANGE-INFORMATION->\n" +
            "What information do you want to change?\n" +
            "\t(1) name\n" +
            "\t(2) gender\n" +
            "\t(3) birthday\n" +
            "\t(4) address\n" +
            "\t(5) company\n" +
            "\t(0) back\n" +
            "\t: ";
    private final static String filterMenuText = "\n<-FILTER-PERSON->\n" +
            "Do you want to try other filters?\n" +
            "\t(1) gender\n" +
            "\t(2) location\n" +
            "\t(0) cancel\n" +
            "\t:";
    private final static String errorMsg = "Sorry, that's not a valid input. Please try again: ";

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
                    //TODO
                    //mod.deleteDummyPersons();
                    //deletePerson(mod);
                    LogHelper.logError("Needs to be implemented");
                    break;
                case '4':
                    //TODO
                    //mod.deleteAllPersons();
                    LogHelper.logError("Needs to be implemented");
                    break;
                case '5':
                    mod.listAllPersons();
                    break;
                case '6':
                    filterPersons(mod);
                    break;
                case '0':
                    System.exit(0);
                default:
                    LogHelper.logError(errorMsg);
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
        String name = ModelCreator.createString();
        if (mod.personExists(name)) {
            System.out.print(changeMenuText);
            do {
                switch (info = ModelCreator.createChar()) {
                    case '1':
                        System.out.println("<-CHANGE-NAME->");
                        LogHelper.logInfo(name);
                        name = mod.changeName(name);
                        break;
                    case '2':
                        System.out.println("<-CHANGE-GENDER->");
                        mod.changeGender(name);
                        break;
                    case '3':
                        System.out.println("<-CHANGE-BIRTHDAY->");
                        mod.changeBirthday(name);
                        break;
                    case '4':
                        System.out.println("<-CHANGE-ADDRESS->");
                        mod.changeAddress(name);
                        break;
                    case '5':
                        System.out.println("<-CHANGE-COMPANY->");
                        mod.changeCompany(name);
                        break;
                    case '0':
                        break;
                    default:
                        LogHelper.logError(errorMsg);
                        break;
                }
                System.out.print(changeMenuText);
            } while (info != '0');
        } else LogHelper.logError("The person " + name + " does not exists!");
    }

    public static void deletePerson(ModelCreator mod) throws IOException {
        System.out.println("<-DELETE-PERSON->");
        mod.deletePerson();
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
                    LogHelper.logError(errorMsg);
                    break;
            }
            System.out.print(filterMenuText);
        } while (input != '0');
    }
}
