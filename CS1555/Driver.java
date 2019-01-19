import java.util.*;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;  //needed by java for database connection and manipulation
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Driver extends MyAuction{
    private static Connection dbcon;
    
    public static void main(String[] args) {
        String username = "", password = "";
		
        try {
            //Oracle variable MUST BE SET by sourcing bash.env or tcsh.env or the following line will not compile
            //DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
            dbcon = DriverManager.getConnection(url, username, password);

            Driver driver = new Driver();
            driver.runMethods();
        }
        catch (SQLException s) {
            System.out.print(s.toString());                
			System.out.println();
        }
    }

    public void Driver(){

    }

    public void runMethods() throws SQLException{
        System.out.println("Printing Pants Prducts");
        super.printProducts("Pants", 1, dbcon);
        System.out.println("\nPrinting Misc Products");
        super.printProducts("Misc", 2, dbcon);
        System.out.println("\nPrinting Tool Products");
        super.printProducts("Tools", 3, dbcon);

        System.out.println("\nChecking create customer");
        Random rand = new Random();
        super.createCustomer("a"+rand.nextInt(1000), "b"+rand.nextInt(1000), "c"+rand.nextInt(1000), "d"+rand.nextInt(1000), "e"+rand.nextInt(1000), dbcon);
        selectAllFrom("Customer");

        System.out.println("\nChecking Update Time");
        super.updateTime("12/25/2010 05:32:12", dbcon);
        selectAllFrom("ourSysDate");

        System.out.println("\nListing All Products");
        super.listProducts(" WHERE ", dbcon);
        
        System.out.println("\nListing User Products (tammy4life)");
        super.listProducts(" WHERE seller = 'tammy4life' AND ", dbcon);

		System.out.println("\nListing Products Statistics(10, 5)");
        super.statistics(10 , 5 , dbcon);

        System.out.println("\nPrinting products with keywords a and b");
        String [] ar = {"a", "b"};
        super.keywordProducts(ar, dbcon);

        System.out.println("\nAdding Product");
        super.putUpAuction("juice26", "Steeler's Jersey", "Cleaning", "3", "washed up", "5",dbcon);
        selectAllFrom("Product");

        System.out.println("\nInserting bid");
        super.insertBid(2, "juice26", 100 ,dbcon);
        selectAllFrom("Bidlog");

        System.out.println("\nSelling Product");
        super.sellThisProduct(1, 100, true ,dbcon);
        selectAllFrom("Product");
        super.sellThisProduct(2, 100, false ,dbcon);
        selectAllFrom("Product");

        System.out.println("\nGetting suggestions for juice26");
        super.getSuggestions("juice26", dbcon);
    }

    public void selectAllFrom(String table) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "SELECT * FROM " + table;
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData meta = resultSet.getMetaData();
        int columns = meta.getColumnCount();

        while(resultSet.next()){
            for(int i = 1; i <= columns; i++){
                System.out.print(resultSet.getString(i) + ", ");
            }
            System.out.println();
        }
    }
}