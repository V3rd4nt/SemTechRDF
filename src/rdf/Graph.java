package rdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Peter on 06.04.2017.
 */
public class Graph {

    private ModelCreator mod;

    public static void main (String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ModelCreator mod = new ModelCreator();
        createPerson(mod);
        mod.write();

    }
        public static void createPerson(ModelCreator mod) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Name: ");
            String person1 = br.readLine();
            mod.createPerson(person1);
            System.out.print("Gender (m/f): ");
            char gender;
            do {
                gender = br.readLine().charAt(0);
                switch (gender) {
                    case 'm':
                        mod.setPersonGender(person1, "male");
                        break;
                    case 'f':
                        mod.setPersonGender(person1, "female");
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
            mod.setBirthday(person1, format.format(date));
            System.out.print("Address: ");
            mod.setAddress(person1, br.readLine());
            System.out.print("Workplace: ");
            mod.setWorkPlace(person1, br.readLine());
    }
}
