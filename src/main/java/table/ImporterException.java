package table;

public class ImporterException extends Exception {
    public ImporterException(Exception e) {
        super("Follwing exception occured during Table import:", e);
    }
}
