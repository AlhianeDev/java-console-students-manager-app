import java.sql.Statement;

import java.util.Arrays;

import java.util.List;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

public class App {

    static final String DB_URL = "jdbc:mysql://localhost:3306/STUDENTS_MANAGER_DB";

    static final String DB_USERNAME = "root";

    static final String DB_PASSWORD = "M64K66L98A01x";

    static Connection connection = null;

    static Statement statement = null;

    static final BufferedReader bufferedReader = new BufferedReader(

            new InputStreamReader(System.in)

    );

    static final List<String> operations = Arrays.asList(

            "CREATE", "UPDATE", "READ", "DELETE", "EXIT"

    );

    static final String select_from_genders_query = "SELECT * FROM Genders";

    static final String select_from_countries_query = "SELECT * FROM Countries";

    public static void main(String[] args) throws SQLException {

        try {

            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            statement = connection.createStatement(

                    ResultSet.TYPE_SCROLL_INSENSITIVE,

                    ResultSet.CONCUR_READ_ONLY

            );

            Class.forName("com.mysql.jdbc.Driver");

            connection.setAutoCommit(false);

            System.out.println("\nStudent Manager App.");

            sign(connection, statement);

        } catch (ClassNotFoundException | SQLException | IOException ex) {

            System.err.println(ex.getMessage());

        } finally {

            if (statement != null)

                statement.close();

            if (connection != null)

                connection.close();

        }

    }

    private static void sign(

            Connection connection, Statement statement

    ) throws IOException, SQLException {

        System.out.println("\nHi, Do You Want To Sign In Your Area At " +

                "Student Manager App, Or You Have Already An Account?");

        System.out.println("\n# 1 - Sign In");

        System.out.println("\n# 2 - Sign Up");

        System.out.print("\nYour Choice Number: ");

        int choice_number = Integer.parseInt(bufferedReader.readLine());

        if (choice_number == 1) {

            sign_in(connection, statement);

        } else {

            sign_up(connection, statement);

        }

    }

    private static int check_if_user_already_exist(

            String user_name, String password, Statement statement

    ) throws SQLException {

        int result = 0;

        String select_query = "SELECT * FROM Users WHERE user_name = " +

                "'" + user_name + "'" +

                " AND password = " + "'" + password + "'" + ";";

        ResultSet resultSet = statement.executeQuery(select_query);

        resultSet.beforeFirst();

        if (resultSet.next())

            result = resultSet.getInt("id");

        return result;

    }

    private static void sign_in(

            Connection connection, Statement statement

    ) throws SQLException, IOException {

        System.out.print("\nUsername: ");

        String username = bufferedReader.readLine();

        System.out.print("\nPassword: ");

        String password = bufferedReader.readLine();

        int user_id = check_if_user_already_exist(username, password, statement);

        String select_query = "SELECT first_name, last_name FROM Users WHERE id = " +

                "'" + user_id + "'" + ";";

        ResultSet resultSet = statement.executeQuery(select_query);

        resultSet.first();

        if (user_id > 0) {

            System.out.print("\nWelcome " + resultSet.getString("first_name"));

            System.out.println(" " + resultSet.getString("last_name") + ".");

            app(connection, statement, user_id);

        } else {

            System.out.println("Uncorrect Username Or Password!");

        }

    }

    private static void sign_up(

            Connection connection, Statement statement

    ) throws SQLException, IOException {

        String insert_query = "INSERT INTO Users " +

                "(first_name, last_name, user_name, password) VALUES " +

                "(?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(insert_query);

        System.out.print("\nFirst Name: ");

        String first_name = bufferedReader.readLine();

        System.out.print("\nLast Name: ");

        String last_name = bufferedReader.readLine();

        System.out.print("\nUsername: ");

        String username = bufferedReader.readLine();

        System.out.print("\nPassword: ");

        String password = bufferedReader.readLine();

        int user_id = check_if_user_already_exist(username, password, statement);

        if (user_id > 0) {

            System.out.println("\nInputed Username Already Exists!");

        } else {

            preparedStatement.setString(1, first_name);

            preparedStatement.setString(2, last_name);

            preparedStatement.setString(3, username);

            preparedStatement.setString(4, password);

            int result = preparedStatement.executeUpdate();

            if (result > 0) {

                connection.commit();

                System.out.println("\nOk " + first_name + " " + last_name + ".");

                System.out.println("\nNow You Can Sign In.");

                System.out.print("\nSign In ? yes (y) Or No (n): ");

                String yes_or_no = bufferedReader.readLine();

                if (yes_or_no.equals("y"))

                    sign_in(connection, statement);

                else

                    System.out.println("Exit The Program...");

            } else {

                System.out.println("\nFailed To Sign Up!");

            }

        }

    }

    private static void app(

            Connection connection, Statement statement, int user_id

    ) throws IOException, SQLException {

        Operations operation = null;

        do {

            operation = choose_operation();

            switch (operation) {

                case CREATE:

                    create_new_student(connection, statement, user_id);

                    break;

                case UPDATE:

                    update_student_by_id(connection, statement, user_id);

                    break;

                case READ:

                    display_all_students(connection, statement, user_id);

                    break;

                case DELETE:

                    delete_student_by_id(connection, statement, user_id);

                    break;

                case EXIT:

                    System.out.println("Exit The Program...");

                    break;

                default:

                    System.out.println("Unknown Operation!");

                    break;

            }

        } while (!operation.equals(Operations.EXIT));

    }

    private static Operations choose_operation() throws IOException {

        System.out.println("\nHi Teacher.");

        System.out.println("\nPlease, Select One Of Theese Operations:");

        for (String operation : operations)

            System.out.println("\n# " + operation);

        System.out.print("\nOperation Name: ");

        String operation = bufferedReader.readLine();

        return Operations.valueOf(operation);

    }

    private static void create_new_student(

            Connection connection, Statement statement, int user_id

    ) throws SQLException, IOException {

        String insert_query = "INSERT INTO Students " +

                "(first_name, last_name, gender_id, country_id, user_id) VALUES " +

                "(?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement =

                connection.prepareStatement(insert_query);

        System.out.print("\nStudent First Name: ");

        String first_name = bufferedReader.readLine();

        System.out.print("\nStudent Last Name: ");

        String last_name = bufferedReader.readLine();

        ResultSet genders_resultSet = statement.executeQuery(

                select_from_genders_query

        );

        genders_resultSet.beforeFirst();

        System.out.println("\nGenders:");

        while (genders_resultSet.next()) {

            System.out.println("\n# " + genders_resultSet.getInt("id") +

                    " - " + genders_resultSet.getString("gender_type"));

        }

        System.out.print("\nGender Id: ");

        int gender_id = Integer.parseInt(bufferedReader.readLine());

        ResultSet countries_resultSet = statement.executeQuery(

                select_from_countries_query

        );

        countries_resultSet.beforeFirst();

        System.out.println("\nCountries:");

        while (countries_resultSet.next()) {

            System.out.println("\n# " + countries_resultSet.getInt("id") +

                    " - " + countries_resultSet.getString("country_name"));

        }

        System.out.print("\nCountry Id: ");

        int country_id = Integer.parseInt(bufferedReader.readLine());

        preparedStatement.setString(1, first_name);

        preparedStatement.setString(2, last_name);

        preparedStatement.setInt(3, gender_id);

        preparedStatement.setInt(4, country_id);

        preparedStatement.setInt(5, user_id);

        int result = preparedStatement.executeUpdate();

        if (result > 0) {

            connection.commit();

            System.out.println("\nStudent Added Successfuly.");

        } else {

            System.out.println("\nFailed To Create Student!");

        }

        genders_resultSet.close();

        countries_resultSet.close();

        preparedStatement.close();

    }

    private static void update_student_by_id(

            Connection connection, Statement statement, int user_id

    ) throws SQLException, IOException {

        String update_query = "UPDATE Students SET " +

                "first_name = ?, last_name = ?, gender_id = ?, country_id = ? " +

                "WHERE id = ? AND user_id = " + "'" + user_id + "'" + ";";

        PreparedStatement preparedStatement =

                connection.prepareStatement(update_query);

        System.out.print("\nStudent Id: ");

        int student_id = Integer.parseInt(bufferedReader.readLine());

        System.out.print("\nNew Student First Name: ");

        String new_first_name = bufferedReader.readLine();

        System.out.print("\nNew Student Last Name: ");

        String new_last_name = bufferedReader.readLine();

        ResultSet genders_resultSet = statement.executeQuery(

                select_from_genders_query

        );

        genders_resultSet.beforeFirst();

        System.out.println("\nGenders:");

        while (genders_resultSet.next()) {

            System.out.println("\n# " + genders_resultSet.getInt("id") +

                    " - " + genders_resultSet.getString("gender_type"));

        }

        System.out.print("\nNew Gender Id: ");

        int new_gender_id = Integer.parseInt(bufferedReader.readLine());

        ResultSet countries_resultSet = statement.executeQuery(

                select_from_countries_query

        );

        countries_resultSet.beforeFirst();

        System.out.println("\nCountries:");

        while (countries_resultSet.next()) {

            System.out.println("\n# " + countries_resultSet.getInt("id") +

                    " - " + countries_resultSet.getString("country_name"));

        }

        System.out.print("\nNew Country Id: ");

        int new_country_id = Integer.parseInt(bufferedReader.readLine());

        preparedStatement.setString(1, new_first_name);

        preparedStatement.setString(2, new_last_name);

        preparedStatement.setInt(3, new_gender_id);

        preparedStatement.setInt(4, new_country_id);

        preparedStatement.setInt(5, student_id);

        int result = preparedStatement.executeUpdate();

        if (result > 0) {

            connection.commit();

            System.out.println("\nStudent Updated Successfuly.");

        } else {

            System.out.println("\nFailed To Update Student!");

        }

        genders_resultSet.close();

        countries_resultSet.close();

        preparedStatement.close();

    }

    private static void display_all_students(

            Connection connection, Statement statement, int user_id

    ) throws SQLException, IOException {

        String select_query = "SELECT Students.id, first_name, last_name, gender_type," +

                "country_name from Students " +

                "INNER JOIN Genders ON Genders.id = Students.gender_id " +

                "INNER JOIN Countries ON Countries.id = Students.country_id " +

                "WHERE Students.user_id = " + "'" + user_id + "'" + " " +

                "ORDER BY Students.id ASC;";

        ResultSet resultSet = statement.executeQuery(select_query);

        resultSet.beforeFirst();

        if (!resultSet.next()) {

            System.out.println("\nNo Students To Show!");

        }

        resultSet.beforeFirst();

        while (resultSet.next()) {

            System.out.print("\n# " + resultSet.getInt("id") + " - ");

            System.out.print(resultSet.getString("first_name") + " ");

            System.out.print(resultSet.getString("last_name") + ", ");

            System.out.print("Gender: ");

            System.out.print(resultSet.getString("gender_type") + ", ");

            System.out.print("Country: ");

            System.out.println(resultSet.getString("country_name") + ".");

        }

        connection.commit();

        resultSet.close();

    }

    private static void delete_student_by_id(

            Connection connection, Statement statement, int user_id

    ) throws SQLException, IOException {

        String delete_query = "DELETE FROM Students WHERE id = ? AND user_id = " +

                "'" + user_id + "'" + ";";

        PreparedStatement preparedStatement = connection.prepareStatement(delete_query);

        System.out.print("\nStudent Id: ");

        int student_id = Integer.parseInt(bufferedReader.readLine());

        preparedStatement.setInt(1, student_id);

        int result = preparedStatement.executeUpdate();

        if (result > 0) {

            connection.commit();

            System.out.println("\nStudent Deleted Successfuly.");

        } else {

            System.out.println("\nFailed To Deleted Student!");

        }

        preparedStatement.close();

    }

}
