package cli;

import java.io.*;
import java.util.*;

import bitonicSort.BitonicSort;
import logging.Logger;

/**
 * Command line application to allow sorting of integers.
 * Input may come from files or standard input.
 * Direction of sort may be reversed.
 */
public class Sort implements AutoCloseable {
    // OPTIONS
    boolean reversed = false;
    boolean quiet = false;
    boolean sourceFromFile = false;
    String sourceFile = "-";
    String inDelimiter = null;
    String outDelimiter = " ";

    List<Integer> list = new ArrayList<>();
    //Console console = System.console();
    //PrintWriter writer = System.console().writer();
    InputStream in;
    PrintStream out;
    PrintStream err;
    Logger logger;
    //Reader reader = System.console().reader();


    /**
     * Constructor, sets up necessary fields.
     */
    public Sort()
    {
        logger = Logger.getSingletonInstance(Logger.Level.ERROR);
        in = System.in;
        out = System.out;
        err =  System.err;
    }

    /**
     * Factory method for constructing Sort
     * @return
     * @throws FactoryException
     */
    public static Sort createDefaultSort() throws FactoryException {
        Sort sort = new Sort();
        if (sort.logger == null)
            throw new FactoryException("no logger available");
        if (sort.out == null || sort.err == null)
            throw new FactoryException("no console available");
        return sort;
    }

    /**
     * Prints help message to out
     */
    void helpMessage() {
        String head =
                    "Sort [OPTION]... [FILE|-]%n" +
                    "%n" +
                    "Sorts a list of integers from Standard Input or from a File.%n" +
                    "%n";
        out.printf(head);

        out.printf(" Options:%n");
        String option = "  %-20.20s %-60.60s%n";
        out.printf(option, "-r, --reverse", "Reverse sort direction.");
        out.printf(option, "-h, --help", "Display this help and exit.");
    }

    /**
     * Method to handle and report back an error when running
     * a command i.e syntax error, list error, file error
     * @param e Error message
     */
    void commandError(String e) {
        logger.log(Logger.Level.ERROR, e);
        if (!quiet) helpMessage();
    }

    /**
     * Given raw arguments from command, will check for validity
     * and extract information, setting appropriate variables in Sort to reflect that.
     * @param args  String of arguments to command supplied from command line.
     * @throws BadParamatersException   If args is null empty or contains invalid/unsupported arguments
     */
    void processArgs(String[] args) throws BadParamatersException {
        if (args == null || args.length  == 0) throw new BadParamatersException("invalid argument");

        for(int i = 0; i < args.length - 1; i++) {
            switch (args[i]) {
                case "-r":
                case "--reverse":
                    reversed = true;
                    break;
                case "-h":
                case "--help":
                    helpMessage();
                    break;
                default:
                    throw new BadParamatersException("invalid argument");
            }
        }

        String source = args[args.length-1];
        if ("-".equals(source)) sourceFromFile = false;
        else sourceFromFile = true;

        if (sourceFromFile) {
            sourceFile = source;
        }
    }

    /**
     * Reads integers from input stream and adds them to list.
     * @param inputStream   Input stream to extract integer values for list
     */
    void readListFromInputStream (InputStream inputStream) {

        Scanner sc = new Scanner(inputStream);
        if (inDelimiter != null)
            sc.useDelimiter(inDelimiter);

        while (sc.hasNextInt()) {
            list.add(sc.nextInt());
        }

        if (list.isEmpty())
            commandError("List empty.");
    }

    /**
     * Reads integers from file and adds them to list.
     * @param filename File name and path to read integers from
     * @throws IOException
     */
    void getListFromFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(new File(sourceFile))) {
            readListFromInputStream(fileInputStream);
        }
    }

    /**
     * Reads integers from standard input and adds them to list.
     */
    void getListFromStandardInput() {
        readListFromInputStream(in);
    }

    /**
     * Reads integers from correct source (as was specified in command's arguments)
     * @throws IOException
     */
    void readListFromSource() throws IOException {
        if (sourceFromFile) {
            getListFromFile(sourceFile);
        }
        else {
            getListFromStandardInput();
        }
    }


    /**
     * Performs sort on list.
     */
    void sort() {
        BitonicSort.sort(list, Integer::compareTo);
    }

    /**
     * Prints list to out.
     */
    void print() {
        for (int i = 0; i < list.size(); i++) {
            out.print(list.get(i));
            if (i != list.size() - 1)
                out.print(outDelimiter);
        }
    }

    @Override
    public void close() throws Exception {

    }

    public static void main(String[] args) {

        try (Sort sort = Sort.createDefaultSort();) {

            try {
                sort.processArgs(args);
            } catch (BadParamatersException e) {
                sort.commandError("Illegal argument");
                return;
            }

            try {
                sort.readListFromSource();
            } catch (IOException e) {
                sort.commandError("Could not read file.");
                return;
            }

            sort.sort();

            sort.print();

        } catch (FactoryException e) {
            System.err.printf("Could not create Sort object. %n%s", e);
        } catch (Exception e) {
            System.err.printf("Error.%n");
        }
    }

}
