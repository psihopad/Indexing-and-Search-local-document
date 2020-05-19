import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;

public class FileSearcher {

    public static void setFilesList(Menu menu) {
            menu.listOfDocuments.setModel(getListOfFiles(menu));
    }

    public static DefaultListModel getListOfFiles(Menu menu){
        DefaultListModel<String> listOfFiles = new DefaultListModel<>();
        try {
            File file = new File(menu.getFolderAddress());
            FilenameFilter filter = (file1, name) -> name.endsWith(".doc")
                    || name.endsWith(".docx")
                    || name.endsWith(".pdf")
                    || name.endsWith(".xls")
                    || name.endsWith(".xlsx");

            File[] files = file.listFiles(filter);


            assert files != null;
            for (File f : files) {
                listOfFiles.addElement(f.getName());
            }
            menu.listOfDocuments.setModel(listOfFiles);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfFiles;
    }

    public static boolean isFileContainsText(){
        return true;
    }
        

}
