package rdf;

import java.io.IOException;

/**
 * Created by Peter & Natalia.
 */
public class Main {

    private ModelCreator mod = new ModelCreator();
    private final static String mainMenuText = "\n<-MAIN-MENU->\n" +
            "What do you like to do?\n\t(a)dd a new person\n" +
            "\t(c)hange person information\n" + "\t(d)elete a person\n\td(e)lete all persons\n" +
            "\t(l)ist all available persons\n\t(f)ilter the person list\n\t(q)uit\n\t: ";
    private final static String changeMenuText = "\n<-CHANGE-INFORMATION->\n" +
            "What information do you want to change?\n" +
            "\t(n)ame\n\t(g)ender\n\t(b)irthday\n\t(a)ddress\n\t(c)ompany\n\t(e)xit\n\t: ";
    private final static String errorMsg = "Sorry, that's not a valid input. Please try again: ";
    private final static String filterNext = "<-FILTER-PERSON->\n" + "Do you want to try other filters? - 1(gender) or 2(location) - (e)xit:\n\t";

    public static void main (String[] args) throws IOException {
        ModelCreator mod = new ModelCreator();
        System.out.println("<-RDF-PERSON-DATABASE->");
        System.out.print(mainMenuText);
        char info;
        do {
            switch (info = ModelCreator.createChar()) {
                case 'a':
                    //mod.createDummyPersons();
                    createPerson(mod);
                    break;
                case 'c':
                    //mod.changeDummyPersons();
                    changePerson(mod);
                    break;
                case 'd':
                    //TODO
                    //mod.deleteDummyPersons();
                    //deletePerson(mod);
                    LogHelper.logError("Needs to be implemented");
                    break;
                case 'e':
                    //TODO
                    //mod.deleteAllPersons();
                    LogHelper.logError("Needs to be implemented");
                    break;
                case 'l':
                    mod.listAllPersons();
                    break;
                case 'f':
                    //TODO
                    filterPersons(mod);
                    //LogHelper.logError("Needs to be implemented");
                    break;
                case 'q':
                    System.exit(0);
                default:
                    LogHelper.logError(errorMsg);
                    break;
            }
            System.out.print(mainMenuText);
        } while (info != 'q');
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
                    case 'n':
                        System.out.println("<-CHANGE-NAME->");
                        LogHelper.logInfo(name);
                        name = mod.changeName(name);
                        break;
                    case 'g':
                        System.out.println("<-CHANGE-GENDER->");
                        mod.changeGender(name);
                        break;
                    case 'b':
                        System.out.println("<-CHANGE-BIRTHDAY->");
                        mod.changeBirthday(name);
                        break;
                    case 'a':
                        System.out.println("<-CHANGE-ADDRESS->");
                        mod.changeAddress(name);
                        break;
                    case 'c':
                        System.out.println("<-CHANGE-COMPANY->");
                        mod.changeCompany(name);
                        break;
                    case 'e':
                        break;
                    default:
                        LogHelper.logError(errorMsg);
                        break;
                }
                System.out.print(changeMenuText);
            } while (info != 'e');
        } else LogHelper.logError("The person " + name + " does not exists!");
    }

    public static void deletePerson(ModelCreator mod) throws IOException {
        System.out.println("<-DELETE-PERSON->");
        mod.deletePerson();
    }

    public static void filterPersons(ModelCreator mod) throws IOException {
        System.out.println("<-FILTER-PERSON->\n" + "Enter what you want to filter - 1(gender) or 2(location) - (e)xit:\t");
        char input;
        do {
            switch (input = ModelCreator.createChar()) {
                case '1':
                    System.out.println("<-FILTER-PERSON-GENDER>");
                    mod.filerPerson1();
                    break;
                case '2':
                    System.out.println("<-FILTER-PERSON-LOCATION>");
                    mod.filerPerson2();
                    break;
                case 'e':
                    break;
                default:
                    LogHelper.logError(errorMsg);
                    break;
            }
            System.out.print(filterNext);
        } while (input != 'e');
    }

}
