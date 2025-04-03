package wanted.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.loan.Phone;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.model.tag.Tag;

/**
 * Contains utility methods for populating {@code LoanBook} with sample data.
 */
public class SampleDataUtil {
    public static Loan[] getSamplePersons() {
        try {
            return new Loan[]{
                new Loan(new Name("Alex Yeoh"),
                        new LoanAmount(new ArrayList<>(Arrays.asList(
                                new AddLoanTransaction(MoneyInt.fromCent(100000), new LoanDate("1st January 2025")),
                                new RepayLoanTransaction(MoneyInt.fromCent(50), new LoanDate("2nd January 2025")),
                                new AddLoanTransaction(MoneyInt.fromCent(20000), new LoanDate("3rd January 2025"))
                        ))),
                        getTagSet("friends", "owesALot"),
                        new Phone("12345678")),
                new Loan(new Name("Bernice Yu"),
                        new LoanAmount(new ArrayList<>(Arrays.asList(
                                new AddLoanTransaction(MoneyInt.fromCent(2024), new LoanDate("1st January 2024")),
                                new AddLoanTransaction(MoneyInt.fromCent(2025), new LoanDate("1st January 2025"))
                        ))),
                        getTagSet("colleagues", "friends"),
                        new Phone("20242025")),
                new Loan(new Name("Charlotte Oliveiro"),
                        new LoanAmount(new ArrayList<>(Arrays.asList(
                                new AddLoanTransaction(MoneyInt.fromCent(15000), new LoanDate("9th August 1965")),
                                new RepayLoanTransaction(MoneyInt.fromCent(15000), new LoanDate("9th August 2065"))
                        ))),
                        getTagSet("neighbours"),
                        new Phone("06021819")),
                new Loan(new Name("David Li"),
                        new LoanAmount(new ArrayList<>(Arrays.asList(
                                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st January 2025")),
                                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd March 2025"))
                        ))),
                        getTagSet("family"),
                        new Phone("04385793")),
                new Loan(new Name("Irfan Ibrahim"),
                        new LoanAmount(),
                        getTagSet("classmates")),
                new Loan(new Name("Roy Balakrishnan"),
                        new LoanAmount(new ArrayList<>(List.of(
                                new AddLoanTransaction(MoneyInt.fromCent(10000), new LoanDate("1st January 2025")),
                                new RepayLoanTransaction(MoneyInt.fromCent(5000), new LoanDate("2nd January 2025")),
                                new AddLoanTransaction(MoneyInt.fromCent(12000), new LoanDate("3rd January 2025")),
                                new RepayLoanTransaction(MoneyInt.fromCent(17000), new LoanDate("4th January 2025"))
                        ))),
                        getTagSet(),
                        new Phone("47821053"))
            };
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReadOnlyLoanBook getSampleLoanBook() {
        LoanBook sampleAb = new LoanBook();
        for (Loan samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
