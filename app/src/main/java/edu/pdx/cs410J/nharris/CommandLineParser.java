package edu.pdx.cs410J.nharris;


import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

class CommandLineParser {
    private final ArrayList<String> options;
    private final ArrayList<String> phoneArgs;
    private String fileName;
    private String prettyDash;

    /**
     * Populates CommandLineParser's options and phoneArgs array lists.
     */
    CommandLineParser () {
        this.phoneArgs = new ArrayList<>();
        this.options = new ArrayList<>();
        this.fileName = "";
        this.prettyDash = "";
    }


    /**
     * Assigns CommandLineParser's array lists with the appropriate options and phone call arguments
     * @throws CommandLineParserException if any of the functions throw a CommandLineParserException
     * go look at validNumber, argIndexExists, and dateTimeValidator for further information on when these are
     * thrown
     */
    void validateCommandLineParser(String [] args) throws CommandLineParserException {
        findOptions(args);
        if(this.options.contains("-README"))
            return;
        phoneCallArgs(args);
    }
    /**
     * Looks for command line options to execute in the program.
     * @param   args    Arguments from the command line
     */
    void findOptions(String[] args) throws CommandLineParserException {
        String allowedArguments = "-README -print -textFile -pretty -";
        ArrayList<String> quickReadme = new ArrayList<>(Arrays.asList(args));
        if(quickReadme.contains("-README")) {
            this.options.add("-README");
            return;
        }

        for (int i = 0; i < args.length; ++i) {
            if (i > 0 && args[i].startsWith("-") && allowedArguments.contains(args[i])
                    && !args[i-1].equals(this.fileName) && !allowedArguments.contains(args[i-1])) {
                throw new CommandLineParserException("Option found in an invalid position.\n"  +
                        "Argument -> " + args[i] + " <- found in phone call arguments and not in options.\n" +
                        "Run the program again with the included option -README to view allowable options.");
            } else if (args[i].startsWith("-") && !allowedArguments.contains(args[i])) {
                throw new CommandLineParserException("Invalid option found.\n" +
                        "Option -> " + args[i] + " <- is not in the allowed options list.\n" +
                        "Run the program again with the included option -README to view allowable options.");
            } else if (args[i].equals("-print") && i < 4) {
                this.options.add("-print");
            } else if (args[i].equals("-textFile") && i < 4) {
                this.options.add("-textFile");
                try {
                    if (args[i + 1].startsWith("-")) {
                        throw new CommandLineParserException("Next argument was " + args[i + 1] + ". " +
                                "Expected input was a file name.");
                    }
                } catch (NullPointerException | IndexOutOfBoundsException ex) {
                    throw new CommandLineParserException("Missing additional option and required argument " +
                            "information.\n Run the program again with the included option -README to view " +
                            "allowable options.");
                }
                this.fileName = args[i + 1];
                if(!this.options.contains(args[i+1]))
                    this.options.add(args[i+1]);
                ++i;
            } else if (args[i].equals("-pretty") && i < 4) {
                this.options.add(args[i]);
                try {
                    if(!this.options.contains(args[i+1]))
                        this.options.add(args[i+1]);
                    this.prettyDash = args[i+1];
                    ++i;
                } catch (NullPointerException ex) {
                    throw new CommandLineParserException("Missing additional option and required argument " +
                            "information.\n Run the program again with the included option -README to view allowable " +
                            "options.");
                }

            }
        }
        if(this.options.size() == args.length) {
            throw new CommandLineParserException("Missing all additional required arguments after options.\n" +
                    "Run the program again with the included option -README to view allowable options.");
        }
        if(this.options.contains("-pretty") && this.options.contains("-textFile") && this.options.contains("-") &&
        this.fileName.isEmpty()) {
            throw new CommandLineParserException("Directed -pretty to use '-' arg for -textFile's file name, but\n" +
                    "there was no filename provided");
        }
        if(this.options.contains("-textFile") && this.fileName.isEmpty())
        {
            throw new CommandLineParserException("Missing file name for -textFile option." +
                    "\nRun the program with the option -README to learn how to use this program.\n");
        }
        if(this.options.contains("-pretty") && allowedArguments.contains(this.fileName))
        {
            if(this.prettyDash.isEmpty()) {
                throw new CommandLineParserException("Missing file name for -pretty option to use." +
                        "\nRun the program with the option -README to learn how to use this program.\n");
            } else if (!this.fileName.isEmpty() && this.options.contains("-textFile")) {
                throw new CommandLineParserException("Directed -pretty to use '-' arg for -textFile's file name, but\n" +
                        "there was no filename provided");
            }
            else if (!this.fileName.isEmpty() && allowedArguments.contains(this.fileName)
                    || allowedArguments.substring(0,31).contains(this.prettyDash))
                throw new CommandLineParserException("Missing file name with current option set -> " + this.options +
                        ". Ensure you've typed a file-name when running the program." +
                        "\nRun the program with the option -README to learn how to use this program.\n");
        }
        if(!this.fileName.isEmpty() && !this.fileName.startsWith("./")) {
            this.fileName = "./" + this.fileName;
        }
        if(!this.prettyDash.equals("-") && !this.prettyDash.startsWith("./")) {
            this.prettyDash = "./" + this.prettyDash;
        }
    }



    /**
     * Separates the options from the args in the command line arguments by using a starting position
     * of the current CommandLineParser's options size.
     */
    void phoneCallArgs(String[] args) throws CommandLineParserException {
        String allowedArguments = "-README -print -textFile -pretty -";
        int startPosition = this.options.size();
        this.phoneArgs.addAll(Arrays.asList(args).subList(startPosition, args.length));
        if(this.phoneArgs.get(0).matches("^\\d{3}-\\d{3}-\\d{4}$"))
            throw new CommandLineParserException("Missing filename following -textFile." +
                    "\nRun the program with the option -README to learn how to use this program.\n");
        argIndexExists();
        validNumber();
        dateTimeValidator();
    }


    /**
     * Validates the phone numbers of both caller and callee
     * @throws CommandLineParserException if either fail to match a typical US phone number regex
     */
    void validNumber() throws CommandLineParserException {
        String callerNum = this.phoneArgs.get(1);
        String calleeNum = this.phoneArgs.get(2);

        if (callerNum.length() > 12) {
            throw new CommandLineParserException("Caller phone number is too long. Max number of digits in phone number is 10!");
        }
        if (!callerNum.matches("^\\d{3}-\\d{3}-\\d{4}$")) {
            throw new CommandLineParserException("Caller phone number is invalid. Phone number must contain " +
                    "only numbers and dashes - EX: 123-456-7890");
        }
        if (calleeNum.length() > 12) {
            throw new CommandLineParserException("Callee phone number is too long. Max number of digits in phone number is 10!");
        }
        if (!calleeNum.matches("^\\d{3}-\\d{3}-\\d{4}|\\d{3}$")) {
            throw new CommandLineParserException("Callee phone number is invalid. Phone number must contain " +
                    "only numbers and dashes - EX: 123-456-7890 -OR- 911.");
        }
    }


    /**
     * Ensures that all required arguments were passed in and that there were no extra garbage argsWithoutOptions.
     * @throws CommandLineParserException Either 1 to all command line arguments are missing
     */

    void argIndexExists() throws CommandLineParserException {
        if (this.phoneArgs.size() < 2)
            throw new CommandLineParserException("\nMissing the CALLERS phone number.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");
        else if (this.phoneArgs.size() < 3)
            throw new CommandLineParserException("\nMissing the CALLEE phone number.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 4)
            throw new CommandLineParserException("\nMissing the begin date and time.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 5)
            throw new CommandLineParserException("\nMissing the time the call began.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 6)
            throw new CommandLineParserException("\nMissing whether time was AM or PM.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 7)
            throw new CommandLineParserException("\nMissing the end date and time.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 8)
            throw new CommandLineParserException("\nMissing the time the call ended.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() < 9)
            throw new CommandLineParserException("\nMissing whether time was AM or PM.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");

        else if (this.phoneArgs.size() > 9)
            throw new CommandLineParserException("\nToo many arguments.\n" +
                    "Run the program with the option -README to learn how to use this program.\n");
    }


    /**
     * Validates all date and time values provided by the user
     *
     * @throws CommandLineParserException Thrown as long as invalid information has been obtained from the arguments
     */
    void dateTimeValidator() throws CommandLineParserException {

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
        //Index position descriptions: 0 = month, 1 = day, 2 = 4 digit year
        this.phoneArgs.set(5,this.phoneArgs.get(5).toUpperCase());
        this.phoneArgs.set(8,this.phoneArgs.get(8).toUpperCase());
        String beginDateTimeString = this.phoneArgs.get(3) + " " + this.phoneArgs.get(4) + " " + this.phoneArgs.get(5);
        String endDateTimeString = this.phoneArgs.get(6) + " " + this.phoneArgs.get(7) + " " + this.phoneArgs.get(8);

        try {
            dateTimeFormat.parse(beginDateTimeString);
        } catch (DateTimeParseException ex) {
            throw new CommandLineParserException("Start date/time is incorrect: " + beginDateTimeString +
                    " - run the program with the option -README to learn how to use this program.");
        }
        try {
            dateTimeFormat.parse(endDateTimeString);
        } catch (DateTimeParseException ex) {
            throw new CommandLineParserException("End date/time is incorrect: " + endDateTimeString +
                    " - run the program with the option -README to learn how to use this program.");
        }

        //convert this.phoneArgs to an array with a new string array size of [0]
        // (thanks intellij for that, not sure why) and take all those String[] args make a new call
        // to invoke dateTimeMaker... This is not an ideal process.... But is required to eliminate duplicate code.
        PhoneCall call = new PhoneCall(this.phoneArgs.toArray(new String[0]));
        Date beginDateObject = call.dateTimeMaker(beginDateTimeString);
        Date endDateObject = call.dateTimeMaker(endDateTimeString);
        if(endDateObject.before(beginDateObject))
            throw new CommandLineParserException("Your END DATE is BEFORE your START DATE for the phone call. " +
                    "-> End date: " + endDateTimeString + " - Start date: " + beginDateTimeString);
    }

    ArrayList<String> getPhoneArgs() {
        return this.phoneArgs;
    }

    ArrayList<String> getOptions() {
        return this.options;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPrettyDash() {
        return this.prettyDash;
    }


    /**
     * Exception used when an option argument is found in the positions outside
     * the first two indices of the command line arguments
     */
    static class CommandLineParserException extends Exception {
        CommandLineParserException(String message) {
            super(message);
        }
    }
}
