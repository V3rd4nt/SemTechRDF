package rdf;

import java.io.IOException;

/**
 * Created by Peter & Natalia.
 */
public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final static String mainMenuText = "\n<-MAIN-MENU->\n" +
            "What do you like to do?\n" +
            "\t(1) create a new person\n" +
            "\t(2) change person's information\n" +
            "\t(3) delete a person from the exisitng person graph\n" +
            ANSI_BLUE + "\t\t\t--> move all triplets of a person to the deleted person graph\n" + ANSI_RESET +
            "\t(4) delete all persons\n" +
            ANSI_BLUE +         "\t\t\t--> move all person triplets to the deleted person graph\n" + ANSI_RESET +
            "\t(5) list all persons\n" +
            ANSI_BLUE +         "\t\t\t--> graph independent, Output in TURTLE notation\n" + ANSI_RESET +
            "\t(6) list all triplets in all graphs\n" +
            ANSI_BLUE +         "\t\t\t--> Output in TURTLE notation\n" + ANSI_RESET +
            "\t(7) filter the persons listed in TDB by spedified criteria\n" +
            ANSI_BLUE + 		"\t\t\t--> graph independent filtering\n" + ANSI_RESET +
            "\t(0) exit program\n" +
            "\t: ";
    private final static String changeMenuText = "\n<-CHANGE-PERSON-INFO->\n" +
            "What information do you want to change?\n" +
            "\t(1) name\n" +
            "\t(2) gender\n" +
            "\t(3) birthday\n" +
            "\t(4) address\n" +
            "\t(5) company\n" +
            "\t(6) add a friend\n" +
            "\t(7) delete a friend\n" +
            "\t(0) back\n" +
            "\t: ";
    private final static String filterMenuText = "\n<-FILTER-PERSON->\n" +
            "What filter do you want to try?\n" +
            "\t(1) gender\n" +
            "\t(2) location\n" +
            "\t(0) cancel\n" +
            "\t: ";

    private final static String deleteMenuText = "\n<-DELETE-ALL-PERSONS->\n" +
            "Are you sure to delete all persons?:\n" +
            "\t(1) yes\n" +
            "\t(2) no\n" +
            "\t: ";

    public static void main (String[] args) throws IOException {
        System.out.println("<-RDF-PERSON-DATABASE->");
        ModelCreator mod = new ModelCreator();
        System.out.print(mainMenuText);
        char input;
        do {
            switch (input = ModelCreator.createChar()) {
                case '1':
                    createPerson(mod);
                    break;
                case '2':
                    changePerson(mod);
                    break;
                case '3':
                    deletePerson(mod);
                    break;
                case '4':
                    deleteAllPersons(mod);
                    break;
                case '5':
                    mod.listAllPersons();
                    break;
                case '6':
                    mod.listGraphExisting();
                    mod.listGraphDeleted();
                    break;
                case '7':
                    filterPersons(mod);
                    break;
                case 'd':
                    // hidden command
                    mod.createDummyPersons();
                    break;
                case '0':
                    System.exit(0);
                default:
                    LogHelper.logError(ModelCreator.errorMsg);
                    break;
            }
            System.out.print(mainMenuText);

        } while (input != '0');
    }

    public static void createPerson(ModelCreator mod) throws IOException {
        System.out.println("<-CREATE-A-PERSON->");
        mod.createPerson();
    }

    public static void changePerson(ModelCreator mod) throws IOException {
        System.out.println("\n<-CHANGE-PERSON-INFO->");
        System.out.print("Enter the ID of the person you want to change information for: ");
        String fullID = ModelCreator.createString();
        char input;
        if (mod.personExists(fullID) && mod.personExistsInGraph(fullID)) {
            System.out.print(changeMenuText);
            do {
                switch (input = ModelCreator.createChar()) {
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
                    case '7':
                        System.out.println("<-DELETE-A-FRIEND->");
                        mod.deleteFriend(fullID);
                        break;
                    case '0':
                        break;
                    default:
                        LogHelper.logError(ModelCreator.errorMsg);
                        break;
                }
                System.out.print(changeMenuText);
            } while (input != '0');
        } else LogHelper.logError("The person with ID: " + fullID + " does not exist\n" +
                "\t\t\t\t\t\t\t\t\t  or has been deleted from the 'existing persons' named graph.");
    }

    public static void deletePerson(ModelCreator mod) throws IOException {
        System.out.println("<-DELETE-A-PERSON->");
        mod.deletePerson();
    }

    public static void deleteAllPersons(ModelCreator mod) throws IOException {
        System.out.print(deleteMenuText);
        char input;
            do {
                switch (input = ModelCreator.createChar()) {
                    case '1':
                        mod.deleteAllPersons();
                    case '2':default:
                }
                System.out.print(deleteMenuText);
            } while (input != '1' && input != '2');
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
