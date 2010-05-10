package toxi.data.csv;

public interface CSVListener {

    void csvNewItemFailure(String[] fields, CSVFieldMapper map);

    void csvNewItemParsed(String[] fields, CSVFieldMapper map);

    void csvParseFailure(CSVParser parser);

    void csvParseSuccess(CSVParser parser);
}
