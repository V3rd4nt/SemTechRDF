package rdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Peter & Natalia.
 */
public class Main {

    private ModelCreator mod = new ModelCreator();


    public static void main (String[] args) throws IOException {
        ModelCreator mod = new ModelCreator();
        createDummyPerson(mod);
        changeDummyPerson(mod);
        //changeDummyPerson(mod);
        //deleteDummyPerson(mod);
        createPerson(mod);
        //changePerson(mod);
        //deletePerson(mod);
    }

    public static void createDummyPerson(ModelCreator mod) throws IOException {
        mod.createDummyPerson();
    }

    public static void changeDummyPerson(ModelCreator mod) throws IOException {
        /*System.out.println("\n===============CHANGE=NAME==========================");
        mod.changeName("Hanna", "Tina");
        System.out.println("\n===============CHANGE=NAME=ERROR====================");
        mod.changeName("Max", "Tom");*/
        System.out.println("\n===============CHANGE=GENDER========================");
        mod.changeGender("Hanna", "female");
        System.out.println("\n===============CHANGE=COMPANY=======================");
        mod.changeCompany("Thomas", "SIEMENS");
        System.out.println("\n===============CHANGE=BIRTHDAY======================");
        mod.changeBirthday("Sarah", "02.02.1991");
        System.out.println("\n===============CHANGE=ADDRESS=======================");
        mod.changeAddress("Sarah", "Umfahrung 67, 1010 Wien");
    }
    public static void deleteDummyPerson(ModelCreator mod) {
        System.out.println("\n===============DELETE=PERSON========================");
        //mod.deletePerson("Sarah");*/
    }

    public static void createPerson(ModelCreator mod) throws IOException {
        System.out.println("\n===============CREATE=PERSON========================");
        mod.createPerson();
    }

    // TODO: NOT READY YET
    /*public static void changePerson(ModelCreator mod) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("What information do you want to change?\nEnter   n - name;   g - gender;   b - birthday; " +
                "a - address;   c - company;   e - exit: ");
        char info;
        String name;
        do {
            info = br.readLine().charAt(0);
            switch (info) {
                case 'n':
                    System.out.print("Name: ");
                    name = br.readLine();
                    System.out.print("New Name: ");
                    String newName = br.readLine();
                    mod.changeName(name, newName);
                    System.out.print("For other changes press n - name;   g - gender;   b - birthday; " +
                            "a - address;   c - company;   or e - exit: ");
                    break;
                case 'g':
                    char gender;
                    System.out.print("Edit person named: ");
                    name = br.readLine();
                    System.out.print("Gender (m/f): ");
                    do {
                        gender = br.readLine().charAt(0);
                        switch (gender) {
                            case 'm':
                                mod.changeGender(name, "male");
                                break;
                            case 'f':
                                mod.changeGender(name, "female");
                                break;
                            default:
                                System.out.print("Sorry, that's not valid.\nA gender must be (m)ale or (f)emale. Please try again: ");
                                break;
                        }
                    } while (gender != 'm' && gender != 'f');
                    System.out.print("For other changes press n - name;   g - gender;   b - birthday; " +
                            "a - address;   c - company;   or e - exit: ");
                    break;
                case 'b':
                    System.out.print("Edit person named: ");
                    name = br.readLine();
                    mod.changeBirthday(name);
                    System.out.print("For other changes press n - name;   g - gender;   b - birthday; " +
                            "a - address;   c - company;   or e - exit: ");
                    break;
                case 'a':
                    System.out.print("Edit person named: ");
                    name = br.readLine();
                    System.out.print("New address: ");
                    mod.changeAddress(name, br.readLine());
                    System.out.print("For other changes press n - name;   g - gender;   b - birthday; " +
                            "a - address;   c - company;   or e - exit: ");
                    break;
                case 'c':
                    System.out.print("Edit person named: ");
                    name = br.readLine();
                    System.out.print("New company: ");
                    mod.changeCompany(name, br.readLine());
                    System.out.print("For other changes press n - name;   g - gender;   b - birthday; " +
                            "a - address;   c - company;   or e - exit: ");
                    break;
                case 'e':
                    break;
                default:
                    System.out.println("Sorry, that's not valid input. Please try again: ");
                    break;
            }
        } while (info != 'e');
        mod.write();
    }

    public static void deletePerson(ModelCreator mod) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Delete the person named: ");
        String name = br.readLine();
        //mod.deletePerson(name);
    }*/
}
