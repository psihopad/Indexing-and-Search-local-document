import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class FileReader {

    public static String readFile(String FilePath){
        String content = "";
        if(FilePath.endsWith("doc") || FilePath.endsWith("docx"))
            content = readWordFile(FilePath);
        else if(FilePath.endsWith("xls") || FilePath.endsWith("xlsx"))
            content = readExcelFile(FilePath);
        else if(FilePath.endsWith("pdf"))
            content = readPdfFile(FilePath);
        return content;
    }
    public static String readWordFile(String FilePath) {
        String content = "";
        FileInputStream fileInputStream;

        if (isDocx(FilePath)) { //is a docx
            try {
                fileInputStream = new FileInputStream(new File(FilePath));
                XWPFDocument doc = new XWPFDocument(fileInputStream);
                XWPFWordExtractor extract = new XWPFWordExtractor(doc);
                content = extract.getText();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {
            try {
                fileInputStream = new FileInputStream(new File(FilePath));
                HWPFDocument doc = new HWPFDocument(fileInputStream);
                WordExtractor extractor = new WordExtractor(doc);
                content = extractor.getText();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static boolean isDocx(String FilePath){
        return FilePath.substring(FilePath.length() - 1).equals("x");
    }
    
    public static String readExcelFile(String FilePath){
        String content = "";

        try {
            File file = new File(FilePath);   
            FileInputStream fileInputStream = new FileInputStream(file);   
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:    //field that represents string cell type
                            content = content + cell.getStringCellValue() + "\t\n";
                            break;
                        case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type
                            content = content + cell.getNumericCellValue() + "\t\n";
                            break;
                        default:
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String readPdfFile(String FilePath){
        String content = "";

        try {
            PDDocument document = PDDocument.load(new File(FilePath));
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
