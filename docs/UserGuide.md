---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# Wanted User Guide

Wanted is a loan tracking application meant for personal use, for those of you who lend out money to others but find it difficult to keep track of what they owe you.

This guide assumes cursory knowledge of operating a Command Line Interface (CLI). In short, almost all actions in the program are performed by typing in a command in the specified formats below and pressing the Enter key.

The link to this guide can be found on the Github page or the Help window of the program.

## Value Proposition
With Wanted, you can
1. Track monetary loans by name
2. Repay loans in small amounts
3. View transaction history of borrowing and repayment
4. Do this all in a gritty, fun interface that resembles a Wanted poster!

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## TL;DR

___How do I track a loan?___<br>
Use the add command to add the loanee’s name to the list, then use the loan command to assign the amount loaned to that person’s entry.

___How do I track repayments?___<br>
Use the repay command when the loanee’s entry is visible on the list.

___I can’t find the entry I want to modify.___<br>
This is likely due to the list currently being filtered to only show a certain name. If you remember the loanee’s name, use the search command to find the loan. Otherwise, use the list or sort command to look for the entry.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Command     | Action                           | Format, Examples
-----------|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------
**add**    | Add new entry                    |`add n/[NAME]​`
**rename** | Change name of entry             | `rename [ID] n/[NAME]`
**phone** | Add/change phone number of entry | `phone [ID] p/[PHONE]`
**tag** | Add/change tags of entry         | `tag [ID] (t/[TAG]…)`
**increase** | Add a loan to entry              | `increase [ID] l/[AMOUNT]`
**repay** | Add a repayment to entry         | `repay [ID] l/[AMOUNT]`
**edit**   | Edit a transaction in entry      |`edit [ID] h/[TRANSACTION ID] l/[AMOUNT]`
**delhist** | Delete a transaction in entry    | `delhist [ID] h/[TRANSACTION ID]`
**list**   | List all entries                 |`list`
**find**   | Search entries by name           |`find [NAME]…`
**delete** | Delete an entry                  |`delete [ID]`
**clear**  | Delete all entries               |`clear`
**help**   | Show help window                 |`help`

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in square brackets and `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/[NAME]`, `[NAME]` is a parameter which can be used as `add n/John Doe`.

* Items with `…`​ after them can be used multiple times.<br>
  e.g. `(t/[TAG]…)​` can be used as ` ` `t/`, `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `l/[AMOUNT] d/[DATE]`, `d/[DATE] l/AMOUNT]` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding an entry: `add`

Adds a new person as a new entry to the Wanted list.<br>
A new entry starts with no money loaned and no money to be returned, no transaction history and no tags.

Format: `add n/[NAME]​`

Restrictions:
* The provided name must be unique to the list, case-sensitive.

Examples:
* `add n/John Doe`
* `add n/Betsy Crowe`

### Renaming an entry: `rename`

Changes the name of the specified entry in the Wanted list.

Format: `rename [ID] n/[NAME]`

### Adding/Updating phone number: `phone`

Adds, deletes and edits a borrowers' phone number in the Wanted list.

Format: `phone [ID] p/[PHONE]` (add and edit phone number) 

Format: `phone [ID] p/delete` (delete the phone number)

### Adding/Updating tags: `tag`

Overwrites the current tags on the specified entry with the tags specified in the command.

<box type="tip" seamless>
**Tip:** A person can have any number of tags (including 0)
**Tip** To clear all tags input an empty tag [INDEX] "t/"
</box>

<box type="warning" seamless>
**Warning:** Writing tag [INDEX] t/ will clear all tags
</box>

Format: `tag (t/[TAG]…)`

Examples:
* `tag 1 t/schoolmate t/nus`
* `tag 1 t/` 

### Adding a loan: `increase`

Adds a transaction indicating that the specified amount was loaned at the specified date to an entry.

Format: `increase [ID] l/[AMOUNT] d/[DATE]`

Restrictions:
* Modifies the loan at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Loaned amount must be a numeric amount with 2 decimal places.
* Date must be in the format YYYY-MM-DD.

### Repay a loan: `repay`

Adds a transaction indicating that the specified amount was returned at the specified date to an entry.

Format: `repay [ID] l/[AMOUNT] d/[DATE]`

Restrictions:
* Modifies the loan at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* Loaned amount must be a numeric amount with 2 decimal places.
* Date must be in the format YYYY-MM-DD.

### Editing a transaction: `edit`

Edits an existing transaction history in the given entry.

Format: `edit [ID] h/[HISTORY ID] l/[AMOUNT]​`

### Deleting a transaction: `delhist`

Edits an existing transaction history in the given entry.

Format: `delhist [ID] i/[TRANSACTION ID]`


### Listing all entries: `list`

Shows a list of all entries in the address book.

Format: `list`

### Locating entries by name: `find`

Finds entries whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned at the top of the Wanted list(i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns entries with borrower names `john` and `John Doe`
* `find alex david` returns entries with borrower names `Alex Yeoh`, `David Li`<br>
  [TODO: update image below]
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Sorting entries: `sort`

Sorts the Wanted list by loaned amount.

Format: `sort`

### Deleting an entry: `delete`

Deletes the specified entry from the loan book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd loan in the loan book.
* `find Betsy` followed by `delete 1` deletes the 1st loan in the results of the `find` command.

### Clearing all entries: `clear`

Clears all entries from the loan book.

<box type="warning" seamless>
**Warning:** No undo for clear command. All loan entries will be wiped.
</box>

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

Wanted saves the loanbook after each operation that modifies it. There is no need to save manually.

### Backing up data files

Wanted data is saved automatically as a JSON file `[JAR file location]/data/loanbook.json`. Copy this file to another folder to create a backup.

### Transferring data across devices

If you wish to transfer your saved data to another device, install Wanted on the new device and replace the JSON data file in `[JAR file location]/data/loanbook.json` with the data file from the old device.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.


--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all loans.

    * `add n/John Doe` : Adds a loan with a borrower named `John Doe` to the Loan Book.
   
    * `increase 2 l/19.87 d/10th December 2024` : Increases the amount borrowed in the 2nd loan in the current list and records the transaction in its loan history.

    * `delete 3` : Deletes the 3rd loan shown in the current list.

    * `clear` : Deletes all loans.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.
