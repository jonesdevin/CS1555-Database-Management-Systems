import java.util.*;
import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;  //needed by java for database connection and manipulation
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class MyAuction {
    private static Connection dbcon;
    private Statement statement;
    private PreparedStatement prepStatement; 
    private ResultSet resultSet; 
    private String query;  

    public static void main(String [] args) {
        String username = "", password = "";	
		
		
        try {
            //Oracle variable MUST BE SET by sourcing bash.env or tcsh.env or the following line will not compile
            //DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
            dbcon = DriverManager.getConnection(url, username, password);

            Scanner reader = new Scanner(System.in);
            int input = 0;
            do{
                System.out.println("Sign In:\n" +
                                    "Admin\t\t(1)\n" +
                                    "Customer\t(2)\n");
                if(reader.hasNextInt()){
                    input = reader.nextInt();
                }else{
                    reader.next();
                }
            }while(input != 1 && input != 2);
            login(input);
        }
        catch (SQLException s) {
            System.out.print(s.toString());                
            System.exit(1);
        }
    }

    static void login(int input) throws SQLException {
        Scanner reader = new Scanner(System.in);
        String username, password;
        boolean badLogin = true;
        do{
            System.out.print("\nUsername: ");
            username = reader.next();
			
            System.out.print("\nPassword: ");
            password = reader.next();
            Statement statement;
            String query;
            ResultSet resultSet;
            if(input == 1){
                statement = dbcon.createStatement();
                query = "SELECT login, password FROM Administrator WHERE login='"+username+"' AND password='"+password+"'";
                resultSet = statement.executeQuery(query);
                //returns boolean, check if result set is not empty
                if(resultSet.next()){
                    adminInterface();
                }else{
                    System.out.println("Admin does not exist");
                }
                badLogin = false;
            }else{
                statement = dbcon.createStatement();
                query = "SELECT login, password FROM Customer WHERE login='"+username+"' AND password='"+password+"'";
                resultSet = statement.executeQuery(query);
                if(resultSet.next()){
                    customerInterface(username);
                }else{
                    System.out.println("Customer does not exist");
                }
                badLogin = false;
            }
        }while(badLogin);
        
    }

    static void adminInterface() throws SQLException {
        Scanner reader = new Scanner(System.in);
        int input = 0;
        do{
            System.out.println("\n\nAdmin Interface:\n" +
                                "Register New Customer\t(1)\n" +
                                "Update System Time\t(2)\n" +
                                "Product Stats\t\t(3)\n" +
								"Statistics\t\t(4)\n" +
                                "Quit\t\t\t(5)\n");
            if(reader.hasNextInt()){
                input = reader.nextInt();
            }else{
                input = 0;
                reader.next();
            }
            if(input == 1){
                registerCustomer();
            }else if(input == 2){
				try{
					updateSystemTime();
				}
				catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }else if(input == 3){
                productStats();
            }
			else if(input == 4){
                statistics(0,0, dbcon);
            }
			
        }while(input != 5);
    }

	
	//Created
    static void registerCustomer() throws SQLException {
        Scanner reader = new Scanner(System.in);
        System.out.println("\nRegister New Customer");
        System.out.print("\nName: ");
        String name = reader.next();
		
		String address = reader.nextLine();
        System.out.print("Address: ");
        address = reader.nextLine();
		
		if( address.toLowerCase().contains("delete") || 
			address.toLowerCase().contains("update") ||
			address.toLowerCase().contains("alter") ||
			address.toLowerCase().contains("create") ||
			address.toLowerCase().contains("drop") ||
			address.toLowerCase().contains("insert")){
				  throw new SQLException("INJECTION DETECTED - Report has been logged!");

			}
		
        System.out.print("\nEmail: ");
        String email = reader.next();
        System.out.print("\nUsername: ");
        String username = reader.next();
        System.out.print("\nPassword: ");
        String password = reader.next();		
		
		createCustomer(username, password, name, address, email, dbcon);
        
		System.out.println("...CUSTOMER ADDED\n");
		
    }

	//Created
    static void updateSystemTime() throws SQLException{
		Scanner reader = new Scanner(System.in);
        System.out.println("\nUpdate System Time: ");
		String date = "";
		
		System.out.println("MONTH in numbers: ");
		int month = reader.nextInt();
		date += (month);
		date += ("/");
		
		System.out.println("DAY: ");
		int day = reader.nextInt();
		date += (day);
		date += ("/");
		
		System.out.println("YEAR: ");
		int year = reader.nextInt();
		date += (year);
		date += (" ");
		
		System.out.println("HOUR: ");
		int hour = reader.nextInt();
		date += (hour);
		date += (":");
		
		System.out.println("MINUTES: ");
		int minutes = reader.nextInt();
		date += (minutes);
		date += (":");
		
		System.out.println("SECONDS:");
		int seconds = reader.nextInt();
		date += (seconds);
		
		updateTime(date, dbcon);
		
		System.out.println("...System Date Updated\n");
		
    }
	
	//Created
    static void productStats() throws SQLException{
       System.out.println("\nProduct Stats:");
       Scanner reader = new Scanner(System.in);
       int input = 0;
       do{
           System.out.println("List all products\t(1)\n" +
                               "List user products\t(2)\n");
           if(reader.hasNextInt()){
               input = reader.nextInt();
           }else{
               reader.nextLine();
           }
       }while(input != 1 && input != 2);
	   
       String where_user = " WHERE ";
       if(input == 2){
           System.out.print("Username: ");
           String username = reader.next();

           where_user += " seller = '" + username + "' AND";
		   
       }

		listProducts(where_user, dbcon);
	   
   }

    static void customerInterface(String username){
        Scanner reader = new Scanner(System.in);
        int input = 0;
        do{
            System.out.println("\n\nCustomer Interface:\n" +
                                "Browse Products\t\t(1)\n" +
                                "Search Products\t\t(2)\n" +
                                "Auction Product\t\t(3)\n" +
                                "Bid on Product\t\t(4)\n" +
                                "Sell Product\t\t(5)\n" +
                                "Suggestions\t\t(6)\n" +
                                "Quit\t\t\t(7)\n");
            if(reader.hasNextInt()){
                input = reader.nextInt();
            }else{
                input = 0;
                reader.next();
            }
            if(input == 1){
				try{
					browseProducts();
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }else if(input == 2){
                try{
				searchProducts();
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
				
            }else if(input == 3){
                try{
				auctionProducts(username);
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }else if(input == 4){
                try{
				bidOnProducts(username);
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }else if(input == 5){
                try{
				sellProduct(username);
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }else if(input == 6){
                try{
				suggestions(username);
				}
			 catch (SQLException s) {
					System.out.print(s.toString());                
					//System.exit(1);
				}
            }
        }while(input != 7);
    }

    //CREATED
    static void browseProducts() throws SQLException{
		Scanner reader = new Scanner(System.in);
		
		
        System.out.println("\nBrowsing Products....\n");

		Statement statement = dbcon.createStatement();
		String query = "SELECT * from Category where parent_category is null";
		ResultSet resultSet = statement.executeQuery(query);

		System.out.println("---PARENT CATEGORIES---");
		
		while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
		
		String category = "";
		boolean exist = false;
		while(!exist){
			ResultSet resultSet1 = statement.executeQuery(query);
		
			System.out.print("\nWhat Category: ");
			
			category = reader.nextLine();
			
			if( category.toLowerCase().contains("delete") || 
				category.toLowerCase().contains("update") ||
				category.toLowerCase().contains("alter") ||
				category.toLowerCase().contains("create") ||
				category.toLowerCase().contains("drop") ||
				category.toLowerCase().contains("insert")){
					  throw new SQLException("INJECTION DETECTED - Report has been logged!");

			}
			
			while (resultSet1.next()) {
				if(category.equalsIgnoreCase(  resultSet1.getString(1))){
					exist = true;
				}
			
			}
		}
	
		query = "SELECT parent_category from category where parent_category = '"+category+"'";
		resultSet = statement.executeQuery(query);
	
		if(resultSet.next()){
			query = "SELECT name from category where parent_category = '"+category+"'";
			resultSet = statement.executeQuery(query);
			
			System.out.println("---SUB CATEGORIES---");
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
			
			exist = false;
			while(!exist){
				ResultSet resultSet1 = statement.executeQuery(query);
			
				System.out.print("\nWhat Category: ");
				
				category = reader.nextLine();
				
							
				if( category.toLowerCase().contains("delete") || 
					category.toLowerCase().contains("update") ||
					category.toLowerCase().contains("alter") ||
					category.toLowerCase().contains("create") ||
					category.toLowerCase().contains("drop") ||
					category.toLowerCase().contains("insert")){
						  throw new SQLException("INJECTION DETECTED - Report has been logged!");

				}
				
				while (resultSet1.next()) {
					if(category.equalsIgnoreCase(  resultSet1.getString(1))){
						exist = true;
					}
				
				}
			}
		}
		
       int order = 0;
	   String order_by = "";
       do{
           System.out.println("\nOrder?:\n" +
                       "Alphabetical\t\t(1)\n" +
                       "Highest Bid\t\t(2)\n" +
                       "No Order\t\t(3)\n");
           order = reader.nextInt();
       }while(order != 1 && order != 2 && order != 3);

      
       printProducts(category, order, dbcon);
		
	
	}

    //Created 
    static void searchProducts() throws SQLException{
       Scanner reader = new Scanner(System.in);
       System.out.println("\n-Search Products-");
       System.out.print("\nKeywords:");
       String words = reader.nextLine();
	   
	   			
		if( words.toLowerCase().contains("delete") || 
			words.toLowerCase().contains("update") ||
			words.toLowerCase().contains("alter") ||
			words.toLowerCase().contains("create") ||
			words.toLowerCase().contains("drop") ||
			words.toLowerCase().contains("insert")){
				  throw new SQLException("INJECTION DETECTED - Report has been logged!");

		}
	   
       String[] keywords = words.split(" ");
      

       System.out.println("\nAUCTION_ID, NAME, DESCRIPTION");

       keywordProducts(keywords, dbcon);
   }

//CREATED
    static void auctionProducts(String username) throws SQLException{
        Scanner reader = new Scanner(System.in);
        System.out.println("Put a product up for auction");
        System.out.print("Name: ");
        String name = reader.nextLine();
		
		if( name.toLowerCase().contains("delete") || 
			name.toLowerCase().contains("update") ||
			name.toLowerCase().contains("alter") ||
			name.toLowerCase().contains("create") ||
			name.toLowerCase().contains("drop") ||
			name.toLowerCase().contains("insert")){
				  throw new SQLException("INJECTION DETECTED - Report has been logged!");

		}
		
		
        System.out.print("Description: ");
        String desc = reader.nextLine();
		
		
		if( desc.toLowerCase().contains("delete") || 
			desc.toLowerCase().contains("update") ||
			desc.toLowerCase().contains("alter") ||
			desc.toLowerCase().contains("create") ||
			desc.toLowerCase().contains("drop") ||
			desc.toLowerCase().contains("insert")){
				  throw new SQLException("INJECTION DETECTED - Report has been logged!");

		}
		
        System.out.print("Categories: ");
        String categories = reader.nextLine();
		
		
		if( categories.toLowerCase().contains("delete") || 
			categories.toLowerCase().contains("update") ||
			categories.toLowerCase().contains("alter") ||
			categories.toLowerCase().contains("create") ||
			categories.toLowerCase().contains("drop") ||
			categories.toLowerCase().contains("insert")){
				  throw new SQLException("INJECTION DETECTED - Report has been logged!");

			}
		
        int days = 0;
        do{
            System.out.print("Days for Auction: ");
            if(reader.hasNextInt()){
                days = reader.nextInt();
                if(days > 0){
                    break;
                }
            }else{
                reader.nextLine();
                
            }
        }while(true);
        int price = 0;
        do{
            System.out.print("Min Price: ");
            if(reader.hasNextInt()){
                price = reader.nextInt();
                if(price > 0){
                    break;
                }
            }else{
                reader.nextLine();
            }
        }while(true);
        
        putUpAuction(username, name, categories, days+"", desc, price+"", dbcon);
    }

//CREATED
    static void bidOnProducts(String username) throws SQLException{
        Scanner reader = new Scanner(System.in);
        System.out.println("Bid on Products");
        
        Statement statement = dbcon.createStatement();
        String query = "SELECT max(auction_id) FROM Product";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();

        int auctionID = 0;
        
        do{
            System.out.print("Product ID: ");
            if(reader.hasNextInt()){
                auctionID = reader.nextInt();
                if(auctionID > 0 && auctionID <= resultSet.getInt(1)){
                    break;
                }
            }else{
                reader.next();
            }
        }while(true);
        
        int amount = 0;
        
        do{
            System.out.print("Amount: ");
            if(reader.hasNextInt()){
                amount = reader.nextInt();
                if(amount > 0){
                    break;
                }
            }else{
                reader.next();
            }
        }while(true);
        insertBid(auctionID, username, amount, dbcon);
    }

	//CREATED
    static void sellProduct(String username) throws SQLException{
        System.out.println("Selling Product");
		
		Scanner reader = new Scanner(System.in);
	   
        Statement statement = dbcon.createStatement();
        String query = "SELECT auction_id FROM Product WHERE status = 'under auction' AND seller = '"+ username +"'";
        ResultSet resultSet = statement.executeQuery(query);
	    
		System.out.println("\n---AUCTION_ID(s)---");
		while(resultSet.next()){
			System.out.println(resultSet.getString(1));
			}
			
		boolean exist = false;
		int x = 0, y = 0;
		
		do{
			System.out.println("Please select auctionID or 0 to ignore: ");
            if(reader.hasNextInt()){
                x = reader.nextInt();
            } 
			
			query = "SELECT auction_id FROM Product WHERE status = 'under auction' AND seller = '"+ username +"'";
			resultSet = statement.executeQuery(query);
			
			if(x == 0){
				exist = true;
			}
			
			
			while(resultSet.next()){
			
				if(x == resultSet.getInt(1)){
						exist = true;
				}
			
			}	
		}while(!exist);
		
		int amount = 0;
	
		if(x != 0){
			query = "SELECT getSecHighBid("+x+") from dual";
			resultSet = statement.executeQuery(query);
			resultSet.next();
			amount = resultSet.getInt(1);
			System.out.println("Bid AMOUNT: " + amount + "" );
			exist = false;
		}
		
		while(!exist){
			
			System.out.println("Would you like to sell(y/n): ");
           
               String b = reader.next();
			   
			if(b.equals("y")){
				sellThisProduct(x, amount, true, dbcon);
				exist = true;
			}	
			
			else if(b.equals("n")){
				sellThisProduct(x, amount, false, dbcon);
				exist = true;
			}
			
			else{exist = false;}		
		}
    }
	
    //DONE
	static void statistics(int x, int k, Connection dbcon) throws SQLException{
       Scanner reader = new Scanner(System.in);
       //int x = 0, k = 0;

       Statement statement = dbcon.createStatement();
       String query;
       ResultSet resultSetCat;
		if(x == 0 && k == 0){
		   do {
			   System.out.println("Number of months to look back: ");
			   if(reader.hasNextInt()){
				   x = reader.nextInt();
			   }
		   } while(x <= 0);
		   do {
				System.out.println("Number of results to list: ");
				if(reader.hasNextInt()){
					k = reader.nextInt();
				}
			} while(k <= 0);
			
		
		}
       
       /*i*/
            //stores category name and product count of respective category
            Map<Integer, String> i = new HashMap<Integer, String>(20);

            //select all categories
            query = "SELECT name FROM Category WHERE parent_category IS NOT NULL";
			resultSetCat = statement.executeQuery(query);
            
            //iterate through set of categories and store product count in HashMap
            while(resultSetCat.next()) {
				ResultSet resultSet;
                statement = dbcon.createStatement();
                query = "SELECT func_productCount("+x+",'"+resultSetCat.getString(1)+"') from dual";
                //System.out.println(query);
                resultSet = statement.executeQuery(query);
                resultSet.next();
                i.put(resultSet.getInt(1) * -1, resultSetCat.getString(1));
                //System.out.println("Added K: "+resultSet.getInt(1)+" and V: "+resultSetCat.getString(1));
				resultSet.close();
			
			}

            //convert to stream and use comparator from Map.Entry to sort in descending order then convert back to Map with a limit of k entries
            Map<Integer, String> treeMap = new TreeMap<Integer,String>(i);

            //print out map
            System.out.println("\n-----Statistics-----\nThe top "+k+" highest categories: ");
            printMap(treeMap, k);
			System.out.println("");
			resultSetCat.close();
			
			
	   
       /*ii*/
            //stores category name and product count of respective category
            Map<Integer, String> ii = new HashMap<Integer, String>(20);

            //select all categories
            query = "SELECT name FROM Category WHERE parent_category IS NULL";
			resultSetCat = statement.executeQuery(query);
            
            //iterate through set of categories and store product count in HashMap
            while(resultSetCat.next()) {
				ResultSet resultSet;
                statement = dbcon.createStatement();
                query = "SELECT func_productCount("+x+",'"+resultSetCat.getString(1)+"') from dual";
                //System.out.println(query);
                resultSet = statement.executeQuery(query);
                resultSet.next();
                ii.put(resultSet.getInt(1), resultSetCat.getString(1));
                //System.out.println("Added K: "+resultSet.getInt(1)+" and V: "+resultSetCat.getString(1));
				resultSet.close();
			
			}

            //convert to stream and use comparator from Map.Entry to sort in descending order then convert back to Map with a limit of k entries
            treeMap = new TreeMap<Integer,String>(ii);

            //print out map
            System.out.println("The top "+k+" highest parent categories: ");
            printMap(treeMap, k);       
			System.out.println("");
			resultSetCat.close();
			
			
    
	   
	   
	   /*iii*/

            Map<Integer, String> iii = new HashMap<Integer, String>(20);

            //select all users
            query = "SELECT login FROM Customer";
			resultSetCat = statement.executeQuery(query);
            
            //iterate through set of users and store bidCount in Map
            while(resultSetCat.next()) {
				ResultSet resultSet;
                statement = dbcon.createStatement();
                query = "SELECT func_bidCount("+x+",'"+resultSetCat.getString(1)+"') from dual";
                //System.out.println(query);
                resultSet = statement.executeQuery(query);
                resultSet.next();
                iii.put(resultSet.getInt(1), resultSetCat.getString(1));
                //System.out.println("Added K: "+resultSet.getInt(1)+" and V: "+resultSetCat.getString(1));
				resultSet.close();
			
			}

            //convert to treeMap to sort by ascending order
            treeMap = new TreeMap<Integer,String>(iii);

            //print out map
            System.out.println("The top "+k+" most active bidders ");
            printMap(treeMap, k);
			System.out.println("");
			resultSetCat.close();
		
			
	   
       /*iv*/
       
       Map<Integer, String> iv = new HashMap<Integer, String>(20);


       //select all users
       query = "SELECT login FROM Customer";
       resultSetCat = statement.executeQuery(query);
       
       //iterate through set of users and store buying amount in map
       while(resultSetCat.next()) {
		   ResultSet resultSet;
           statement = dbcon.createStatement();
           query = "SELECT func_buyingAmount("+x+",'"+resultSetCat.getString(1)+"') from dual";
           //System.out.println(query);
           resultSet = statement.executeQuery(query);
           resultSet.next();
           iv.put(resultSet.getInt(1), resultSetCat.getString(1));
           //System.out.println("Added K: "+resultSet.getInt(1)+" and V: "+resultSetCat.getString(1));
		   resultSet.close();
	   
	   }

       //convert to treeMap to sort by ascending order
       treeMap = new TreeMap<Integer,String>(iv);

       //print out map
       System.out.println("The top "+k+" most active buyers ");
       printMap(treeMap, k);
       
       //close statements
       resultSetCat.close();

   }

	//Created
    static void suggestions(String username) throws SQLException{
        System.out.println("--SUGGESTED AUCTION_ID(s)--");
		
		getSuggestions(username, dbcon);
		
		
    }
	
//FUNCTIONS FOR DRIVER AND BENCHMARK	
    static void printProducts(String cat, int order, Connection dbcon) throws SQLException {
        Statement statement = dbcon.createStatement();
        String amount = "";
        String order_by = "";
        if(order == 1){
            order_by = " ORDER BY name";
        }else if(order == 2){
            order_by = " ORDER BY amount desc";
        }else{
            order_by = "";
        }
        String where_cat = " WHERE category = '" + cat + "'AND status = 'under auction'";

        if(order_by.equals(" ORDER BY amount desc")){
            amount = ", coalesce(amount,0) as amount ";
        }
        String query = "SELECT auction_id, name" + amount +" FROM (Product NATURAL JOIN BelongsTo) " + where_cat +" " +order_by;
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("AUCTION_ID, NAME, BID(SHOWN ONLY WHEN SORTED BY BID)");
        
        while (resultSet.next()) {
            String output = resultSet.getString(1) + " " + resultSet.getString(2);
            if(order_by.equals(" ORDER BY amount desc")){
                output += " " + resultSet.getString(3);
            }
            System.out.println(output);
        }
        resultSet.close();
    }
   
   
    static void createCustomer(String username, String password, String name, String address, String email, Connection dbcon) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "INSERT INTO Customer VALUES ('"+username+"','"+password+"','"+name+"','"+address+"','"+email+"')";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.close();
    }

    static void updateTime(String date, Connection dbcon) throws SQLException{
		Statement statement = dbcon.createStatement();
        String query = "UPDATE ourSysDate set c_date = (to_date('"+date+"', 'mm/dd/yyyy hh24:mi:ss'))";
        ResultSet resultSet = statement.executeQuery(query);
		resultSet.close();
	}
	
	
    static void listProducts(String where_user, Connection dbcon) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "SELECT name, status, amount, bidder FROM Product natural join bidLog " + where_user + " status = 'under auction'";
        ResultSet resultSet = statement.executeQuery(query);

        System.out.println("NAME, STATUS, AMOUNT, BIDDER/BUYER");
        
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + " '" + resultSet.getString(2) + "' " + resultSet.getString(3) + " " + resultSet.getString(4));
        }
        
        statement = dbcon.createStatement();
        query = "SELECT name, status, amount, buyer FROM Product" + where_user + " status = 'sold'";
        resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + " '" + resultSet.getString(2) + "' " + resultSet.getString(3) + " " + resultSet.getString(4));
        }
        
        resultSet.close();
    }


    static void keywordProducts(String[] keywords, Connection dbcon) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "SELECT auction_id, name, description FROM Product WHERE description LIKE '%" + keywords[0] +"%'";
        if(keywords.length >= 2){
            query += " AND description LIKE '%" + keywords[1] + "%'";
        }
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " '" + resultSet.getString(3) + "'");
        }
        resultSet.close();
    }



    static void putUpAuction(String username, String name, String categories, String days, String desc, String price, Connection dbcon) throws SQLException{
        //String inputs = "'" + username + "', '" + name + "', '" + categories + "', " + days + ", '" + desc + "', " + price + "";
        String query = "{CALL proc_putProduct(?, ?, ?, ?, ?, ?)}";
        CallableStatement statement = dbcon.prepareCall(query);
        statement.setString(1, username);
        statement.setString(2, name);
        statement.setString(3, categories);
        statement.setString(4, days);
        statement.setString(5, desc);
        statement.setString(6, price);
        
        statement.executeQuery();
        
        System.out.println("...Item put up for Auction");
    }


    static void insertBid(int auctionID, String username, int amount, Connection dbcon) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "SELECT * from view_maxBidsn";
        ResultSet resultSet = statement.executeQuery(query);
        resultSet.next();
        int num = 1 + resultSet.getInt(1);
        
        query = "INSERT INTO Bidlog VALUES("+num+", " + auctionID + ", '" + username + "', (SELECT c_date FROM ourSysDate)  ," + amount + ")";
        statement.executeQuery(query);
        
        System.out.println("...Bid has been placed");
        resultSet.close();
    }


    static void sellThisProduct(int x, int amount, boolean toSell, Connection dbcon) throws SQLException{

        Statement statement = dbcon.createStatement();

        if(toSell){
            String query = "Update Product set status = 'sold' WHERE auction_id = "+x+"";
            ResultSet resultSet = statement.executeQuery(query);

            //add
            query = "SELECT bidder from bidlog WHERE auction_id = "+x+"";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            
            query = "Update Product set buyer = '"+resultSet.getString(1)+"' WHERE auction_id = "+x+"";
            resultSet = statement.executeQuery(query);	

            //add
            query = "SELECT amount from bidlog WHERE auction_id = "+x+" AND amount = "+amount+"";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            
            query = "Update Product set amount = "+resultSet.getString(1)+" WHERE auction_id = "+x+"";
            resultSet = statement.executeQuery(query);	

            //add
            query = "Update Product set sell_date = getcurdate WHERE auction_id = "+x+"";
            resultSet = statement.executeQuery(query);
            
            System.out.print("...Product has been sold!");
            resultSet.close();
        }else{
            
            String query = "Update Product set status = 'closed' WHERE auction_id = "+x+"";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.print("...Product has been closed!");
            resultSet.close();
        }
    }

    static void getSuggestions(String username, Connection dbcon) throws SQLException{
        Statement statement = dbcon.createStatement();
        String query = "select auction_id, count(bidder) as bidders from bidlog natural join (select bidder from bidlog natural join (select auction_id from bidlog where bidder = 'juice26') where bidder != '" +username + "') where (auction_id not in (select auction_id from bidlog where bidder = '" +username + "'))Group by auction_id Order by bidders desc";
        ResultSet resultSet = statement.executeQuery(query);
        
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
        resultSet.close();
    }
	
	static <K,V> void printMap(Map<K,V> treeMap, int num) {
    int c = 0;
    for (Map.Entry<K,V> entry : treeMap.entrySet()) {
        c++;
        if (c <= num) {
            System.out.println(c+": "+entry.getValue());
        }
        else {
            break;
        }
    }
}
	
}

/*
"THIS QUERY IS FOR SUGGESTIONS just put the customer that is looking for 
suggestions in place of 'juice26' will return in most recommended order"

select auction_id, count(bidder) as bidders
    from bidlog natural join (select bidder from bidlog natural join
        (select auction_id from bidlog where bidder = 'juice26') where bidder != 'juice26')
    where 
        (auction_id not in (select auction_id from bidlog where bidder = 'juice26'))
    Group by auction_id
    Order by bidders desc;
*/