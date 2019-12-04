package com.elendil.training;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Simple program to explore various Java8 lang features.
 * Just run and a results are printed to console.
 */
public class Main {

    public static void main(String[] args) {

        exploreComparators();
        dataProcessingWithLambdas();
        mapsWithLambdas();
        streamProcessing();
    }


    private static void exploreComparators() {
        Person[] personArray = {
                new Person("John", "Doe", 20),
                new Person("Zac", "Ainsley", 48),
                new Person("Charles", "Johnson", 48),
                new Person("Boris", "Johnson", 48),
                new Person("Jeremy ", "Johnson", 48),
                new Person("Jo", "Swinston", 48),
                new Person("Nicola", "Sturgeon", 104),
        };

        List<Person> listSource = new LinkedList<>(Arrays.asList(personArray));

        try {

            // Use lambda expressions to create standard comparators
            MyComparator<Person> compareByAge = (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge());
            listSource.sort(compareByAge);
            if (listSource.get(0).getAge() != 20 || listSource.get(6).getAge() != 104)
                throw new Exception("compare by age failed");

            MyComparator<Person> compareByFirstName = (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName());
            listSource.sort(compareByFirstName);
            if (!listSource.get(0).getFirstName().equals("Boris") || !listSource.get(6).getFirstName().equals("Zac"))
                throw new Exception("compare by firstName failed:" + listSource.toString());

            MyComparator<Person> compareByLastName = (p1, p2) -> p1.getLastName().compareTo(p2.getLastName());
            listSource.sort(compareByLastName);
            if (!listSource.get(0).getLastName().equals("Ainsley") || !listSource.get(6).getLastName().equals("Swinston"))
                throw new Exception("compare by lastName failed" + listSource.toString());


            //Create Functional objects to reference the lambda expressions
            Function<Person, Integer> fAge = p -> p.getAge();
            Function<Person, String> fLastName = p -> {
                return p.getLastName();
            };
            Function<Person, String> fFirstName = p -> {
                return p.getFirstName();
            };

            MyComparator<Person> comparatorByAge = MyComparator.comparing(fAge);
            listSource.sort(comparatorByAge);
            if (listSource.get(0).getAge() != 20 || listSource.get(6).getAge() != 104)
                throw new Exception("Functional object: compare by age failed" + listSource.toString());

            MyComparator<Person> comparatorByFirstName = MyComparator.comparing(fFirstName);
            listSource.sort(comparatorByFirstName);
            if (!listSource.get(0).getFirstName().equals("Boris") || !listSource.get(6).getFirstName().equals("Zac"))
                throw new Exception("Functional object: compare by firstName failed:" + listSource.toString());

            MyComparator<Person> comparatorByLastName = MyComparator.comparing(fLastName);
            listSource.sort(comparatorByLastName);
            if (!listSource.get(0).getLastName().equals("Ainsley") || !listSource.get(6).getLastName().equals("Swinston"))
                throw new Exception("Functional object: compare by lastName failed" + listSource.toString());


            // Create Comparators using MethodReferences - simpler to read
            // more readable than passing p -> p.getAge() or or Functional objects
            MyComparator<Person> comparatorMrefByAge = MyComparator.comparing(Person::getAge);
            listSource.sort(comparatorMrefByAge);
            if (listSource.get(0).getAge() != 20 || listSource.get(6).getAge() != 104)
                throw new Exception("Method References: compare by age failed" + listSource.toString());

            MyComparator<Person> comparatorMRefByFirstName = MyComparator.comparing(Person::getFirstName);
            listSource.sort(comparatorMRefByFirstName);
            if (!listSource.get(0).getFirstName().equals("Boris") || !listSource.get(6).getFirstName().equals("Zac"))
                throw new Exception("Method References: compare by firstName failed:" + listSource.toString());

            MyComparator<Person> comparatorMRefByLastName = MyComparator.comparing(Person::getLastName);
            listSource.sort(comparatorMRefByLastName);
            if (!listSource.get(0).getLastName().equals("Ainsley") || !listSource.get(6).getLastName().equals("Swinston"))
                throw new Exception("Method References: compare by lastName failed" + listSource.toString());


            // Create comparator using method references and order them, using chaining
            Comparator<Person> compareByAgeLastnameFirstname = MyComparator.comparing(Person::getAge)
                    .thenComparing(Person::getLastName)
                    .thenComparing(Person::getFirstName);
            listSource.sort(compareByAgeLastnameFirstname);
            if (listSource.get(0).getAge() != 20 || !listSource.get(1).getLastName().equals("Ainsley")
                    || !listSource.get(2).getFirstName().equals("Boris"))
                throw new Exception("Chained method ref compares failed" + listSource.toString());


            // For completion *only*, create comparator using functional objects and order them, using chaining
            Comparator<Person> comparatorByAgeLastnameFirstname = MyComparator.comparing(fAge)
                    .thenComparing(fLastName)
                    .thenComparing(fFirstName);
            listSource.sort(comparatorByAgeLastnameFirstname);
            if (listSource.get(0).getAge() != 20 || !listSource.get(1).getLastName().equals("Ainsley")
                    || !listSource.get(2).getFirstName().equals("Boris"))
                throw new Exception("Chained functional object compares failed" + listSource.toString());

            System.out.println("PASSED:exploreComparators");
        } catch (Exception exception) {
            System.out.println("FAILED:exploreComparators" + exception);
        }
    }

    private static void dataProcessingWithLambdas() {
        try {
            Predicate<String> lenLessThan20 = s -> s.length() < 20;
            Predicate<String> lenGrtrThan5 = s -> s.length() > 5;

            Predicate<String> lenBetween6And20 = lenLessThan20.and(lenGrtrThan5);
            boolean b1 = lenBetween6And20.test("ffive");
            boolean b2 = lenBetween6And20.test("sixsix");
            boolean b3 = lenBetween6And20.test("nnnnnnnnnnnnineteen");
            boolean b4 = lenBetween6And20.test("twentytwentytwentyxx");
            if (b1 || !b2 || !b3 || b4) throw new Exception("chained predicates failed:and");

            Predicate<String> lenAny = lenLessThan20.or(lenGrtrThan5);
            b1 = lenAny.test("ffive");
            b2 = lenAny.test("sixsix");
            b3 = lenAny.test("nnnnnnnnnnnnineteen");
            b4 = lenAny.test("twentytwentytwentyxx");
            if (!b1 || !b2 || !b3 || !b4) throw new Exception("chained predicates failed:or");

            Predicate<String> equalsToYes = Predicate.isEqualTo("Yes");
            if (!equalsToYes.test("Yes") || equalsToYes.test("No"))
                throw new Exception("chained predicates failed:static");

            System.out.println("PASSED:dataProcessingWithLambdas");

        } catch (Exception exception) {
            System.out.println("FAILED:dataProcessingWithLambdas:" + exception);
        }
    }

    private static void mapsWithLambdas() {

        try {
            Person[] malePeople = {
                    new Person("John", "Doe", 20),
                    new Person("Zac", "Ainsley", 48),
                    new Person("Charles", "Johnson", 48),
                    new Person("Boris", "Johnson", 48),
            };

            Person[] femalePeople = {
                    new Person("Jemina ", "Johnson", 48),
                    new Person("Jo", "Swinston", 48),
                    new Person("Nicola", "Sturgeon", 104),
            };

            Map<String, List<Person>> peopleByGender = new HashMap<>();

            peopleByGender.put("male", Arrays.asList(malePeople));
            peopleByGender.put("female", Arrays.asList(femalePeople));

            Map<Integer, List<Person>> peopleByAge = new HashMap<>();

            //Generate new map (by age) using other map by gender
            peopleByGender.forEach(
                    (gender, peopleList) -> {
                        peopleList.forEach(
                                person -> {
                                    peopleByAge.computeIfAbsent(person.getAge(),
                                            p -> {
                                                return new ArrayList<>();
                                            }).add(person);
                                }
                        );
                    }
            );

            if (peopleByAge.get(48).size() != 5 || peopleByAge.get(20).size() != 1
                    || peopleByAge.get(104).size() != 1)
                throw new Exception("age groups different from what expected 48="
                        + peopleByAge.get(48).size());

            System.out.println("PASSED:map with lambdas");

        } catch (Exception exception) {
            System.out.println("FAILED:map with lambdas failed:" + exception);
        }
    }

    private static void streamProcessing() {
        try {

            //Generate stream containing first 10 terms of fibonacci series
            int[] valuePair = {0, 1};  // Seed of first two digits to add

            Stream<int[]> intStream =
                    Stream.iterate(valuePair, (vp) ->
                            {   // Shift digits left and calculate next one.
                                int v = vp[0];
                                vp[0] = vp[1];
                                vp[1] = vp[0] + v;
                                return vp;
                            }
                    );

            OptionalInt maxValue = intStream.mapToInt(vp -> vp[0]).limit(10)
                    .max();

            if (maxValue.isEmpty() || !(maxValue.getAsInt() == 34))
                throw new Exception("streamProcessing fibonacci failed");

            //  Now create a stream of person and do operations on that stream

            Stream<Person> personStream = Stream.of(
                    new Person("Charles", "Johnson", 48),
                    new Person("Nicola", "Sturgeon", 104),
                    new Person("Jeremy ", "Johnson", 48),
                    new Person("John", "Doe", 20),
                    new Person("Zac", "Ainsley", 48),
                    new Person("Jo", "Swinston", 48),
                    new Person("Boris", "Johnson", 48)
            );


            // A contrived stream operation..
            Optional<String> longestLastname =
                    personStream.sorted(Comparator.comparing(Person::getAge)    //Re-order stream
                            .thenComparing(Person::getLastName)
                            .thenComparing(Person::getFirstName))
                            .peek(p -> p.setLastName(p.getLastName().toUpperCase())) //conv lastname to uppercase
                            .takeWhile(p -> p.getAge() < 100) // remove entries over 100
                            .map(Person::getLastName)  //convert into stream of lastnames strings
                            .filter(l -> !l.equals("JOHNSON")) // filter out johnsons
                            .max(Comparator.comparing(String::length));  // the longest remaining surname

            if (longestLastname.isEmpty() || !longestLastname.get().equals("SWINSTON"))
                throw new Exception("streamProcessing: ln=" + longestLastname);

            System.out.println("PASSED:streamProcessing interface");

        } catch (Exception exception) {
            System.out.println("FAILED:streamProcessing interface:" + exception);
        }
    }
}

