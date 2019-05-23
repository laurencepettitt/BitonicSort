package cli;

import java.io.*;
import java.util.*;
import logging.Logger;

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


    public Sort()
    {
        logger = Logger.getSingletonInstance(Logger.Level.ERROR);
        in = System.in;
        out = System.out;
        err =  System.err;
    }

    public static Sort createDefaultSort() throws FactoryException {
        Sort sort = new Sort();
        if (sort.logger == null)
            throw new FactoryException("no logger available");
        if (sort.out == null || sort.err == null)
            throw new FactoryException("no console available");
        return sort;
    }

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

    void commandError(String e) {
        logger.log(Logger.Level.ERROR, e);
        if (!quiet) helpMessage();
    }

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

    void getListFromFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(new File(sourceFile))) {
            readListFromInputStream(fileInputStream);
        }
    }

    void getListFromStandardInput() {
        readListFromInputStream(in);
    }

    void readListFromSource() throws IOException {
        if (sourceFromFile) {
            getListFromFile(sourceFile);
        }
        else {
            getListFromStandardInput();
        }
    }

    void sort() {
        list.sort(Integer::compareTo);
    }

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
