package wanted.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.AddCommand;
import wanted.logic.commands.ClearCommand;
import wanted.logic.commands.CommandTestUtil;
import wanted.logic.commands.DeleteCommand;
import wanted.logic.commands.EditCommand;
import wanted.logic.commands.EditCommand.EditPersonDescriptor;
import wanted.logic.commands.ExitCommand;
import wanted.logic.commands.FindCommand;
import wanted.logic.commands.HelpCommand;
import wanted.logic.commands.IncreaseCommand;
import wanted.logic.commands.ListCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Loan;
import wanted.model.loan.NameContainsKeywordsPredicate;
import wanted.testutil.EditPersonDescriptorBuilder;
import wanted.testutil.PersonBuilder;
import wanted.testutil.PersonUtil;

public class LoanBookParserTest {

    private final LoanBookParser parser = new LoanBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        // TODO: Date field needs to be cleaned out from all the tests involving Loan itself
        Loan person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        assumeTrue(EditCommand.IS_ENABLED);
        Loan person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        assumeTrue(FindCommand.IS_ENABLED);
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_increase() throws Exception {
        assertTrue(parser.parseCommand(IncreaseCommand.COMMAND_WORD
                + " 1 l/10.10 d/9th September 2024") instanceof IncreaseCommand);
        assertTrue(parser.parseCommand(IncreaseCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
            + " " + "l/" + CommandTestUtil.VALID_AMOUNT_AMY
            + " " + " d/" + CommandTestUtil.VALID_DATE_AMY) instanceof IncreaseCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
