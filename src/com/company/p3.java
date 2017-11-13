import java.sql.*;
import java.util.Scanner;

public class p3 {

    public static void main(String[] args) {
	    if(args.length == 2) {
	        System.out.println("1 - Report Location Information \n2 - Report Edge Information" +
                    "\n3 - Report CS Staff Information \n4 - Report New Phone Extension \n" +
                    "5 - Exit Program");
	        return;
        }
        else {
            lineScan(args);
        }
    }
    public static void lineScan(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for(int i = 1 ; i < args.length ; i++) {
            if (args[i].equals("1")) {
                System.out.println("Enter Location ID: ");
                String input = scanner.nextLine();
                locationInfo(args[0], args[1], input);
            }
            if (args[i].equals("2")) {
                System.out.println("Enter Edge ID: ");
                String input = scanner.nextLine();
                edgeInfo(args[0], args[1], input);
            }
            if (args[i].equals("3")) {
                System.out.println("Enter CS Staff Account Name: ");
                String input = scanner.nextLine();
                staffInfo(args[0], args[1], input);
            }
            if (args[i].equals("4")) {
                System.out.println("Enter CS Staff Account Name: ");
                String input = scanner.nextLine();
                System.out.println("Enter the new Phone Extension: ");
                String input2 = scanner.nextLine();
                if(input2.length() != 4) {
                    System.out.println("Phone Extensions must be 4 digits!");
                    return;
                }
                insertNewPhoneExtension(args[0], args[1], input, input2);
            }
            if (args[i].equals("5")) {
                return;
            }
        }
    }
    public static void locationInfo(String USER, String PASS, String LocID){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected!");
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * " + "FROM LOCATIONS " + "WHERE locationID = '" + LocID + "'";
            //System.out.println(str);
            ResultSet rset = stmt.executeQuery(str);

            String locationID = "";
            String locationName = "";
            String locationType = "";
            int xCord = 0;
            int yCord = 0;
            String floor = "";
            // Process the results
            while (rset.next()) {
                locationID = rset.getString("locationID");
                locationName = rset.getString("locationName");
                locationType = rset.getString("locationType");
                xCord = rset.getInt("xcoord");
                yCord = rset.getInt("ycoord");
                floor = rset.getString("mapFloor");
                System.out.println("Location Information\nLocation ID: " + locationID + "\nLocation Name: " + locationName + "\nLocation Type: " + locationType + "\nX-Coordinate: " + xCord
                + "\nY-Coordinate: " + yCord + "\nFloor:" + floor);
            } // end while

            rset.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
    public static void edgeInfo(String USER, String PASS, String edgID){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected!");
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM Edges WHERE edgeID = '" + edgID + "'";
            ResultSet rset = stmt.executeQuery(str);

            String edgeID = "";
            String startingLocationID = "";
            String startName = "";
            String startFloor = "";
            String endingLocationID = "";
            String endName = "";
            String endFloor = "";

            // Process the results
            while (rset.next()) {
                edgeID = rset.getString("edgeID");
                startingLocationID = rset.getString("startingLocationID");
                endingLocationID = rset.getString("endingLocationID");

                // get the locationName and floor of starting location in the edge

                String str2 = "SELECT locationName, mapFloor " +
                        "FROM Locations " +
                        "WHERE locationID = '" + startingLocationID + "'";
                ResultSet rset2 = stmt.executeQuery(str2);

                while(rset2.next()) {
                    startName = rset2.getString("locationName");
                    startFloor = rset2.getString("mapFloor");
                }

                // get the locationName and floor of ending location in the edge
                String str3 = "SELECT locationName, mapFloor " +
                        "FROM Locations " +
                        "WHERE locationID = '" + endingLocationID + "'";
                ResultSet rset3 = stmt.executeQuery(str3);

                while(rset3.next()) {
                    endName = rset3.getString("locationName");
                    endFloor = rset3.getString("mapFloor");
                }

                System.out.println("Edges Information\n Edge ID: " + edgeID + "\n Starting Location Name: " + startName + "\n Starting Location floor: " + startFloor +
                        "\n Ending Location Name: " + endName + "\n Ending Location Floor: " + endFloor + "\n");
            } // end while

            rset.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
    public static void staffInfo(String USER, String PASS, String actName){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected!");
        try {
            Statement stmt = connection.createStatement();
            String str = "SELECT * FROM CSStaff WHERE accountName = '" + actName + "'";
            ResultSet rset = stmt.executeQuery(str);

            String accountName = "";
            String firstName = "";
            String lastName = "";
            String officeID = "";
            String title = null;
            int phoneExt = 0;

            // Process the results
            while (rset.next()) {
                accountName = rset.getString("accountName");
                firstName = rset.getString("firstName");
                lastName = rset.getString("lastName");
                officeID = rset.getString("officeID");

                System.out.println("CS Staff Information\nAccount Name: " + accountName + "\nFirst Name: " + firstName + "\nLast Name: " + lastName + "\nOffice ID: " + officeID);

                String str2 = "SELECT titleName " + "FROM CSStaffTitles CS, Titles T " + "WHERE CS.accountName = '" + accountName + "' and CS.acronym = T.acronym";
                ResultSet rset2 = stmt.executeQuery(str2);

                while(rset2.next()) {
                    title = rset2.getString("titleName");
                    System.out.println("Title: " + title);
                }

                // get the locationName and floor of ending location in the edge
                String str3 = "SELECT phoneExt " + "FROM PhoneExtensions " + "WHERE accountName = '" + accountName + "'";
                ResultSet rset3 = stmt.executeQuery(str3);

                while(rset3.next()) {
                    phoneExt = rset3.getInt("phoneExt");
                    System.out.println("Phone Ext: " + phoneExt);
                }
            } // end while

            rset.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
    public static void insertNewPhoneExtension(String USER, String PASS, String acctName, String newPhoneExtension){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        System.out.println("Oracle JDBC Driver Connected!");

        try {
            int intPhoneNum = Integer.parseInt(newPhoneExtension);
            String str = "INSERT INTO PhoneExtensions (ACCOUNTNAME, PHONEEXT) VALUES" + "('" + acctName + "', " + intPhoneNum + ")" ;
            PreparedStatement pstmt = connection.prepareStatement(str);
            pstmt.executeUpdate(str);

            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Get Data Failed! Check output console");
            e.printStackTrace();
            return;
        }
    }

}
