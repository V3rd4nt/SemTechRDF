package rdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Peter & Natalia on 06.04.2017.
 */
public class Graph {

    private ModelCreator mod = new ModelCreator();


    public static void main (String[] args) throws IOException {
        ModelCreator mod = new ModelCreator();
        createDummyPerson(mod);
        changeDummyPerson(mod);
        deleteDummyPerson(mod);
        //createPerson(mod);
        //changePerson(mod);
        //deletePerson(mod);
    }

    public static void createDummyPerson(ModelCreator mod) {
        String person = "Max";
        mod.createPerson(person);
        mod.setPersonGender(person, "male");
        mod.setBirthday(person,"01.01.1992");
        mod.setAddress(person, "Hauptstraße 1, 4020 Linz");
        mod.setCompany(person, "Johannes Kepler University");
        mod.write();
    }

    public static void changeDummyPerson(ModelCreator mod) {
        System.out.println("\n===============CHANGE=NAME==========================");
        mod.changeName("Max", "Sarah");
        System.out.println("\n===============CHANGE=NAME=ERROR====================");
        mod.changeName("Max", "Tom");
        System.out.println("\n===============CHANGE=GENDER========================");
        mod.changeGender("Sarah", "female");
        System.out.println("\n===============CHANGE=BIRTHDAY======================");
        mod.changeBirthday("Sarah", "02.02.1991");
        System.out.println("\n===============CHANGE=ADDRESS=======================");
        mod.changeAddress("Sarah", "Seitenstraße 2, 4030 Linz");
        System.out.println("\n===============CHANGE=COMPANY=======================");
        mod.changeCompany("Sarah", "SIEMENS");
    }

    public static void deleteDummyPerson(ModelCreator mod) {
        System.out.println("\n===============DELETE=PERSON========================");
        mod.deletePerson("Sarah");
    }

    public static void createPerson(ModelCreator mod) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Name: ");
        String person = br.readLine();
        mod.createPerson(person);
        System.out.print("Gender (m/f): ");
        char gender;
        do {
            gender = br.readLine().charAt(0);
            switch (gender) {
                case 'm':
                    mod.setPersonGender(person, "male");
                    break;
                case 'f':
                    mod.setPersonGender(person, "female");
                    break;
                default:
                    System.out.print("Sorry, that's not valid.\nA gender must be (m)ale or (f)emale. Please try again: ");
                    break;
            }
        } while (gender != 'm' && gender != 'f');
        Date date = null;
        mod.checkDate(date);
        System.out.print("Address: ");
        mod.setAddress(person, br.readLine());
        System.out.print("Workplace: ");
        mod.setCompany(person, br.readLine());
        mod.write();
    }

    public static void changePerson(ModelCreator mod) throws IOException {
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
                    Date newDate = null;
                    mod.checkDate(newDate);
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
        mod.deletePerson(name);
    }
}
