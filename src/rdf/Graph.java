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
 * Created by Peter on 06.04.2017.
 */
public class Graph {

    private ModelCreator mod;


    public static void main (String[] args) throws IOException {
        ModelCreator mod = new ModelCreator();
        createDummyPerson(mod);
        changeDummyPerson(mod);

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
        System.out.println("\n===============CHANGE=GENDER========================");
        mod.changeGender("Sarah", "female");
        System.out.println("\n===============CHANGE=BIRTHDAY======================");
        mod.changeBirthday("Sarah", "02.02.1991");
        System.out.println("\n===============CHANGE=ADDRESS=======================");
        mod.changeAddress("Sarah", "Seitenstraße 2, 4030 Linz");
        System.out.println("\n===============CHANGE=COMPANY=======================");
        mod.changeCompany("Sarah", "SIEMENS");
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
        Scanner scanner = new Scanner(System.in);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        System.out.print("Enter date of birth in the format 'dd.MM.yyyy': ");
        Date date = null;
        while (date == null) {
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.print("Sorry, that's not valid. Please try again: ");
            }
        }
        mod.setBirthday(person, format.format(date));
        System.out.print("Address: ");
        mod.setAddress(person, br.readLine());
        System.out.print("Workplace: ");
        mod.setCompany(person, br.readLine());
        mod.write();
    }

    public static void changePerson(ModelCreator mod) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Name: ");
        String name = br.readLine();
        System.out.print("New name: ");
        String newName = br.readLine();
        mod.changeName(name, newName);
        mod.write();
    }
}
