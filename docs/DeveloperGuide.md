---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `LoanListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Loan` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `LoanBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a loan).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `LoanBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `LoanBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the loan book data i.e., all `Loan` objects (which are contained in a `UniqueLoanList` object).
* stores the currently 'selected' `Loan` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Loan>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `LoanBook`, which `Loan` references. This allows `LoanBook` to only require one `Tag` object per unique tag, instead of each `Loan` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both loan book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `LoanBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedLoanBook`. It extends `LoanBook` with an undo/redo history, stored internally as an `loanBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedLoanBook#commit()` — Saves the current loan book state in its history.
* `VersionedLoanBook#undo()` — Restores the previous loan book state from its history.
* `VersionedLoanBook#redo()` — Restores a previously undone loan book state from its history.

These operations are exposed in the `Model` interface as `Model#commitLoanBook()`, `Model#undoLoanBook()` and `Model#redoLoanBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedLoanBook` will be initialized with the initial loan book state, and the `currentStatePointer` pointing to that single loan book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th loan in the loan book. The `delete` command calls `Model#commitLoanBook()`, causing the modified state of the loan book after the `delete 5` command executes to be saved in the `loanBookStateList`, and the `currentStatePointer` is shifted to the newly inserted loan book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new loan. The `add` command also calls `Model#commitLoanBook()`, causing another modified loan book state to be saved into the `loanBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitLoanBook()`, so the loan book state will not be saved into the `loanBookStateList`.

</box>

Step 4. The user now decides that adding the loan was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoLoanBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous loan book state, and restores the loan book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial LoanBook state, then there are no previous LoanBook states to restore. The `undo` command uses `Model#canUndoLoanBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoLoanBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the loan book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `loanBookStateList.size() - 1`, pointing to the latest loan book state, then there are no undone LoanBook states to restore. The `redo` command uses `Model#canRedoLoanBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the loan book, such as `list`, will usually not call `Model#commitLoanBook()`, `Model#undoLoanBook()` or `Model#redoLoanBook()`. Thus, the `loanBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitLoanBook()`. Since the `currentStatePointer` is not pointing at the end of the `loanBookStateList`, all loan book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire loan book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the loan being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of loans
* prefer desktop apps over other types
* can type quickly
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage loans faster than a typical mouse/GUI driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                       | I want to …​                                                          | So that I can…​                                                        |
|----------|-------------------------------|-----------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | loan issuer                   | keep track of the total amount of money owed to me                    | I can see how much I am due to collect                                 |
| `* * *`  | user                          | add a new loan                                                        | I can track new loans that are given out                               |
| `* * *`  | user                          | delete a loan                                                         | remove loans that are no longer relevant                               |
| `* * *`  | user                          | view current loans                                                    | I can see the full list of loans that need to be returned              |
| `* *`    | user                          | edit loans                                                            | I can update loan information if necessary                             |
| `* *`    | user                          | mark loans as returned                                                | I can keep track of whether a loan was repayed                         |
| `* *`    | forgetful user                | track the number of days since the loan was given                     | I can remind friends to return their loans                             |
| `* *`    | forgetful user                | add address of people who owe me money                                | I can look for them if necessary                                       |
| `* *`    | forgetful user                | add phone numbers of people who owe me money                          | I can contact them if necessary                                        |
| `* *`    | frequent loaner               | view a history of all loans that have been fulfilled                  | I can keep track of past lending habits                                |
| `* *`    | cash-strapped user            | track the number of days since the loan was given                     | I can remind friends to return their loans                             |
| `* *`    | new user                      | go through a new user guide                                           | I learn how to use the program                                         |
| `* *`    | new user                      | view example entries                                                  | I can learn how to use from the examples                               |
| `* *`    | user                          | set deadlines to receive loans                                        | I can follow up with lenders on time                                   |
| `* *`    | user                          | blacklist people who are always late to return money                  | I can avoid loaning to particular individuals                          |
| `* *`    | user                          | tag individuals based on the amount of money lent                     | I can prioritise users that have larger ticket size loans              |
| `* *`    | user                          | tag individuals based on loan duration                                | I can group friends by loan durations                                  |
| `* *`    | user                          | tag individuals based on their spending habits                        | I can avoid lending to high risk individuals                           |
| `* *`    | user                          | view a leaderboard of those with highest or longest unreturned loans  | I can have a visual representation of who needs to be chased for loans |
| `* *`    | user                          | see past loans categorised by month and loan type                     | I can project loaning for future months                                |
| `* *`    | user                          | delete all records                                                    | I can purge examples                                                   |
| `* *`    | user                          | sort records, by loan amount, period, priority etc.                   | I can see the most 'important' records for me                          |
| `*`      | forgetful user                | upload photos of people who owe me money                              | I can match their appearance to their loans                            |
| `*`      | user                          | have notifications for those who have loaned for longer than duration | I can prompt them to return that it has been past a grace period       |
| `*`      | user                          | send messages to send to people who owe me money                      | I can efficiently prod them to return the money                        |
| `*`      | user                          | customize the autogenerated message sent to those who owe money       | I can better persuade them to return my money                          |
| `*`      | cash strapped user            | calculate projected returns if everyone was to return loans           | I can see how much I can earn back from chasing people for loans       |

### Use cases

(For all use cases below, the **System** is the `LoanBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: `Add` a New Loan**

**MSS**

1. User requests to add a new loan with borrower's name.

2. System records the new loan in the loan list.

3. System confirms that the loan has been successfully added.
Use case ends.

**Extensions**

* 1a. The loans details are incomplete or invalid.
  * 1a.1. System displays an error message and tells the user the correct format to enter.
  * Use Case Resumes at Step 1.

**Use case: `Delete` a Loan**

**MSS**

1. User requests to delete a specific loan.
2. System removes the loan from the loan list.
3. System confirms that the loan has been successfully deleted.

    Use case ends.

**Extensions**

* 1a. The loan does not exist in the system.
  * 1a.1. System displays a message indicating that the loan is not found.
  * Use Case Ends.

**Use case: `List` Current Loans**

**MSS**

1. User requests to view the list of current loans.
2. System retrieves and displays the list of loans.

   Use case ends.

**Extensions**

* 1a. No loans exists.
  * 1a.1. System displays a message indicating that there are no loans that need to be returned.
  * Use Case Ends

**Use Case: `Increase` a Loan Amount**

**MSS**
1. User requests to add a loan amount to a loan ID, and records the date of the transaction
2. System creates a new add loan transaction
3. System adds transaction to the loan history
4. System updates and displays total amount loaned and total amount to be repaid

   Use Case Ends.

**Extensions**

* 1a. No loan with the given ID exists.
  * 1a.1. System displays a message indicating that an invalid ID is being called.
  * Use Case Ends.

**Use Case:`Repay` a Loan Amount in Full or Partially**

**MSS**
1. User requests to repay a loan amount to a loan ID, and records the date of the transaction
2. System creates a new repay loan transaction
3. System adds transaction to the loan history, and displays total amount loaned and total amount to be repaid
4. System display repaid success message.

   Use Case Ends.

**Extensions**

* 1a. No loan with the given ID exists.
  * 1a.1. System displays a message indicating that an invalid ID is being called.
  * Use Case Ends.

**Use Case: `Rename` a Loan Transaction**

*MSS**
1. User requests a loans ID to rename a loan borrower
2. System creates an updated loan
3. System display rename success message.

   Use Case Ends.

**Extensions**

* 1a. No loan with the given ID exists.
  * 1a.1. System displays a message indicating that an invalid ID is being called.
  * Use case ends.
* 1b. Invalid name field is received as an input
  * 1b.1. System displays a message indicating that an invalid ID is being called.
  * Use case ends.

**Use Case: `Tag` a Loan Transaction**

*MSS**
1. User requests a loan ID to tag a loan
2. System creates an updated loan
3. System display tag success message.

   Use Case Ends.

**Extensions**

* 1a. No loan with the given ID exists.
  * 1a.1. System displays a message indicating that an invalid ID is being called.
  * Use case ends.

**Use Case: Add and Edit a `Phone` Value**

*MSS**
1. User requests a loan ID to add or edit a borrowers' phone number
2. System creates an updated loan
3. System display phone success message.

   Use Case Ends.

**Extensions**

* 1a. No loan with the given ID exists.
    * 1a.1. System displays a message indicating that an invalid ID is being called.
    * Use case ends.

**Use Case: `Sort` Loan by Amount Loaned**

**MSS**
1. User requests to sort loans
2. System sort loans by amount owed and displays it to the user.

   Use Case Ends.

**Extensions**

* 1a. No outstanding loans.
  * 1a.1. No loans are displayed.
  * Use Case Ends.

  Use Case Ends.

**Use Case: `Sort` Loans by Borrowers' Name TBD**

**MSS**
1. User requests to sort loans by borrowers' name
2. System sort loans and displays it to the user.

   Use Case Ends.

**Extensions**

* 1a. No outstanding loans.
  * 1a.1. No loans are displayed.

  * Use Case Ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 loans without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Loan Issuer**: The loan who lends money or items to others and expects repayment or return.
* **User**: A general term for loan issuers managing their loans.
* **Forgetful User**: A user who needs additional reminders and tracking features to recall outstanding loans.
* **Frequent Loaner**: A user who frequently lends money or items and needs an organized record of past and present loans.
* **Cash-Strapped User**: A user who urgently needs to recover loaned money to maintain financial stability.
* **New User**: Someone who has just started using the system and may require guidance on how to navigate it.
* **Amount**: A sum of money.
* **Loan Transaction**: An action to increase or decrease the loan owed to the user.
* **Tags**: A method of categorizing individuals based on different attributes such as spending habits, friends, family etc.
* **Leaderboard**: A visual ranking system displaying individuals with the highest loans.
* **Loan History**: A record of all loan transactions under a current loan, categorized by increase or repay transactions.
* **Clear**: The ability to delete all stored loan data, often used to clear test or example entries.
* **Sorting**: Organizing loan records based on factors such as amount, duration, priority, or borrower.
* **Example Entries**: Pre-filled sample data to help new users understand how the system works.
* **Not Wanted**: A status tag that indicates a loan  has been completely repaid.
* **Wanted**: A status tag that indicates a loan has not been repaid.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a loan

1. Deleting a loan while all loans are being shown

   1. Prerequisites: List all loans using the `list` command. Multiple loans in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No loan is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team Size: 5 pax

1. **Display most recent loan transaction at the top of transaction history**: The current transaction history displays
    loan transactions from oldest to newest. We intend to change the display from newest transaction recorded to oldest recorded transaction.
2. **Enforce stricter date parsing**: Current string parsing requires three string inputs separated by spaces.
   We will enhance this by parsing dates as DateTime objects to enforce a consistent format and improve reliability. 
3. **Display total amount loaned to a borrower**: The LoanCard currently displays only the total amount owed by a borrower. 
   We will enhance it to also display the total amount borrowed overall for better financial tracking.

