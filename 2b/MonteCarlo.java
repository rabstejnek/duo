import java.util.ArrayList;
import java.util.Random;

/**
 * The abstract class of template pattern.
 * 
 * @author
 *
 */
abstract class MonteCarloTemplate {
    public void executeMonteCarlo ( final String[] args ) {
        retrieveNumberRepeatsAndSeed( args );
        createRandomGenerator();
        generateRandomSamples();
    }

    /**
     * retrieve number repeats and seed.
     * 
     * @param args
     *            args input
     */
    public abstract void retrieveNumberRepeatsAndSeed ( String[] args );

    /**
     * create random generator
     */
    public abstract void createRandomGenerator ();

    /**
     * generate random samples
     */
    public abstract void generateRandomSamples ();
}

/**
 * The MonteCarlo main program
 * 
 * @author
 *
 */
public class MonteCarlo extends MonteCarloTemplate {

    /** the number of repeats */
    int                 numRepeats;
    /** the seed */
    long                seed;
    /** the verbose */
    boolean             verbose;
    /** all the columns */
    ArrayList<Variable> variables = new ArrayList<Variable>();
    /** the print method using strategy pattern */
    PrintStrategy       printer;

    /**
     * the constructor
     */
    public MonteCarlo () {

        // add all columns to the list
        variables.add( new Variable( "pomposity", 0, 100 ) );
        variables.add( new Variable( "learning_curve", 1, 100 ) );
        variables.add( new Variable( "optimism", 0.1, 100 ) );
        variables.add( new Variable( "atleast", 0, 10 ) );
        variables.add( new Variable( "done_percent", 0, 10 ) );
        variables.add( new Variable( "productivity_new", 0, 1 ) );
        variables.add( new Variable( "productivity_exp", 1, 10 ) );
        variables.add( new Variable( "d", 0, 1 ) );
        variables.add( new Variable( "ep", 1, 10 ) );
        variables.add( new Variable( "nprod", 0.1, 10 ) );
        variables.add( new Variable( "np", 1, 30 ) );
        variables.add( new Variable( "ts", 1, 100 ) );
        variables.add( new Variable( "to", 1, 100 ) );
        variables.add( new Variable( "r", 1, 1000 ) );

        // calling the print method to print the list
        printer = new DictPrintStrategy( variables );
    }

    /**
     * retrieve number repoeats and seed
     */
    @Override
    public void retrieveNumberRepeatsAndSeed ( final String[] args ) {

        // Check number of arguments
        if ( args.length != 3 ) {
            System.err.println( "Expected three arguments." );
            System.exit( 1 );
        }

        // Parse number of repeats to integer
        try {
            this.numRepeats = Integer.parseInt( args[0] );
        }
        catch ( final NumberFormatException nfe ) {
            System.err.println( "The number of repeats must be an integer." );
            System.exit( 1 );
        }

        // Parse seed number to integer
        try {
            this.seed = Integer.parseInt( args[1] );
        }
        catch ( final NumberFormatException nfe ) {
            System.err.println( "The random seed number must be an integer." );
            System.exit( 1 );
        }

        // Parse verbose to boolean
        if ( args[2].equals( "True" ) ) {
            this.verbose = true;
        }
        else if ( args[2].equals( "False" ) ) {
            this.verbose = false;
        }
        else {
            System.err.println( "Verbose must be 'True' or 'False'" );
            System.exit( 1 );
        }
    }

    /**
     * create random generator
     */
    @Override
    public void createRandomGenerator () {
        RandomGeneratorSingleton.getInstance().setSeed( seed );
        ;
    }

    /**
     * generate random samples
     */
    @Override
    public void generateRandomSamples () {
        printer.print( numRepeats, verbose );
    }

    /**
     * the main method of MonteCarlo
     * 
     * @param args
     *            the argument
     */
    public static void main ( final String[] args ) {
        final MonteCarlo mc = new MonteCarlo();
        mc.executeMonteCarlo( args );
    }

    /**
     * The variable object
     * 
     * @author
     *
     */
    private class Variable {

        /**
         * The inner class of variable
         * 
         * @author
         *
         */
        private class VariableDomain {
            /** the min val of the variable */
            double min;
            /** the max val of the variable */
            double max;

            /**
             * The constructor of variable
             * 
             * @param min
             *            value of the variable
             * @param max
             *            value of the variable
             */
            public VariableDomain ( final double min, final double max ) {
                this.min = min;
                this.max = max;
            }

            /**
             * return the result
             * 
             * @return the result
             */
            public double uniform () {
                return min + ( RandomGeneratorSingleton.getInstance().nextDouble() * ( max - min ) );
            }
        }

        /**
         * the name of the variable
         */
        String         name;
        /**
         * the variable domain
         */
        VariableDomain domain;

        /**
         * Initialize a variable
         * 
         * @param name
         *            of the variable
         * @param min
         *            value of the variable
         * @param max
         *            value of the variable
         */
        public Variable ( final String name, final double min, final double max ) {
            this.name = name;
            this.domain = new VariableDomain( min, max );
        }

        /**
         * return the uniform result
         * 
         * @return the uniform result
         */
        public double uniform () {
            return domain.uniform();
        }
    }

    /**
     * A singleton class for the seed generator
     * 
     * @author
     *
     */
    private static class RandomGeneratorSingleton {

        /**
         * the Singleton pattern instance
         */
        private static RandomGeneratorSingleton instance;
        /**
         * The random value r
         */
        private Random                          r;

        /**
         * The constructor of the instance
         */
        private RandomGeneratorSingleton () {
            r = new Random();
        }

        /**
         * method to return instance of class
         * 
         * @return the instance
         */
        public static RandomGeneratorSingleton getInstance () {
            if ( instance == null ) {
                instance = new RandomGeneratorSingleton();
            }
            return instance;
        }

        /**
         * set the seed
         * 
         * @param seed
         *            to be set
         */
        public void setSeed ( final long seed ) {
            r = new Random( seed );
        }

        /**
         * next double to be generated
         * 
         * @return the next double to be generated
         */
        public double nextDouble () {
            return r.nextDouble();
        }
    }

    /**
     * the interface of the print Strategy pattern
     * 
     * @author
     *
     */
    public interface PrintStrategy {
        public void print ( int num, boolean verbose );
    }

    /**
     * The csv print method that implements printStrategy
     * 
     * @author
     *
     */
    public class CSVPrintStrategy implements PrintStrategy {

        /** the variables list */
        ArrayList<Variable> variables;

        /**
         * The constructor of the print strategy
         * 
         * @param variables
         *            the list of column
         */
        public CSVPrintStrategy ( final ArrayList<Variable> variables ) {
            this.variables = variables;
        }

        @Override
        public void print ( int num, final boolean verbose ) {

            final String verboseString = verbose ? "1" : "0";

            // Print header
            for ( int i = 0; i < variables.size(); i++ ) {
                System.out.printf( "%s,", variables.get( i ).name );
            }
            System.out.println( "verbose" );

            // Print rows
            for ( ; num > 0; num-- ) {
                for ( final Variable v : variables ) {
                    System.out.printf( "%.2f,", v.uniform() );
                }
                System.out.printf( "%s%n", verboseString );
            }
        }
    }

    /**
     * The dictionary print method that implements printSrategy
     * 
     * @author
     *
     */
    public class DictPrintStrategy implements PrintStrategy {

        /** the variables list */
        ArrayList<Variable> variables;

        /**
         * The constructor of the print strategy
         * 
         * @param variables
         *            the list of column
         */
        public DictPrintStrategy ( final ArrayList<Variable> variables ) {
            this.variables = variables;
        }

        @Override
        public void print ( int num, final boolean verbose ) {

            final String verboseString = verbose ? "True" : "False";

            // Print python formatted dicts
            for ( ; num > 0; num-- ) {
                System.out.print( "{" );
                for ( final Variable v : variables ) {
                    System.out.printf( "'%s': %.2f, ", v.name, v.uniform() );
                }
                System.out.printf( "'verbose': %s}%n", verboseString );
            }
        }
    }
}
