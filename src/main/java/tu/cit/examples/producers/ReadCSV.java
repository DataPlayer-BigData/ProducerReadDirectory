package tu.cit.examples.producers;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.util.List;

public class ReadCSV {

    private String csvFileName;
    private List stdList;

    ReadCSV(String csvFileName){
        this.csvFileName = csvFileName;
    }

    public List ReadCSVFile(){
        try{
            CSVReader csvReader =  new CSVReader(new FileReader(csvFileName));
            CsvToBean csvToBean = new CsvToBeanBuilder(csvReader)
                                    .withType(tu.cit.examples.producers.StudentModel.class)
                                    .withIgnoreLeadingWhiteSpace(true).build();

            stdList = csvToBean.parse();

        }catch(Exception FileNotFoundException){
            System.out.println("File is not found...");
        }

        return stdList;
    }
}
