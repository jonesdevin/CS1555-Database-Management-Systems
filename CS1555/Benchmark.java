import java.util.*;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;  //needed by java for database connection and manipulation
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Benchmark extends MyAuction{
    private static Connection dbcon;
    
    public static void main(String[] args) {
        String username = "", password = "";
		
        try {
            //Oracle variable MUST BE SET by sourcing bash.env or tcsh.env or the following line will not compile
            //DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
            dbcon = DriverManager.getConnection(url, username, password);

            Benchmark driver = new Benchmark();
            driver.runMethods();
        }
        catch (SQLException s) {
            System.out.print(s.toString());                
            System.exit(1);
        }
    }

    public void Driver(){

    }

    public void runMethods() throws SQLException{

        printAllProducts();

        createManyCustomers();

        updateMoreTime();

        listAllUserProducts();

		listAllStats();

        searchManyKeywords();

        putManyUpAuction();

        insertManyBids();

        getAllSuggestions();
		

    }

    public void printAllProducts() throws SQLException{
        String [] ar = {"Pants", "Shirts", "Socks", "Underwear", "Phones",
                        "Computers", "Stereos", "Radios", "Electric", "Engine",
                        "Body", "Accesories", "Tools", "Furniture", "Misc",
                        "Cleaning", "Decorations"};

        for(int i = 0; i < ar.length; i++){
            System.out.println("\n"+ar[i]);
            super.printProducts(ar[i], 1, dbcon);
            super.printProducts(ar[i], 2, dbcon);
            super.printProducts(ar[i], 3, dbcon);
        }
        
    }

    public void createManyCustomers() throws SQLException{
        for(int i = 0; i < 50; i++){
            super.createCustomer("a"+i, "b"+i, "c"+i, "d"+i, "e"+i, dbcon);
            selectAllFrom("Customer");
        }
    }

    public void updateMoreTime() throws SQLException{
        for(int i = 10; i < 60; i++){
            super.updateTime("12/25/2010 05:32:"+i, dbcon);
            selectAllFrom("ourSysDate");
        }
        
    }

    public void listAllUserProducts() throws SQLException{
        String [] ar = {"tammy4life", "juice26", "prucker_"};
        System.out.println("\nAll");
        super.listProducts(" WHERE ", dbcon);
        for(int i = 0; i < ar.length; i++){
            System.out.println("\n"+ar[i]);
            super.listProducts(" WHERE seller = '" + ar[i] + "' AND ", dbcon);
        }
    }

    public void searchManyKeywords() throws SQLException{
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String [] ar = {"a", "b"};
        Random rand = new Random();
        for(int i = 0; i < 30; i++){
            ar[0] = letters.charAt(rand.nextInt(26)) + "";
            ar[1] = letters.charAt(rand.nextInt(26)) + "";
            super.keywordProducts(ar, dbcon);
        }
    }

    public void putManyUpAuction() throws SQLException{
        for(int i = 0; i < 30; i++){
            super.putUpAuction("juice26", "a" +i, "Cleaning", "3", "washed up", "5",dbcon);
            selectAllFrom("Product");
        }
    }

    public void insertManyBids() throws SQLException{
        for(int i = 0; i < 40; i++){
            super.insertBid(1, "juice26", 200+i ,dbcon);
            selectAllFrom("Bidlog");
        }
    }

    public void getAllSuggestions() throws SQLException{
        String [] ar = {"tammy4life", "juice26", "prucker_"};
        for(int i = 0; i < ar.length; i++){
            super.getSuggestions(ar[i], dbcon);
        }
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
	
	public void listAllStats() throws SQLException{
		
		for(int k = 1; k<=10; k++){
			for(int i = 1; i <= 6; i++){
					super.statistics(i , k , dbcon);
				}
		}
		System.out.println();
		
	}
}