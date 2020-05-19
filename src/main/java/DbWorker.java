import java.sql.*;
import java.util.ArrayList;

public class DbWorker {
    Connection connection;
    String userName = "root";
    String password = "1234";

    String connectionUrl = "jdbc:mysql://localhost:3306/textSearchDB?serverTimezone=UTC";

    public  DbWorker() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionUrl,userName,password);

            //connection.close();
        }
        catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void insertInSearch(String word_to_search) {
        try {
            Statement statement = connection.createStatement();
            String query = "INSERT search(word_to_search) values(\"" + word_to_search + "\");";
            statement.executeUpdate(query);
            query = "INSERT query(search_id) values(LAST_INSERT_ID());";
            statement.executeUpdate(query);
            query = "INSERT searchhistory(query_id) values(LAST_INSERT_ID());";
            statement.executeUpdate(query);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void insertInFileList(String file_name,  String path, String file_content, String file_type) {
        try {
            Statement statement = connection.createStatement();

            String query = "INSERT files(file_name, path, file_content, file_type) values(\" " + file_name
                    + "\", \""+path +  "\", \"" + file_content + "\", \"" + file_type + " \");" ;
            statement.executeUpdate(query);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public ArrayList getFileListIncludesSearchingWord( String word_to_search ) {
        ArrayList<String> fileList = new ArrayList<String>();

        try {
            Statement statement = connection.createStatement();
            String query = "select file_name,file_type from files where file_content like " + "\"%" + word_to_search + "%\"";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                String file_name = rs.getString("file_name");

                fileList.add(file_name);
            }

            return fileList;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}





