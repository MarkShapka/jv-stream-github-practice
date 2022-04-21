package practice;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.Candidate;
import model.Person;

public class StreamPractice {
    private static final int CANDIDATE_AGE = 35;
    /**
     * Given list of strings where each element contains 1+ numbers:
     * input = {"5,30,100", "0,22,7", ...}
     * return min integer value. One more thing - we're interested in even numbers.
     * If there is no needed data throw RuntimeException with message
     * "Can't get min value from list: < Here is our input 'numbers' >"
     */

    public int findMinEvenNumber(List<String> numbers) {
        int minNumber = numbers.stream()
                .map(s -> s.split(","))
                .flatMap(Arrays::stream)
                .map(Integer::valueOf)
                .sorted()
                .filter(i -> i % 2 == 0)
                .mapToInt(i -> i)
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can't get min value from list: method_input_list"));
        return minNumber;
    }

    /**
     * Given a List of Integer numbers,
     * return the average of all odd numbers from the list or throw NoSuchElementException.
     * But before that subtract 1 from each element on an odd position (having the odd index).
     */
    public Double getOddNumsAverage(List<Integer> numbers) {
        OptionalDouble averageDouble = IntStream.range(0, numbers.size())
                .map(i -> i % 2 == 0 ? numbers.get(i) : numbers.get(i) - 1)
                .boxed()
                .filter(i -> i % 2 != 0)
                .mapToDouble(i -> i)
                .average();
        return averageDouble.orElseThrow(NoSuchElementException::new);
    }

    /**
     *
     * Given a List of `Person` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new Person(«Victor», 16, Sex.MAN),
     * new Person(«Helen», 42, Sex.WOMAN))`,
     * select from the List only men whose age is from `fromAge` to `toAge` inclusively.
     * <p>
     * Example: select men who can be recruited to army (from 18 to 27 years old inclusively).
     */
    public List<Person> selectMenByAge(List<Person> peopleList, int fromAge, int toAge) {
        List<Person> people = peopleList.stream()
                .filter(p -> p.getSex().equals(Person.Sex.MAN))
                .filter(a -> a.getAge() > fromAge && a.getAge() <= toAge)
                .collect(Collectors.toList());
        return people;
    }

    /**
     * Given a List of `Person` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new Person(«Victor», 16, Sex.MAN),
     * new Person(«Helen», 42, Sex.WOMAN))`,
     * select from the List only people whose age is from `fromAge` and to `maleToAge` (for men)
     * or to `femaleToAge` (for women) inclusively.
     * <p>
     * Example: select people of working age
     * (from 18 y.o. and to 60 y.o. for men and to 55 y.o. for women inclusively).
     */
    public List<Person> getWorkablePeople(int fromAge, int femaleToAge,
                                          int maleToAge, List<Person> peopleList) {
        List<Person> people = peopleList.stream()
                .filter(a -> a.getAge() >= fromAge)
                .filter(a -> (a.getAge() <= maleToAge && a.getSex().equals(Person.Sex.MAN))
                        || (a.getAge() <= femaleToAge && a.getSex().equals(Person.Sex.WOMAN)))
                .collect(Collectors.toList());
        return people;
    }

    /**
     * Given a List of `Person` instances (having `name`, `age`, `sex` and `cats` fields,
     * and each `Cat` having a `name` and `age`),
     * return the names of all cats whose owners are women from `femaleAge` years old inclusively.
     */
    public List<String> getCatsNames(List<Person> peopleList, int femaleAge) {
        List<String> catNames = peopleList.stream()
                .filter(a -> a.getAge() >= femaleAge && a.getSex().equals(Person.Sex.WOMAN))
                .map(c -> c.getCats())
                .flatMap(l -> l.stream())
                .map(c -> c.getName())
                .collect(Collectors.toList());
        return catNames;
    }

    /**
     * Your help with a election is needed. Given list of candidates, where each element
     * has Candidate.class type.
     * Check which candidates are eligible to apply for president position and return their
     * names sorted alphabetically.
     * The requirements are: person should be older than 35 years, should be allowed to vote,
     * have nationality - 'Ukrainian'
     * and live in Ukraine for 10 years. For the last requirement use field periodsInUkr,
     * which has following view: "2002-2015"
     * We want to reuse our validation in future, so let's write our own impl of Predicate
     * parametrized with Candidate in CandidateValidator.
     */
    public List<String> validateCandidates(List<Candidate> candidates) {
        CandidateValidator candidateValidator = new CandidateValidator();
        List<String> ukrainian = candidates.stream()
                .filter(c -> c.getAge() >= CANDIDATE_AGE
                        && c.getNationality().equals("Ukrainian")
                        && c.isAllowedToVote())
                .filter(candidateValidator::test)
                .map(c -> c.getName())
                .sorted()
                .collect(Collectors.toList());
        return ukrainian;
    }
}
